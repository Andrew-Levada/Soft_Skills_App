package com.andrewlevada.softskills.logic;

import android.app.Activity;
import android.renderscript.RenderScript;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.*;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class Roadmap {
    private ArrayList<ComponentViewUnit> story;
    private ArrayList<Component> active;
    private ArrayDeque<Component> queue;

    private UserTraits userTraits;
    private TaskManageThread taskManageThread;

    private Activity activity;
    private RecyclerView recyclerView;

    private static Roadmap instance;

    public static Roadmap getInstance(Activity activity) {
        if (instance == null)
            instance = new Roadmap(activity);

        return instance;
    }

    private Roadmap(Activity activity) {
        this.activity = activity;
        story = new ArrayList<>();
        active = new ArrayList<>();
        queue = new ArrayDeque<>();
        userTraits = UserTraits.getInstance();

        taskManageThread = new TaskManageThread();
        taskManageThread.setPriority(Thread.MIN_PRIORITY);
        taskManageThread.setName("TaskManageThread");
        taskManageThread.start();

        recyclerView = (RecyclerView)activity.findViewById(R.id.roadmap_recycler);
        setLayoutManagerToRecyclerView();
        setAdapterToRecyclerView();
    }

    private boolean moveComponentToNextStep(Component component) {
        int index = active.indexOf(component);

        if (index == -1) return false;
        return this.moveComponentToNextStep(index);
    }

    private boolean moveComponentToNextStep(int index) {
        Component component = active.get(index);

        if (component.moveToNextStep() == null) {
            active.remove(index);

            if (!requestQueue()) {
                //TODO: Ask manageThread to give task
            }

            return false;
        }

        story.add(component.generateComponentViewUnit());

        return true;
    }

    private boolean requestQueue() {
        if (queue.size() != 0) {
            addToActive(queue.poll());
            return true;
        } else return false;
    }

    private void addToActive(Component component) {
        active.add(component);
        story.add(component.generateComponentViewUnit());
    }

    private void setLayoutManagerToRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setAdapterToRecyclerView() {
        RecyclerView.Adapter adapter = new RoadmapListAdapter(story, activity.getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
