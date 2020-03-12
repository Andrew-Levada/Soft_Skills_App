package com.andrewlevada.softskills.logic.components.tasks;

public class TaskPreviewHolder {
    private String headerText;
    private String previewText;
    private boolean isNew;
    private int changeIndex;
    private boolean isUpdatable;

    public String getHeaderText() {
        return headerText;
    }

    public String getPreviewText() {
        return previewText;
    }

    public boolean isNew() {
        return isNew;
    }

    public int getChangeIndex() {
        return changeIndex;
    }

    public boolean isUpdatable() {
        return isUpdatable;
    }

    public TaskPreviewHolder(String headerText, String previewText, boolean isNew, int changeIndex, boolean isUpdatable) {
        this.headerText = headerText;
        this.previewText = previewText;
        this.isNew = isNew;
        this.changeIndex = changeIndex;
        this.isUpdatable = isUpdatable;
    }
}
