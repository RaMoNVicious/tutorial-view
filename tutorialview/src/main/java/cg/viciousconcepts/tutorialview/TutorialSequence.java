/**
 * File TutorialSequence
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

package cg.viciousconcepts.tutorialview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cg.viciousconcepts.tutorialview.models.TutorialItem;
import cg.viciousconcepts.tutorialview.models.TutorialTargetType;

/**
 * Class TutorialSequence
 *
 * JDK version 8
 *
 * @author RaMoNVicious
 * @category tutorial-view
 * @package cg.viciousconcepts.tutorialview
 * @copyright 2017-2018 RaMoNVicious
 * @created 28.11.18 13:46
 */

public class TutorialSequence {

    private static final String TAG = "Tutorial";

    private static final String SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID.concat(".sequence");
    private static final String IS_DONE = "isDone";
    private static final String LAST_SHOWN = "lastShown";

    private SharedPreferences sharedPreferences;

    private Activity activity;
    private int shownItem = -1;
    private List<TutorialItem> tutorialItems = new ArrayList<>();
    private String screenKey = "";
    private boolean debugMode = false;

    private TutorialView tutorialView;

    private TutorialSequence(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.activity = activity;
    }

    public void start() {
        if (!debugMode && !sharedPreferences.contains(screenKey.concat(IS_DONE)) && sharedPreferences.contains(screenKey.concat(LAST_SHOWN))) {
            long lastShownAt = sharedPreferences.getLong(screenKey.concat(LAST_SHOWN), Long.MAX_VALUE);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, -20);
            if (calendar.getTimeInMillis() < lastShownAt) return;
        }

        Log.d(TAG, "started");
        sharedPreferences.edit().putLong(screenKey.concat(LAST_SHOWN), Calendar.getInstance().getTimeInMillis()).apply();
        showNext();
    }

    private void showNext() {
        Log.d(TAG, "show next");
        shownItem++;
        showTutorial();
    }

    public boolean back() {
        if (tutorialView == null || shownItem == tutorialItems.size()) {
            return false;
        }

        if (shownItem == 0) {
            Log.d(TAG, "close");
            tutorialView.dismiss(true);
            tutorialView = null;
        } else {
            Log.d(TAG, "show previous");
            tutorialView.dismiss(true);
            shownItem--;
            showTutorial();
        }
        return true;
    }

    private void showTutorial() {
        if (shownItem <= tutorialItems.size() - 1) {
            TutorialItem item = tutorialItems.get(shownItem);
            Log.d(TAG, item.getTitle());

            item.getTarget().post(() -> tutorialView = new TutorialView.Builder(activity)
                    .setTarget(item.getTarget(), item.getTargetType())
                    .setContentTitle(item.getTitle())
                    .setContentDescription(item.getDescription())
                    .setPerformClickOnTarget(item.isPerformViewClick())
                    .setOnTutorialEndsListener(() -> {
                        if (item.getOnTutorialEndsListener() != null)
                            item.getOnTutorialEndsListener().onTutorialEnds();
                        showNext();
                    })
                    .setOnTutorialShowListener(item.getOnTutorialShowListener())
                    .show());
        } else {
            sharedPreferences.edit().putBoolean(screenKey.concat(IS_DONE), true).apply();
        }
    }

    public static class Builder {

        private final TutorialSequence tutorialSequence;

        public Builder(Activity activity) {
            this.tutorialSequence = new TutorialSequence(activity);
        }

        public Builder setScreenKey(String screenKey) {
            this.tutorialSequence.screenKey = screenKey;
            return this;
        }

        public Builder debugMode() {
            this.tutorialSequence.debugMode = true;
            return this;
        }

        private Builder addTutorialItem(TutorialItem tutorialItem) {
            this.tutorialSequence.tutorialItems.add(tutorialItem);
            return this;
        }

        public Builder addTutorialItem(View view, TutorialTargetType targetType, String title, String description) {
            TutorialItem tutorialItem = new TutorialItem(view, targetType, title, description);
            return addTutorialItem(tutorialItem);
        }

        public Builder addTutorialItem(View view, TutorialTargetType targetType, String title, String description, TutorialView.OnTutorialShowListener onTutorialShowListener, TutorialView.OnTutorialEndsListener onTutorialEndsListener) {
            TutorialItem tutorialItem = new TutorialItem(view, targetType, title, description, onTutorialShowListener, onTutorialEndsListener);
            return addTutorialItem(tutorialItem);
        }

        public Builder addTutorialItem(View view, TutorialTargetType targetType, @StringRes int titleId, @StringRes int descriptionId) {
            TutorialItem tutorialItem = new TutorialItem(view, targetType, titleId, descriptionId);
            return addTutorialItem(tutorialItem);
        }

        public Builder addTutorialItem(View view, TutorialTargetType targetType, @StringRes int titleId, @StringRes int descriptionId, TutorialView.OnTutorialShowListener onTutorialShowListener, TutorialView.OnTutorialEndsListener onTutorialEndsListener) {
            TutorialItem tutorialItem = new TutorialItem(view, targetType, titleId, descriptionId, onTutorialShowListener, onTutorialEndsListener);
            return addTutorialItem(tutorialItem);
        }

        public TutorialSequence start() {
            tutorialSequence.start();
            return tutorialSequence;
        }
    }
}
