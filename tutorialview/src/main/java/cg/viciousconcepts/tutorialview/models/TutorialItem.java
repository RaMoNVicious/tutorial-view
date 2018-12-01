/**
 * File TutorialItem
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

package cg.viciousconcepts.tutorialview.models;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import cg.viciousconcepts.tutorialview.TutorialView;

/**
 * Class TutorialItem
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @package cg.viciousconcepts.tutorialview
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

public class TutorialItem {

    private View target;
    private TutorialTargetType targetType;
    private String title;
    private String description;
    private boolean isPerformViewClick;
    private boolean isShowOnce;
    private TutorialView.OnTutorialShowListener onTutorialShowListener;
    private TutorialView.OnTutorialEndsListener onTutorialEndsListener;

    public TutorialItem(@NonNull View target, TutorialTargetType targetType, @StringRes int title, @StringRes int description) {
        this.target = target;
        this.targetType = targetType;
        this.title = target.getContext().getResources().getString(title);
        this.description = target.getContext().getResources().getString(description);
        this.isPerformViewClick = false;
        this.isShowOnce = true;
        this.onTutorialShowListener = null;
        this.onTutorialEndsListener = null;
    }

    public TutorialItem(@NonNull View target, TutorialTargetType targetType, @StringRes int title, @StringRes int description, TutorialView.OnTutorialShowListener onTutorialShowListener, TutorialView.OnTutorialEndsListener onTutorialEndsListener) {
        this.target = target;
        this.targetType = targetType;
        this.title = target.getContext().getResources().getString(title);
        this.description = target.getContext().getResources().getString(description);
        this.isPerformViewClick = false;
        this.isShowOnce = true;
        this.onTutorialShowListener = onTutorialShowListener;
        this.onTutorialEndsListener = onTutorialEndsListener;
    }

    public TutorialItem(@NonNull View target, TutorialTargetType targetType, String title, String description) {
        this.target = target;
        this.targetType = targetType;
        this.title = title;
        this.description = description;
        this.isPerformViewClick = false;
        this.isShowOnce = true;
        this.onTutorialShowListener = null;
        this.onTutorialEndsListener = null;
    }

    public TutorialItem(@NonNull View target, TutorialTargetType targetType, String title, String description, TutorialView.OnTutorialShowListener onTutorialShowListener, TutorialView.OnTutorialEndsListener onTutorialEndsListener) {
        this.target = target;
        this.targetType = targetType;
        this.title = title;
        this.description = description;
        this.isPerformViewClick = false;
        this.isShowOnce = true;
        this.onTutorialShowListener = onTutorialShowListener;
        this.onTutorialEndsListener = onTutorialEndsListener;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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

    public TutorialView.OnTutorialShowListener getOnTutorialShowListener() {
        return onTutorialShowListener;
    }

    public void setOnTutorialShowListener(TutorialView.OnTutorialShowListener onTutorialShowListener) {
        this.onTutorialShowListener = onTutorialShowListener;
    }

    public TutorialView.OnTutorialEndsListener getOnTutorialEndsListener() {
        return onTutorialEndsListener;
    }

    public void setOnTutorialEndsListener(TutorialView.OnTutorialEndsListener onTutorialEndsListener) {
        this.onTutorialEndsListener = onTutorialEndsListener;
    }
}
