package com.andrewlevada.softskills.logic.server;

import android.content.res.Resources;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.tasks.*;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerProxy implements ServerInterface {

    @Override
    public ArrayList<ComparableTask> getFullTaskList(RoadmapActivity activity) {
        ArrayList<ComparableTask> taskList = new ArrayList<>();
        Resources res = activity.getResources();

        DeltaTraits deltaTraits = new DeltaTraits(new HashMap<Integer, Integer>());

        AbstractTask yesNoTask = YesNoTask.getInstance(activity, deltaTraits, res.getString(R.string.yntask_header),
                res.getString(R.string.yntask_short),  res.getString(R.string.yntask_full));

        AbstractTask edittextTask = EditTextTask.getInstance(activity, deltaTraits,
                res.getString(R.string.ettask_header), res.getString(R.string.ettask_short_task),
                res.getString(R.string.ettask_full_task), res.getString(R.string.ettask_short_review),
                res.getString(R.string.ettask_full_review));

        taskList.add(new ComparableTask(yesNoTask));
        taskList.add(new ComparableTask(edittextTask));

        return taskList;
    }

    @Override
    public UserTraits getFullTraitsList() {
        return UserTraits.getInstance();
    }
}
