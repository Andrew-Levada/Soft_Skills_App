package com.andrewlevada.softskills.logic.components.tasks;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.logic.components.Component;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Task extends Component {
    private ArrayList<DeltaTraits> deltaTraitsList;
    private DeltaTraits generalDeltaTraits;

    public abstract boolean isAbleToExecute();
    @NonNull
    public abstract Task clone();

    public DeltaTraits getGeneralDeltaTraits() {
        return generalDeltaTraits;
    }

    public DeltaTraits getDeltaTraits() {
        return deltaTraitsList.get(getStep());
    }

    protected ArrayList<DeltaTraits> getDeltaTraitsListCopy() {
        return new ArrayList<>(deltaTraitsList);
    }

    private void countGeneralDeltaTraits() {
        HashMap<Integer, Integer> generalMap = new HashMap<>();

        for (DeltaTraits deltaTraits: deltaTraitsList) {
            for (Integer key: deltaTraits.getKeySet()) {
                if (generalMap.containsKey(key))
                    generalMap.put(key, generalMap.get(key) + deltaTraits.getValue(key));
                else generalMap.put(key, deltaTraits.getValue(key));
            }
        }

        generalDeltaTraits = new DeltaTraits(generalMap);
    }

    public Task(Activity activity, ArrayList<DeltaTraits> deltaTraitsList) {
        super(activity, deltaTraitsList.size() + 1);
        this.deltaTraitsList = deltaTraitsList;
        countGeneralDeltaTraits();
    }
}
