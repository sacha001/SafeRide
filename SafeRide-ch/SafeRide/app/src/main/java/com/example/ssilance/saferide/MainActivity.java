package com.example.ssilance.saferide;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

    ImageView obj;

    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        obj = (ImageView)findViewById(R.id.imageView2);
        ObjectAnimator animation = ObjectAnimator.ofFloat(obj, "translationX", 1400f);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setRepeatMode(ObjectAnimator.RESTART);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(1100);
        animation.start();

        configureDriverLoginBtn();
        configureRiderLoginBtn();

    }






    private void configureDriverLoginBtn(){
        Button driverLoginBtn = (Button) findViewById(R.id.driverLoginBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DriverMainActivity.class));
            }
        });
    }

    private void configureRiderLoginBtn(){
        Button riderLoginBtn = (Button) findViewById(R.id.riderLoginBtn);
        riderLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RiderMainActivity.class));
            }
        });
    }
}