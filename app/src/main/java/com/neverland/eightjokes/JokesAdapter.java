package com.neverland.eightjokes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.neverland.eightjokes.entities.Joke;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_joke_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.jokeContentTextView = (TextView) convertView.findViewById(R.id.jokeContentTextView);
            viewHolder.shareButton = (Button) convertView.findViewById(R.id.shareButton);
            viewHolder.rateUpButton = (Button) convertView.findViewById(R.id.rateUpButton);
            viewHolder.rateDownButton = (Button) convertView.findViewById(R.id.rateDownButton);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        Joke currentJoke = allJokes.get(position);

        viewHolder.jokeContentTextView.setText(currentJoke.getContent());

        return convertView;
    }

    private class ViewHolder {

        private TextView jokeContentTextView;
        private Button shareButton;
        private Button rateUpButton;
        private Button rateDownButton;
    }
}
