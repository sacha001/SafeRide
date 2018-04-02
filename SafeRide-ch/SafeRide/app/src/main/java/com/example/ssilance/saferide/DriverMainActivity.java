package com.example.ssilance.saferide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.TypedValue;
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
    private final int REQUEST_CODE = 1;
    private DatabaseHandler db;
    private SimpleAdapter adapter;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        final View actionA = findViewById(R.id.action_a);
        final View actionB = findViewById(R.id.action_b);
        final View sendETA = findViewById(R.id.setTime);
        final View routeAll = findViewById(R.id.routeAll);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        db = new DatabaseHandler(this);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");

        riderData = db.getList();
        String[] from = {"name", "address"};
        int[] to = {R.id.nameText, R.id.addressText};
        adapter  = new SimpleAdapter(this, riderData, R.layout.activity_listview, from, to);

        listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(addressClickedHandler);

        Button addRiderBtn = new Button(this);
        addRiderBtn.setText("+");
        addRiderBtn.setBackgroundColor(getResources().getColor(R.color.grey));
        addRiderBtn.setTextColor(getResources().getColor(R.color.colorAccent));
        addRiderBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
        listView.addFooterView(addRiderBtn);

        addRiderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverMainActivity.this, AddByAddressOrQRActivity.class);
                startActivityForResult(intent,ADDRESS_OR_QR);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteItem(riderData.get(position));
                riderData.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(DriverMainActivity.this, "Item Deleted", Toast.LENGTH_LONG).show();
                countListView();
                return true;
            }
        });

        countListView();


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

        routeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(riderData.size() > 1) {
                    String waypoints = riderData.get(0).get("address");
                    int i;
                    for(i = 1; i < riderData.size() - 1; i++){
                        waypoints += "|" + riderData.get(i).get("address");
                    }
                    String destination = riderData.get(i).get("address");
                    String url = "https://www.google.com/maps/dir/?api=1&destination=" + destination + "&waypoints=" + waypoints;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else if(riderData.size() > 0){
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + riderData.get(0).get("address")));
                    startActivity(intent);
                }
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
                    String addressString = data.getStringExtra("ADDRESS_STRING");

                    if(!addressString.equals(null) && !addressString.equals("")){
                        Map m = new HashMap();
                        m.put("name",nameString);
                        m.put("address",addressString);
                        riderData.add(m);
                        adapter.notifyDataSetChanged();
                        db.addListItem(m);
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
                        adapter.notifyDataSetChanged();
                        db.addListItem(m);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }

    }
    private void countListView() {
        String message = "";
        int count = riderData.size();
        Log.i("count", String.valueOf(count));
        if(count == CAPACITY) {
            message = "Full";
        }
        else{
            int currentSpotsLeft = CAPACITY - count;
            message = String.valueOf(currentSpotsLeft);
        }

        myFirebaseRef.child("capacity").setValue(message);
    }

    @Override
    public void onPause(){
        super.onPause();
        countListView();
    }
    @Override
    public void onResume(){
        super.onResume();
        countListView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.driverInfoItem) {
            startActivityForResult(new Intent(DriverMainActivity.this, DriverHelp.class), REQUEST_CODE);
            return true;
        }

        if (item.getItemId() == R.id.resetItem) {

            if(riderData.size() == 0)
            {
                Toast.makeText(DriverMainActivity.this, "Nothing to delete", Toast.LENGTH_LONG).show();
            }else{
                for (int i=0;i<riderData.size();i++){
                    db.deleteItem(riderData.get(i));
                }

                riderData.clear();
                adapter.notifyDataSetChanged();
                Calendar now = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                String actualtime = df.format(now.getTime());
                myFirebaseRef.child("eta").setValue("TBD");
                myFirebaseRef.child("message").setValue(actualtime + " Safe Ride is loading!");
                countListView();
                Toast.makeText(DriverMainActivity.this, "Reset & Loading message sent", Toast.LENGTH_LONG).show();
            }
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }

}
