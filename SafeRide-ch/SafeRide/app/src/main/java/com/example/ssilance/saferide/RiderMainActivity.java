package com.example.ssilance.saferide;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.WriterException;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Sacha on 2018-01-27.
 */



public class RiderMainActivity extends AppCompatActivity {

    private TextView header;
    private TextView receive;
    private Firebase myFirebaseRef;
    public static final String MESSAGE = "Message";
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        header = (TextView) findViewById(R.id.header2);
        receive = (TextView) findViewById(R.id.receive);

        // Setup Firebase
        Firebase.setAndroidContext(this);
        // Replace the string below with your Firebase URL
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");


        //SharedPreferences to save the Firebase message
        SharedPreferences prefs = getSharedPreferences(MESSAGE, MODE_PRIVATE);
        String restoredText = prefs.getString("message", null);
        if(restoredText != null)
        {
            String message = prefs.getString("message", null);
            receive.setText(message);
        }






        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            // An event listener that listens for changes in the value of the key "message"
            // in Firebase
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                // When the data has changed, get the new value
                Object v = snapshot.getValue();
                String text;
                // The data can be null, so check for that...
                if (v == null) {
                    text = "";
                }
                else {
                    text = v.toString();
                }
                // Update the TextView that displays the current message
                receive.setText(text);
                NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                       .setContentTitle(text);
               //         .setContentText("Click here");
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                 //notificationID allows you to update the notification later on.
                mNotificationManager.notify(1, mBuilder.build());

                 editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                 editor.putString("message", text);
                 editor.apply();


            }
            @Override public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });



        Button createProfile = (Button) findViewById(R.id.createProfileBtn);
        createProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(RiderMainActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });

        Button viewProfile = (Button) findViewById(R.id.viewProfileBtn);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // String addressString;
               // String nameString;
                String addressString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ADDRESS","");
                String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME","");

                //SharedPreferences prefs = getSharedPreferences(MY_PROFILE, MODE_PRIVATE);

                //     String nameString = prefs.getString("name", null);//"No name defined" is the default value.
                //    String addressString = prefs.getString("address", null); //0 is the default value.


                String data = nameString + "\n" + addressString;


                Toast.makeText(getApplicationContext(), data,
                        Toast.LENGTH_LONG).show();

            }
        });

        Button qrCode = (Button) findViewById(R.id.qrBtn);
        qrCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent2 = new Intent(RiderMainActivity.this, GenerateQRCodeActivity.class);
                startActivity(intent2);
            }
        });
    }


    /** @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == R.id.createProfileBtn){
    Intent intent = new Intent(RiderMainActivity.this, ProfileSettingsActivity.class);
    startActivity(intent);
    return true;
    } else if(item.getItemId() == R.id.qrBtn){
    Intent intent = new Intent(RiderMainActivity.this, GenerateQRCodeActivity.class);
    startActivity(intent);
    return true;
    } else {
    return super.onOptionsItemSelected(item);
    }
    }
     */
}
