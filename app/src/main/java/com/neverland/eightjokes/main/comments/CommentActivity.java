package com.neverland.eightjokes.main.comments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.neverland.eightjokes.R;
import com.neverland.eightjokes.entities.Comment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends ActionBarActivity implements View.OnClickListener {

    private Button postButton;
    private EditText commentEditText;
    private ListView commentsListView;

    private CommentsAdapter commentsAdapter;
    private ProgressDialog progressDialog;

    private String jokeId;
    private ArrayList<Comment> allComments = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postButton = (Button) findViewById(R.id.postButton);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        commentsListView = (ListView) findViewById(R.id.commentsListView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_message));

        Bundle extras = getIntent().getExtras();
        jokeId = extras.getString("joke_id");

        postButton.setOnClickListener(this);
        commentsAdapter = new CommentsAdapter(this, allComments);
        commentsListView.setAdapter(commentsAdapter);
        getAllComments();
    }

    /**
     * Make a request to get all comments of current Joke.
     */
    private void getAllComments() {

        progressDialog.show();

        ParseQuery<Comment> commentsQuery = ParseQuery.getQuery(Comment.class);

        commentsQuery.whereEqualTo("joke_id", jokeId);
        commentsQuery.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {

                if (e == null) {

                    allComments = (ArrayList<Comment>) comments;
                    updateAdapterData();
                }

                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.postButton) {

            if (jokeId != null) {

                saveComment();
            }
        }
    }

    /**
     * Save Comment in background
     */
    private void saveComment() {

        Comment comment = new Comment();
        ParseUser currentUser = ParseUser.getCurrentUser();
        String commentContent = commentEditText.getText().toString();

        if(!TextUtils.isEmpty(commentContent)) {

            comment.setAuthor(currentUser.getUsername());
            comment.setContent(commentContent);
            comment.setJokeId(jokeId);

            comment.saveInBackground();
            Toast.makeText(this, getString(R.string.successfully_post_comment_message), Toast.LENGTH_SHORT).show();

            finish();

        }else{
            Toast.makeText(this, getString(R.string.empty_comment_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAdapterData() {

        commentsAdapter.clear();

        for (Comment comment : allComments) {
            commentsAdapter.addItem(comment);
        }

        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
