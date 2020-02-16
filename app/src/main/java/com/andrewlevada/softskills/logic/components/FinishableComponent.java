package com.andrewlevada.softskills.logic.components;

import com.andrewlevada.softskills.logic.traits.DeltaTrait;
import java.util.List;

public interface FinishableComponent extends Component {
    void getFinishView();
    List<DeltaTrait> getDeltaTraits();
}
