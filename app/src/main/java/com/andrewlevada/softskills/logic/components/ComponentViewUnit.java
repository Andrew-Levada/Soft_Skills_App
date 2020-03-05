package com.andrewlevada.softskills.logic.components;

import android.view.View;

public class ComponentViewUnit {
    private Component component;
    private View view;
    private int storyIndex;

    public View getView() {
        return view;
    }

    public Component getComponent() {
        return component;
    }

    public void requestUpdate() {
        view = component.getPreview();
    }

    public int getStoryIndex() {
        return storyIndex;
    }

    public void setStoryIndex(int storyIndex) {
        if (storyIndex >= 0) this.storyIndex = storyIndex;
    }

    public ComponentViewUnit(Component component) {
        this.component = component;

        requestUpdate();
    }
}
