package com.andrewlevada.softskills.logic.components.tasks;

public class TaskPreviewHolder {
    private String headerText;
    private String previewText;
    private boolean isNew;
    private int changeIndex;

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

    public TaskPreviewHolder(String headerText, String previewText, boolean isNew, int changeIndex) {
        this.headerText = headerText;
        this.previewText = previewText;
        this.isNew = isNew;
        this.changeIndex = changeIndex;
    }
}
