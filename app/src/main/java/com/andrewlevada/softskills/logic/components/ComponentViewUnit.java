package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;
    private int viewId;

    public int getViewId() {
        return viewId;
    }

    public View getView() {
        return component.getView();
    }

    public Component getComponent() {
        return component;
    }

    public ComponentViewUnit(Component component) {
        this.component = component;
        viewId = component.getViewLayoutId();
    }
}
