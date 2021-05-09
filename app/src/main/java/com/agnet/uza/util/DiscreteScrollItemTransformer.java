package com.agnet.uza.util;

import android.view.View;

public interface DiscreteScrollItemTransformer {
    /**
     * In this method you apply any transform you can imagine (perfomance is not guaranteed).
     * @param position is a value inside the interval [-1f..1f]. In idle state:
     * |view1|  |currentlySelectedView|  |view2|
     * -view1 and everything to the left is on position -1;
     * -currentlySelectedView is on position 0;
     * -view2 and everything to the right is on position 1.
     */
    void transformItem(View item, float position);
}