package com.melmo.androidchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melmo.androidchat.model.Utilisateur;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    OkHttpClient clientWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //affectation des vues
        EditText editTextUserName = findViewById(R.id.editTextUserName);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonConnexion = findViewById(R.id.buttonConnexion);
        Button buttonInscription = findViewById(R.id.buttonInscription);

        clientWeb = new OkHttpClient();

        buttonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Request request = new Request.Builder()
                        .url("http://51.15.207.57:8080/utilisateurs")
                        .build();
                clientWeb.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {}

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonUsers = response.body().string();
                        Gson gson = new Gson();
                        List<Utilisateur> list = gson.fromJson(
                                jsonUsers,
                                new TypeToken<List<Utilisateur>>(){}.getType()
                        );
                    }
                });
            }
        });

        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
