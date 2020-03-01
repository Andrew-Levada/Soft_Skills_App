package com.andrewlevada.softskills.logic;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
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

    private UserTraits userTraits;
    private TaskManageThread taskManageThread;
    private Handler taskManageHandler;

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

        taskManageHandler = new TaskManageHandler();

        taskManageThread = new TaskManageThread(activity, taskManageHandler);
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
        Task component = active.get(index);

        userTraits.applyDeltaTraits(component.getDeltaTraits());

        if (component.moveToNextStep() == null) {
            active.remove(index);
            startNewComponent();
            return false;
        }

        story.add(component.generateComponentViewUnit());
        recyclerView.getAdapter().notifyDataSetChanged();

        return true;
    }

    private void startNewComponent() {
        if (!requestQueue()) {
            taskManageThread.requestTaskSelector();
        } else startNewComponent();
    }

    private boolean requestQueue() {
        if (queue.size() != 0) {
            addToStory(queue.poll());
            return true;
        } else return false;
    }

    private void addToActive(Task task) {
        active.add(task);
        addToStory(task);
    }

    private void addToStory(@NonNull Component component) {
        story.add(component.generateComponentViewUnit());
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, 0);
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

    public void fillFullView() {
        if (active.size() != 0) active.get(0).fillFullView();
    }

    public void testAction() {
        startNewComponent();
    }

    private class TaskManageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case TaskManageThread.TASKSELECTOR_CODE:
                    if (msg.obj != null) {
                        addToActive((Task)msg.obj);
                    } else {
                        //TODO: Out of acceptable tasks
                        Log.e("DBG", "OUT OF TASKS");
                    }
                    break;
            }
        }
    }
}
