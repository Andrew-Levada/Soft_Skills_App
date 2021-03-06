package com.andrewlevada.softskills.logic;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.Component;
import com.andrewlevada.softskills.logic.components.ComponentFinished;
import com.andrewlevada.softskills.logic.components.ComponentViewUnit;
import com.andrewlevada.softskills.logic.components.MoveToNextStepStatuses;
import com.andrewlevada.softskills.logic.components.tasks.Task;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Roadmap {
    private ArrayList<ComponentViewUnit> story;
    private ArrayList<Task> active;
    private ArrayDeque<Component> queue;
    private ArrayDeque<Task> taskQueue;

    private UserTraits userTraits;
    private TaskManageThread taskManageThread;
    private Handler taskManageHandler;

    private RoadmapActivity activity;
    private RecyclerView recyclerView;
    private RoadmapListAdapter recyclerAdapter;

    @Nullable
    private Task ongoingTask;

    private static Roadmap instance;


    public static Roadmap getInstance(RoadmapActivity activity) {
        if (instance == null)
            instance = new Roadmap(activity);

        return instance;
    }

    private Roadmap(RoadmapActivity activity) {
        this.activity = activity;
        story = new ArrayList<>();
        active = new ArrayList<>();
        queue = new ArrayDeque<>();
        taskQueue = new ArrayDeque<>();
        userTraits = UserTraits.getInstance();

        taskManageHandler = new TaskManageHandler();

        taskManageThread = new TaskManageThread(activity, taskManageHandler);
        taskManageThread.setPriority(Thread.MIN_PRIORITY);
        taskManageThread.setName("TaskManageThread");
        taskManageThread.start();

        recyclerView = (RecyclerView) activity.findViewById(R.id.roadmap_recycler);
        setupRecyclerView();
    }


    public boolean moveTaskToNextStep(Task task, int status) {
        int index = active.indexOf(task);

        if (index == -1) return false;
        return this.moveTaskToNextStep(index, status);
    }

    public boolean moveTaskToNextStep(Task task) {
        return moveTaskToNextStep(task, MoveToNextStepStatuses.STATUS_NORMAL);
    }

    private boolean moveTaskToNextStep(int index, int status) {
        Task task = active.get(index);

        if (task != ongoingTask) {
            taskQueue.add(task);
            return true;
        }

        if (task.getDeltaTraits() != null)
            userTraits.applyDeltaTraits(task.getDeltaTraits());

        if (!task.moveToNextStep()) {
            active.remove(index);

            if (status == MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS)
                addToStory(new ComponentFinished(activity, true));
            else addToStory(new ComponentFinished(activity, false));

            ongoingTask = null;
            requestNewTask();
            return false;
        }

        if (status == MoveToNextStepStatuses.STATUS_BACKGROUND) {
            ongoingTask = null;
            requestNewTask();
        }

        if (task.isMakingNewPreview()) addToStory(task);
        else task.effectExistingCVU();

        task.performStepAction();

        return true;
    }


    private boolean requestQueue() {
        if (queue.size() != 0) {
            addToStory(queue.poll());
            return true;
        } else return false;
    }

    private boolean requestTaskQueue() {
        if (taskQueue.size() != 0) {
            Task task = taskQueue.poll();
            ongoingTask = task;
            moveTaskToNextStep(task);
            return true;
        } else return false;
    }

    public void addTaskToQueue(Task task) {
        if (task == null) return;
        if (ongoingTask == null) {
            moveTaskToNextStep(task);
            return;
        }

        taskQueue.add(task);
    }

    private void requestNewTask() {
        if (active.size() >= 3) return;
        if (ongoingTask != null) return;
        if (requestQueue()) {
            requestNewTask();
            return;
        }
        if (requestTaskQueue()) return;

        //Job schedule this
        taskManageThread.requestTaskSelector();
    }


    private void addToActive(Task task) {
        active.add(task);
        addToStory(task);
    }

    private void addToStory(@NonNull Component component) {
        ComponentViewUnit viewUnit = component.generateComponentViewUnit();
        viewUnit.setStoryIndex(story.size());
        story.add(viewUnit);

        recyclerAdapter.addElement(viewUnit);
    }


    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        RoadmapListAdapter adapter = new RoadmapListAdapter(recyclerView);
        recyclerAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }


    public boolean fillFullView() {
        if (active.size() != 0 && ongoingTask != null) {
            ongoingTask.fillFullView(activity.findViewById(R.id.full_layout));
            return true;
        } else return false;
    }


    public void testAction() {
        requestNewTask();
    }


    private class TaskManageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TaskManageThread.TASKSELECTOR_CODE:
                    if (msg.obj != null) {
                        Task task = (Task) msg.obj;
                        ongoingTask = task;
                        addToActive(task);
                    } else {
                        //TODO: Out of acceptable tasks
                        Log.e("DBG", "OUT OF TASKS");
                    }
                    break;
            }
        }
    }
}
