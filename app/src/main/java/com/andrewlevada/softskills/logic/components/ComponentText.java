package com.andrewlevada.softskills.logic.components;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewlevada.softskills.R;

public class ComponentText extends Component {
    private String text;
    private boolean isHeadline;

    @Override
    public View getView() {
        View view = LayoutInflater.from(activity.getApplicationContext())
                .inflate(R.layout.component_text, (ViewGroup)activity.findViewById(R.id.roadmap_recycler), false);

        ((TextView)view.findViewById(R.id.componentText)).setText(text);

        if (isHeadline) {
            ((TextView)view.findViewById(R.id.componentText)).setTextColor(Color.BLACK);
        }

        return view;
    }

    @Override
    public int getViewLayoutId() {
        return R.layout.component_text;
    }

    @Override
    public boolean hasNextView() {
        return false;
    }

    public ComponentText(Activity activity, String text, boolean isHeadline) {
        super(activity);
        this.text = text;
        this.isHeadline = isHeadline;
    }
}
