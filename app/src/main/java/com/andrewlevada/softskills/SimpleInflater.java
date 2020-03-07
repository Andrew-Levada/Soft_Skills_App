package com.andrewlevada.softskills;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

public class SimpleInflater {
    private Context context;
    private ViewGroup parent;

    public SimpleInflater(Context context, ViewGroup parent) {
        this.context = context;
        this.parent = parent;
    }

    public SimpleInflater(Activity activity, View parent) {
        this(activity.getApplicationContext(), (ViewGroup) parent);
    }

    @SuppressLint("ResourceType")
    public View inflate(@LayoutRes int id) {
        LayoutInflater.from(context)
                .inflate(id, parent, true);

        return Toolbox.getLastChild(parent);
    }
}
