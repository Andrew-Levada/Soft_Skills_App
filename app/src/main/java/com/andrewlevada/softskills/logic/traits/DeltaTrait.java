package com.andrewlevada.softskills.logic.traits;

public class DeltaTrait {
    private String name;
    public int delta;

    public String getName() {
        return name;
    }

    public DeltaTrait(String name, int delta) {
        this.name = name;
        this.delta = delta;
    }

    public DeltaTrait(String name) { this(name, 0); }
}
