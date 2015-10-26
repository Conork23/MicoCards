package com.micocards.cclj.micocards;
/*
SpellingTest.Java
10/03/2015
@author  Conor Donnelly
x13734595
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class SpellingTest extends ActionBarActivity {
    MicoDbAdapter dbAdapter;
    private EditText enterSpellingTf;
    private CheckBox showBox;
    private CheckBox hideBox;
    private TextView spellingView;
    private TextView iView;
    private Button enterBtn;
    private int totalWords;
    private int count, score;
    private Audio sound;
    private Menu menu;
    private Boolean soundOn;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spelling_test);
        enterSpellingTf = (EditText) findViewById(R.id.enterSpellingTf);
        showBox = (CheckBox) findViewById(R.id.showBox);
        hideBox = (CheckBox) findViewById(R.id.hideBox);
        spellingView = (TextView) findViewById(R.id.spellingView);
        iView = (TextView) findViewById(R.id.instructionsView);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        dbAdapter = new MicoDbAdapter(this);
        count = dbAdapter.getSpellingCount();
        score = dbAdapter.getSpellingScore();
        enterBtn.setVisibility(View.INVISIBLE);
        enterSpellingTf.setVisibility(View.INVISIBLE);
        totalWords = dbAdapter.numWords();
        sound = new Audio();
        sound.loadSounds(this);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);

        setWord();
    }

    public void setUser() {

        MenuItem User = menu.findItem(R.id.userTag);
        String activeUser = dbAdapter.getActiveUser();

        User.setTitle(activeUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spelling_test, menu);
        this.menu = menu;
        setUser();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.userTag) {
            Message.message(this, "User can only be changed at main menu.");

        }

        return super.onOptionsItemSelected(item);
    }


    public void setWord() {

        spellingView.setText(dbAdapter.getSWord(count));

    }

    public void hideBox(View v) {
        if (soundOn) {
            sound.playPageTurn();
        }
        if (hideBox.isChecked()) {
            spellingView.setVisibility(View.INVISIBLE);
            enterSpellingTf.setVisibility(View.VISIBLE);
            enterBtn.setVisibility(View.VISIBLE);
            showBox.setChecked(false);
        }

    }

    public void showBox(View v) {
        if (soundOn) {
            sound.playPageTurn();
        }
        if (showBox.isChecked()) {
            spellingView.setVisibility(View.VISIBLE);
            enterSpellingTf.setVisibility(View.INVISIBLE);
            enterBtn.setVisibility(View.INVISIBLE);
            enterSpellingTf.setText("");
            hideBox.setChecked(false);
        }
    }

    public void EnterBtn(View v) {
        if (soundOn) {
            sound.playPop();
        }

        if ((enterSpellingTf.getText().toString()).equalsIgnoreCase(spellingView.getText().toString())) {
            spellingView.setVisibility(View.VISIBLE);
            enterSpellingTf.setVisibility(View.INVISIBLE);
            enterBtn.setVisibility(View.INVISIBLE);
            showBox.setChecked(true);
            hideBox.setChecked(false);
            enterSpellingTf.setText("");
            iView.setText("That is correct, you have gained a point! Now try this one!");
            score++;
            dbAdapter.setSpellingScore(score);

            if (count == totalWords) {
                count = 1;
                dbAdapter.setSpellingCount(count);
                iView.setText("Return to the Main Menu");
                spellingView.setText("Congratulations, you have reached the end of the Spelling Test!");
                enterBtn.setVisibility(View.INVISIBLE);
                showBox.setVisibility(View.INVISIBLE);
                hideBox.setVisibility(View.INVISIBLE);

            } else {
                count++;
                dbAdapter.setSpellingCount(count);
                setWord();

            }
        } else {
            iView.setText("That is incorrect! Don't worry, try again!");
            enterSpellingTf.setText("");
            spellingView.setVisibility(View.VISIBLE);
            enterSpellingTf.setVisibility(View.INVISIBLE);
            enterBtn.setVisibility(View.INVISIBLE);
            showBox.setChecked(true);
            hideBox.setChecked(false);
        }


    }
}