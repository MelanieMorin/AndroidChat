package com.melmo.androidchat.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melmo.androidchat.R;
import com.melmo.androidchat.model.Message;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by quentin for MyChat on 19/12/2018.
 */
public class MessageAdapter extends RecyclerView.Adapter<com.melmo.androidchat.adapter.MessageViewHolder> {

    ArrayList<Message> arrayListMessage;

    public MessageAdapter(final Activity activity) {
        super();
        arrayListMessage = new ArrayList<>();

        final Request request = new Request.Builder()
                .url("http://51.15.207.57:8080/messages")
                .build();
        OkHttpClient clientWeb = new OkHttpClient();
        clientWeb.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonMessages = response.body().string();
                Gson gson = new Gson();
                arrayListMessage = gson.fromJson(
                        jsonMessages,
                        new TypeToken<List<Message>>(){}.getType());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItemView =LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message,parent,false);
        return new MessageViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull com.melmo.androidchat.adapter.MessageViewHolder holder, int position) {
        Message messsageToShow = arrayListMessage.get(position);
        holder.textViewDate.setText(messsageToShow.getCreationdate());
        holder.textViewMessage.setText(messsageToShow.getMessage());
        holder.textViewUsername.setText(messsageToShow.getUtilisateur().getUsername());

    }

    @Override
    public int getItemCount() {
        return arrayListMessage.size();
    }

    public String formatDate(String dateNonFormatee){
        String retourDate;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date creationDate = parser.parse(dateNonFormatee);
            retourDate = new SimpleDateFormat("E:d:M à  HH:mm").format(creationDate).toString();

        } catch (ParseException e) {

            e.printStackTrace();

            retourDate = "Date inconnue";
        }
        return retourDate;

    }
}
