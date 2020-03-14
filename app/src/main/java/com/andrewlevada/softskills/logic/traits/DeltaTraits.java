package com.andrewlevada.softskills.logic.traits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DeltaTraits {
    private HashMap<Integer, Integer> deltaTraits;

    public Set<Integer> getKeySet() {
        return deltaTraits.keySet();
    }

    public int getValue(int key) {
        if (deltaTraits.get(key) != null)
            return deltaTraits.get(key);
        else return -1;
    }

    public DeltaTraits(HashMap<Integer, Integer> deltaTraits) {
        this.deltaTraits = deltaTraits;
    }
    public DeltaTraits(ArrayList<Integer> keys, ArrayList<Integer> values) {
        deltaTraits = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            deltaTraits.put(keys.get(i), values.get(i));
        }
    }
}
