package com.andrewlevada.softskills.logic;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.*;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Roadmap {
    private ArrayList<ComponentViewUnit> story;
    private ArrayList<Component> active;

    private UserTraits userTraits;

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
        userTraits = UserTraits.getInstance();

        recyclerView = (RecyclerView)activity.findViewById(R.id.roadmap_recycler);
        setLayoutManagerToRecyclerView();
        setAdapterToRecyclerView();
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
