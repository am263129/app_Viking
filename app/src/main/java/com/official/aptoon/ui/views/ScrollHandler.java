package com.official.aptoon.ui.views;



import android.view.View;
import android.widget.FrameLayout;

import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class ScrollHandler extends CoordinatorLayout.Behavior<BubbleNavigationConstraintView> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BubbleNavigationConstraintView child, View dependency) {
        return dependency instanceof FrameLayout;

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, BubbleNavigationConstraintView child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, BubbleNavigationConstraintView child,
                                  View target, int dx, int dy, int[] consumed) {
        if (dy < 0) {
            showBottomNavigationView(child);
        } else if (dy > 0) {
            hideBottomNavigationView(child);
        }
    }

    private void hideBottomNavigationView(BubbleNavigationConstraintView view) {
        view.animate().translationY(view.getHeight());
    }

    private void showBottomNavigationView(BubbleNavigationConstraintView view) {
        view.animate().translationY(0);
    }
}