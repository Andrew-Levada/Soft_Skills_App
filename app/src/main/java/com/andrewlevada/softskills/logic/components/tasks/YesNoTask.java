package com.andrewlevada.softskills.logic.components.tasks;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

import java.util.ArrayList;

public class YesNoTask extends Task {
    private String headerText;
    private String previewText;

    @Override
    public boolean isAbleToExecute() {
        return true;
    }

    @Override
    public View getView() {
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.task_yesno, (ViewGroup)getActivity().findViewById(R.id.roadmap_recycler), false);

        ((TextView)view.findViewById(R.id.componentHeader)).setText(headerText);
        ((TextView)view.findViewById(R.id.componentText)).setText(previewText);

        return view;
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.task_yesno;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

    @NonNull
    @Override
    public Task clone() {
        return new YesNoTask(getActivity(), getDeltaTraitsListCopy(), headerText, previewText);
    }

    public YesNoTask(Activity activity, ArrayList<DeltaTraits> deltaTraitsList, String headerText, String previewText) {
        super(activity, deltaTraitsList);
        this.headerText = headerText;
        this.previewText = previewText;
    }
}
