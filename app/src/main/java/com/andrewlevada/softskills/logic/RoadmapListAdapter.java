package com.andrewlevada.softskills.logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.softskills.logic.components.ComponentViewUnit;

import java.util.List;

public class RoadmapListAdapter extends RecyclerView.Adapter<RoadmapListAdapter.RoadmapViewHolder> {
    private List<ComponentViewUnit> dataset;
    private int index;

    public static class RoadmapViewHolder extends RecyclerView.ViewHolder {
        public View componentView;

        public RoadmapViewHolder(View v) {
            super(v);
            componentView = v;
        }
    }

    public RoadmapListAdapter(List<ComponentViewUnit> dataset) {
        this.dataset = dataset;
        index = 0;
    }

    @Override @NonNull
    public RoadmapListAdapter.RoadmapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = dataset.get(index).getView();

        index++;

        return new RoadmapViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) {
        //Fill content maybe
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}