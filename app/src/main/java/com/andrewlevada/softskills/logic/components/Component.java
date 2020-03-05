package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.RoadmapActivity;

import java.util.ArrayList;

public abstract class Component {
    private RoadmapActivity activity;

    private ArrayList<ComponentViewUnit> viewUnits;

    @Nullable
    public abstract View getPreview();

    public ComponentViewUnit generateComponentViewUnit() {
        ComponentViewUnit viewUnit = new ComponentViewUnit(this);
        viewUnits.add(viewUnit);
        return viewUnit;
    }

    public RoadmapActivity getActivity() {
        return activity;
    }

    public ComponentViewUnit getViewUnit(int index) {
        return viewUnits.get(index);
    }

    public Component(RoadmapActivity activity) {
        this.activity = activity;
        viewUnits = new ArrayList<>();
    }
}
