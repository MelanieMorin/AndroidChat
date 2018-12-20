package com.melmo.androidchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.melmo.androidchat.R;

/**
 * Created by quentin for MyChat on 19/12/2018.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewUsername;
    public TextView textViewMessage;
    public TextView textViewDate;

    public MessageViewHolder(View itemView) {
        super(itemView);
        textViewUsername = itemView.findViewById(R.id.textViewUsername);
        textViewMessage = itemView.findViewById(R.id.textViewMessage);
        textViewDate = itemView.findViewById(R.id.textViewDate);
    }
}
