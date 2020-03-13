package com.andrewlevada.softskills.logic.components.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.AbstractComponent;
import com.andrewlevada.softskills.logic.components.ComponentViewUnit;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractTask extends AbstractComponent implements Task {
    private ArrayList<DeltaTraits> deltaTraitsList;
    private DeltaTraits generalDeltaTraits;

    private int step;
    private int stepsCount;

    ArrayList<TaskPreviewHolder> previewHolders;
    ArrayList<String> fullTexts;

    public boolean hasNextStep() {
        return step < stepsCount - 1;
    }

    public boolean moveToNextStep() {
        if (hasNextStep()) {
            step++;
            return true;
        } else return false;
    }

    @Override
    public ComponentViewUnit generateComponentViewUnit() {
        return generateComponentViewUnit(previewHolders.get(step).isUpdatable());
    }

    public DeltaTraits getGeneralDeltaTraits() {
        return generalDeltaTraits;
    }

    @Nullable
    public DeltaTraits getDeltaTraits() {
        return deltaTraitsList.get(step);
    }

    ArrayList<DeltaTraits> getDeltaTraitsListCopy() {
        return new ArrayList<>(deltaTraitsList);
    }

    private void countGeneralDeltaTraits() {
        HashMap<Integer, Integer> generalMap = new HashMap<>();

        for (DeltaTraits deltaTraits: deltaTraitsList) {
            if (deltaTraits == null) continue;

            for (Integer key: deltaTraits.getKeySet()) {
                if (generalMap.containsKey(key))
                    generalMap.put(key, generalMap.get(key) + deltaTraits.getValue(key));
                else generalMap.put(key, deltaTraits.getValue(key));
            }
        }

        generalDeltaTraits = new DeltaTraits(generalMap);
    }

    View getPreviewBase() {
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.task_preview_base, (ViewGroup) getActivity().findViewById(R.id.roadmap_recycler), false);

        ((TextView)view.findViewById(R.id.componentHeader)).setText(previewHolders.get(step).getHeaderText());
        ((TextView)view.findViewById(R.id.componentText)).setText(previewHolders.get(step).getPreviewText());

        return view;
    }

    public boolean isMakingNewPreview() {
        return previewHolders.get(step).isNew();
    }

    public void effectExistingCVU() {
        if (previewHolders.get(step).isNew()) return;

        getViewUnit(previewHolders.get(step).getChangeIndex()).requestUpdate();
    }

    void fillFullViewBase(View parent) {
        ((TextView)parent.findViewById(R.id.fullHeader)).setText(previewHolders.get(step).getHeaderText());
        ((TextView)parent.findViewById(R.id.fullText)).setText(fullTexts.get(step));
    }

    int getStep() {
        return step;
    }

    public AbstractTask(RoadmapActivity activity, int stepsCount, ArrayList<DeltaTraits> deltaTraitsList,
                        ArrayList<TaskPreviewHolder> previewHolders, ArrayList<String> fullTexts) {
        super(activity);
        this.stepsCount = stepsCount;
        this.deltaTraitsList = deltaTraitsList;
        this.previewHolders = previewHolders;
        this.fullTexts = fullTexts;

        countGeneralDeltaTraits();
    }
}
