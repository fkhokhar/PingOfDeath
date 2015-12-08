package com.utexas.cs371m.fahad.pingofdeath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FinalWaiting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();
        final User thisUser = (User) intent.getSerializableExtra("value");

        /* add logic for waiting on 2nd player here */

        Firebase.setAndroidContext(this);
        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Current number of players: " + dataSnapshot.getValue()); // for debugging

                if(((Long) dataSnapshot.getValue() == 6L)){
                    Intent myIntent = new Intent(getApplicationContext(), FinalBattle.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("value", thisUser);
                    startActivity(myIntent);
                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }


        });

    }
}
