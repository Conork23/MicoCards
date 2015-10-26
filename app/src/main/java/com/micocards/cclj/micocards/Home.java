package com.micocards.cclj.micocards;

/*
 * Home.java
 *
 * Version 1
 *
 * 18/02/15
 *
 * @author Conor Keenan, x13343806
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class Home extends ActionBarActivity {

    private static Activity thisInstance;
    private Menu menu;
    private MicoDbAdapter dbAdapter;
    private Audio sound;
    private ImageButton mute;
    private Boolean soundOn;
    private SharedPreferences preferences;

    public static Activity getInstance() {
        return thisInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbAdapter = new MicoDbAdapter(this);

        thisInstance = this;

        sound = new Audio();
        sound.loadSounds(this);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);

        mute = (ImageButton) findViewById(R.id.muteBtn);


    }

    public void onMuteBtnClick(View v) {
        SharedPreferences.Editor editor = preferences.edit();


        if (soundOn) {
            sound.playPop();
            mute.setImageResource(R.drawable.off);
            editor.putBoolean("soundOn", false);

            editor.commit();
        } else {
            mute.setImageResource(R.drawable.on);
            editor.putBoolean("soundOn", true);

            editor.commit();
        }

        soundOn = preferences.getBoolean("soundOn", true);

    }

    public void setUser() {

        MenuItem User = menu.findItem(R.id.userTag);
        String activeUser = dbAdapter.getActiveUser();

        User.setTitle(activeUser);

    }

    public void onTriviaClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        Intent Intent = new Intent(this, Trivia.class);
        startActivity(Intent);
    }

    public void onAchievementsClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        Intent Intent = new Intent(this, Achievements.class);
        startActivity(Intent);
    }

    public void onFlashCardClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        Intent Intent = new Intent(this, ViewCards.class);
        startActivity(Intent);
    }

    public void onSpellingTestClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        Intent Intent = new Intent(this, SpellingTest.class);
        startActivity(Intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

            Intent Intent = new Intent(this, Users.class);
            startActivity(Intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
