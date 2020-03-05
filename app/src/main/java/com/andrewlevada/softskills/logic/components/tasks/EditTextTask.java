package com.andrewlevada.softskills.logic.components.tasks;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.Roadmap;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditTextTask extends Task {
    private String text;
    private String review;

    @Override @NonNull
    public Task clone() {
        return new EditTextTask(getActivity(), getDeltaTraitsListCopy(), previewHolders, fullTexts);
    }

    @Override
    public boolean isAbleToExecute() {
        return true;
    }

    @Override
    public View getPreview() {
        View view = getPreviewBase();

        switch (getStep()) {
            case 1:
                Button button = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_preview_button, (ViewGroup) view, true)
                        .findViewById(R.id.open_button);

                button.setText("Посмотреть");

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: Add AIL call here
                    }
                });
                break;

            case 3:

                break;
        }

        return view;
    }

    @Override
    public void fillFullView(View parent) {
        fillFullViewBase(parent);

        final EditTextTask itself = this;

        switch (getStep()) {
            case 0:
                View view = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_edittext, (ViewGroup) parent, true);

                final TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.edit_text);

                view.findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText() == null) return;

                        String value = editText.getText().toString().trim();

                        if (!value.equals("")) {
                            text = value;
                            Roadmap.getInstance(getActivity()).moveTaskToNextStep(itself);
                            getActivity().hideKeyboard();
                            getActivity().closeFull();
                        }
                    }
                });
                break;

            case 1:

                break;

            case 2:

                break;

            case 3:

                break;
        }
    }

    @Override
    public void performStepAction() {
        switch (getStep()) {
            case 1:

                break;

            case 2:

                break;
        }
    }

    private EditTextTask(RoadmapActivity activity, ArrayList<DeltaTraits> deltaTraitsList,
                         ArrayList<TaskPreviewHolder> previewHolders, ArrayList<String> fullTexts) {
        super(activity, 4, deltaTraitsList, previewHolders, fullTexts);

        review = null;
    }

    public static EditTextTask getInstance(RoadmapActivity activity, DeltaTraits baseDeltaTraits,
                                           String header, String shortTask, String fullTask, String shortReview, String fullReview) {
        ArrayList<DeltaTraits> deltaTraitsList = new ArrayList<>(5);
        deltaTraitsList.add(baseDeltaTraits);
        deltaTraitsList.add(null);
        deltaTraitsList.add(null);
        deltaTraitsList.add(null);

        ArrayList<TaskPreviewHolder> previewHolders = new ArrayList<>(5);
        previewHolders.add(new TaskPreviewHolder(header, shortTask, true, 0));
        previewHolders.add(new TaskPreviewHolder(header, shortTask, false, 0));
        previewHolders.add(new TaskPreviewHolder(header, shortReview, true, 0));
        previewHolders.add(new TaskPreviewHolder(header, shortReview, false, 1));

        ArrayList<String> fullTexts = new ArrayList<>(5);
        fullTexts.add(fullTask);
        fullTexts.add(fullTask);
        fullTexts.add(fullReview);
        fullTexts.add(fullReview);

        return new EditTextTask(activity, deltaTraitsList, previewHolders, fullTexts);
    }
}
