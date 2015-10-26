package com.micocards.cclj.micocards;
/*
* ViewCards.java
* 10/03/2015
* @author Lee Redmond x13488632
* */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewCards extends ActionBarActivity {

    private static TextView q, a;
    private static MicoDbAdapter adapter;
    private static int count;
    private static int fliper;
    private static LinearLayout answer;
    private static LinearLayout question;
    private Audio sound;
    private int length;
    private Menu menu;
    private String activeUser;
    private String[] frontArr, backArr;
    private Boolean soundOn;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cards);
        answer = (LinearLayout) findViewById(R.id.BackLayout);
        question = (LinearLayout) findViewById(R.id.FrontLayout);
        q = (TextView) findViewById(R.id.DisplayFront);
        a = (TextView) findViewById(R.id.DisplayBack);
        answer.setVisibility(View.GONE);
        adapter = new MicoDbAdapter(this);
        count = 0;
        fliper = 0;
        sound = new Audio();
        sound.loadSounds(this);
        activeUser = adapter.getActiveUser();
        frontArr = adapter.usersFlashCardsFront(activeUser);
        backArr = adapter.usersFlashCardsBack(activeUser);
        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);
        length = frontArr.length;
        if (frontArr.length != 0) {
            setText();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_cards, menu);
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
        if (id == R.id.addbutton) {
            Intent createcard = new Intent(this, CreateCards.class);
            startActivity(createcard);
        }
        if (id == R.id.deletebutton) {
            if (length != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("delete Flash Card")
                        .setMessage("Are you sure you want to delete ?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteFcard(q.getText().toString());
                                if (length == 1) {
                                    q.setText(null);
                                    a.setText(null);
                                    length = 0;
                                }
                                if (length == frontArr.length && count == 0) {
                                    length = frontArr.length;
                                    frontArr = adapter.usersFlashCardsFront(activeUser);
                                    backArr = adapter.usersFlashCardsBack(activeUser);
                                    String front = frontArr[count];
                                    String back = backArr[count];
                                    q.setText(front);
                                    a.setText(back);
                                    length--;
                                } else if (length == 2 && count == 0) {
                                    length = frontArr.length;
                                    frontArr = adapter.usersFlashCardsFront(activeUser);
                                    backArr = adapter.usersFlashCardsBack(activeUser);
                                    String front = frontArr[count];
                                    String back = backArr[count];
                                    q.setText(front);
                                    a.setText(back);
                                    length--;
                                } else if (length == frontArr.length) {
                                    length--;
                                    count--;
                                    frontArr = adapter.usersFlashCardsFront(activeUser);
                                    backArr = adapter.usersFlashCardsBack(activeUser);
                                    String front = frontArr[count];
                                    String back = backArr[count];
                                    q.setText(front);
                                    a.setText(back);
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Message.message(this, "No flash card selected to delete");
            }
        } else if (id == R.id.userTag) {
            Message.message(this, "User can only be changed at main menu.");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setText();
    }

    public void setUser() {
        MenuItem User = menu.findItem(R.id.userTag);
        User.setTitle(activeUser);
    }

    public void setText() {
        length = frontArr.length;
        if (length != 0 || count != 0) {
            frontArr = adapter.usersFlashCardsFront(activeUser);
            backArr = adapter.usersFlashCardsBack(activeUser);
            String front = frontArr[count];
            String back = backArr[count];
            q.setText(front);
            a.setText(back);
            length = frontArr.length;
        }
    }

    public void NextClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        if (fliper != 0) {
            answer.setVisibility(View.GONE);
            question.setVisibility(View.VISIBLE);
            fliper = 0;
        }
        if (length != 0) {
            if (count == length - 1) {
                count = 0;
                setText();
            } else {
                count++;
                setText();
            }
        } else {
            Message.message(this, "No flash Cards saved");
        }
    }

    public void PrevClick(View v) {
        if (soundOn) {
            sound.playPop();
        }
        if (fliper != 0) {
            answer.setVisibility(View.GONE);
            question.setVisibility(View.VISIBLE);
            fliper = 0;
        }
        if (length != 0) {
            if (count == 0) {
                count = length - 1;
                setText();
            } else {
                count--;
                setText();
            }
        } else {
            Message.message(this, "No flash Cards saved");
        }
    }

    public void FlipClick(View v) {
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
    }
}