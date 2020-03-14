package com.andrewlevada.softskills.logic.server;

import android.content.res.Resources;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.tasks.ComparableTask;
import com.andrewlevada.softskills.logic.components.tasks.EditTextTask;
import com.andrewlevada.softskills.logic.components.tasks.Task;
import com.andrewlevada.softskills.logic.components.tasks.YesNoTask;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerProxy implements ServerInterface {
    private ArrayList<ComparableTask> taskList;

    @Override
    public void initiateConnection(RoadmapActivity activity) {
        taskList = new ArrayList<>();
        Resources res = activity.getResources();

        DeltaTraits deltaTraits = new DeltaTraits(new HashMap<Integer, Integer>());

        Task yesNoTask = YesNoTask.getInstance(null, deltaTraits, res.getString(R.string.yntask_header),
                res.getString(R.string.yntask_short), res.getString(R.string.yntask_full));

        Task edittextTask = EditTextTask.getInstance(null, deltaTraits,
                res.getString(R.string.ettask_header), res.getString(R.string.ettask_short_task),
                res.getString(R.string.ettask_full_task), res.getString(R.string.ettask_short_review),
                res.getString(R.string.ettask_full_review));

        taskList.add(new ComparableTask(yesNoTask));
        taskList.add(new ComparableTask(edittextTask));
    }


    @Override
    public ArrayList<ComparableTask> getFullTaskList() {
        return taskList;
    }

    @Override
    public UserTraits getFullTraitsList() {
        return UserTraits.getInstance();
    }
}
