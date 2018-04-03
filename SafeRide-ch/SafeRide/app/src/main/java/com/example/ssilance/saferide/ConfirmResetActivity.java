package com.example.ssilance.saferide;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;


/**
 * Created by Sacha on 2018-01-27.
 */

public class ConfirmResetActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_reset);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.2));

        Button cancelBtn = (Button) findViewById(R.id.cancelReset);
        Button yesBtn = (Button) findViewById(R.id.yesReset);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(0);
                finish();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1);
                finish();
            }
        });


    }

}