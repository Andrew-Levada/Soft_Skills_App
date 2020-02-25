package com.andrewlevada.softskills.logic.traits;

import com.andrewlevada.softskills.Toolbox;

import java.util.HashMap;
import java.util.Set;

public class UserTraits {
    private HashMap<Integer, Integer> traits;
    private HashMap<String, Integer> traitNames;

    private static UserTraits instance;

    public int getTrait(int key) {
        Integer value = traits.get(key);
        if (value != null) return value;
        else return -1;
    }

    public int getTrait(String name) {
        if (traitNames.get(name) != null)
            return getTrait(traitNames.get(name));
        else return -1;
    }

    public Set<Integer> getTraitsKeySet() {
        return traits.keySet();
    }

    public void applyDeltaTraits(DeltaTraits deltaTraits) {
        for (int key: deltaTraits.getKeySet()) {
            if (traits.containsKey(key))
                traits.put(key,
                        Toolbox.clamp(traits.get(key) + deltaTraits.getValue(key), 1, 100));
        }
    }

    public static UserTraits getInstance() {
        if (instance == null)
            instance = new UserTraits();

        return instance;
    }

    private UserTraits() {
        traits = new HashMap<>();
        traitNames = new HashMap<>();
    }
}
