package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Intent;
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
    private IntentIntegrator qrScan;
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
        configureScanQRBtn();
    }

    private void configureDoneBtn() {
        Button driverLoginBtn = (Button) findViewById(R.id.doneBtn);
        driverLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addressInput = (EditText) findViewById(R.id.addressInput);
                addressString = addressInput.getText().toString();
                resultIntent = new Intent();
                resultIntent.putExtra("ADDRESS_STRING", addressString);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void configureScanQRBtn() {
        Button scanQRBtn = (Button) findViewById(R.id.scanQRBtn);
        qrScan = new IntentIntegrator(this);
        scanQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrScan.initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                addressString = result.getContents();
                resultIntent = new Intent();
                resultIntent.putExtra("ADDRESS_STRING", addressString);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

