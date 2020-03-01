package com.andrewlevada.softskills.logic.components.tasks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.Component;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Task extends Component {
    private ArrayList<DeltaTraits> deltaTraitsList;
    private DeltaTraits generalDeltaTraits;

    String headerText;
    String previewText;
    String fullText;

    public abstract boolean isAbleToExecute();
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

    @Override
    public View getPreview() {
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.task_preview_base, (ViewGroup)getActivity().findViewById(R.id.roadmap_recycler), false);

        ((TextView)view.findViewById(R.id.componentHeader)).setText(headerText);
        ((TextView)view.findViewById(R.id.componentText)).setText(previewText);

        return view;
    }

    public void fillFullView() {
        ((TextView)getActivity().findViewById(R.id.fullHeader)).setText(headerText);
        ((TextView)getActivity().findViewById(R.id.fullText)).setText(fullText);
    }

    public Task(Activity activity, ArrayList<DeltaTraits> deltaTraitsList, String headerText, String previewText, String fullText) {
        super(activity, deltaTraitsList.size() + 1);
        this.deltaTraitsList = deltaTraitsList;
        this.headerText = headerText;
        this.previewText = previewText;
        this.fullText = fullText;
        countGeneralDeltaTraits();
    }
}
