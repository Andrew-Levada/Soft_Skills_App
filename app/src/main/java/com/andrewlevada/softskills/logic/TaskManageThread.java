package com.andrewlevada.softskills.logic;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.logic.components.tasks.ComparableTask;
import com.andrewlevada.softskills.logic.components.tasks.Task;
import com.andrewlevada.softskills.logic.components.tasks.YesNoTask;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TaskManageThread extends Thread {
    public static final int TASKSELECTOR_CODE = 1;

    private boolean running;
    private int taskCode;
    private Handler handler;

    private UserTraits userTraits;
    private ArrayList<ComparableTask> taskList;

    private Activity activity;

    @Override
    public synchronized void run() {
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

    public TaskManageThread(Activity activity, Handler handler) {
        this.activity = activity;
        this.handler = handler;
        running = true;

        userTraits = UserTraits.getInstance();
        taskList = new ArrayList<>();

        updateTaskList();
    }

    public interface GetTaskCallback {
        void finished(@Nullable Task result);
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

            scores.add(value);
        }

        Collections.sort(taskList);

        for (int i = taskList.size() - 1; i >= 0; i--) {
            if (taskList.get(i).getWrapped().isAbleToExecute()) return taskList.get(i).getWrapped().clone();
        }

        return null;
    }

    private void updateTaskList() {
        ArrayList<DeltaTraits> a = new ArrayList<>();
        a.add(new DeltaTraits(new HashMap<Integer, Integer>()));

        Task test = new YesNoTask(activity, a, "Тестовый заголовок", "Немного текста про лорем ипсум", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.");
        taskList.add(new ComparableTask(test));
    }
}
