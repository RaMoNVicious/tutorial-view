package cg.viciousconcepts.tutorialview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cg.viciousconcepts.tutorialview.models.TutorialItem;

public class TutorialSequence {

    public interface SequenceListener {

        void onTutorialShow();
    }

    private static final String TAG = "Tutorial";

    private static final String SHARED_PREFERENCES_NAME = BuildConfig.APPLICATION_ID.concat(".sequence");
    private static final String IS_DONE = "isDone";
    private static final String LAST_SHOWN = "lastShown";
    private boolean debugMode = false;

    private SharedPreferences sharedPreferences;

    private Activity activity;
    private int shownItem = -1;
    private List<TutorialItem> tutorialItems = new ArrayList<>();
    private String screenKey = "";

    private TutorialView tutorialView;

    private TutorialSequence(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.activity = activity;
    }

    private void addTutorialItem(TutorialItem tutorialItem) {
        tutorialItems.add(tutorialItem);
    }

    private void setScreenKey(String screenKey) {
        this.screenKey = screenKey;
    }

    private void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    private static void insertTutorialSequence(TutorialSequence tutorialSequence) {
        tutorialSequence.start();
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
        if (tutorialView == null) {
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
            Log.d(TAG, activity.getString(item.getTitle()));

            tutorialView = new TutorialView.Builder(activity)
                    .setTarget(item.getTarget(), item.getTargetType())
                    .setContentTitle(item.getTitle())
                    .setContentDescription(item.getDescription())
                    .setPerformClickOnTarget(item.isPerformViewClick())
                    .setOnTutorialEndsListener(this::showNext)
                    .show();
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
            this.tutorialSequence.setScreenKey(screenKey);
            return this;
        }

        public Builder debugMode() {
            this.tutorialSequence.setDebugMode(true);
            return this;
        }

        public Builder addTutorialItem(TutorialItem tutorialItem) {
            this.tutorialSequence.addTutorialItem(tutorialItem);
            return this;
        }

        public TutorialSequence start() {
            insertTutorialSequence(tutorialSequence);
            return tutorialSequence;
        }
    }
}
