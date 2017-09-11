/*******************************************************************************
 * Copyright (c) 2016 Alex Shapiro - github.com/shpralex
 * This program and the accompanying materials
 * are made available under the terms of the The MIT License (MIT)
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *******************************************************************************/
package com.sproutlife;

import java.util.HashMap;

import com.sproutlife.model.seed.SeedFactory.SeedType;

public class Settings {
    HashMap<String, Object> settings;
    public static String SEED_TYPE = "seedType";
    public static String LIFE_MODE = "lifeMode";
    public static String MUTATION_ENABLED = "mutationEnabled";
    public static String MUTATION_RATE = "mutationRate";
    public static String MAX_LIFESPAN = "maxLifespan";
    public static String CHILD_ONE_ENERGY = "childOneEnergy";
    public static String CHILD_TWO_ENERGY = "childTwoEnergy";
    public static String CHILD_THREE_ENERGY = "childThreeEnergy";
    //Determines whether seeds are sprouted immediately upon detection,
    //Or a step later so seed cells can be displayed before the sprout is.
    public static String SPROUT_DELAYED_MODE = "sproutDelayedMode";
    
    public Settings() {
        settings = new HashMap<String, Object>();
        initSettings();
    }

    public void initSettings() {
        set(Settings.SEED_TYPE, SeedType.Bentline1_RPentomino.toString());

        set(Settings.LIFE_MODE, "cooperative");

        set(Settings.MUTATION_ENABLED, true);
        set(Settings.MUTATION_RATE, 6); //0-10

        set(Settings.MAX_LIFESPAN, 75);

        set(Settings.CHILD_ONE_ENERGY, 0);
        set(Settings.CHILD_TWO_ENERGY, 0);
        set(Settings.CHILD_THREE_ENERGY, 2);

        set(Settings.SPROUT_DELAYED_MODE, false);

    }

    public void set(String s, Object o) {
        settings.put(s, o);
    }

    public boolean getBoolean(String s) {
        return (Boolean) settings.get(s);
    }

    public int getInt(String s) {
        return (Integer) settings.get(s);
    }

    public String getString(String s) {
        return (String) settings.get(s);
    }
}
