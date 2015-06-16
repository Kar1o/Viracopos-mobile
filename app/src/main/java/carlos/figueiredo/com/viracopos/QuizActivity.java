package carlos.figueiredo.com.viracopos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import carlos.figueiredo.com.viracopos.Model.Player;
import carlos.figueiredo.com.viracopos.Model.Round;
import carlos.figueiredo.com.viracopos.util.SystemUiHider;


public class QuizActivity extends Activity {

    private SystemUiHider answersHider;

    private Button confirmButton;

    private TextView questions;

    private RadioButton option1, option2, option3, option4;

    private RadioGroup radioGroup;

    private Player player = new Player();

    private Round round = new Round();

    private String url;

    private int currentRound = 1, totalRound = 0;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove TitleBar from screen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_quiz);

        final View contentQuestions = findViewById(R.id.fullscreen_questions);
        final View contentAnswers = findViewById(R.id.fullscreen_answers);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        questions = (TextView) findViewById(R.id.fullscreen_questions);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);

        player.setNome(getIntent().getExtras().getString("name"));

        new HttpAsyncTask().execute(newUrl());

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
        answersHider.toggle();

        contentQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answersHider.toggle();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.option1) {
                    //option1.setChecked(true);
                    option2.setChecked(false);
                    option3.setChecked(false);
                    option4.setChecked(false);
                } else if (checkedId == R.id.option2) {
                    option1.setChecked(false);
                    option3.setChecked(false);
                    option4.setChecked(false);

                } else if (checkedId == R.id.option3) {
                    option1.setChecked(false);
                    option2.setChecked(false);
                    option4.setChecked(false);
                } else if (checkedId == R.id.option4) {
                    option1.setChecked(false);
                    option2.setChecked(false);
                    option3.setChecked(false);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (option1.isChecked() || option2.isChecked() || option3.isChecked() || option4.isChecked()) {
                    //Toast.makeText(getBaseContext(), "option selected", Toast.LENGTH_SHORT).show();
                    if (isConnected()) {
                        currentRound += 1;
                        new HttpAsyncTask().execute(newUrl());
                        //changeQuestion(round);
                        radioGroup.clearCheck();
                        answersHider.toggle();

                    }
                    else {
                        Toast.makeText(getBaseContext(), "Connection Problem!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getBaseContext(), "Choose an answer", Toast.LENGTH_SHORT).show();
                }

            }
        });


        }

    /*private void checkAnswer() {
        int selectedRadio = radioGroup.getCheckedRadioButtonId();

        if (selectedRadio == 1) {

        }
    }*/

    private String newUrl() {
        url = "http://23.239.18.68:8080/question?round=" + currentRound + "&index=" + (random.nextInt(4) + 1);
        return url;
    }

    private void changeQuestion(Round round) {
        questions.setText(round.getQuestion());
        option1.setText(round.getAnswer1());
        option2.setText(round.getAnswer2());
        option3.setText(round.getAnswer3());
        option4.setText(round.getAnswer4());

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            Toast.makeText(getBaseContext(), "sucess", Toast.LENGTH_SHORT).show();

            try {

                round.setQuestion(result.getString("question"));
                round.setAnswer1(result.getString("answer1"));
                round.setAnswer2(result.getString("answer2"));
                round.setAnswer3(result.getString("answer3"));
                round.setAnswer4(result.getString("answer4"));
                changeQuestion(round);
                /*
                questions.setText(result.getString("question"));
                option1.setText(result.getString("answer1"));
                option2.setText(result.getString("answer2"));
                option3.setText(result.getString("answer3"));
                option4.setText(result.getString("answer4"));*/


            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println(result);
        }
    }

    public static JSONObject GET(String url) {
        InputStream inputStream;
        BufferedReader bufferedReader;
        JSONObject result = null;
        String line;

        try{
            HttpClient httpClient = new DefaultHttpClient();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(url));

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null){
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                if ((line = bufferedReader.readLine()) != null) {
                    result = new JSONObject(line);
                }
            inputStream.close();
                }
            } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
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


