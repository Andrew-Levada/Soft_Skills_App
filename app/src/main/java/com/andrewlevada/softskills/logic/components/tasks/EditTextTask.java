package com.andrewlevada.softskills.logic.components.tasks;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.SimpleInflater;
import com.andrewlevada.softskills.Toolbox;
import com.andrewlevada.softskills.logic.Roadmap;
import com.andrewlevada.softskills.logic.components.MoveToNextStepStatuses;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Random;

public class EditTextTask extends AbstractTask {
    private String text;
    private String review;

    @Override
    @NonNull
    public AbstractTask copy() {
        return new EditTextTask(getActivity(), getDeltaTraitsListCopy(), previewHolders, fullTexts);
    }

    @Override
    public boolean isAbleToExecute() {
        return true;
    }

    @Override
    public View getPreview() {
        View view = getPreviewBase();

        SimpleInflater inflater = new SimpleInflater(getActivity(), view);

        switch (getStep()) {
            case 1:

            case 3:
                Button button = inflater.inflate(R.layout.task_preview_button)
                        .findViewById(R.id.open_button);

                button.setText("Посмотреть");

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
        SimpleInflater inflater = new SimpleInflater(getActivity(), parent);

        switch (getStep()) {
            case 0:
                final TextInputEditText editTextInput = inflater.inflate(R.layout.task_full_edittext)
                        .findViewById(R.id.edit_text);

                inflater.inflate(R.layout.task_full_button_contained)
                        .findViewById(R.id.button)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (editTextInput.getText() == null) return;

                                String value = editTextInput.getText().toString().trim();

                                if (!value.equals("")) {
                                    text = value;
                                    Toolbox.closeFullAndMoveStep(getActivity(), itself, MoveToNextStepStatuses.STATUS_BACKGROUND);
                                }
                            }
                        });
                break;

            case 1:
                ((TextView) inflater.inflate(R.layout.task_full_outlined_text)
                        .findViewById(R.id.text)).setText(text);
                break;

            case 2:
                ((TextView) inflater.inflate(R.layout.task_full_outlined_text)
                        .findViewById(R.id.text)).setText(review);

                ((TextView) inflater.inflate(R.layout.task_full_textview)
                        .findViewById(R.id.text)).setText("Ваш текст:");

                ((TextView) inflater.inflate(R.layout.task_full_outlined_text)
                        .findViewById(R.id.text)).setText(text);

                inflater.inflate(R.layout.task_full_button_contained)
                        .findViewById(R.id.button)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toolbox.closeFullAndMoveStep(getActivity(), itself);
                            }
                        });
                break;

            case 3:
                ((TextView) inflater.inflate(R.layout.task_full_outlined_text)
                        .findViewById(R.id.text)).setText(review);

                ((TextView) inflater.inflate(R.layout.task_full_textview)
                        .findViewById(R.id.text)).setText("Ваш текст:");

                ((TextView) inflater.inflate(R.layout.task_full_outlined_text)
                        .findViewById(R.id.text)).setText(text);
                break;
        }
    }

    @Override
    public void performStepAction() {
        final AbstractTask itself = this;

        switch (getStep()) {
            case 1:
                //TODO: Send data to server and continusly check for task to be reviewed

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
                Toolbox.closeFullAndMoveStep(getActivity(), itself, MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS);
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
        previewHolders.add(new TaskPreviewHolder(header, shortTask, true, 0, true));
        previewHolders.add(new TaskPreviewHolder(header, shortTask, false, 0, false));
        previewHolders.add(new TaskPreviewHolder(header, shortReview, true, 0, true));
        previewHolders.add(new TaskPreviewHolder(header, shortReview, false, 1, false));

        ArrayList<String> fullTexts = new ArrayList<>(5);
        fullTexts.add(fullTask);
        fullTexts.add(fullTask);
        fullTexts.add(fullReview);
        fullTexts.add(fullReview);

        return new EditTextTask(activity, deltaTraitsList, previewHolders, fullTexts);
    }
}
