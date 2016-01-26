package com.sproutlife.model.step;

import java.util.ArrayList;

import com.sproutlife.Settings;
import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.step.lifemode.CompetitiveLife;
import com.sproutlife.model.step.lifemode.CooperativeLife;
import com.sproutlife.model.step.lifemode.FriendlyLife;
import com.sproutlife.model.step.lifemode.LifeMode;

public class LifeStep extends Step {     
  
    LifeMode cooperativeLife;
    LifeMode friendlyLife;
    LifeMode competitiveLife;
    LifeMode lifeMode;
                      
    public LifeStep(GameModel gameModel) {
        super(gameModel); 
        cooperativeLife = new CooperativeLife(gameModel);
        friendlyLife = new FriendlyLife(gameModel);
        competitiveLife = new CompetitiveLife(gameModel);
        
        lifeMode = friendlyLife;
    }
    
    public void perform() {
        initStats();
        updateLifeMode();
        updateCells();                
    }
    
    public LifeMode getLifeMode() {
        return lifeMode;
    }
    
    private void updateLifeMode() {
        if("friendly".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = friendlyLife;
        }
        else if("cooperative".equals(getSettings().getString(Settings.LIFE_MODE))) {
            this.lifeMode = cooperativeLife;
        }
        else {
            this.lifeMode = competitiveLife;
        }
    }
    
    public void updateCells() {        
                
        ArrayList<Cell> bornCells = new ArrayList<Cell>(); 
        ArrayList<Cell> deadCells = new ArrayList<Cell>();         
        
        for (int i=0; i<getBoard().getWidth(); i++) {
            
            for (int j=0; j<getBoard().getHeight(); j++) {                                   
                
                Cell me = getBoard().getCell(i,j);

                if (me!=null) {
                    ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);
                    Cell result = getLifeMode().keepAlive(me,neighbors,i,j);
                    if (result!=null) {
                        getStats().stayed++;
                    }
                    else {
                        deadCells.add(me);
                    }

                } 
                else {                        
                    if (getBoard().hasNeighbors(i,j)) {
                        ArrayList<Cell> neighbors = getBoard().getNeighbors(i,j);

                        Cell result = getLifeMode().getBorn(neighbors,i,j);

                        if (result!=null) {
                            bornCells.add(result);
                            getStats().born++;
                        }

                    }
                }           
            }
        }
        
        for (Cell c: bornCells) {
            getEchosystem().addCell(c);
            //getBoard().setCell(c);            
        }
        for (Cell c: deadCells) {
            getEchosystem().removeCell(c);
        }
    }     
    
    private void initStats() {                
        getStats().born =0;
        getStats().die1 =0;
        getStats().die2 =0;
        getStats().stayed = 0;
        getStats().gridSize = getBoard().getWidth()*getBoard().getHeight();    
    }
    
}
