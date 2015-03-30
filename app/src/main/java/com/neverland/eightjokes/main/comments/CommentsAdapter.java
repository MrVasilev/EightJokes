package com.neverland.eightjokes.main.comments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neverland.eightjokes.R;
import com.neverland.eightjokes.entities.Comment;

import java.util.ArrayList;

/**
 * Created by Vasilev on 24.3.2015 г..
 */
public class CommentsAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Comment> allComments;
    private LayoutInflater inflater;

    public CommentsAdapter(Activity activity, ArrayList<Comment> allComments) {
        this.activity = activity;
        this.allComments = allComments;
        this.inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return allComments.size();
    }

    @Override
    public Object getItem(int position) {
        return allComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Comment comment) {
        if (comment != null)
            allComments.add(comment);
    }

    public void clear() {
        allComments.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_comment_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
            viewHolder.commentContentTextView = (TextView) convertView.findViewById(R.id.commentContentTextView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        Comment currentComment = allComments.get(position);

        viewHolder.userNameTextView.setText(currentComment.getAuthor());
        viewHolder.commentContentTextView.setText(currentComment.getContent());

        return convertView;
    }

    private class ViewHolder {

        private ImageView userImageView;
        private TextView userNameTextView;
        private TextView commentContentTextView;
    }
}
