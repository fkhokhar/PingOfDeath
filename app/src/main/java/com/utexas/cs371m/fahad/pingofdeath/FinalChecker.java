package com.utexas.cs371m.fahad.pingofdeath;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

/**
 * Created by fahad on 11/29/15.
 */
public class FinalChecker extends AppCompatActivity implements Runnable {

    FinalBattle myBattle;

    public FinalChecker(FinalBattle myBattle){
        this.myBattle = myBattle;
    }

    public void run(){

        try{

            Firebase.setAndroidContext(myBattle.getApplicationContext());

            /* Check the winner */
            Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/finalRound/users/");

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
                        //Toast.makeText(myBattle, "YOU WON THE FINAL ROUND!!!", Toast.LENGTH_SHORT).show();

                        Button ib = (Button) myBattle.findViewById(R.id.button3);
                        ib.setVisibility(Button.GONE);

                        TextView et = (TextView) myBattle.findViewById(R.id.textView3);
                        et.setText("YOU WON THE FINAL ROUND!!!!!!!");
                    } else {  //the opponent won
                        Button ib = (Button) myBattle.findViewById(R.id.button3);
                        ib.setVisibility(Button.GONE);

                        TextView et = (TextView) myBattle.findViewById(R.id.textView3);
                        et.setText("YOU LOST THE FINAL ROUND!!!!!!!");
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

            this.myBattle.handler.postDelayed(this, 100);

        } catch(Resources.NotFoundException e){
            Log.d("Updater", "Things not initialized yet!");
        }

    }
}
