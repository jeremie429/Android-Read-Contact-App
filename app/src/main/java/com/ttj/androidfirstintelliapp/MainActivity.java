package com.ttj.androidfirstintelliapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_PERMISSION = 520;
    public static final int REQUEST_READ_CONTACTS = 2;
    TextView txtDisplay;
    EditText edtUrl;
    Button btnFind, btnGo, btnGBack, btnForward;
    WebView webView;
    ArrayList<Contact> contacts;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDisplay = findViewById(R.id.txtDisplay);
        btnFind = findViewById(R.id.btnFind);
        btnGo = findViewById(R.id.btnGo);
        btnGBack = findViewById(R.id.btnGBack);
        btnForward = findViewById(R.id.btnForward);
        edtUrl = findViewById(R.id.edtUrl);

        listView = findViewById(R.id.list_item);


       // webView = findViewById(R.id.webview);
       // webView.getSettings().setJavaScriptEnabled(true);

        contacts = new ArrayList<>();
/*
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
*/

        getContacts();
        btnGo.setOnClickListener(view -> {

            loadURL(edtUrl.getText().toString());

        });

        btnForward.setOnClickListener(view -> {

           // webView.goForward();
        });

        btnGBack.setOnClickListener(view -> {

            webView.goBack();

        });

        btnFind.setOnClickListener(view -> {


         getCoord();
        });

    }

    public void loadURL(String url){
            webView.loadUrl(url);
    }

    public void getContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            }
        }else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

                while (cursor.moveToNext()){
                    @SuppressLint("Range")

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    @SuppressLint("Range")
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    Contact contact = new Contact(name, phoneNumber);
                    contacts.add(contact);

                }

                MyListAdapter adapter = new MyListAdapter(this,contacts);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void getCoord(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
            }

            return;
        }
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 500, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double lat = location.getLatitude();
                double longitude = location.getLongitude();

                txtDisplay.setText("Latitude: " + lat + ", Longitude: " + longitude);
            }
        });


        /*double lat = location.getLatitude();
        double longitude = location.getLongitude();

        txtDisplay.setText("Latitude: " + lat + ", Longitude: " + longitude);*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

       switch (requestCode){

           case REQUEST_CODE_PERMISSION:
               if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                   getCoord();
               }
               break;
           case REQUEST_READ_CONTACTS:
               if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   getContacts();
               }
           default:
               super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       }

    }
}