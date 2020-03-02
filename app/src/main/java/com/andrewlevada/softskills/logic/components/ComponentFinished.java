package com.andrewlevada.softskills.logic.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrewlevada.softskills.R;
import com.andrewlevada.softskills.RoadmapActivity;

public class ComponentFinished extends Component {
    private boolean isSuccess;

    public ComponentFinished(RoadmapActivity activity, boolean isSuccess) {
        super(activity);
        this.isSuccess = isSuccess;
    }

    @Override
    public View getPreview() {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.component_finished, (ViewGroup)getActivity().findViewById(R.id.roadmap_recycler), false);

        if (!isSuccess) ((ImageView)view.findViewById(R.id.component_finished_icon)).setImageResource(R.drawable.ic_icon_thumb_down);

        return view;
    }
}
