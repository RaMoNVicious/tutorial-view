package cg.viciousconcepts.tutorialview.models;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

public class TutorialItem {

    private View target;
    private TutorialTargetType targetType;
    private int title;
    private int description;
    private boolean isPerformViewClick;
    private boolean isShowOnce;

    public TutorialItem(@NonNull View target, TutorialTargetType targetType, @StringRes int title, @StringRes int description) {
        this.target = target;
        this.targetType = targetType;
        this.title = title;
        this.description = description;
        this.isPerformViewClick = false;
        this.isShowOnce = true;
    }

    public View getTarget() {
        return target;
    }

    public void setTarget(View target) {
        this.target = target;
    }

    public TutorialTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TutorialTargetType targetType) {
        this.targetType = targetType;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }

    public boolean isPerformViewClick() {
        return isPerformViewClick;
    }

    public void setPerformViewClick(boolean performViewClick) {
        isPerformViewClick = performViewClick;
    }

    public boolean isShowOnce() {
        return isShowOnce;
    }

    public void setShowOnce(boolean showOnce) {
        isShowOnce = showOnce;
    }
}
