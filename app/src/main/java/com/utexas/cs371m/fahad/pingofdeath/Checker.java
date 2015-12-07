package com.utexas.cs371m.fahad.pingofdeath;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
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
public class Checker implements Runnable {

    Battle myBattle;

    public Checker(Battle myBattle){
        this.myBattle = myBattle;
    }

    public void run(){

        try{

            /* Check the winner */

            Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/users/");

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {

                }

                @Override
                public void onChildChanged(DataSnapshot snapshot, String s) {

                    /* Get the user's object */

                    String name = (String) snapshot.child("username").getValue();
                    Boolean successfullyPinged =  (Boolean) snapshot.child("successfullyPinged").getValue();
                    User temp = new User(name, successfullyPinged);

                    System.out.println(temp.getUsername() + " " + temp.getSuccessfullyPinged());

                    /* Decide the winner */

                    if (temp.getUsername().equals(myBattle.thisUser.getUsername())) { //it means I won
                        Toast.makeText(myBattle, "YOU WON!!!", Toast.LENGTH_SHORT).show();
                    } else {  //the opponent won
                        Toast.makeText(myBattle, "YOU LOST!!!", Toast.LENGTH_SHORT).show();

                        /* decrease the player count */
                        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/numPlayers");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/numPlayers");
                                ref.setValue((Long) dataSnapshot.getValue() - 1L);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    myBattle.finish();
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

            this.myBattle.handler.postDelayed(this, 100);

        } catch(Resources.NotFoundException e){
            Log.d("Updater", "Things not initialized yet!");
        }

    }
}
