package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private ProgressBar progressBar;
    private TextView cityName,temperature , condition;
    private TextInputEditText cityEditText;
    private RecyclerView recyclerView;
    private ImageView back_imageView, searchIcon , iconImageView  ;
    private ArrayList<WeatherModel> arrayList;
    private WeatherAdapter adapter;
    private LocationManager locationManager;
    private int PermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        cityName = (TextView)findViewById(R.id.idEdtCity);
        temperature = (TextView)findViewById(R.id.temperature);
        condition = (TextView)findViewById(R.id.condition);
        back_imageView = (ImageView)findViewById(R.id.background_image);
        searchIcon = (ImageView)findViewById(R.id.searchIcon);
        iconImageView = (ImageView)findViewById(R.id.icon);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        cityEditText = (TextInputEditText)findViewById(R.id.idTILCity);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrayList = new ArrayList<WeatherModel>();
        adapter = new WeatherAdapter(this , arrayList);
        recyclerView.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,
                    Manifest.permission.ACCESS_COARSE_LOCATION} , PermissionCode);
        }
       Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


    }
    private String getCityName(double longitude , double latitude){
        String cityName = "Not Found";
//        Geocoder gcd = new GeoCoder(getBaseContext() , Locale.getDefault());
//        try{
//            List<Address> addresses = gcd.getFromLocation(latitude , longitude , 10);
//            for(Address address:addresses){
//                if(address != null){
//                    String city = address.getLocality();
//                    if(city != null && !city.equals("")){
//                        cityName = city;
//
//                    }else {
//                        Toast.makeText(this , "User City Not Found.." , Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
        return cityName;
    }
    private void getWeatherInfo(String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=68ea7c5dd8244ec5a88111310210311&q="+ cityName + "&days=1&aqi=yes&alerts=yes\n";
    }
}