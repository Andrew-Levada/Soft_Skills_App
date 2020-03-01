package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class Component {
    private Activity activity;
    private int step;

    public abstract View getPreview();
    public abstract boolean hasNextStep();

    @Nullable
    public Component moveToNextStep() {
        if (hasNextStep()) {
            step++;
            return this;
        } else return null;
    }

    public ComponentViewUnit generateComponentViewUnit() {
        return new ComponentViewUnit(this);
    }

    public int getStep() {
        return step;
    }

    public Activity getActivity() {
        return activity;
    }

    public Component(Activity activity, int step) {
        this.activity = activity;
        this.step = step;
    }
}
