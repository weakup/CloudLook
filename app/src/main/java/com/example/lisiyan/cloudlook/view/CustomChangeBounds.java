package com.example.lisiyan.cloudlook.view;

import android.animation.Animator;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

/**
 * Created by lisiyan on 2017/11/24.
 */

public class CustomChangeBounds extends ChangeBounds {

    @Override
    public Animator createAnimator(final ViewGroup sceneRoot,
                                   TransitionValues startValues,
                                   final TransitionValues endValues) {
        Animator changeBounds = super.createAnimator(sceneRoot,startValues,endValues);
        if (startValues == null || endValues == null || changeBounds ==null) return null;

        changeBounds.setDuration(500);
        changeBounds.setInterpolator(AnimationUtils.loadInterpolator(sceneRoot.getContext(),
                android.R.interpolator.fast_out_slow_in));
        return changeBounds;
    }

}
