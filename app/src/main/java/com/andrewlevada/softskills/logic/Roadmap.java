package com.andrewlevada.softskills.logic;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.*;
import com.andrewlevada.softskills.logic.traits.UserTraits;

import java.util.ArrayList;

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
    }
    
    public void startTest() {
        setLayoutManagerToRecyclerView();

        Component test = new ComponentText(activity, "Тестовый текст", false);
        story.add(new ComponentViewUnit(test));

        test = new ComponentText(activity, "Тестовый текст, однако весьма и весьма длинный и интересный. Вот.", false);
        story.add(new ComponentViewUnit(test));

        test = new ComponentText(activity, "Я - Заголовок", true);
        story.add(new ComponentViewUnit(test));

        setAdapterToRecyclerView();
    }

    private void setLayoutManagerToRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setAdapterToRecyclerView() {
        RecyclerView.Adapter adapter = new RoadmapListAdapter(story);
        recyclerView.setAdapter(adapter);
    }
}
