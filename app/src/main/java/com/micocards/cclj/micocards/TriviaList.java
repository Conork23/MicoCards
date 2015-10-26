package com.micocards.cclj.micocards;

/*
 * TriviaList.java
 *
 * Version 1
 *
 * 18/02/15
 *
 * @author Conor Keenan, x13343806
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class TriviaList extends ActionBarActivity {

    static Activity oldInstance;
    private SimpleCursorAdapter sca;
    private MicoDbAdapter dbAdapter;
    private Menu menu;
    private String activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_list);
        oldInstance = Trivia.getInstance();
        dbAdapter = new MicoDbAdapter(this);
        sca = dbAdapter.viewTriviaQuestionList(this);


        ListView qList = (ListView) findViewById(R.id.tqList);
        qList.setAdapter(sca);

        qList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);
                int qNum = c.getInt(0);

                Intent i = new Intent(TriviaList.this, Trivia.class);
                i.putExtra("qNum", qNum);
                startActivity(i);
                oldInstance.finish();


            }
        });

    }

    public void setUser() {
        MenuItem User = menu.findItem(R.id.userTag);
        activeUser = dbAdapter.getActiveUser();

        User.setTitle(activeUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trivia_list, menu);
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
