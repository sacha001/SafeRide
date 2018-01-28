package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

/**
 * Created by Sacha on 2018-01-27.
 */

public class ProfileSettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        configureDoneBtn();
    }

    private void configureDoneBtn(){
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addressInput = (EditText) findViewById(R.id.addressInput);
                String addressString = addressInput.getText().toString();
                String jsonString = "";
                try{
                    jsonString = new JSONObject().put("Address", addressString).toString();
                } catch (JSONException e){
                    Toast.makeText(getApplicationContext(), "JSON Exception",
                            Toast.LENGTH_SHORT).show();
                }

                try{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString("PROFILE",jsonString.toString()).apply();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), "IO Exception",
                            Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }
}
