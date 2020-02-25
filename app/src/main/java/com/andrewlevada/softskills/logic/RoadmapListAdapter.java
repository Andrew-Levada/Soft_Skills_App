package com.andrewlevada.softskills.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.ComponentViewUnit;

import java.util.List;

public class RoadmapListAdapter extends RecyclerView.Adapter<RoadmapListAdapter.RoadmapViewHolder> {
    private static final int HEADER_VIEW = -1;

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
        if (position == dataset.size()) {
            return HEADER_VIEW;
        }

        return position;
    }

    @Override @NonNull
    public RoadmapListAdapter.RoadmapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == HEADER_VIEW)
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.roadmap_recycleview_header, parent, false);
        else v = dataset.get(dataset.size() - viewType - 1).getView();

        return new RoadmapViewHolder((v));
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) { }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }
}