package com.andrewlevada.softskills.logic.components.tasks;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.Roadmap;
import com.andrewlevada.softskills.logic.components.MoveToNextStepStatuses;
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

    @Override
    public void fillFullView(View parent) {
        super.fillFullView(parent);

        View buttons = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.task_full_yesno, (ViewGroup) parent, true);

        final Task itself = this;

        buttons.findViewById(R.id.tips_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttons.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roadmap.getInstance(getActivity()).moveTaskToNextStep(itself, MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS);
                getActivity().closeFull();
            }
        });

        buttons.findViewById(R.id.failed_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Roadmap.getInstance(getActivity()).moveTaskToNextStep(itself, MoveToNextStepStatuses.STATUS_FINISHED_FAIL);
                getActivity().closeFull();
            }
        });
    }

    @Override @NonNull
    public Task clone() {
        return new YesNoTask(getActivity(), getDeltaTraitsListCopy(), headerText, previewText, fullText);
    }

    public YesNoTask(RoadmapActivity activity, ArrayList<DeltaTraits> deltaTraitsList, String headerText, String previewText, String fullText) {
        super(activity, deltaTraitsList, headerText, previewText, fullText);
    }
}
