package com.example.ssilance.saferide;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;



import java.util.zip.Checksum;

public class MainActivity extends AppCompatActivity {

    private ImageView obj;
    private Button driverLoginBtn;
    private Button riderLoginBtn;
    LinearLayout mLinearLayout;
    private static final String TAG = "MyActivity";
    private Firebase myFirebaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        driverLoginBtn = (Button) findViewById(R.id.driverLoginBtn);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");
        myFirebaseRef.child("capacity").setValue("5");
        configureDriverLoginBtn();
        configureRiderLoginBtn();

    }



    private void configureDriverLoginBtn(){
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DriverMainActivity.class));
            }
        });
    }

   private void configureRiderLoginBtn(){
       riderLoginBtn = (Button) findViewById(R.id.riderLoginBtn);
        riderLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addressString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ADDRESS","");
                String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME","");

                if(addressString.equals("") || nameString.equals("")) {
                    startActivity(new Intent(MainActivity.this, ProfileSettingsActivity.class));
                }
                else
                    startActivity(new Intent(MainActivity.this, RiderMainActivity.class));
            }
        });
    }
}