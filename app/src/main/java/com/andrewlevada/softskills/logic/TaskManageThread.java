package com.andrewlevada.softskills.logic;

import com.andrewlevada.softskills.logic.components.Component;

public class TaskManageThread extends Thread {
    public static final int TASKGEN_CODE = 1;

    private boolean running;
    private int code;
    private  GetTaskCallback callback;

    @Override
    public void run() {
        while (running) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (code) {
                case TASKGEN_CODE:

                    break;
            }
        }
    }

    public void requestStop() {
        running = false;
    }

    public TaskManageThread() {
        running = true;
    }

    public interface GetTaskCallback {
        Component executed();
    }

    public void requestTaskGeneration(GetTaskCallback callback) {
        this.callback = callback;
        code = TASKGEN_CODE;
        notify();
    }
}
