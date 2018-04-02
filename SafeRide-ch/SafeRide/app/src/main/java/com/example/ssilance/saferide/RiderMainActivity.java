package com.example.ssilance.saferide;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sacha on 2018-01-27.
 */



public class RiderMainActivity extends AppCompatActivity {

    private TextView header;
    private TextView receive;
    private TextView etatext;
    private TextView capacity;
    private Firebase myFirebaseRef;
    public static final String MESSAGE = "Message";
    public static final int ADD_ADDRESS = 1;
    private SharedPreferences.Editor editor;
    private final int REQUEST_CODE = 1;
    private FloatingActionButton mInfo;
    private ArrayList<String> addressList;
    private DatabaseHandler db;
    private ListView listView;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main);

        String addressString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ADDRESS", "");
        String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME", "");

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("Welcome " + nameString);


        header = (TextView) findViewById(R.id.header2);
        receive = (TextView) findViewById(R.id.receive);
        etatext = (TextView) findViewById(R.id.ETA2);
        capacity = (TextView) findViewById(R.id.receive2);
        capacity.setText("5");

        // Setup Firebase
        Firebase.setAndroidContext(this);
        // Replace the string below with your Firebase URL
        myFirebaseRef = new Firebase("https://tryfire-71c5c.firebaseio.com/");


        //SharedPreferences to save the Firebase message
        SharedPreferences prefs = getSharedPreferences(MESSAGE, MODE_PRIVATE);
        String restoredText = prefs.getString("message", null);
        String restoredText2 = prefs.getString("eta", null);
        String restoredText3 = prefs.getString("capacity", null);
        if(restoredText2 != null)
        {
            String message = prefs.getString("eta", null);
            etatext.setText(message);
        }
        if (restoredText != null) {
            String message = prefs.getString("message", null);
            receive.setText(message);
        }
        if (restoredText3 != null) {
            String message = prefs.getString("capacity", null);
            capacity.setText(message);
        }
        mInfo = (FloatingActionButton) findViewById(R.id.info);
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(RiderMainActivity.this, InfoActivity.class);
                startActivity(intent2);
           }
        });

        listen();

        addressList = new ArrayList<>();
        addressList.add(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("ADDRESS", ""));
        db = new DatabaseHandler(this);
        addressList.addAll(db.getRiderAddressesList());

        adapter = new ArrayAdapter<String>(this,
                               R.layout.rider_listview,R.id.text_view_id, addressList);

        listView = (ListView) findViewById(R.id.addressList);

        listView.setAdapter(adapter);
        Button addAddress = new Button(this);
        addAddress.setText("+");
        addAddress.setBackgroundColor(getResources().getColor(R.color.grey));
        addAddress.setTextColor(getResources().getColor(R.color.colorAccent));
        addAddress.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
        listView.addFooterView(addAddress);
        listView.setOnItemClickListener(addressClickedHandler);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderMainActivity.this, AddNewAddressActivity.class);
                startActivityForResult(intent,ADD_ADDRESS);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {
                    db.deleteRiderAddressItem(adapter.getItem(position).toString());
                    addressList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(RiderMainActivity.this, "Item Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RiderMainActivity.this, "You can't delete your main address!", Toast.LENGTH_LONG).show();

                }
                return true;

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                String address = data.getStringExtra("ADDRESS_STRING");
                adapter.add(address);
                db.addRiderAddress(address);
            }
        }
    }
    private AdapterView.OnItemClickListener addressClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            Intent intent2 = new Intent(RiderMainActivity.this, GenerateQRCodeActivity.class);
            intent2.putExtra("ADDRESS", addressList.get(position));
            startActivity(intent2);;
        }
    };

    public void listen()
    {
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                receive.setText(text);
                NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(text);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("message", text);
                editor.apply();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });



        myFirebaseRef.child("eta").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Object v = snapshot.getValue();
                String text;
                if (v == null) {text = "";}
                else {text = v.toString();}

                etatext.setText(text);
                NotificationCompat.Builder mBuilder = mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(text);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, mBuilder.build());
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("eta", text);
                editor.apply();


            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });


        myFirebaseRef.child("capacity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String currentTime = DateFormat.getTimeInstance().format(new Date());
                Object v = snapshot.getValue();
                String text;

                if (v == null) {text = "";}
                else {text = v.toString();}
                capacity.setText(text);
                editor = getSharedPreferences(MESSAGE, MODE_PRIVATE).edit();
                editor.putString("capacity", text);
                editor.apply();
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.i("hello", "onCancelled");
            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
        ActionBar mActionBar = getSupportActionBar();
        String nameString = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("NAME", "");
        mActionBar.setTitle("Welcome " + nameString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.editNameItem) {
            startActivityForResult(new Intent(RiderMainActivity.this, EditNameActivity.class), REQUEST_CODE);
            return true;
        }
        else if (item.getItemId() == R.id.riderHelpItem) {
            startActivityForResult(new Intent(RiderMainActivity.this, RiderHelpActivity.class), REQUEST_CODE);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }

    }
}
