package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import com.andrewlevada.softskills.RoadmapActivity;

public abstract class Component {
    private RoadmapActivity activity;

    public abstract View getPreview();

    public ComponentViewUnit generateComponentViewUnit() {
        return new ComponentViewUnit(this);
    }

    public RoadmapActivity getActivity() {
        return activity;
    }

    public Component(RoadmapActivity activity) {
        this.activity = activity;
    }
}
