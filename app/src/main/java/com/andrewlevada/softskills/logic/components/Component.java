package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Nullable;

public abstract class Component {
    Activity activity;
    private int step;

    public abstract View getView();
    public abstract int getViewLayoutId();
    public abstract boolean hasNextView();

    @Nullable
    public View getNextView() {
        if (hasNextView()) {
            step++;
            return getView();
        } else return null;
    }

    public Component(Activity activity) {
        this.activity = activity;
    }
}
