package com.micocards.cclj.micocards;

/*
 * Trivia.java
 *
 * Version 1
 *
 * 18/02/15
 *
 * @author Conor Keenan, x13343806
 *
 */

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Users extends ActionBarActivity {
    static Activity oldInstance;
    private SimpleCursorAdapter sca;
    private MicoDbAdapter dbAdapter;
    private EditText uName;
    private Button addUserBtn;
    private LinearLayout viewUsers, viewAddUsers;
    private ListView uList;
    private Boolean soundOn;
    private SharedPreferences preferences;
    private Audio sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        dbAdapter = new MicoDbAdapter(this);


        oldInstance = Home.getInstance();

        uList = (ListView) findViewById(R.id.userList);
        uName = (EditText) findViewById(R.id.uName);
        addUserBtn = (Button) findViewById(R.id.addUserBtn);
        viewUsers = (LinearLayout) findViewById(R.id.viewUser);
        viewAddUsers = (LinearLayout) findViewById(R.id.addUser);

        sound = new Audio();
        sound.loadSounds(this);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        soundOn = preferences.getBoolean("soundOn", true);


        setUserList();

    }


    public void setUserList() {
        sca = dbAdapter.viewUserList(this);

        uList.setAdapter(sca);

        uList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (soundOn) {
                    sound.playPop();
                }

                TextView user = (TextView) view.findViewById(android.R.id.text1);
                final String name = user.getText().toString();


                dbAdapter.setActiveUser(name);

                Intent i = new Intent(Users.this, Home.class);
                startActivity(i);
                oldInstance.finish();
                finish();


            }
        });

        uList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int userCount = dbAdapter.numUsers();
                TextView user = (TextView) view.findViewById(android.R.id.text1);
                final String name = user.getText().toString();
                String activeUser = dbAdapter.getActiveUser();

                if (userCount == 1) {
                    Message.message(Users.this, "Their Must Be At Least One User.");
                } else if (name.equalsIgnoreCase(activeUser)) {
                    Message.message(Users.this, "You Connot Delete the Active User.");

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Users.this);
                    builder
                            .setTitle("Delete User")
                            .setMessage("Are you sure you want to delete " + name + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    dbAdapter.deleteUser(name);
                                    setUserList();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();


                }

                return true;

            }
        });


    }

    public void addUserBtn(View v) {
        if (soundOn) {
            sound.playPop();
        }
        String name;
        if (viewUsers.getVisibility() == v.VISIBLE) {
            viewUsers.setVisibility(v.GONE);
            viewAddUsers.setVisibility(v.VISIBLE);
        } else {
            name = uName.getText().toString();
            if (name == null || name.equals("") || name.equals(" ")) {

                Message.message(this, "Please enter a Name");

            } else {


                dbAdapter.addUser(name);


                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(uName.getWindowToken(), 0);

                setUserList();
                uName.setText(null);
                viewUsers.setVisibility(v.VISIBLE);
                viewAddUsers.setVisibility(v.GONE);

            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_users, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
