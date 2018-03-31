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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
    private TextView etatext;
    private TextView capacity;
    private Firebase myFirebaseRef;
    public static final String MESSAGE = "Message";
    private SharedPreferences.Editor editor;
    private final int REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        String addressString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ADDRESS", "");
        String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME", "");

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Welcome " + nameString);


        header = (TextView) findViewById(R.id.header2);
        receive = (TextView) findViewById(R.id.receive);
        etatext = (TextView) findViewById(R.id.ETA2);
        capacity = (TextView) findViewById(R.id.receive2);
        capacity.setText("5");

        // Setup Firebase
        Firebase.setAndroidContext(this);
        // Replace the string below with your Firebase URL
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");


        //SharedPreferences to save the Firebase message
        SharedPreferences prefs = getSharedPreferences(MESSAGE, MODE_PRIVATE);
        String restoredText = prefs.getString("message", null);
        String restoredText2 = prefs.getString("eta", null);
        String restoredText3 = prefs.getString("capacity", null);
        if(restoredText2 != null)
        {
            String message = prefs.getString("eta", null);
            etatext.setText(message);
        }
        if (restoredText != null) {
            String message = prefs.getString("message", null);
            receive.setText(message);
        }
        if (restoredText3 != null) {
            String message = prefs.getString("capacity", null);
            capacity.setText(message);
        }

        Button qrCode = (Button) findViewById(R.id.qrBtn);
        qrCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent2 = new Intent(RiderMainActivity.this, GenerateQRCodeActivity.class);
                startActivity(intent2);
            }
        });

        listen();
    }

    public void listen()
    {
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                receive.setText(text);
                NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(text);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("message", text);
                editor.apply();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });



        myFirebaseRef.child("eta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                etatext.setText(text);
                NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(text);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("eta", text);
                editor.apply();


            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });


        myFirebaseRef.child("capacity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;

                if (v == null) {text = "";}
                else {text = v.toString();}
                capacity.setText(text);
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("capacity", text);
                editor.apply();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        ActionBar mActionBar = getSupportActionBar();
        String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME", "");
        mActionBar.setTitle("Welcome " + nameString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editNameItem) {
            startActivityForResult(new Intent(RiderMainActivity.this, EditNameActivity.class), REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }
}
