package com.andrewlevada.softskills;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.andrewlevada.softskills.logic.Roadmap;
import com.andrewlevada.softskills.logic.components.MoveToNextStepStatuses;
import com.andrewlevada.softskills.logic.components.tasks.AbstractTask;

public class Toolbox {
    public static int clamp(int value, int min, int max) {
        if (value >= max) return max;
        if (value <= min) return min;
        return value;
    }

    @Nullable
    public static View getLastChild(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1;  i >= 0; i--) {
            View child = parent.getChildAt(i);

            if (child != null) return child;
        }
        return null;
    }

    @Nullable
    public static View getLastChild(View parent) {
        if (!(parent instanceof ViewGroup)) return null;
        return getLastChild((ViewGroup) parent);
    }

    public static void closeFullAndMoveStep(final RoadmapActivity activity, final AbstractTask task, final int status) {
        activity.hideKeyboard();

        activity.closeFull(new RoadmapActivity.CloseFullHandler() {
            @Override
            public void next() {
                Roadmap.getInstance(activity).moveTaskToNextStep(task, status);
            }
        });
    }

    public static void closeFullAndMoveStep(final RoadmapActivity activity, final AbstractTask task) {
        closeFullAndMoveStep(activity, task, MoveToNextStepStatuses.STATUS_NORMAL);
    }
}
