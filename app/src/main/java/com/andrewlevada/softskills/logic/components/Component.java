package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class Component {
    Activity activity;
    private int step;

    public abstract View getView();
    public abstract int getViewLayoutId();
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

    public Component(Activity activity) {
        this.activity = activity;
    }
}
