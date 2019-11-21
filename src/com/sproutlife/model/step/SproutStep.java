/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;
import com.sproutlife.model.seed.Seed;
import com.sproutlife.model.seed.SeedFactory;
import com.sproutlife.model.seed.SeedFactory.SeedType;
import com.sproutlife.model.utils.SproutUtils;

public class SproutStep extends Step {
    SeedType seedType;
    boolean isSproutDelayedMode = false;
    HashMap<Organism,ArrayList<Seed>> savedSeeds;
    
    public SproutStep(GameModel gameModel) {
        super(gameModel);       
    }
    
    public void setSeedType(SeedType seedType) {
        this.seedType = seedType;
    }

    public SeedType getSeedType() {
        return seedType;
    }    

    public void perform() {
        setSeedType(SeedType.get(getSettings().getString(Settings.SEED_TYPE)));
        this.isSproutDelayedMode = getSettings().getBoolean(Settings.SPROUT_DELAYED_MODE);
        if (isSproutDelayedMode) {
            if (this.savedSeeds!=null) {
                sproutSeeds(this.savedSeeds);
            }
            savedSeeds = findSeeds();
        }
        else { 
            //simple way of doing things, makes it harder to display seeds;
            HashMap<Organism,ArrayList<Seed>> seeds = findSeeds();
            sproutSeeds(seeds);
        }
    }

    public int getMinParentAge(Organism org, int childNumber) {
        switch (childNumber) {
            case 1: return getSettings().getInt(Settings.CHILD_ONE_PARENT_AGE);
            case 2: return getSettings().getInt(Settings.CHILD_TWO_PARENT_AGE);
            case 3: return getSettings().getInt(Settings.CHILD_THREE_PARENT_AGE);
            default: return 0;
        }
    }

    public boolean checkMinAgeToHaveChildren(Organism org, int seedCount) {
        int childNumberToBe = seedCount;
        if (org.getChildren()!=null) {
            childNumberToBe += org.getChildren().size();
        }

        // for loop in case user set min age for 1 child > min age for 2 children
        for (int n = 1; n <= childNumberToBe && n <=3; n++) {
            if (org.getAge()+1<getMinParentAge(org,n)) {
                return false;
            }
        }
        return true;
    }

    private void sproutSeeds(HashMap<Organism,ArrayList<Seed>> seeds) {
        
        for (Organism o: seeds.keySet()) {
            if(!o.isAlive()) {
                continue;
            }
            ArrayList<Seed> seedList = seeds.get(o);

            if (!checkMinAgeToHaveChildren(o, seedList.size())) {
                continue;
            }

            for (Seed s : seedList) {
               Point seedOnPosition = s.getSeedOnPosition();
                
               Cell c = getBoard().getCell(seedOnPosition);
               
               if (c==null && !isSproutDelayedMode) {
                   //Should almost never happen, only if seeds overlapped.
                   continue;
               }
               
               SproutUtils.sproutSeed(s, o, getEchosystem());
               
               // update stats
               int childCount = o.getChildren().size()-1;
               if(childCount>=0&&getTime()>100&&childCount<20) { //sproutSeed() above may have failed
                   getStats().childEnergy[childCount]+=o.getAge();
                   getStats().sproutNumber[childCount]++;
               }

               
            }
        }
    }
            
    private HashMap<Organism,ArrayList<Seed>> findSeeds() {
        
        List<Organism> organisms = new ArrayList<>(getEchosystem().getOrganisms());

        HashMap<Organism,ArrayList<Seed>> seeds = new HashMap<Organism,ArrayList<Seed>>();
        // initialize the hash map so we don't need a synchronous way to add keys by multiple threads
        for (Organism o : organisms) {
            seeds.put(o,new ArrayList<>(5));
        }

        // Split finding seeds into multiple threads for multi-core CPU processing
        int PARTITION_SIZE = 20;
        List<Thread> threads = new ArrayList<>();
        for (int orgPartition=0; orgPartition<organisms.size();orgPartition+=PARTITION_SIZE) {
            final int orgPartitionFinal = orgPartition;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    for (int oi = orgPartitionFinal; oi<orgPartitionFinal+PARTITION_SIZE && oi<organisms.size();oi++) {
                        Organism o = organisms.get(oi);
                        for (Cell c : o.getCells()) {
                            Seed s = checkAndMarkSeed(c);
                            if (s!=null) {
                                seeds.get(o).add(s);
                            }
                        }
                    }
                }
            });
            t.start();
            threads.add(t);
        }
        try {
            for (Thread t : threads) {
                t.join();
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        return seeds;

    }          

    private Seed checkAndMarkSeed(Cell topLeftCell) {
        for (Seed s : SeedFactory.getSeedRotations(getSeedType())) {
            
            Point seedOnBit = s.getSeedOnBit();
            
            int x = topLeftCell.x-seedOnBit.x;
            int y = topLeftCell.y-seedOnBit.y;
            
            if (x<0||y<0) {
                continue;
            }
            
            s.setPosition(x, y);                      
            s.setParentPosition(topLeftCell.getOrganism().getLocation());
            
            if(checkAndMarkSeed(s)) {          
                return s;
            }
        }
        return null;

    }

    public boolean checkAndMarkSeed(Seed seed) {

        ArrayList<Cell> seedCells = new ArrayList<Cell>();        
        Organism seedOrg = null;
        int i = seed.getPosition().x;
        int j = seed.getPosition().y;
        int seedWidth = seed.getSeedWidth();
        int seedHeight = seed.getSeedHeight();
        int border = seed.getSeedBorder();

        //Check seed bounds
        if( i+seedWidth>=getBoard().getWidth() || j+seedHeight>=getBoard().getHeight()) {            
            return false;
        }

        //Check seed;
        for (int si=0;si<seedWidth;si++) {
            for (int sj=0;sj<seedHeight;sj++) {                              
                Cell c = getBoard().getCell(i+si,j+sj);                
                if (seed.getSeedBit(si,sj)) {

                    if (c==null) {
                        return false;                        
                    }
                    //else
                    if (seedOrg==null) {
                        seedOrg = c.getOrganism();
                    }
                    if (!c.getOrganism().equals(seedOrg)) {
                        return false;
                    }
                    seedCells.add(c);                                                                
                }
                else {
                    if (c!=null) {
                        return false;
                    }
                }
            }            
        }

        //Check border
        for (int si=-border;si<seedWidth+border;si++) { 
            for (int sj=-border;sj<seedHeight+border;sj++) {                       
                if(si<=-1 || sj<=-1 || si>=(seedWidth) || sj>=seedHeight) {                    
                    if(i+si>=0 && j+sj>=0 && 
                            i+si<=getBoard().getWidth()-1 &&  
                            j+sj<=getBoard().getHeight()-1) {
                        if (getBoard().getCell(i+si,j+sj)!=null) {
                            return false;
                        }
                    }
                }
            }
        }

        for (Cell c: seedCells) {
            c.setMarkedAsSeed(true);
        }
        return true;
    }
}
