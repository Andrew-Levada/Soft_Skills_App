package com.andrewlevada.softskills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionValues;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.andrewlevada.softskills.logic.Roadmap;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RoadmapActivity extends AppCompatActivity {
    private Roadmap roadmap;
    private Point display;
    private int statusbarHeight = 0;

    private BottomAppBar bar;
    private FloatingActionButton fab;
    private ConstraintLayout constraintLayout;

    private int fabHeight;

    private boolean isFullOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);

        roadmap = Roadmap.getInstance(this);
        display = new Point();
        getWindowManager().getDefaultDisplay().getSize(display);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusbarHeight = getResources().getDimensionPixelSize(resourceId);

        bar = (BottomAppBar)findViewById(R.id.bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        constraintLayout = findViewById(R.id.roadmap_layout);

        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullOpened) {
                    isFullOpened = false;
                    bar.setNavigationIcon(R.drawable.ic_icon_tips);

                    playFullOpenAnimation(true);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.load(getApplicationContext(), R.layout.activity_roadmap);
                    TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.roadmap_layout));
                    constraintSet.applyTo(constraintLayout);
                } else roadmap.testAction();
            }
        });

        findViewById(R.id.fab_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFullOpened) return;

                isFullOpened = true;
                bar.setNavigationIcon(R.drawable.ic_icon_close);
                roadmap.fillFullView();

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.load(getApplicationContext(), R.layout.activity_roadmap_full);
                TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.roadmap_layout));
                constraintSet.applyTo(constraintLayout);

                playFullOpenAnimation(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottombar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_info:
                //TODO Add logic
                break;
        }
        return true;
    }

    private void playFullOpenAnimation(boolean isReverse) {
        CoordinatorLayout coordinator = findViewById(R.id.coordinator);

        if (!isReverse) {
            ObjectAnimator fabTranslation = ObjectAnimator.ofFloat(fab, "TranslationY", -56f -10f);
            fabTranslation.setDuration(250);
            fabTranslation.setStartDelay(200);
            fabTranslation.setInterpolator(new AccelerateInterpolator());

            final AnimatorSet fullViewerOpen = new AnimatorSet();
            fullViewerOpen.play(fabTranslation);

            fullViewerOpen.start();
        } else {
            ObjectAnimator fabTranslation = ObjectAnimator.ofFloat(fab, "TranslationY", 0f);
            fabTranslation.setDuration(300);
            fabTranslation.setInterpolator(new DecelerateInterpolator());

            final AnimatorSet fullViewerOpen = new AnimatorSet();
            fullViewerOpen.play(fabTranslation);

            fullViewerOpen.start();
        }
    }
}
