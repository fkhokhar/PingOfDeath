package com.utexas.cs371m.fahad.pingofdeath;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;

/**
 * Created by jason_000 on 12/7/2015.
 */
public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void play(View v) {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}
