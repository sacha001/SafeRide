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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private ArrayList<String> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        addresses = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
               R.layout.activity_listview,R.id.text_view_id, addresses);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(addressClickedHandler);

    }

    private AdapterView.OnItemClickListener addressClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            String clickedAddress = addresses.get((int)parent.getItemIdAtPosition(position)).toString();
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
                        addresses.add(addressString);
                    }
                }
                break;
            }
        }
    }

//    private void routeAll(){
//        String formattedAddress = addressString.replace(' ','+');
//        String q = "10 gulliver st miramichi|9 dineen drive";
//        String url = "";
//        try{
//            url = "&waypoints=" + URLEncoder.encode(q, "UTF-8");
//        } catch(Exception e){
//            System.out.print(e);
//        }
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=725+george"+url));
//        startActivity(intent);
//    }
}
