package com.example.ssilance.saferide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DriverMainActivity extends AppCompatActivity {
    private static final int ADD_BY_ADDRESS = 1;
    private static final int ADD_BY_QR = 49374;
    private static final int ADDRESS_OR_QR = 2;

    private final int CAPACITY = 5;
    private ArrayList<Map<String,String>> riderData;
    private IntentIntegrator qrScan;
    private ListView listView;
    private Firebase myFirebaseRef;
    //private FloatingActionButton sendNotification;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);


        // Setup Firebase
        Firebase.setAndroidContext(this);
        // Replace the string below with your Firebase URL
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");

        riderData = new ArrayList<Map<String, String>>();
        String[] from = {"name", "address"};
        int[] to = {R.id.nameText, R.id.addressText};
        final SimpleAdapter adapter = new SimpleAdapter(this, riderData, R.layout.activity_listview, from, to);

        listView = (ListView) findViewById(R.id.mobile_list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(addressClickedHandler);

        Button addRiderBtn = new Button(this);
        addRiderBtn.setText("+");
        listView.addFooterView(addRiderBtn);

        addRiderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverMainActivity.this, AddByAddressOrQRActivity.class);
                startActivityForResult(intent,ADDRESS_OR_QR);

            }
        });

        /*
         *  Delete functionality
         *  single item
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                riderData.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(DriverMainActivity.this, "Item Deleted", Toast.LENGTH_LONG).show();
                countListView();
                return true;
            }
        });


        countListView();


        android.support.design.widget.FloatingActionButton help =  findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverMainActivity.this, DriverHelp.class);
                startActivity(intent);
            }
        });


//       FloatingActionButton deleteAll = (FloatingActionButton) findViewById(R.id.deleteAll);
//        deleteAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                riderData.clear();
//                adapter.notifyDataSetChanged();
//            }
//        });
//
//
//

//
//        FloatingActionButton addAddress = (FloatingActionButton) findViewById(R.id.addAddressBtn);
//        addAddress.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
//                startActivityForResult(intent, ADD_BY_ADDRESS);
//            }
//        });
//
//        FloatingActionButton qrAdd = (FloatingActionButton) findViewById(R.id.addQRBtn);
//        qrAdd.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                qrScan = new IntentIntegrator(DriverMainActivity.this);
//                qrScan.initiateScan();
////                Intent intent = new Intent(DriverMainActivity.this, AddRiderActivity.class);
////                startActivityForResult(intent, REQUEST_CODE);
//            }
//        });

        final View actionA = findViewById(R.id.action_a);
        final View actionB = findViewById(R.id.action_b);
        final View sendETA = findViewById(R.id.setTime);

        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });


        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Store the text in Firebase with the key "message"
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, 5);
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                String time = df.format(now.getTime());
                String message = time + "  Safe Ride will be back in 5 minutes.";
                myFirebaseRef.child("eta").setValue(time);
                myFirebaseRef.child("message").setValue(message);
            }
        });


        sendETA.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(DriverMainActivity.this, ETAActivity.class);
                startActivity(intent);
            }
        });
    }







    private AdapterView.OnItemClickListener addressClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            String clickedAddress = riderData.get((int)parent.getItemIdAtPosition(position)).get("address").toString();
            clickedAddress = clickedAddress.replace(' ','+');
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + clickedAddress));
            startActivity(intent);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDRESS_OR_QR){
            if (resultCode == 1) {
                        qrScan = new IntentIntegrator(DriverMainActivity.this);
                        qrScan.initiateScan();
                } else if (resultCode == 2){
                        startActivityForResult(new Intent(DriverMainActivity.this, AddRiderActivity.class), ADD_BY_ADDRESS);
            }
        }
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
                        countListView();
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
                        JSONObject json;
                        String nameString = "";
                        String addressString = "";
                        try{
                            json = new JSONObject(result.getContents());
                            nameString = json.get("name").toString();
                            addressString = json.get("address").toString();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        Map m = new HashMap();
                        m.put("name",nameString);
                        m.put("address",addressString);
                        riderData.add(m);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

    }
    private void countListView() {
        String message = "";
        int count = listView.getAdapter().getCount();
        if(count == CAPACITY)
        {
            message = "Full";
        }
        else{
            int currentSpotsLeft = CAPACITY - count;
            message = String.valueOf(currentSpotsLeft);

        }

        myFirebaseRef.child("capacity").setValue(message);
    }

}
