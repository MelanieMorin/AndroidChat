package com.melmo.androidchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get URL result
        client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://baconipsum.com/api/?type=meat-and-filler")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.d("response", "onResponse:"+response.body().string());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setTextEditText(response.body().string());
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }
    private void setTextEditText(String texte){
        EditText editTextSaisie = findViewById(R.id.editText);
        editTextSaisie.setText(texte);
    }
}
