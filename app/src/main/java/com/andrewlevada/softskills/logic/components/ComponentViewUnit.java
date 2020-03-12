package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;
    private View view;
    private int storyIndex;

    private boolean updatable;

    public View getView() {
        return view;
    }

    public Component getComponent() {
        return component;
    }

    public void requestUpdate() {
        if (updatable) view = component.getPreview();
    }

    public int getStoryIndex() {
        return storyIndex;
    }

    public void setStoryIndex(int storyIndex) {
        if (storyIndex >= 0) this.storyIndex = storyIndex;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public ComponentViewUnit(Component component, boolean updatable) {
        this.component = component;
        this.updatable = updatable;

        view = component.getPreview();
    }
}
