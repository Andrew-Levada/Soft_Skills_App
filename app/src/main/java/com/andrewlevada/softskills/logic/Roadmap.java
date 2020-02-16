package com.andrewlevada.softskills.logic;

import com.andrewlevada.softskills.logic.components.*;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.List;

public class Roadmap {
    private List<Component> story;
    private List<Component> active;

    private UserTraits userTraits;

    private static Roadmap instance;

    public static Roadmap getInstance() {
        if (instance == null)
            instance = new Roadmap();

        return instance;
    }

    private Roadmap() {

    }
}
