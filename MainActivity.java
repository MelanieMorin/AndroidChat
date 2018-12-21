package com.melmo.androidchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melmo.androidchat.adapter.MessageAdapter;
import com.melmo.androidchat.model.Message;
import com.melmo.androidchat.model.Utilisateur;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    MessageAdapter messageAdapter;
    RecyclerView recyclerViewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextMessageField = findViewById(R.id.editText);
        ImageButton buttonSend = findViewById(R.id.imageButton);
        messageAdapter = new MessageAdapter(this);

        //Affichage de notre liste
        recyclerViewMessage = findViewById(R.id.recyclerView);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerViewMessage.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //TODO GET MESSAGE FILLED
                String messageField = editTextMessageField.getText().toString();
                String idUser = getSharedPreferences("user",MODE_PRIVATE).getString("id","");

                //START CHANGE
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                SimpleDateFormat parser= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String formatted = parser.format(cal.getTime());
                String creationDate="Date inconnue";
                try {
                    creationDate = parser.parse(formatted).toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                //TODO SEND TO API
                Utilisateur userToSend = new Utilisateur(idUser);
                Message messageToSend = new Message(messageField, userToSend, creationDate);
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
                        Log.d("MainActivity", "onResponse: "+response.body());
                        final Message messageToDisplay = new Gson().fromJson(
                                response.body().string(),
                                new TypeToken<Message>(){}.getType()
                        );

                        //todo add message adapter
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextMessageField.setText("");
                                hideKeyboard(MainActivity.this);
                                messageAdapter.add(messageToDisplay);
                                recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount()-1);
                            }
                        });


                    }
                });
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuReload) {
            messageAdapter.reloadMessages(MainActivity.this);
            recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount()-1);
        }
        return true;
    }


}