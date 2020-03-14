package com.andrewlevada.softskills.logic.components;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;

public class ComponentText extends AbstractComponent {
    private String text;
    private boolean isHeadline;

    @Override
    public View getPreview() {
        View view = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.component_text, (ViewGroup) getActivity().findViewById(R.id.roadmap_recycler), false);

        ((TextView) view.findViewById(R.id.componentText)).setText(text);

        if (isHeadline) {
            ((TextView) view.findViewById(R.id.componentText)).setTextColor(Color.BLACK);
        }

        return view;
    }

    public ComponentText(RoadmapActivity activity, String text, boolean isHeadline) {
        super(activity);
        this.text = text;
        this.isHeadline = isHeadline;
    }

    @Override
    public Component copy() {
        return new ComponentText(getActivity(), text, isHeadline);
    }
}
