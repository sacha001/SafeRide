package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by sacha on 2018-01-20.
 */

public class AddRiderActivity extends Activity {
   private String nameString;
    private String addressString;
    private Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_rider);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int height = dm.heightPixels;
        int width = dm.widthPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.5));

        configureDoneBtn();
    }

    private void configureDoneBtn() {
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = (EditText) findViewById(R.id.nameInput);
                EditText addressInput = (EditText) findViewById(R.id.addressInput);
                nameString = nameInput.getText().toString();
                addressString = addressInput.getText().toString();

                if(addressString.equals("")) {
                    addressInput.setHint("We need your address.");
                    addressInput.setHintTextColor(Color.RED);
                }


                resultIntent = new Intent();

               // if(!nameString.equals("")){
                    resultIntent.putExtra("NAME_STRING", nameString);
               // }
               // if(!addressString.equals("")){
                    resultIntent.putExtra("ADDRESS_STRING", addressString);
              //  }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

}

