package com.example.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RelativeLayout homeRL;
    ProgressBar progressBar;
    TextView cityNameTV,temperatureTV , conditionTV;
    TextInputEditText cityEdt;
    RecyclerView recyclerView;
    ImageView backIV, searchIcon , iconIV  ;
     ArrayList<WeatherModel> arrayList;
     WeatherAdapter adapter;
     LocationManager locationManager;
     int PermissionCode = 1;
     String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.temperature);
        conditionTV = findViewById(R.id.idTVCondition);
        backIV = findViewById(R.id.idIVBack);
        searchIcon = findViewById(R.id.searchIcon);
        iconIV = findViewById(R.id.idIVIcon);
        progressBar = findViewById(R.id.progress_bar);
        cityEdt = findViewById(R.id.idEdtCity);
        homeRL =  findViewById(R.id.idRLHome);
        recyclerView =  findViewById(R.id.recyclerView);
        arrayList = new ArrayList<WeatherModel>();
        adapter = new WeatherAdapter(this , arrayList);
        recyclerView.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        progressBar.setVisibility(View.GONE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION ,
                    Manifest.permission.ACCESS_COARSE_LOCATION} , PermissionCode);
        }
       Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            searchIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city = cityEdt.getText().toString();
                    if (city.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Enter city name..", Toast.LENGTH_SHORT).show();
                    } else {
                        cityNameTV.setText(city);
                        getWeatherInfo(city);
                    }
                }
            });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionCode){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this , "Permissions granted..." , Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this , "Please provide the permissions" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
//    private String getCityName(double longitude , double latitude){
//        String city_Name = "Not Found";
//        Geocoder gcd = new Geocoder(getBaseContext() , Locale.getDefault());
//        try{
//            List<Address> addresses = gcd.getFromLocation(latitude , longitude , 10);
//            for(Address address:addresses) {
//                if (address != null) {
//                    String city = address.getLocality();
//                    if (city != null && !city.equals("")) {
//                        city_Name = city;
//
//                    } else {
//                        Toast.makeText(this, "User City Not Found..", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        return city_Name;
//    }
    private void getWeatherInfo(String cityName){
        String url = "https://api.weatherapi.com/v1/forecast.json?key=68ea7c5dd8244ec5a88111310210311&q="+ cityName +"&days=1&aqi=yes&alerts=yes";
        cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                arrayList.clear();
                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                     temperatureTV.setText(temperature + "°C");
                     int isDay = response.getJSONObject("current").getInt("is_day");
                     String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                  //  String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    if(isDay == 1){
                        Picasso.get().load("https://www.google.com/imgres?imgurl=https%3A%2F%2Fwallpaperaccess.com%2Ffull%2F3265126.jpg&imgrefurl=https%3A%2F%2Fwallpaperaccess.com%2Fsunny-day&tbnid=szUnE3zyaQCA1M&vet=12ahUKEwjMm-WK64j0AhWwm0sFHaMRBx0QMygAegUIARDbAQ..i&docid=Oqpd-Fe0D5k-fM&w=2560&h=1600&q=day%20background%20image&ved=2ahUKEwjMm-WK64j0AhWwm0sFHaMRBx0QMygAegUIARDbAQ").into(backIV);
                    }else{
                        Picasso.get().load("https://www.google.com/imgres?imgurl=https%3A%2F%2Fimage.shutterstock.com%2Fimage-photo%2Fbackgrounds-night-sky-stars-moon-260nw-255423403.jpg&imgrefurl=https%3A%2F%2Fwww.shutterstock.com%2Fsearch%2Fnight%2Bbackground&tbnid=iADafWnjCJAyDM&vet=12ahUKEwjBh87L64j0AhVxzXMBHSk6ClkQMygBegUIARDPAQ..i&docid=wCeLTpUSLEDzOM&w=299&h=280&q=night%20background%20image&ved=2ahUKEwjBh87L64j0AhVxzXMBHSk6ClkQMygBegUIARDPAQ").into(backIV);
                    }
                    JSONObject forecastObject = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObject.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");

                    for(int i=0; i < hourArray.length() ; i++){
                        JSONObject hourObject = hourArray.getJSONObject(i);
                        String tim = hourObject.getString("time");
                        String temper = hourObject.getString("temp_c");
                        String img = hourObject.getJSONObject("condition").getString("icon");
                        String win = hourObject.getString("wind_kph");
                        arrayList.add(new WeatherModel(tim ,temper,img ,win));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this , "Please enter valid city name.." , Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}