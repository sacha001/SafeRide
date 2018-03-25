package com.example.ssilance.saferide;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.text.DateFormat;
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
                String currentTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
                EditText mess = (EditText) findViewById(R.id.writeMessageETA);
                String getTheMess = mess.getText().toString();
                int add = Integer.parseInt(getTheMess);

                Scanner scan = new Scanner(currentTime);
                scan.useDelimiter(":");
                String hour = scan.next();
                String mins = scan.next();
                String m = mins.substring(0,2);
                String amOrpm = mins.substring(2,mins.length());
                int HR = Integer.parseInt(hour);
                int M = Integer.parseInt(m);

                int total = M + add;
                String stringToPass = "";
                if(total >= 60) {
                    M = total - 60;
                    if(HR != 12) {HR += 1;}
                    else{
                        if(amOrpm.equalsIgnoreCase("pm") && HR == 12) {
                            HR = 1;
                            amOrpm = "am";
                        }
                        else{
                            HR = 1;
                            amOrpm = "pm";
                        }
                    }
                }else{M = total;}
                if(M == 0){stringToPass = HR + ":" + "00" +" " + amOrpm;}
                else{stringToPass = HR + ":" + M +" " + amOrpm;}

                myFirebaseRef.child("eta").setValue(stringToPass);
                myFirebaseRef.child("message").setValue("SafeRide has departed.");


                finish();
            }
        });
    }
}