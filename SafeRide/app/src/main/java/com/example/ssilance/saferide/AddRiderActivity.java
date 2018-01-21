package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sacha on 2018-01-20.
 */

public class AddRiderActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_rider);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.5));

        configureDoneBtn();
    }

    private void configureDoneBtn(){
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addressInput = (EditText) findViewById(R.id.addressInput);
                String addressString = addressInput.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ADDRESS_STRING", addressString);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
