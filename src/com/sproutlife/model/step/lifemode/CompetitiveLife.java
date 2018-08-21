/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CompetitiveLife extends LifeMode {
    
    public CompetitiveLife(GameModel gameModel) {
        super(gameModel);
    }

    public double getCompare(Cell c1, Cell c2) {
        Organism o1 = c1.getOrganism();
        Organism o2 = c2.getOrganism();
        if (o1.getParent()!=null && o2.getParent()!=null) {
            return o1.getParent().getAttributes().cellSum - o2.getParent().getAttributes().cellSum;
        }
        return o1.getAttributes().cellSum - o2.getAttributes().cellSum;
    }
    
    public void updateCells() {
        for (Organism o : getEchosystem().getOrganisms()) {
            o.getAttributes().cellSum += o.getCells().size();
        }

        ArrayList<Cell> bornCells = new ArrayList<Cell>(); 
        ArrayList<Cell> deadCells = new ArrayList<Cell>();   
                              
        for (int i=0; i<getBoard().getWidth(); i++) {            
            for (int j=0; j<getBoard().getHeight(); j++) {                                   

                Cell me = getBoard().getCell(i,j);
                Cell result = null;
                boolean wasBorn = false;

                if (getBoard().hasNeighbors(i,j)) {
                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);

                    result = getBorn(neighbors,i,j);

                    if (result!=null) {
                        if(me==null || me.getOrganism()!=result.getOrganism()) {
                            bornCells.add(result);
                            wasBorn=true;
                            getStats().born++;
                            if (me!=null) {
                                deadCells.add(me);
                            }
                        }
                    }
                }

                if (me!=null && !wasBorn){

                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);
                    result = keepAlive(me,neighbors,i,j);
                    if (result!=null) {
                        getStats().stayed++;
                    }
                    else {
                        deadCells.add(me);
                    }

                }           
            }
        }       
        
        //Remove cells before adding cells to avoid Organism having duplicate cells,
        //Orgs don't do Contains checks for speed
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);            
        }
       
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
        }
 
    } 
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {        
        
        int friendCount = 0;

        for (Cell neighbor : neighbors) {            
            if (me.getOrganism() == neighbor.getOrganism()) {
                friendCount++;
            }
            else if (getCompare(me, neighbor)<0) {
                me.getOrganism().getAttributes().collisionCount++;
                return null;
            }
        }

        if ((friendCount == 2 || friendCount==3)) {
            for (Cell neighbor : getBoard().getExtra12Neighbors(i, j)) {
                if (neighbor.getOrganism()!=me.getOrganism() && neighbor.getOrganism()!=me.getOrganism().getParent()
                        && getCompare(me, neighbor)<0) {
                    me.getOrganism().getAttributes().collisionCount++;
                    return null;
                }
            }
            me.age+=1;
            return me;
        }

        return null;
    }

    public Cell getBorn(ArrayList<Cell> neighbors, int i, int j) {
        if (i<0||i>getBoard().getWidth()-1||j<0||j>getBoard().getHeight()-1) {
            return null;
        }
        
        if (neighbors.size()!=3 ) {
            return null;
        }

        Organism checkSingleOrg = neighbors.get(0).getOrganism();
        
        //Quick check to see if all neighbors are from the same organism
        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                return null;
            }
        }
        Cell bornCell = getEchosystem().createCell(i,j,neighbors);

        for (Cell neighbor : getBoard().getExtra12Neighbors(i, j)) {
            if (neighbor.getOrganism()!=bornCell.getOrganism() && neighbor.getOrganism()!=bornCell.getOrganism().getParent()
                    && getCompare(bornCell, neighbor)<0) {
                return null;
            }
        }
        
        return bornCell;
    }
}
