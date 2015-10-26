package com.micocards.cclj.micocards;

/*
 * Achievements.java
 *
 * Version 1
 *
 * 16/3/15
 *
 * @author Jakub Nazar, x13446722
 *
 */


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Achievements extends ActionBarActivity {
    int Points;
    int trivPoints;
    int flashPoints;
    int spellPoints;
    ListView listView;
    TextView trivscoreView;
    TextView flashscoreView;
    TextView spellscoreView;
    private ProgressBar progress;
    MicoDbAdapter dbAdapter;
    int progCounter;
    private Menu menu;
    String activeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        dbAdapter = new MicoDbAdapter(this);
        progCounter = 0;
        activeUser = dbAdapter.getActiveUser();

        //These are the variables for the points that you can earn
        Points = 0;
        trivPoints = dbAdapter.getTriviaScore();
        flashPoints = dbAdapter.getFlashLength(activeUser);
        spellPoints = dbAdapter.getSpellingScore();

        listView = (ListView) findViewById(R.id.listView);

        trivscoreView = (TextView) findViewById(R.id.trivScore);
        trivscoreView.setText("" + trivPoints);

        flashscoreView = (TextView) findViewById(R.id.flashScore);
        flashscoreView.setText("" + flashPoints);

        spellscoreView = (TextView) findViewById(R.id.spellScore);
        spellscoreView.setText("" + spellPoints);

        setAchievements();
        setProgressBar();


    }

    public void setProgressBar() {
        progCounter = dbAdapter.getAchievedLength() * 4;

        progress.setProgress(progCounter);


        if (progress.equals(100)) {
            Message.message(this, "Congratulations! You got 100% score.");
        }

    }


    public void setAchievements() {

        dbAdapter.setEarnedAchiv("trivia", trivPoints);
        dbAdapter.setEarnedAchiv("flash", flashPoints);
        dbAdapter.setEarnedAchiv("spelling", spellPoints);

        SimpleCursorAdapter theAdapter = dbAdapter.earnedList(this);
        listView.setAdapter(theAdapter);


    }

    public void setUser() {

        MenuItem User = menu.findItem(R.id.userTag);

        User.setTitle(activeUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievements, menu);
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
}