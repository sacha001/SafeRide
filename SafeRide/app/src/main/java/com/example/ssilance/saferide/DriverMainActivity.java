package com.example.ssilance.saferide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverMainActivity extends AppCompatActivity {
    private static final int ADD_BY_ADDRESS = 1;
    private static final int ADD_BY_QR = 49374;

    private ArrayList<Map<String,String>> riderData;
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        riderData = new ArrayList<Map<String,String>>();
        String[] from = { "name", "address" };
        int[] to = { R.id.nameText, R.id.addressText };
        SimpleAdapter adapter = new SimpleAdapter(this, riderData, R.layout.activity_listview, from, to);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(addressClickedHandler);

        FloatingActionButton addAddress = (FloatingActionButton) findViewById(R.id.addAddressBtn);
        addAddress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
                startActivityForResult(intent, ADD_BY_ADDRESS);
            }
        });

        FloatingActionButton qrAdd = (FloatingActionButton) findViewById(R.id.addQRBtn);
        qrAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                qrScan = new IntentIntegrator(DriverMainActivity.this);
                qrScan.initiateScan();
//                Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    private AdapterView.OnItemClickListener addressClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            String clickedAddress = riderData.get((int)parent.getItemIdAtPosition(position)).get("address").toString();

            clickedAddress = clickedAddress.replace(' ','+');
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + clickedAddress));
            startActivity(intent);

        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_driver, menu);
        return true;
    }

    /** @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    if(item.getItemId() == R.id.addRiderItem){
    Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
    startActivityForResult(intent, REQUEST_CODE);
    return true;
    } else {
    return super.onOptionsItemSelected(item);
    }
    } */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ADD_BY_ADDRESS) : {
                if (resultCode == Activity.RESULT_OK) {

                    String nameString = data.getStringExtra("NAME_STRING");
                    String addressString = data.getStringExtra("ADDRESS_STRING"); //TODO sometimes pauses here..

                    if(!addressString.equals(null) && !addressString.equals("")){
                        Map m = new HashMap();
                        m.put("name",nameString);
                        m.put("address",addressString);
                        riderData.add(m);
                    }
                }

                break;
            }
            case (ADD_BY_QR) : {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                    } else {
                        //addresses.add(result.getContents());
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

    }

}
