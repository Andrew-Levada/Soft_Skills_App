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
import com.andrewlevada.softskills.SimpleInflater;
import com.andrewlevada.softskills.Toolbox;
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
    public View getPreview() {
        return getPreviewBase();
    }

    @Override
    public void fillFullView(View parent) {
        fillFullViewBase(parent);

        SimpleInflater inflater = new SimpleInflater(getActivity(), parent);

        View buttons = inflater.inflate(R.layout.task_full_yesno);

        final Task itself = this;

        buttons.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toolbox.closeFullAndMoveStep(getActivity(), itself, MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS);
            }
        });

        buttons.findViewById(R.id.failed_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toolbox.closeFullAndMoveStep(getActivity(), itself, MoveToNextStepStatuses.STATUS_FINISHED_FAIL);
            }
        });
    }

    @Override
    public void performStepAction() {

    }

    @Override @NonNull
    public Task clone() {
        return new YesNoTask(getActivity(), getDeltaTraitsListCopy(), previewHolders, fullTexts);
    }

    private YesNoTask(RoadmapActivity activity, ArrayList<DeltaTraits> deltaTraitsList,
                      ArrayList<TaskPreviewHolder> previewHolders, ArrayList<String> fullTexts) {
        super(activity, 1, deltaTraitsList, previewHolders, fullTexts);
    }

    public static YesNoTask getInstance(RoadmapActivity activity, DeltaTraits deltaTraits,
                                        String headerText, String previewText, String fullText) {
        ArrayList<DeltaTraits> deltaTraitsList = new ArrayList<>(2);
        deltaTraitsList.add(deltaTraits);

        ArrayList<TaskPreviewHolder> previewHolders = new ArrayList<>(2);
        previewHolders.add(new TaskPreviewHolder(headerText, previewText, true, 0, false));

        ArrayList<String> fullTexts = new ArrayList<>();
        fullTexts.add(fullText);

        return new YesNoTask(activity, deltaTraitsList, previewHolders, fullTexts);
    }
}
