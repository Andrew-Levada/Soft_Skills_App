package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;

    public View getView() {
        return component.getPreview();
    }

    public Component getComponent() {
        return component;
    }

    public ComponentViewUnit(Component component) {
        this.component = component;
    }
}
