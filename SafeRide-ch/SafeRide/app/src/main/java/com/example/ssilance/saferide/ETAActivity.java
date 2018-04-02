package com.example.ssilance.saferide;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Sacha on 2018-01-27.
 */

public class ETAActivity extends Activity {

    private Firebase myFirebaseRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eta_message);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.3));
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");

        configureDoneBtn();
    }

    private void configureDoneBtn(){
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtnETA);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText mess = (EditText) findViewById(R.id.writeMessageETA);
                String getTheMess = mess.getText().toString();

                if(getTheMess.equals("")) {
                   mess.setHint("Don't forget to enter something!");
                    mess.setHintTextColor(Color.RED);

                }
                else {

                    int add1 = Integer.parseInt(getTheMess);
                    Calendar now = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                    String actualtime = df.format(now.getTime());
                    now.add(Calendar.MINUTE, add1);

                    String stringToPass = df.format(now.getTime());

                    myFirebaseRef.child("eta").setValue(stringToPass);
                    myFirebaseRef.child("message").setValue(actualtime + "  SafeRide has departed.");
                    finish();
                }


            }
        });
    }
}