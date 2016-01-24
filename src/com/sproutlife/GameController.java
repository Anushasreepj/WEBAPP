package com.sproutlife;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sproutlife.model.GameModel;
import com.sproutlife.model.GameThread;
import com.sproutlife.panel.GameFrame;
import com.sproutlife.panel.PanelController;

public class GameController {
    
    private GameModel gameModel;
    
    private PanelController panelController;

    private Settings settings;

    protected ReentrantReadWriteLock interactionLock;

    public GameController() {
        settings = new Settings();
         
        interactionLock = new ReentrantReadWriteLock();
        
        gameModel = new GameModel(settings, interactionLock);
        
        panelController = new PanelController(this);

        //frame = new GameFrame(this);

        //game = new GameThread(frame.getGamePanel());
    }

    public void start() {
        panelController.start();
    }        

    public GameModel getGameModel() {
        return gameModel;
    }

    public ReentrantReadWriteLock getInteractionLock() {
        return interactionLock;
    }
    
    public Settings getSettings() {
        return settings;
    }

    public void set(String s, Object o) {
        getSettings().set(s,o);
    }    

}
