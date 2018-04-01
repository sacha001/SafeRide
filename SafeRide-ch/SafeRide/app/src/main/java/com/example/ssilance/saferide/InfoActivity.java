package com.example.ssilance.saferide;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sacha on 2018-01-27.
 */

public class InfoActivity extends Activity {
    public static final String MY_PROFILE = "Profile";
    private Firebase myFirebaseRef;

    private TextView mMonday;
    private TextView mFriday;
    private TextView mSunday;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width*0.8), (int) (height*0.4));

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");
        mMonday = (TextView) findViewById(R.id.setMtoTH);
        mFriday = (TextView) findViewById(R.id.setFri);
        mSunday = (TextView) findViewById(R.id.setSunday);
        listen();
       // configureDoneBtn();
    }

   // private void configureDoneBtn(){
    //    Button driverLoginBtn = (Button) findViewById(R.id.doneBtnETA);
    //    driverLoginBtn.setOnClickListener(new View.OnClickListener() {
       //     @Override
    //        public void onClick(View view) {

     //               finish();



     //       }
     //   });
   // }
    public void listen()
    {
        myFirebaseRef.child("Monday-Thursday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                mMonday.setText(text);

            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });



        myFirebaseRef.child("Friday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                mFriday.setText(text);



            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });


        myFirebaseRef.child("Sunday").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;

                if (v == null) {text = "";}
                else {text = v.toString();}
                mSunday.setText(text);

            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });

    }

}