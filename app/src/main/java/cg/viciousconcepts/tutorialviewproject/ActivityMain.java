package cg.viciousconcepts.tutorialviewproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cg.viciousconcepts.tutorialview.TutorialSequence;
import cg.viciousconcepts.tutorialview.models.TutorialTargetType;

public class ActivityMain extends AppCompatActivity {

    TextView txtHelloWorld;

    TutorialSequence tutorialSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtHelloWorld = findViewById(R.id.txtHelloWorld);

        tutorialSequence = new TutorialSequence.Builder(this)
                .debugMode()
                .addTutorialItem(txtHelloWorld,
                        TutorialTargetType.TARGET_RECTANGLE,
                        "Hello World!",
                        "This is Hello World label!")
                .addTutorialItem(txtHelloWorld,
                        TutorialTargetType.TARGET_CIRCLE,
                        "Goodbye World!",
                        "This is Hello World label changed to Goodbye World label!",
                        () -> txtHelloWorld.setText("Goodbye World!"))
                .start();
    }

    @Override
    public void onBackPressed() {
        if (tutorialSequence == null || !tutorialSequence.back()) {
            super.onBackPressed();
        }
    }
}
