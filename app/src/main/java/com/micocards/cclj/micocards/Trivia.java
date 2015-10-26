package com.micocards.cclj.micocards;

/*
 * Trivia.java
 *
 * Version 1
 *
 * 18/02/15
 *
 * @reference http://algs4.cs.princeton.edu/11model/Knuth.java.html
 * @author Conor Keenan, x13343806
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Trivia extends ActionBarActivity {
    static Activity thisInstance;
    private int qCount, qScoreCount, totalQs;
    private Button aBtn, bBtn, cBtn, dBtn, tryAgain, skip;
    private TextView tr, tc, qNum, qScore;
    private boolean isFinished;
    private MicoDbAdapter dbAdapter;
    private Menu menu;
    private String activeUser;
    private SoundPool sounds;
    private int popSound;
    private Audio sound;
    private Boolean soundOn;
    private SharedPreferences preferences;
    private LinearLayout optionsBtns, incorrectAns, finishedQuiz;


    /**
     * @author Conor Keenan, x13343806
     */
    public static Activity getInstance() {
        return thisInstance;
    }

    /**
     * @reference http://algs4.cs.princeton.edu/11model/Knuth.java.html
     */
    public static void shuffle(Object[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            int r = i + (int) (Math.random() * (N - i));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }

    /**
     * @author Conor Keenan, x13343806
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        dbAdapter = new MicoDbAdapter(this);
        thisInstance = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            qCount = extras.getInt("qNum");
        } else {
            qCount = dbAdapter.getTriviaCount();
            ;
        }

        sound = new Audio();
        sound.loadSounds(this);
        qScoreCount = dbAdapter.getTriviaScore();
        totalQs = dbAdapter.numQuestions();
        aBtn = (Button) findViewById(R.id.optABtn);
        bBtn = (Button) findViewById(R.id.optBBtn);
        cBtn = (Button) findViewById(R.id.optCBtn);
        dBtn = (Button) findViewById(R.id.optDBtn);
        optionsBtns = (LinearLayout) findViewById(R.id.optionBtns);
        incorrectAns = (LinearLayout) findViewById(R.id.incorrectAns);
        finishedQuiz = (LinearLayout) findViewById(R.id.quizFinished);
        tryAgain = (Button) findViewById(R.id.tryAgainBtn);
        skip = (Button) findViewById(R.id.skipBtn);
        tr = (TextView) findViewById(R.id.textAnsResult);
        tc = (TextView) findViewById(R.id.triviaCard);
        qNum = (TextView) findViewById(R.id.qNum);
        qScore = (TextView) findViewById(R.id.qScore);
        isFinished = false;
        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);

        setQuestion();


    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void onResetButtonClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        qCount = 1;
        isFinished = false;
        aBtn.setTextColor(Color.parseColor("#ffffff"));
        bBtn.setTextColor(Color.parseColor("#ffffff"));
        cBtn.setTextColor(Color.parseColor("#ffffff"));
        dBtn.setTextColor(Color.parseColor("#ffffff"));
        aBtn.setEnabled(true);
        bBtn.setEnabled(true);
        cBtn.setEnabled(true);
        dBtn.setEnabled(true);
        setQuestion();

    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void setQuestion() {

        finishedQuiz.setVisibility(View.GONE);
        incorrectAns.setVisibility(View.GONE);
        optionsBtns.setVisibility(View.VISIBLE);

        if (!isFinished) {
            tr.setText(null);
            String[] options = dbAdapter.getTOptions(qCount);

            shuffle(options);

            tc.setText(dbAdapter.getTQuestion(qCount));
            qNum.setText(qCount + " / " + totalQs);
            qScore.setText("Score: " + qScoreCount);
            aBtn.setText(options[0]);
            bBtn.setText(options[1]);
            cBtn.setText(options[2]);
            dBtn.setText(options[3]);

        } else {

            qScore.setText("Score: " + qScoreCount);
            tr.setTextColor(Color.parseColor("#00FF00"));
            tr.setText("Correct Quiz Complete");
            optionsBtns.setVisibility(View.GONE);
            finishedQuiz.setVisibility(View.VISIBLE);


            String[] options = dbAdapter.getTOptions(qCount);


            tc.setText("Congratulations You Have Completed the Quiz !!!");
            qNum.setText(qCount + " / " + totalQs);


        }
    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void onTryAgain(View v) {
        if (soundOn) {
            sound.playPop();
        }
        setQuestion();
    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void onSkip(View v) {
        if (soundOn) {
            sound.playPop();
        }
        if (qCount == 26) {
            Message.message(this, "Cannot Skip Last Question.");
        } else {
            qCount++;
            setQuestion();
        }
    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void onAnsClick(View v) {

        if (soundOn) {
            sound.playPop();
        }

        int id = v.getId();
        Button clicked = (Button) findViewById(id);

        String answer = dbAdapter.getTAnswer(qCount);

        if ((clicked.getText()).equals(answer)) {


            if (qCount == totalQs) {
                isFinished = true;
                qScoreCount++;
                dbAdapter.setTriviaScore(qScoreCount);
                setQuestion();

            } else {
                qCount++;
                dbAdapter.setTriviaCount(qCount);
                qScoreCount++;
                dbAdapter.setTriviaScore(qScoreCount);
                setQuestion();
            }

        } else {

            optionsBtns.setVisibility(v.GONE);
            incorrectAns.setVisibility(v.VISIBLE);


            tr.setTextColor(Color.parseColor("#ff0000"));
            tr.setText("Incorrect");
        }


    }

    /**
     * @author Conor Keenan, x13343806
     */
    public void setUser() {
        MenuItem User = menu.findItem(R.id.userTag);
        activeUser = dbAdapter.getActiveUser();

        User.setTitle(activeUser);

    }

    /**
     * @author Conor Keenan, x13343806
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trivia, menu);
        this.menu = menu;
        setUser();
        return true;
    }

    /**
     * @author Conor Keenan, x13343806
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.questionList) {
            Intent qListIntent = new Intent(this, TriviaList.class);
            startActivity(qListIntent);

        } else if (id == R.id.userTag) {
            Message.message(this, "User can only be changed at main menu.");

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * @author Conor Keenan, x13343806
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putInt("qNum", qCount);
        outState.putInt("Score", qScoreCount);
        outState.putBoolean("isFinished", isFinished);


    }

    /**
     * @author Conor Keenan, x13343806
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        qCount = savedInstanceState.getInt("qNum");
        qScoreCount = savedInstanceState.getInt("Score");
        isFinished = savedInstanceState.getBoolean("isFinished");


        setQuestion();

    }


}
