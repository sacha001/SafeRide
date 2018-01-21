package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class DriverMainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.addRiderItem){
            Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    String addressString = data.getStringExtra("ADDRESS_STRING");
                    if(!addressString.equals(null) && !addressString.equals("")){
                        addNewAddress(addressString);
                    }
                }
                break;
            }
        }
    }
    private void addNewAddress(final String addressString) {
        Button myButton = new Button(this);
        myButton.setText(addressString);

        LinearLayout ll = (LinearLayout) findViewById(R.id.driverLinearLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formattedAddress = addressString.replace(' ','+');
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + formattedAddress));
                startActivity(intent);
            }
        });
    }
}
