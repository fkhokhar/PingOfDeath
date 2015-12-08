package com.utexas.cs371m.fahad.pingofdeath;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.AbsoluteLayout;
import android.util.DisplayMetrics;
import java.util.Random;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Battle extends AppCompatActivity {

    public User thisUser;
    public Handler handler;
    public Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        Firebase.setAndroidContext(this);

        //System.out.println("I'm in the battle class!");

        Button button = (Button)findViewById(R.id.button2);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        Random rand = new Random();

        int randx = rand.nextInt(width/2);
        int randy = rand.nextInt(height/2);
        button.setX(randx);
        button.setY(randy);

        Intent intent = getIntent();
        thisUser = (User) intent.getSerializableExtra("value");

        /* spawn a thread that periodically checks if a user has pinged their opponent or not */
        handler = new Handler();
        r = new Checker(this);
        handler.postDelayed(r, 100);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);

    }

    public void pressedPing(View view){

        /* set own ping to true */

        Firebase temp = new Firebase("https://pingofdeath.firebaseio.com/rooms/" + thisUser.getRoomNumber() + "/users/"
                + thisUser.getUsername());

        User player = new User(thisUser.getUsername(), true, thisUser.getRoomNumber());
        temp.setValue(player);
    }
}
