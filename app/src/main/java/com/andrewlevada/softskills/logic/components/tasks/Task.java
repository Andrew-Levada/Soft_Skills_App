package com.andrewlevada.softskills.logic.components.tasks;

import android.view.View;

import com.andrewlevada.softskills.logic.components.Component;
import com.andrewlevada.softskills.logic.traits.DeltaTraits;

public interface Task extends Component {
    Task copy();

    boolean isAbleToExecute();

    View getPreview();
    void fillFullView(View parent);
    void performStepAction();

    boolean hasNextStep();
    boolean moveToNextStep();

    DeltaTraits getGeneralDeltaTraits();
    DeltaTraits getDeltaTraits();

    boolean isMakingNewPreview();
    void effectExistingCVU();
}
