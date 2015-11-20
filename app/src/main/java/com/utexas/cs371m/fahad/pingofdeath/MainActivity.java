package com.utexas.cs371m.fahad.pingofdeath;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Firebase fb;
    Activity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        fb = new Firebase("https://pingofdeath.firebaseio.com/");
        //fb.child("rooms").child("room1").child("numPlayers").setValue(0); // comment this line when checking 2nd player
    }

    public void startGameClicked(View view){
        EditText field = (EditText) findViewById(R.id.editText);

        if(field.getText().toString().length() > 0){
            startGameClickedHelper();
        }
    }

    public void startGameClickedHelper(){
        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/numPlayers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(((Long) snapshot.getValue() < 2L)){
                    Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/numPlayers");
                    ref.setValue((Long) snapshot.getValue() + 1L);

                    System.out.println(snapshot.getValue()); // for debugging

                    EditText field = (EditText) findViewById(R.id.editText);
                    String playerName = field.getText().toString();

                    Firebase playerRef = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/users/" + playerName);

                    User player = new User(playerName, false);

                    playerRef.setValue(player);

                    Intent myIntent;

                    if(((Long) snapshot.getValue() == 1L)){ //2nd player's entry logic
                        myIntent = new Intent(getApplicationContext(), Battle.class);
                    } else { //1st player's entry logic
                        myIntent = new Intent(getApplicationContext(), Waiting.class);
                    }

                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("value", player);
                    getApplicationContext().startActivity(myIntent);

                } else {
                    /* room is full -- do nothing */
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
