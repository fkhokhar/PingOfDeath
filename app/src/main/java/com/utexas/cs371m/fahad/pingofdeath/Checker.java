package com.utexas.cs371m.fahad.pingofdeath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by fahad on 11/29/15.
 */
public class Checker extends AppCompatActivity implements Runnable {

    public Context tempContext;

    Battle myBattle;

    public Checker(Battle myBattle){
        this.myBattle = myBattle;
    }

    public void run(){

        try{

            /* Check the winner */

            Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/"
                    + myBattle.thisUser.getRoomNumber() +"/users/");

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String s) {

                    /* Get the user's object */

                    String name = (String) snapshot.child("username").getValue();
                    Boolean successfullyPinged =  (Boolean) snapshot.child("successfullyPinged").getValue();
                    String roomNumber = (String) snapshot.child("roomNumber").getValue();
                    User temp = new User(name, successfullyPinged, roomNumber);

                    System.out.println(temp.getUsername() + " " +
                            temp.getSuccessfullyPinged() + " " + temp.getRoomNumber());

                    /* Decide the winner */

                    if (temp.getUsername().equals(myBattle.thisUser.getUsername())) { //it means I won
                        Toast.makeText(myBattle, "YOU WON!!!", Toast.LENGTH_SHORT).show();

                        /* Progress to the final round */

                        Firebase playerRef = new Firebase("https://pingofdeath.firebaseio.com/rooms/finalRound/users/" + myBattle.thisUser.getUsername());
                        playerRef.setValue(myBattle.thisUser);

                        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {

                                Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");
                                ref.setValue((Long) snapshot.getValue() + 1L);
                                System.out.println(snapshot.getValue()); // for debugging

                                Intent myIntent;

                                if (((Long) snapshot.getValue() == 5L)) { //2nd player's entry logic for final round
                                    myIntent = new Intent(myBattle.getApplicationContext(), FinalBattle.class);
                                } else { //1st player's entry logic for final round
                                    myIntent = new Intent(myBattle.getApplicationContext(), FinalWaiting.class);
                                }

                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("value", myBattle.thisUser);
                                myBattle.handler.removeCallbacks(myBattle.r);
                                myBattle.startActivity(myIntent);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });


                    } else {  //the opponent won
                        Toast.makeText(myBattle, "YOU LOST!!!", Toast.LENGTH_SHORT).show();
                        myBattle.handler.removeCallbacks(myBattle.r);
                        myBattle.finish();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });

            this.myBattle.handler.postDelayed(this, 500);

        } catch(Resources.NotFoundException e){
            Log.d("Updater", "Things not initialized yet!");
        }

    }
}
