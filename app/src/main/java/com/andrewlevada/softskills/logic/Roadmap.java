package com.andrewlevada.softskills.logic;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.AnyThread;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.*;
import com.andrewlevada.softskills.logic.components.tasks.Task;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

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

        recyclerView = (RecyclerView)activity.findViewById(R.id.roadmap_recycler);
        setupRecyclerView();
    }

    @UiThread
    public boolean moveTaskToNextStep(Task task, int status) {
        int index = active.indexOf(task);

        if (index == -1) return false;
        return this.moveTaskToNextStep(index, status);
    }

    @UiThread
    public boolean moveTaskToNextStep(Task task) {
        return moveTaskToNextStep(task, MoveToNextStepStatuses.STATUS_NORMAL);
    }

    private boolean moveTaskToNextStep(int index, int status) {
        Task task = active.get(index);

        if (task != ongoingTask) taskQueue.add(task);

        if (task.getDeltaTraits() != null)
            userTraits.applyDeltaTraits(task.getDeltaTraits());

        if (task.moveToNextStep() == null) {
            active.remove(index);

            if (status == MoveToNextStepStatuses.STATUS_FINISHED_SUCCESS)
                addToStory(new ComponentFinished(activity, true));
            else addToStory(new ComponentFinished(activity, false));

            ongoingTask = null;
            requestNewTask();
            return false;
        }

        task.performStepAction();

        if (status == MoveToNextStepStatuses.STATUS_BACKGROUND) {
            ongoingTask = null;
            requestNewTask();
        }

        if (task.makesNewPreview()) addToStory(task);
        else {
            int i = task.effectExistingCVU();
            if (i != -1) recyclerView.getAdapter().notifyDataSetChanged();
        }

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

    private void addToActive(Task task) {
        active.add(task);
        addToStory(task);
    }

    private void addToStory(@NonNull Component component) {
        ComponentViewUnit viewUnit = component.generateComponentViewUnit();
        viewUnit.setStoryIndex(story.size());
        story.add(viewUnit);

        recyclerView.getAdapter().notifyItemInserted(story.size());
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, story.size());
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new RoadmapListAdapter(story, activity.getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public boolean fillFullView() {
        if (active.size() != 0 && ongoingTask != null) {
            ViewGroup parent = (ViewGroup) activity.findViewById(R.id.full_layout);
            for (int i = parent.getChildCount() - 1; i >= 0; i--) {
                int id = parent.getChildAt(i).getId();

                if (id == R.id.fullHeader) continue;
                if (id == R.id.fullText) continue;

                parent.removeViewAt(i);
            }

            ongoingTask.fillFullView(activity.findViewById(R.id.full_layout));

            return true;
        } else return false;
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

    @AnyThread
    public void addTaskToQueue(Task task) {
        if (task == null) return;
        if (ongoingTask == null) {
            moveTaskToNextStep(task);
            return;
        }

        taskQueue.add(task);
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
