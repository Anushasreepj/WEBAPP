/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.model.seed;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sproutlife.model.seed.patterns.Bentline1RpPattern;
import com.sproutlife.model.seed.patterns.Bentline1mRpPattern;
import com.sproutlife.model.seed.patterns.BentlineMargin35Pattern;
import com.sproutlife.model.seed.patterns.BoxhatRpPattern;
import com.sproutlife.model.seed.patterns.Boxlid3RpPattern;
import com.sproutlife.model.seed.patterns.BoxlidRpPattern;
import com.sproutlife.model.seed.patterns.GliderRpPattern;
import com.sproutlife.model.seed.patterns.L2B1RpPattern;
import com.sproutlife.model.seed.patterns.L2RpPattern;
import com.sproutlife.model.seed.patterns.OnebitB1RpPattern;
import com.sproutlife.model.seed.patterns.OnebitRpPattern;
import com.sproutlife.model.seed.patterns.RpRpPattern;
import com.sproutlife.model.seed.patterns.Square2RpPattern;


public class SeedFactory {
    
    public static enum SeedType { 
               
        L2_RPentomino ("L2 to R-Pentomino"),
        L2B1_RPentomino ("L2B1 to R-Pentomino"), 
        Square2_RPentomino ("2x2 Box to R-Pentomino"), 
        Glider_RPentomino ("Glider to R-Pentomino"), 
        RPentomino_RPentomino ("R-Pentomino to R-Pentomino"),
        Boxlid_RPentomino ("Boxlid to R-Pentomino"), 
        Boxlid3_RPentomino ("Boxlid3 to R-Pentomino"),
        Boxhat_RPentomino ("Boxhat to R-Pentomino"),
        Bentline1_RPentomino ("Bentline1 to R-Pentomino"),
        Bentline1m_RPentomino ("Bentline1m to R-Pentomino"),
        BentlineMargin35_RPentomino ("BentlineM35 to R-Pentomino"),
        Onebit_RPentomino ("Onebit to R-Pentomino"), 
        OnebitB1_RPentomino ("OnebitB1 to R-Pentomino"),  
        Test_Pattern ("Test Pattern");
                   
        private final String name;
        
        private SeedType(String name) {
            this.name = name;
        }

        public String toString() {
           return this.name;
        }
        
        
        public boolean isSymmetric4() {
            switch(this) {
                case Square2_RPentomino : return true; 
                case Onebit_RPentomino : return true;
                case OnebitB1_RPentomino : return true;
                //case Test_Pattern : return true;
                default: return false;
            }                
        }
        
        public boolean isSymmetric2() {
            switch(this) {
                case L2_RPentomino : return true; 
                case L2B1_RPentomino : return true; 
                default: return false;
            }                
        }
    }   
    
    private static HashMap<SeedType, SeedSproutPattern> patterns = new HashMap<SeedType, SeedSproutPattern>();  
    
    static {        
        patterns.put(SeedType.L2_RPentomino, new L2RpPattern());
        patterns.put(SeedType.L2B1_RPentomino, new L2B1RpPattern());
        patterns.put(SeedType.Square2_RPentomino, new Square2RpPattern());
        patterns.put(SeedType.Glider_RPentomino, new GliderRpPattern());
        patterns.put(SeedType.RPentomino_RPentomino, new RpRpPattern());
        patterns.put(SeedType.Boxlid_RPentomino, new BoxlidRpPattern());
        patterns.put(SeedType.Boxlid3_RPentomino, new Boxlid3RpPattern());
        patterns.put(SeedType.Boxhat_RPentomino, new BoxhatRpPattern());
        patterns.put(SeedType.Bentline1_RPentomino, new Bentline1RpPattern());
        patterns.put(SeedType.Bentline1m_RPentomino, new Bentline1mRpPattern());
        patterns.put(SeedType.BentlineMargin35_RPentomino, new BentlineMargin35Pattern());
        patterns.put(SeedType.Onebit_RPentomino, new OnebitRpPattern());
        patterns.put(SeedType.OnebitB1_RPentomino, new OnebitB1RpPattern());
        
        
        patterns.put(SeedType.Test_Pattern, 
            new SeedSproutPattern() {                
                {                    
                    this.seedPattern = new BitPattern(new int[][]                                             
                    	   {{0,0,1},
                            {0,1,0},                         
                            {0,1,0}},
                          true);

                    this.sproutPattern = new BitPattern(new int[][]  
                         {{0,1,1},
                          {1,1,0},                         
                          {0,1,0}},
                          true);

                    this.sproutOffset = new Point(1,0);
                }
            });  
    } 
    
    public static List<Seed> getSeedRotations(SeedType type) {
        SeedSproutPattern ssp = patterns.get(type);
        
        if (ssp==null) {
            return null;
        }
        
        if (type.isSymmetric4()) {
            return getSymmetricRotation(ssp);
        }
        if (type.isSymmetric2()) {
            return get4SeedRotations(ssp);
        }
        else {        
            return get8SeedRotations(ssp);
        }
                
    }    
    
    private static List<Seed> getSymmetricRotation(SeedSproutPattern ssp) {
        ArrayList<Seed> seedRotations = new ArrayList<Seed>();
        seedRotations.add(new SymmetricSeed(ssp));        
        return seedRotations;
    }
    
    private static List<Seed> get4SeedRotations(SeedSproutPattern ssp) {
        ArrayList<Seed> seedRotations = new ArrayList<Seed>();
        
        seedRotations.add(new Seed(ssp, 0));
        seedRotations.add(new Seed(ssp, 1));
        seedRotations.add(new Seed(ssp, 2));
        seedRotations.add(new Seed(ssp, 3));
        
        return seedRotations;
    }
    
    private static List<Seed> get8SeedRotations(SeedSproutPattern ssp) {
        ArrayList<Seed> seedRotations = new ArrayList<Seed>();
        
        seedRotations.add(new Seed(ssp, 0, false));
        seedRotations.add(new Seed(ssp, 1, false));
        seedRotations.add(new Seed(ssp, 2, false));
        seedRotations.add(new Seed(ssp, 3, false));
        
        seedRotations.add(new Seed(ssp, 0, true));
        seedRotations.add(new Seed(ssp, 1, true));
        seedRotations.add(new Seed(ssp, 2, true));
        seedRotations.add(new Seed(ssp, 3, true));
        
        return seedRotations;
    }
}
