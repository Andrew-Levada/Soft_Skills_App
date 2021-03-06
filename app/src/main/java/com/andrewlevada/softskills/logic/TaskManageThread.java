package com.andrewlevada.softskills.logic;

import android.os.Handler;
import android.os.Message;

import com.andrewlevada.softskills.RoadmapActivity;
import com.andrewlevada.softskills.logic.components.tasks.ComparableTask;
import com.andrewlevada.softskills.logic.components.tasks.Task;
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

    public TaskManageThread(RoadmapActivity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        running = true;
        rnd = new Random("hi".hashCode());

        userTraits = UserTraits.getInstance();
        taskList = new ArrayList<>();
    }

    @Override
    public synchronized void run() {
        server = new ServerProxy();
        server.initiateConnection(activity);

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

    public synchronized void requestTaskSelector() {
        taskCode = TASKSELECTOR_CODE;
        notify();
    }
    private Task selectTask() {
        HashMap<Integer, Double> multipliers = new HashMap<>();

        for (Integer key : userTraits.getTraitsKeySet()) {
            double value = userTraits.getTrait(key) / 100d;
            value = 1 - value * Math.sqrt(value);
            multipliers.put(key, value);
        }

        for (ComparableTask task : taskList) {
            double value = 0;
            DeltaTraits deltaTraits = task.getWrapped().getGeneralDeltaTraits();

            for (Integer key : deltaTraits.getKeySet()) {
                if (multipliers.get(key) != null)
                    value += deltaTraits.getValue(key) * multipliers.get(key);
            }

            value += rnd.nextDouble() * spread;

            task.setScore(value);
        }

        Collections.sort(taskList);

        for (int i = taskList.size() - 1; i >= 0; i--) {
            if (taskList.get(i).getWrapped().isAbleToExecute())
                return taskList.get(i).getWrapped().copy();
        }

        return null;
    }

    private void syncData() {
        taskList = server.getFullTaskList();

        for (ComparableTask task : taskList) {
            task.getWrapped().setActivity(activity);
        }

        userTraits = server.getFullTraitsList();
    }
}
