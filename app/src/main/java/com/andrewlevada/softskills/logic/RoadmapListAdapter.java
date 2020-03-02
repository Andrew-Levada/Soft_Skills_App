package com.andrewlevada.softskills.logic;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

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
        if (position == 0) {
            return HEADER_VIEW;
        }

        return position - 1;
    }

    @Override @NonNull
    public RoadmapListAdapter.RoadmapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roadmap_recycleview_container, parent, false);

        if (viewType == HEADER_VIEW) {
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.roadmap_recycleview_header, view, true);
        } else {
            view.addView(dataset.get(viewType).getView());
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.component_line_base, view, true);
        }

        view.setAlpha(0f);
        ObjectAnimator fabTranslation = ObjectAnimator.ofFloat(view, "Alpha", 1f);
        fabTranslation.setDuration(700);
        fabTranslation.setInterpolator(new DecelerateInterpolator());
        fabTranslation.start();

        return new RoadmapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }
}