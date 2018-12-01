package cg.viciousconcepts.tutorialviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cg.viciousconcepts.tutorialview.TutorialSequence;
import cg.viciousconcepts.tutorialview.models.TutorialTargetType;

public class ActivityMain extends AppCompatActivity {

    TextView txtTop, txtTopRight, txtBottom, txtBottomRight;

    TutorialSequence tutorialSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTop = findViewById(R.id.txtTop);
        txtTopRight = findViewById(R.id.txtTopRight);
        txtBottom = findViewById(R.id.txtBottom);
        txtBottomRight = findViewById(R.id.txtBottomRight);

        tutorialSequence = new TutorialSequence.Builder(this)
                .debugMode()
                .addTutorialItem(txtTop,
                        TutorialTargetType.TARGET_RECTANGLE,
                        "Hello World!",
                        "This is Hello World label!")
                .addTutorialItem(txtTopRight,
                        TutorialTargetType.TARGET_RECTANGLE,
                        "Hello World!",
                        "This is Hello World label!")
                .addTutorialItem(txtBottom,
                        TutorialTargetType.TARGET_RECTANGLE,
                        "Hello World!",
                        "This is Hello World label!")
                .addTutorialItem(txtBottomRight,
                        TutorialTargetType.TARGET_CIRCLE,
                        "Goodbye World!",
                        "This is Hello World label changed color after Tutorial shown!\n...and will change back after Tutorial ends.",
                        () -> txtBottomRight.setTextColor(getResources().getColor(R.color.colorAccent)),
                        () -> txtBottomRight.setTextColor(getResources().getColor(R.color.colorPrimaryDark)))
                .start();
    }

    @Override
    public void onBackPressed() {
        if (tutorialSequence == null || !tutorialSequence.back()) {
            super.onBackPressed();
        }
    }
}
