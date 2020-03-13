package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.RoadmapActivity;

import java.util.ArrayList;

public abstract class AbstractComponent implements Component {
    private RoadmapActivity activity;

    private ArrayList<ComponentViewUnit> viewUnits;

    public ComponentViewUnit generateComponentViewUnit() {
        ComponentViewUnit viewUnit = new ComponentViewUnit(this, false);
        viewUnits.add(viewUnit);
        return viewUnit;
    }

    protected ComponentViewUnit generateComponentViewUnit(boolean updatable) {
        ComponentViewUnit viewUnit = new ComponentViewUnit(this, updatable);
        viewUnits.add(viewUnit);
        return viewUnit;
    }

    public RoadmapActivity getActivity() {
        return activity;
    }

    protected ComponentViewUnit getViewUnit(int index) {
        return viewUnits.get(index);
    }

    public AbstractComponent(RoadmapActivity activity) {
        this.activity = activity;
        viewUnits = new ArrayList<>();
    }
}
