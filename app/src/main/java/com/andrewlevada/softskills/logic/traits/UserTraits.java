package com.andrewlevada.softskills.logic.traits;

import java.util.List;

public class UserTraits {
    private List<Trait> traits;

    private static UserTraits instance;

    private Trait findTrait(String name) {
        for (Trait trait: traits) {
            if (trait.getName().equals(name))
                return trait;
        }

        return null;
    }

    public int getTrait(int index) {
        return traits.get(index).level;
    }

    public int getTrait(String name) {
        Trait result = findTrait(name);

        if (result != null) return result.level;
        else return -1;
    }

    public void applyDeltaTrait(DeltaTrait deltaTrait) {
        Trait obj = findTrait(deltaTrait.getName());
        if (obj != null) obj.level += deltaTrait.delta;
    }

    public void applyDeltaTraits(List<DeltaTrait> deltaTraits) {
        for (DeltaTrait trait: deltaTraits) {
            applyDeltaTrait(trait);
        }
    }

    public static UserTraits getInstance() {
        if (instance == null)
            instance = new UserTraits();

        return instance;
    }

    private UserTraits() {

    }
}
