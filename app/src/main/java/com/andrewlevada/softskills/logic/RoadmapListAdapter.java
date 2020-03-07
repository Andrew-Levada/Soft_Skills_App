package com.andrewlevada.softskills.logic;

import android.animation.AnimatorSet;
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
        public RoadmapViewHolder(View v) {
            super(v);
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

        return new RoadmapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) {
        ViewGroup item = (ViewGroup) holder.itemView;
        item.removeAllViews();

        if (position == 0) {
            LayoutInflater.from(context) .inflate(R.layout.roadmap_recycleview_header, item, true);
        } else {
            View view = dataset.get(position - 1).getView();
            item.addView(view);
            LayoutInflater.from(context) .inflate(R.layout.component_line_base, item, true);
        }

        item.setAlpha(0f);
        item.setTranslationY(20);

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(item, "Alpha", 1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator yAnimation = ObjectAnimator.ofFloat(item, "TranslationY", 0f);
        yAnimation.setDuration(1000);
        yAnimation.setInterpolator(new DecelerateInterpolator());

        final AnimatorSet animation = new AnimatorSet();
        animation.play(alphaAnimation)
                 .with(yAnimation);

        animation.start();
    }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }
}