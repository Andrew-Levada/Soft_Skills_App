package com.andrewlevada.softskills.logic.server;

import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.tasks.ComparableTask;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;

public interface ServerInterface {
    void initiateConnection(RoadmapActivity activity);

    ArrayList<ComparableTask> getFullTaskList();
    UserTraits getFullTraitsList();
}
