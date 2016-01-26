package com.sproutlife.model.step.lifemode;

import java.util.ArrayList;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.echosystem.Cell;
import com.sproutlife.model.echosystem.Organism;

public class CooperativeLife extends LifeMode {
    
    public CooperativeLife(GameModel gameModel) {
        super(gameModel);
        // TODO Auto-generated constructor stub
    }
    
    public Cell keepAlive(Cell me, ArrayList<Cell> neighbors, int i, int j) {
        
        if ((neighbors.size() == 2 || neighbors.size()==3)) { 
            me.age+=1;                
            return me;
        }
        
        return null;
        
    }

    public Cell getBorn(ArrayList<Cell> neighbors, int i, int j) {
        if (i<0||i>getBoard().getWidth()-1||j<0||j>getBoard().getHeight()-1) {
            return null;
        }
        
        if (neighbors.size()<3 || neighbors.size()>6) {
            return null;
        }

        //Quick check to see if all neighbors are from the same organism
        Organism checkSingleOrg = neighbors.get(0).getOrganism();
        boolean singleOrg = true;
        for (Cell cell : neighbors) {
            if (cell.getOrganism() != checkSingleOrg) {
                singleOrg = false;
                break;
            }
        }
        if (singleOrg) {
            if (neighbors.size()==3) {
                Cell bornCell = getEchosystem().createCell(i,j,neighbors);
                return bornCell;
            }
            else {
                return null;
            }
        }        
        
        return null;
    }
}
