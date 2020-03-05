package com.andrewlevada.softskills.logic.components.tasks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.Component;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Task extends Component {
    private ArrayList<DeltaTraits> deltaTraitsList;
    private DeltaTraits generalDeltaTraits;

    private int step;
    private int stepsCount;

    ArrayList<TaskPreviewHolder> previewHolders;
    ArrayList<String> fullTexts;

    @NonNull
    public abstract Task clone();
    public abstract boolean isAbleToExecute();

    public boolean hasNextStep() {
        return step < stepsCount - 1;
    }

    @Nullable
    public Task moveToNextStep() {
        if (hasNextStep()) {
            step++;
            return this;
        } else return null;
    }

    public DeltaTraits getGeneralDeltaTraits() {
        return generalDeltaTraits;
    }

    public DeltaTraits getDeltaTraits() {
        return deltaTraitsList.get(step);
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

    @Override
    public View getPreview() {
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.task_preview_base, (ViewGroup)getActivity().findViewById(R.id.roadmap_recycler), false);

        ((TextView)view.findViewById(R.id.componentHeader)).setText(previewHolders.get(step).getHeaderText());
        ((TextView)view.findViewById(R.id.componentText)).setText(previewHolders.get(step).getPreviewText());

        return view;
    }

    public boolean makesNewPreview() {
        return previewHolders.get(step).isNew();
    }

    public void effectExistingCVU() {
        if (previewHolders.get(step).isNew()) return;

        getViewUnit(previewHolders.get(step).getChangeIndex()).requestUpdate();
    }

    public void fillFullView(View parent) {
        ((TextView)parent.findViewById(R.id.fullHeader)).setText(previewHolders.get(step).getHeaderText());
        ((TextView)parent.findViewById(R.id.fullText)).setText(fullTexts.get(step));
    }

    public Task(RoadmapActivity activity, int stepsCount, ArrayList<DeltaTraits> deltaTraitsList,
                ArrayList<TaskPreviewHolder> previewHolders, ArrayList<String> fullTexts) {
        super(activity);
        this.stepsCount = stepsCount;
        this.deltaTraitsList = deltaTraitsList;
        this.previewHolders = previewHolders;
        this.fullTexts = fullTexts;

        countGeneralDeltaTraits();
    }
}
