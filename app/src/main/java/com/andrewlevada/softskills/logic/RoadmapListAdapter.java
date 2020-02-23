package com.andrewlevada.softskills.logic;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.ComponentViewUnit;

import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

public class RoadmapListAdapter extends RecyclerView.Adapter<RoadmapListAdapter.RoadmapViewHolder> {
    private static final int SPACE_VIEW = -1;
    private static final int SPACEFILL_VIEW = -2;

    private List<ComponentViewUnit> dataset;
    private Context context;

    static class RoadmapViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public RoadmapViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public RoadmapListAdapter(List<ComponentViewUnit> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        position = position - 1;

        if (position == 0) {
            return SPACE_VIEW;
        }

        if (position == dataset.size()) {
            return SPACEFILL_VIEW;
        }

        return position;
    }

    @Override @NonNull
    public RoadmapListAdapter.RoadmapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == SPACE_VIEW || viewType == SPACEFILL_VIEW)
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.roadmap_recycleview_space, parent, false);
        else v = dataset.get(dataset.size() - viewType - 1).getView();

        if (viewType == SPACEFILL_VIEW)
            v.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorBackground, null));

        return new RoadmapViewHolder((v));
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) { }

    @Override
    public int getItemCount() {
        return dataset.size() + 2;
    }
}