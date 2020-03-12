package com.andrewlevada.softskills;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.AutoTransition;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;

import com.andrewlevada.softskills.logic.Roadmap;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RoadmapActivity extends AppCompatActivity {
    private Roadmap roadmap;
    private Point display;

    private BottomAppBar bar;
    private FloatingActionButton fab;
    private ConstraintLayout constraintLayout;

    private boolean isFullOpened = false;
    private OnBackPressedCallback fullBackCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap);

        roadmap = Roadmap.getInstance(this);
        display = new Point();
        getWindowManager().getDefaultDisplay().getSize(display);

        bar = (BottomAppBar)findViewById(R.id.bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        constraintLayout = findViewById(R.id.roadmap_layout);

        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullOpened) {
                    closeFull(null);
                } else roadmap.testAction();
            }
        });

        findViewById(R.id.fab_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFull();
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

    public void openFull() {
        if (isFullOpened) return;
        if (!roadmap.fillFullView()) return;

        isFullOpened = true;
        bar.setNavigationIcon(R.drawable.ic_icon_close);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(getApplicationContext(), R.layout.activity_roadmap_full);
        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.roadmap_layout));
        constraintSet.applyTo(constraintLayout);

        playFullOpenAnimation(false);

        fullBackCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                closeFull(null);
                remove();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, fullBackCallback);
    }

    public void closeFull(final CloseFullHandler handler) {
        if (!isFullOpened) {
            if (handler != null) handler.next();
            return;
        }

        isFullOpened = false;
        bar.setNavigationIcon(R.drawable.ic_icon_tips);

        playFullOpenAnimation(true);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.load(getApplicationContext(), R.layout.activity_roadmap);

        Transition transition = new AutoTransition() {
            @Override
            protected void end() {
                cleanFull();
                if (handler != null) handler.next();
            }
        };

        transition.setDuration(500);
        transition.setInterpolator(new DecelerateInterpolator());

        TransitionManager.beginDelayedTransition((ViewGroup) findViewById(R.id.roadmap_layout), transition);
        constraintSet.applyTo(constraintLayout);

        fullBackCallback.remove();
        fullBackCallback = null;
    }

    private void cleanFull() {
        ViewGroup parent = (ViewGroup) findViewById(R.id.full_layout);
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            int id = parent.getChildAt(i).getId();

            if (id == R.id.fullHeader) continue;
            if (id == R.id.fullText) continue;

            parent.removeViewAt(i);
        }
    }

    private void playFullOpenAnimation(boolean isReverse) {
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

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();
        if (view == null) view = new View(this);

        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static interface CloseFullHandler {
        void next();
    }
}
