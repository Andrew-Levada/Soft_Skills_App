package com.andrewlevada.softskills.logic.components.tasks;

public class ComparableTask implements Comparable<ComparableTask> {
    private Task wrapped;
    private double score;

    public ComparableTask(Task task) {
        wrapped = task;
    }

    @Override
    public int compareTo(ComparableTask other) {
        if (score - other.getScore() == 0) return 0;
        else if (score > other.getScore()) return 1;
        else return -1;
    }

    public Task getWrapped() {
        return wrapped;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
