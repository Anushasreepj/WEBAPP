package com.sproutlife;

import javax.swing.JPanel;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.GameStep;
import com.sproutlife.model.LifeStep;
import com.sproutlife.view.GamePanel;

class GameThread extends Thread {
    boolean playGame = false;
    
    private GameController controller;          
    
    public GameThread(GameController controller) {     
        this.controller = controller;
        // TODO Auto-generated constructor stub
    }
    
    private GamePanel getGamePanel() {
        return controller.getFrame().getGamePanel();
    }
    
    private GameModel getGameModel() {
        return controller.getGameModel();
    }
    
    private GameStep getGameStep() {
        return getGameModel().getGameStep();
    }
    
    public void setPlayGame(boolean playGame) {
        this.playGame = playGame;
    }

    public void run() {

        while(playGame) {

            try {
                //Thread.sleep(1000/i_movesPerSecond);
                //
                synchronized (getGameModel().getEchosystem()) {
                    getGameStep().perform();
                }
                
                int sleep = 1;
                
                boolean superSlowIntro = false;
              
                if (superSlowIntro) {
	                if (getGameModel().getClock()<100 ) {
	                    sleep = 800 - (int) (Math.log10(getGameModel().getClock()/13.0+1)*800) ;
	                }
	                else {
	                    sleep = Math.max(1, 40-(int) Math.sqrt(getGameModel().getClock()/4));
	                }
                }
                boolean slowIntro = false;
                
                
                if (slowIntro && !superSlowIntro) {                    
                    if (getGameModel().getClock()<2000 ) {
                        sleep = 10;
                    }
                    else if (getGameModel().getClock()<4000 ) {
                        sleep = 8;
                    }
                }
                int iterations = 1;

                if(getGameModel().getEchosystem().getOrganisms().size()>120) {
                    iterations =2;
                }
                if (getGameModel().getEchosystem().getOrganisms().size()>180) {
                    iterations = 4;
                }
                if (getGameModel().getEchosystem().getOrganisms().size()>240) {
                    iterations = 8;
                }
                if (getGameModel().getClock()%iterations==0) {

                    getGamePanel().repaint();
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException ex) {}

        }
    } 
}

