package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;
    private View view;

    public View getView() {
        return view;
    }

    public Component getComponent() {
        return component;
    }

    public void requestUpdate() {
        view = component.getPreview();
    }

    public ComponentViewUnit(Component component) {
        this.component = component;
        view = component.getPreview();
    }
}
