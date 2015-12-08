package com.utexas.cs371m.fahad.pingofdeath;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable{

    public Firebase fb;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        context = getApplicationContext();
        fb = new Firebase("https://pingofdeath.firebaseio.com/");
    }

    public void startGameClicked(View view){
        EditText field = (EditText) findViewById(R.id.editText);

        if(field.getText().toString().length() > 0){
            startGameClickedHelper();
        } else{
            Toast.makeText(context, "Cannot start game without a name!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startGameClickedHelper(){
        Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(((Long) snapshot.getValue() < 2L)){
                    Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");
                    ref.setValue((Long) snapshot.getValue() + 1L);

                    System.out.println(snapshot.getValue()); // for debugging

                    EditText field = (EditText) findViewById(R.id.editText);
                    String playerName = field.getText().toString();
                    String roomNumber = "room1";

                    Firebase playerRef = new Firebase("https://pingofdeath.firebaseio.com/rooms/room1/users/" + playerName);

                    User player = new User(playerName, false, roomNumber);

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

                } else if((Long) snapshot.getValue() < 4L && (Long) snapshot.getValue() >= 2L) {
                    System.out.println("I am the third player");

                    Firebase ref = new Firebase("https://pingofdeath.firebaseio.com/rooms/numPlayers");
                    ref.setValue((Long) snapshot.getValue() + 1L);

                    System.out.println(snapshot.getValue()); // for debugging

                    EditText field = (EditText) findViewById(R.id.editText);
                    String playerName = field.getText().toString();
                    String roomNumber = "room2";

                    Firebase playerRef = new Firebase("https://pingofdeath.firebaseio.com/rooms/room2/users/" + playerName);

                    User player = new User(playerName, false, roomNumber);

                    playerRef.setValue(player);

                    Intent myIntent;

                    if(((Long) snapshot.getValue() == 3L)){ //4th player's entry logic
                        myIntent = new Intent(getApplicationContext(), Battle.class);
                    } else { //1st player's entry logic
                        myIntent = new Intent(getApplicationContext(), Waiting.class);
                    }

                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    myIntent.putExtra("value", player);
                    getApplicationContext().startActivity(myIntent);

                } else {
                    /* rooms are full -- do nothing */
                    Toast.makeText(context, "No more rooms available! Wait till the next round!!!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}
