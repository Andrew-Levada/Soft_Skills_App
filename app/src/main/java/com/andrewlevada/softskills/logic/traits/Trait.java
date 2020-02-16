package com.andrewlevada.softskills.logic.traits;

public class Trait {
    private String name;
    public int level;

    public String getName() {
        return name;
    }

    public Trait(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public Trait(String name) { this(name, 0); }
}
