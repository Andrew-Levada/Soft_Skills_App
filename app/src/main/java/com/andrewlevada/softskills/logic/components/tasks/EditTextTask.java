package com.andrewlevada.softskills.logic.components.tasks;

import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.Toolbox;
import com.andrewlevada.softskills.logic.Roadmap;
import com.andrewlevada.softskills.logic.components.MoveToNextStepStatuses;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Random;

public class EditTextTask extends Task {
    private String text;
    private String review;

    @Override @NonNull
    public Task clone() {
        return new EditTextTask(getActivity(), getDeltaTraitsListCopy(), previewHolders, fullTexts);
    }

    private static boolean yes = true;
    @Override
    public boolean isAbleToExecute() {
        if (!yes) return false;
        yes = false;
        return true;
    }

    @Override
    public View getPreview() {
        View view = getPreviewBase();

        switch (getStep()) {
            case 1:

            case 3:
                Button button = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_preview_button, (ViewGroup) view, true)
                        .findViewById(R.id.open_button);

                button.setText("Посмотреть");

                final Task itself = this;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: Add AIL call here
                    }
                });
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
                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_edittext, (ViewGroup) parent, true);

                final TextInputEditText editTextInput = (TextInputEditText) parent.findViewById(R.id.edit_text);

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_button_contained, (ViewGroup) parent, true)
                        .findViewById(R.id.button)
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editTextInput.getText() == null) return;

                        String value = editTextInput.getText().toString().trim();

                        if (!value.equals("")) {
                            text = value;
                            Roadmap.getInstance(getActivity()).moveTaskToNextStep(itself, MoveToNextStepStatuses.STATUS_BACKGROUND);
                            getActivity().hideKeyboard();
                            getActivity().closeFull();
                        }
                    }
                });
                break;

            case 1:
                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_outlined_text, (ViewGroup) parent, true);

                ((TextView) parent.findViewById(R.id.text)).setText(text);
                break;

            case 2:
                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_outlined_text, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText(review);

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_textview, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText("Ваш текст:");

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_outlined_text, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText(text);

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_button_contained, (ViewGroup) parent, true)
                        .findViewById(R.id.button)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Roadmap.getInstance(getActivity()).moveTaskToNextStep(itself);
                                getActivity().closeFull();
                            }
                        });
                break;

            case 3:
                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_outlined_text, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText(review);

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_textview, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText("Ваш текст:");

                LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.task_full_outlined_text, (ViewGroup) parent, true);

                ((TextView) Toolbox.getLastChild(parent).findViewById(R.id.text)).setText(text);
                break;
        }
    }

    @Override
    public void performStepAction() {
        switch (getStep()) {
            case 1:
                //TODO: Send data to server and continusly check for task to be reviewed
                final Task itself = this;

                class DebugThread extends Thread {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000 + (new Random()).nextInt(10000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Log.e("DBG", "EditTextTask done");
                        review = "У вас получился замечательный отзыв.";
                        Roadmap.getInstance(getActivity()).addTaskToQueue(itself);
                    }
                }

                Thread thread = new DebugThread();
                thread.start();
                break;

            case 3:
                Roadmap.getInstance(getActivity()).moveTaskToNextStep(this, MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS);
                getActivity().closeFull();
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
