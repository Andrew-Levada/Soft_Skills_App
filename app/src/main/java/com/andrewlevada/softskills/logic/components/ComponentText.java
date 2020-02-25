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
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.component_text, (ViewGroup)getActivity().findViewById(R.id.roadmap_recycler), false);

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
    public boolean hasNextStep() {
        return false;
    }

    public ComponentText(Activity activity, String text, boolean isHeadline) {
        super(activity, 1);
        this.text = text;
        this.isHeadline = isHeadline;
    }
}
