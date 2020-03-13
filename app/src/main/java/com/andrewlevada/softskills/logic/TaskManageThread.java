package com.andrewlevada.softskills.logic;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.tasks.ComparableTask;
import com.andrewlevada.softskills.logic.components.tasks.EditTextTask;
import com.andrewlevada.softskills.logic.components.tasks.AbstractTask;
import com.andrewlevada.softskills.logic.components.tasks.Task;
import com.andrewlevada.softskills.logic.components.tasks.YesNoTask;
import com.andrewlevada.softskills.logic.server.ServerInterface;
import com.andrewlevada.softskills.logic.server.ServerProxy;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class TaskManageThread extends Thread {
    public static final int TASKSELECTOR_CODE = 1;

    private static final int spread = 2;

    private ServerInterface server;

    private boolean running;
    private int taskCode;
    private Handler handler;

    private ArrayList<ComparableTask> taskList;
    private UserTraits userTraits;

    private RoadmapActivity activity;
    private Random rnd;

    @Override
    public synchronized void run() {
        server = new ServerProxy();

        syncData();

        while (running) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (taskCode) {
                case TASKSELECTOR_CODE:
                    Message msg = new Message();
                    msg.what = taskCode;
                    msg.obj = selectTask();
                    handler.sendMessage(msg);
                    break;
            }
        }
    }

    public void requestStop() {
        running = false;
    }

    public TaskManageThread(RoadmapActivity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        running = true;
        rnd = new Random("hi".hashCode());

        userTraits = UserTraits.getInstance();
        taskList = new ArrayList<>();
    }

    public interface GetTaskCallback {
        void finished(@Nullable AbstractTask result);
    }

    public synchronized void requestTaskSelector() {
        taskCode = TASKSELECTOR_CODE;
        notify();
    }

    private Task selectTask() {
        ArrayList<Double> scores = new ArrayList<>(taskList.size() + 2);
        HashMap<Integer, Double> multipliers = new HashMap<>();

        for (Integer key: userTraits.getTraitsKeySet()) {
            double value = userTraits.getTrait(key) / 100d;
            value = 1 - value * Math.sqrt(value);
            multipliers.put(key, value);
        }

        for (ComparableTask task: taskList) {
            double value = 0;
            DeltaTraits deltaTraits = task.getWrapped().getGeneralDeltaTraits();

            for (Integer key: deltaTraits.getKeySet()) {
                if (multipliers.get(key) != null)
                    value += deltaTraits.getValue(key) * multipliers.get(key);
            }

            value += rnd.nextDouble() * spread;
            
            task.setScore(value);
        }

        Collections.sort(taskList);

        for (int i = taskList.size() - 1; i >= 0; i--) {
            if (taskList.get(i).getWrapped().isAbleToExecute()) return taskList.get(i).getWrapped().copy();
        }

        return null;
    }

    private void syncData() {
        taskList = server.getFullTaskList(activity);
        userTraits = server.getFullTraitsList();
    }

    private void updateTaskList() {
        Resources res = activity.getResources();

        DeltaTraits deltaTraits = new DeltaTraits(new HashMap<Integer, Integer>());

        AbstractTask yesNoTask = YesNoTask.getInstance(activity, deltaTraits, res.getString(R.string.yntask_header),
                res.getString(R.string.yntask_header),  res.getString(R.string.yntask_header));

        AbstractTask edittextTask = EditTextTask.getInstance(activity, deltaTraits,
                res.getString(R.string.ettask_header), res.getString(R.string.ettask_short_task),
                res.getString(R.string.ettask_full_task), res.getString(R.string.ettask_short_review),
                res.getString(R.string.ettask_full_review));

        taskList.add(new ComparableTask(yesNoTask));
        taskList.add(new ComparableTask(edittextTask));
    }
}
