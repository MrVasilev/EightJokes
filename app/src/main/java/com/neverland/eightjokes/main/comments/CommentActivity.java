package com.neverland.eightjokes.main.comments;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.neverland.eightjokes.R;
import com.neverland.eightjokes.entities.Comment;

import java.util.ArrayList;

public class CommentActivity extends ActionBarActivity {

    private ListView commentsListView;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String joke = extras.getString("joke");

        ArrayList<Comment> allComments = new ArrayList<Comment>();

        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        comment1.setAuthor("John Smith");
        comment1.setContent("The joke is not really good but it is not bad :)");
        comment2.setAuthor("Michael Jordan");
        comment2.setContent("I like to move it move it, \nI like to move it move it, \nI like to move it move it");

        allComments.add(comment1);
        allComments.add(comment2);

        commentsListView = (ListView) findViewById(R.id.commentsListView);
        commentsAdapter = new CommentsAdapter(this, allComments);
        commentsListView.setAdapter(commentsAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
