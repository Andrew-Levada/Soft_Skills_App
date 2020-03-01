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
    @Override
    public boolean isAbleToExecute() {
        return true;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

    @Override @NonNull
    public Task clone() {
        return new YesNoTask(getActivity(), getDeltaTraitsListCopy(), headerText, previewText, fullText);
    }

    public YesNoTask(Activity activity, ArrayList<DeltaTraits> deltaTraitsList, String headerText, String previewText, String fullText) {
        super(activity, deltaTraitsList, headerText, previewText, fullText);
    }
}
