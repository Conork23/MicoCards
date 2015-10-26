package com.micocards.cclj.micocards;
/*
* CreateCards.java
*
* 10/03/2015
*
* @author Lee Redmond x13488632
*
* */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CreateCards extends ActionBarActivity {

    private static int fliper;
    private static LinearLayout answer;
    private static LinearLayout question;
    TextView q, a;
    MicoDbAdapter adapter;
    Audio sound;
    private Menu menu;
    private Boolean soundOn;
    private SharedPreferences preferences;
    private String activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_cards);
        answer = (LinearLayout) findViewById(R.id.BackLayout);
        question = (LinearLayout) findViewById(R.id.FrontLayout);
        q = (TextView) findViewById(R.id.FrontEnter);
        a = (TextView) findViewById(R.id.BackEnter);
        answer.setVisibility(View.GONE);
        adapter = new MicoDbAdapter(this);
        fliper = 0;
        sound = new Audio();
        sound.loadSounds(this);
        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);
        activeUser = adapter.getActiveUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_cards, menu);
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
        if (id == R.id.Flip) {
            if (soundOn) {
                sound.playPageTurn();
            }
            if (fliper == 0) {

                answer.setVisibility(View.VISIBLE);
                question.setVisibility(View.GONE);
                fliper++;
            } else {

                answer.setVisibility(View.GONE);
                question.setVisibility(View.VISIBLE);
                fliper = 0;
            }
        } else if (id == R.id.userTag) {
            Message.message(this, "User can only be changed at main menu.");

        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateCard(View v) {
        if (soundOn) {
            sound.playPop();
        }


        String ques = q.getText().toString();
        String ans = a.getText().toString();

        if (ques.equals(null) || ques.equals("") || ques.equals(" ")) {
            Message.message(this, "Please insert text to be added.");
        } else if (ans.equals(null) || ans.equals("") || ans.equals(" ")) {
            ans = "No text was added to the back of the card.";
            adapter.insertQuestion(ques, ans, activeUser);
            q.setText(null);
            a.setText(null);
        } else {

            adapter.insertQuestion(ques, ans, activeUser);
            q.setText(null);
            a.setText(null);

        }
    }

    public void setUser() {
        MenuItem User = menu.findItem(R.id.userTag);

        User.setTitle(activeUser);

    }
}