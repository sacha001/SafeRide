package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sacha on 2018-01-27.
 */

public class EditNameActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.4));

        configureDoneBtn();
    }

    private void configureDoneBtn(){

        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = (EditText) findViewById(R.id.nameInput);
                EditText addressInput = findViewById(R.id.addressInput);


                String nameString = nameInput.getText().toString();
                String addString = addressInput.getText().toString();

                if(addString.equals("")){
                    addressInput.setHintTextColor(Color.RED);
                    addressInput.setHint("We need a main address!");
                }
                if(nameString.equals("")) {
                    nameInput.setHintTextColor(Color.RED);
                    nameInput.setHint("Please enter a name!");
                }
                if (!nameString.equals("") && !addString.equals("")) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("NAME", nameString).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("ADDRESS", addString).apply();
                    finish();
                }

            }
        });

    }


}