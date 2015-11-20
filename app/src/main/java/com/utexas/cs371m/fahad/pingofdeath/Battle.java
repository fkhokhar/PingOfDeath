package com.utexas.cs371m.fahad.pingofdeath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

        /* set own ping to true */

        Firebase temp = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/users/"
                + thisUser.getUsername() + "/successfullyPinged");

        temp.setValue(true);

        /* check who won */

        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/users/");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User temp = postSnapshot.getValue(User.class);
                    System.out.println(temp.getUsername() + " " + temp.getSuccessfullyPinged());

                    /* Get the other user's object */

                    if(!temp.getUsername().equals(thisUser.getUsername())){
                        User opponent = temp;
                        //System.out.println(opponent.getUsername() + " " + opponent.getSuccessfullyPinged());

                        /* Decide the winner */
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }
}
