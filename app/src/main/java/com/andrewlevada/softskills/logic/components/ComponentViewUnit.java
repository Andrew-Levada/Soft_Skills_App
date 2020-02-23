package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;
    private int viewId;
    private View view;

    public int getViewId() {
        return viewId;
    }

    public View getView() {
        return view;
    }

    public Component getComponent() {
        return component;
    }

    public ComponentViewUnit(Component component) {
        this.component = component;
        viewId = component.getViewLayoutId();
        view = component.getView();
    }
}
