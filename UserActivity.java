package com.melmo.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melmo.androidchat.model.Utilisateur;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    OkHttpClient clientWeb;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Affectation des vues
        final EditText editTextUsername = findViewById(R.id.editTextUsername);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonConnexion = findViewById(R.id.buttonConnexion);
        Button buttonInscription = findViewById(R.id.buttonInscription);


        clientWeb = new OkHttpClient();

        //GESTION Connexion
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
                                new TypeToken<List<Utilisateur>>(){}.getType());

                        String userNameFilled = editTextUsername.getText().toString();
                        String passwordFilled = editTextPassword.getText().toString();
                        for(Utilisateur u : list){
                            Log.d("User", "onResponse: "+ u.getUsername() + ":" +u.getPassword());
                            if (userNameFilled.equals(u.getUsername()) &&
                                    passwordFilled.equals(u.getPassword())){
                                SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
                                sp.edit().putString("id",u.getId()).apply();

                                Intent intentToMainActivity = new Intent(UserActivity.this,MainActivity.class);
                                startActivity(intentToMainActivity);
                                finish();
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserActivity.this, "Identifiant ou mdp incorrect", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
            }
        });

        buttonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();


                String json = new Gson().toJson(new Utilisateur(null,username,password));
                RequestBody requestBody = RequestBody.create(JSON,json);
                final Request request = new Request.Builder()
                        .url("http://51.15.207.57:8080/utilisateurs")
                        .post(requestBody)
                        .build();

                clientWeb.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonUser = response.body().string();
                        Gson gson = new Gson();
                        final Utilisateur userInscrit = gson.fromJson(
                                jsonUser,
                                new TypeToken<Utilisateur>(){}.getType());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserActivity.this, "Vous êtes bien inscrit : "+userInscrit.getId(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });


    }
}
