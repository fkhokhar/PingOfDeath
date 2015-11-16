package com.utexas.cs371m.fahad.pingofdeath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Battle extends AppCompatActivity {

    User thisUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        System.out.println("I'm in the battle class!");

        Intent intent = getIntent();
        thisUser = (User) intent.getSerializableExtra("value");
    }

    public void pressedPing(View view){
        /* Get the other user's object */
    }
}
