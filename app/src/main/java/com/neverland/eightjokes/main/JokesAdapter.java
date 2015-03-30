package com.neverland.eightjokes.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.neverland.eightjokes.R;
import com.neverland.eightjokes.entities.Joke;
import com.neverland.eightjokes.main.comments.CommentActivity;

import java.util.ArrayList;

/**
 * Created by Vasilev on 7.3.2015 г..
 */
public class JokesAdapter extends BaseAdapter {

    private ArrayList<Joke> allJokes;
    private Activity activity;
    private LayoutInflater inflater;

    public JokesAdapter(Activity activity, ArrayList<Joke> allJokes) {
        this.activity = activity;
        this.allJokes = allJokes;
        this.inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return allJokes.size();
    }

    @Override
    public Object getItem(int position) {
        return allJokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Joke joke) {
        if (joke != null)
            allJokes.add(joke);
    }

    public void clear() {
        allJokes.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_joke_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.jokeContentTextView = (TextView) convertView.findViewById(R.id.jokeContentTextView);
            viewHolder.shareButton = (Button) convertView.findViewById(R.id.shareButton);
            viewHolder.commentButton = (Button) convertView.findViewById(R.id.commentButton);
            viewHolder.rateUpButton = (Button) convertView.findViewById(R.id.rateUpButton);
            viewHolder.rateDownButton = (Button) convertView.findViewById(R.id.rateDownButton);

            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        Joke currentJoke = allJokes.get(position);

        viewHolder.shareButton.setOnClickListener(rowButtonsOnClickListener);
        viewHolder.commentButton.setOnClickListener(rowButtonsOnClickListener);
        viewHolder.jokeContentTextView.setText(currentJoke.getContent());

        return convertView;
    }

    private View.OnClickListener rowButtonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view != null) {

                View parentRow = (View) view.getParent().getParent();

                if(view.getId() == R.id.commentButton)
                    parentRow = (View) parentRow.getParent();

                ListView listView = (ListView) parentRow.getParent();
                int selectedPosition = listView.getPositionForView(parentRow);
                Joke selectedJoke = null;

                if (selectedPosition >= 0 && allJokes != null) {
                    selectedJoke = allJokes.get(selectedPosition);
                }

                switch (view.getId()) {

                    case R.id.shareButton:
                        shareJoke(selectedJoke);
                        break;

                    case R.id.commentButton:
                        openJokeCommentScreen(selectedJoke);
                        break;

                    default:
                        break;
                }
            }
        }
    };

    /**
     * Create ACTION_SEND Intent which will display Share picker
     * and send to an another User
     */
    private void shareJoke(Joke joke) {

        if (joke != null) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, joke.getContent());
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        }
    }

    private void openJokeCommentScreen(Joke joke) {

        if (joke != null) {

            Intent commentIntent = new Intent(activity, CommentActivity.class);
            commentIntent.putExtra("joke", joke.toString());
            activity.startActivity(commentIntent);
        }
    }

    private class ViewHolder {

        private TextView jokeContentTextView;
        private Button shareButton;
        private Button commentButton;
        private Button rateUpButton;
        private Button rateDownButton;
    }
}
