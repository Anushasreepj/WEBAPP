/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife.panel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sproutlife.Settings;
import javax.swing.SpinnerNumberModel;

public class SettingsControlPanel extends JPanel {

    PanelController panelController;
    private JSpinner maxLifespanSpinner;
    private JSpinner childOneParentAgeSpinner;
    private JSpinner childTwoParentAgeSpinner;
    private JSpinner childThreeParentAgeSpinner;
    private JLabel lblSproutMode;
    private JPanel panel;
    private JRadioButton rdbtnFunctional;
    private JRadioButton rdbtnVisual;
    private JLabel lblMutationRate;
    private JSpinner mutationRateSpinner;

    public SettingsControlPanel(PanelController panelController) {
        setMinimumSize(new Dimension(220, 0));
        setPreferredSize(new Dimension(260, 500));

        this.panelController = panelController;
        buildPanel();
    }
    /**
     * Create the panel.
     */
    public void buildPanel() {

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {10, 89, 60, 10};
        gridBagLayout.rowHeights = new int[]{20, 0, 15, 20, 0, 0, 0, 0, 15, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
        setLayout(gridBagLayout);

        childOneParentAgeSpinner = new JSpinner();
        childOneParentAgeSpinner.setPreferredSize(new Dimension(50, 20));

        ButtonGroup lifeModeButtonGroup = new ButtonGroup();

        JLabel lblMaxLifespan = new JLabel("Max self-destruct age");
        GridBagConstraints gbc_lblMaxLifespan = new GridBagConstraints();
        gbc_lblMaxLifespan.anchor = GridBagConstraints.WEST;
        gbc_lblMaxLifespan.insets = new Insets(0, 0, 5, 5);
        gbc_lblMaxLifespan.gridx = 1;
        gbc_lblMaxLifespan.gridy = 1;
        add(lblMaxLifespan, gbc_lblMaxLifespan);

        maxLifespanSpinner = new JSpinner();
        maxLifespanSpinner.setPreferredSize(new Dimension(50, 20));
        GridBagConstraints gbc_maxLifespanSpinner = new GridBagConstraints();
        gbc_maxLifespanSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_maxLifespanSpinner.anchor = GridBagConstraints.NORTH;
        gbc_maxLifespanSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_maxLifespanSpinner.gridx = 2;
        gbc_maxLifespanSpinner.gridy = 1;
        add(maxLifespanSpinner, gbc_maxLifespanSpinner);

        JLabel childOneEnergyLabel = new JLabel("Min age for 1st child");
        GridBagConstraints gbc_childOneEnergyLabel = new GridBagConstraints();
        gbc_childOneEnergyLabel.anchor = GridBagConstraints.WEST;
        gbc_childOneEnergyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_childOneEnergyLabel.gridx = 1;
        gbc_childOneEnergyLabel.gridy = 3;
        add(childOneEnergyLabel, gbc_childOneEnergyLabel);
        GridBagConstraints gbc_childOneEnergySpinner = new GridBagConstraints();
        gbc_childOneEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_childOneEnergySpinner.anchor = GridBagConstraints.NORTH;
        gbc_childOneEnergySpinner.insets = new Insets(0, 0, 5, 5);
        gbc_childOneEnergySpinner.gridx = 2;
        gbc_childOneEnergySpinner.gridy = 3;
        add(childOneParentAgeSpinner, gbc_childOneEnergySpinner);

        childTwoParentAgeSpinner = new JSpinner();
        childTwoParentAgeSpinner.setPreferredSize(new Dimension(50, 20));

        JLabel childTwoEnergyLabel = new JLabel("Min age for 2nd child");
        GridBagConstraints gbc_childTwoEnergyLabel = new GridBagConstraints();
        gbc_childTwoEnergyLabel.anchor = GridBagConstraints.WEST;
        gbc_childTwoEnergyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_childTwoEnergyLabel.gridx = 1;
        gbc_childTwoEnergyLabel.gridy = 4;
        add(childTwoEnergyLabel, gbc_childTwoEnergyLabel);
        GridBagConstraints gbc_childTwoEnergySpinner = new GridBagConstraints();
        gbc_childTwoEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_childTwoEnergySpinner.anchor = GridBagConstraints.NORTH;
        gbc_childTwoEnergySpinner.insets = new Insets(0, 0, 5, 5);
        gbc_childTwoEnergySpinner.gridx = 2;
        gbc_childTwoEnergySpinner.gridy = 4;
        add(childTwoParentAgeSpinner, gbc_childTwoEnergySpinner);

        childThreeParentAgeSpinner = new JSpinner();
        childThreeParentAgeSpinner.setPreferredSize(new Dimension(50, 20));

        JLabel childThreeEnergyLabel = new JLabel("Min age for 3+ chilren");
        GridBagConstraints gbc_childThreeEnergyLabel = new GridBagConstraints();
        gbc_childThreeEnergyLabel.anchor = GridBagConstraints.WEST;
        gbc_childThreeEnergyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_childThreeEnergyLabel.gridx = 1;
        gbc_childThreeEnergyLabel.gridy = 5;
        add(childThreeEnergyLabel, gbc_childThreeEnergyLabel);
        GridBagConstraints gbc_childThreeEnergySpinner = new GridBagConstraints();
        gbc_childThreeEnergySpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_childThreeEnergySpinner.insets = new Insets(0, 0, 5, 5);
        gbc_childThreeEnergySpinner.anchor = GridBagConstraints.NORTH;
        gbc_childThreeEnergySpinner.gridx = 2;
        gbc_childThreeEnergySpinner.gridy = 5;
        add(childThreeParentAgeSpinner, gbc_childThreeEnergySpinner);

        lblMutationRate = new JLabel("Mutation Rate (0-10)");
        GridBagConstraints gbc_lblMutationRate = new GridBagConstraints();
        gbc_lblMutationRate.anchor = GridBagConstraints.WEST;
        gbc_lblMutationRate.insets = new Insets(0, 0, 5, 5);
        gbc_lblMutationRate.gridx = 1;
        gbc_lblMutationRate.gridy = 7;
        add(lblMutationRate, gbc_lblMutationRate);

        mutationRateSpinner = new JSpinner();
        mutationRateSpinner.setModel(new SpinnerNumberModel(5, 0, 10, 1));
        GridBagConstraints gbc_mutationRateSpinner = new GridBagConstraints();
        gbc_mutationRateSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_mutationRateSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_mutationRateSpinner.gridx = 2;
        gbc_mutationRateSpinner.gridy = 7;
        add(mutationRateSpinner, gbc_mutationRateSpinner);


        lblSproutMode = new JLabel("Sprout Mode (Show Seeds)");
        GridBagConstraints gbc_lblSproutMode = new GridBagConstraints();
        gbc_lblSproutMode.anchor = GridBagConstraints.WEST;
        gbc_lblSproutMode.insets = new Insets(0, 0, 5, 5);
        gbc_lblSproutMode.gridx = 1;
        gbc_lblSproutMode.gridy = 9;
        add(lblSproutMode, gbc_lblSproutMode);

        panel = new JPanel();
        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.gridwidth = 2;
        gbc_panel.insets = new Insets(0, 0, 5, 5);
        gbc_panel.fill = GridBagConstraints.BOTH;
        gbc_panel.gridx = 1;
        gbc_panel.gridy = 10;
        add(panel, gbc_panel);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{30, 40, 40, 0};
        gbl_panel.rowHeights = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);

        ButtonGroup sproutModeButtonGroup = new ButtonGroup();
        rdbtnFunctional = new JRadioButton("Functional");
        sproutModeButtonGroup.add(rdbtnFunctional);
        rdbtnFunctional.setToolTipText("<html>Seeds are immediately replaced<br>by sprouts before being shown.<br>More efficient for reproduction.</html>");
        rdbtnFunctional.setSelected(true);
        rdbtnFunctional.setAlignmentX(1.0f);
        GridBagConstraints gbc_radioButton = new GridBagConstraints();
        gbc_radioButton.anchor = GridBagConstraints.EAST;
        gbc_radioButton.insets = new Insets(0, 0, 0, 5);
        gbc_radioButton.gridx = 1;
        gbc_radioButton.gridy = 0;
        panel.add(rdbtnFunctional, gbc_radioButton);

        rdbtnVisual = new JRadioButton("Visual");
        sproutModeButtonGroup.add(rdbtnVisual);
        rdbtnVisual.setToolTipText("<html>Seeds are displayed but an<br>extra step is added to show them.</html>");
        GridBagConstraints gbc_rdbtnVisual = new GridBagConstraints();
        gbc_rdbtnVisual.anchor = GridBagConstraints.EAST;
        gbc_rdbtnVisual.gridx = 2;
        gbc_rdbtnVisual.gridy = 0;
        panel.add(rdbtnVisual, gbc_rdbtnVisual);

    }

    public JSpinner getMaxLifespanSpinner() {
        return maxLifespanSpinner;
    }

    public JSpinner getChildOneParentAgeSpinner() {
        return childOneParentAgeSpinner;
    }
    public JSpinner getChildTwoParentAgeSpinner() {
        return childTwoParentAgeSpinner;
    }
    public JSpinner getChildThreeParentAgeSpinner() {
        return childThreeParentAgeSpinner;
    }

    public JRadioButton getRdbtnVisual() {
        return rdbtnVisual;       
    }
    public JRadioButton getRdbtnFunctional() {
        return rdbtnFunctional;
    }

    public JSpinner getMutationRateSpinner() {
        return mutationRateSpinner;
    }
}
