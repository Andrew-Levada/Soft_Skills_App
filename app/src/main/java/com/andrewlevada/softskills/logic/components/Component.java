package com.andrewlevada.softskills.logic.components;

import android.view.View;

import com.andrewlevada.softskills.RoadmapActivity;

public interface Component {
    Component copy();

    View getPreview();

    RoadmapActivity getActivity();

    void setActivity(RoadmapActivity activity);

    ComponentViewUnit generateComponentViewUnit();
}
