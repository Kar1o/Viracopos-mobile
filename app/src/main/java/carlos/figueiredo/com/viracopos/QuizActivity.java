package carlos.figueiredo.com.viracopos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewAnimator;

import carlos.figueiredo.com.viracopos.util.SystemUiHider;


public class QuizActivity extends Activity {

    private SystemUiHider answersHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_quiz);

        final View contentQuestions = findViewById(R.id.fullscreen_questions);
        final View contentAnswers = findViewById(R.id.fullscreen_answers);

        answersHider = SystemUiHider.getInstance(this, contentAnswers, View.SYSTEM_UI_FLAG_LOW_PROFILE);
        answersHider.setup();
        answersHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

            int mControlsHeight;
            int mShortAnimTime;

            @Override
            public void onVisibilityChange(boolean visible) {
                if (mControlsHeight == 0) {
                    mControlsHeight = contentAnswers.getHeight();
                }
                if (mShortAnimTime == 0) {
                    mShortAnimTime = getResources().getInteger(
                            android.R.integer.config_shortAnimTime);
                }
                contentAnswers.animate().translationY(visible ? 0 : mControlsHeight).setDuration(mShortAnimTime);
            }
        });

        contentQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answersHider.toggle();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exit Game")
                .setMessage("Progress will be lost! Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
