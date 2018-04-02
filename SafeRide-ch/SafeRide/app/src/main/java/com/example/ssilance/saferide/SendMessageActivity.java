package com.example.ssilance.saferide;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sacha on 2018-01-27.
 */

public class SendMessageActivity extends Activity {

    private Firebase myFirebaseRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        // Setup Firebase
        Firebase.setAndroidContext(this);
        // Replace the string below with your Firebase URL
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");
        configureDoneBtn();
    }

    private void configureDoneBtn(){
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                String actualtime = df.format(now.getTime());
                EditText mess = (EditText) findViewById(R.id.writeMessage);
                String getMessage = mess.getText().toString();
                if(!getMessage.equals("")) {
                    getMessage = actualtime + " " + mess.getText().toString();

                    myFirebaseRef.child("message").setValue(getMessage);
                    finish();
                }else{
                    mess.setHint("Don't forget to enter something!");
                    mess.setHintTextColor(Color.RED);
                }

            }
        });
    }
}