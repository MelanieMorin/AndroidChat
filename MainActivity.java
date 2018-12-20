package com.melmo.androidchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.melmo.androidchat.adapter.MessageAdapter;
import com.melmo.androidchat.model.Message;
import com.melmo.androidchat.model.Utilisateur;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextMessageFilled = findViewById(R.id.editText);
        ImageButton buttonSend = findViewById(R.id.imageButton);

        //Affichage de notre liste
        RecyclerView recyclerViewMessage = findViewById(R.id.recyclerView);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerViewMessage.setAdapter(new MessageAdapter(this));

        buttonSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO GET MESSAGE FILLED
                String messageFilled = editTextMessageFilled.getText().toString();
                String idUser = getSharedPreferences("user",MODE_PRIVATE).getString("id","");
                SimpleDateFormat parser= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String creationDate="Date inconnue";
                try {
                    creationDate = parser.parse(new Date().toString()).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //TODO SEND TO API
                Utilisateur userToSend = new Utilisateur(idUser, "","");
                Message messageToSend = new Message(messageFilled, userToSend, creationDate);
                String jsonMessage = new Gson().toJson(messageToSend);
                final Request request = new Request.Builder()
                        .url("http://51.15.207.57:8080/messages")
                        .post(RequestBody.create(UserActivity.JSON, jsonMessage))
                        .build();

                client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("MainActivity", "onResponse: "+e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("MainActivity", "onResponse: "+response.body().toString());
                    }
                });

                //TODO ADD MESSAGE TO ADAPTER

            }
        });
    }

}