/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sproutlife.Settings;
import com.sproutlife.io.GenomeIo;
import com.sproutlife.panel.PanelController;

@SuppressWarnings("serial")
public class LoadGenomeAction extends AbstractAction {

    protected PanelController controller;
    private boolean resetBeforeLoading = true;

    public LoadGenomeAction(PanelController controller, String name) {
        super(name);
        this.controller = controller;
    }

    public LoadGenomeAction(PanelController controller, boolean resetBeforeLoading) {
        this(controller, resetBeforeLoading ? "Load New Genome" : "Load Additional Genome");
        this.resetBeforeLoading = resetBeforeLoading;
    }

    public JFileChooser getFileChooser() {
        JFileChooser fileChooser = controller.getFileChooser();

        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
            }

            public String getDescription() {
                return "txt files (*.txt)";
            }
        });

        return fileChooser;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = getFileChooser();
        controller.setPlayGame(false);
        int returnVal = fileChooser.showOpenDialog(controller.getGameFrame());
        File loadFile;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            loadFile = fileChooser.getSelectedFile();
            try {
                if (resetBeforeLoading) {
                    controller.getGameModel().resetGame();
                    controller.getGameController().clearLoadedFiles();
                    controller.getActionManager().getReloadAction().setEnabled(false);
                } else if (controller.getGameModel().getEchosystem().getOrganisms().size() > 15) {
                    // Kind of a hack for now.
                    // When getOrganisms().size() is 15 or more that means the user loaded a second
                    // or third genome, so we switch to tri-color aka. split-color mode so they can
                    // watch them compete
                    controller.getDisplayControlPanel().getChckbxAutoSplitColors().setSelected(false);
                    controller.getSettings().set(Settings.COLOR_MODEL, "SplitColorModel");
                }
                GenomeIo.loadGenome(loadFile, controller.getGameModel());
                controller.updateFromSettings();
                controller.getImageManager().repaintNewImage();
                System.out.println("Loaded Orgs " + controller.getGameModel().getEchosystem().getOrganisms().size());
                controller.getGameController().addLoadedFile(loadFile);
                controller.getActionManager().getReloadAction().setEnabled(true);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(controller.getGameFrame(), "Load Error", ex.toString(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}