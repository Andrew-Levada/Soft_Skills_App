package com.andrewlevada.softskills.logic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.logic.components.ComponentViewUnit;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class RoadmapListAdapter extends RecyclerView.Adapter<RoadmapListAdapter.RoadmapViewHolder> {
    private final float SCROLL_SPEED_MS_PER_INCH = 300f;

    private ArrayList<ComponentViewUnit> dataset;
    private ArrayDeque<ComponentViewUnit> queue;
    private Context context;
    private RecyclerView recyclerView;

    private boolean isAnimating;

    static class RoadmapViewHolder extends RecyclerView.ViewHolder {
        private boolean isUpdatable;

        public RoadmapViewHolder(View v) {
            super(v);
            isUpdatable = false;
        }

        public RoadmapViewHolder(View v, boolean isUpdatable) {
            super(v);
            this.isUpdatable = isUpdatable;
        }
    }

    public RoadmapListAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        context = recyclerView.getContext();
        dataset = new ArrayList<>();
        queue = new ArrayDeque<>();
        isAnimating = false;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override @NonNull
    public RoadmapListAdapter.RoadmapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewGroup item = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.roadmap_recycleview_container, parent, false);

        RoadmapViewHolder holder;

        if (viewType == 0) {
            LayoutInflater.from(context).inflate(R.layout.roadmap_recycleview_header, item, true);
            holder = new RoadmapViewHolder(item, false);
        } else {
            generateView(item, viewType - 1);
            holder = new RoadmapViewHolder(item, dataset.get(viewType - 1).isUpdatable());
        }

        addAnimate(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoadmapViewHolder holder, int position) {
        if (holder.isUpdatable) {
            ViewGroup parent = (ViewGroup) holder.itemView;
            parent.removeAllViews();
            generateView(parent, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size() + 1;
    }

    public void addElement(ComponentViewUnit viewUnit) {
        if (isAnimating) queue.add(viewUnit);
        else {
            isAnimating = true;
            dataset.add(viewUnit);
            notifyDataSetChanged();
        }
    }

    private void generateView(ViewGroup parent, int position) {
        View view = dataset.get(position).getView();

        if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);

        parent.addView(view);
        LayoutInflater.from(context).inflate(R.layout.component_line_base, parent, true);
    }

    private void addAnimate(RecyclerView.ViewHolder holder) {
        View item = holder.itemView;

        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(item, "Alpha", 1f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet animation = new AnimatorSet();
        animation.addListener(new LocalAnimatorListener());
        animation.play(alphaAnimation);

        animation.start();
    }

    class LocalAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return SCROLL_SPEED_MS_PER_INCH / displayMetrics.densityDpi;
                }
            };

            linearSmoothScroller.setTargetPosition(dataset.size());
            recyclerView.getLayoutManager().startSmoothScroll(linearSmoothScroller);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (!queue.isEmpty()) {
                dataset.add(queue.poll());
                notifyDataSetChanged();
            } else isAnimating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}