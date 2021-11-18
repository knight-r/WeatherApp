package com.example.weatherapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
    private static final int REQUEST_LOCATION = 1;
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
     String latitude,longitude;

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

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


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
                    Log.i(TAG, "isDay: "+ isDay);
                    if(isDay == 1){
                        Picasso.get().load("https://www.google.com/imgres?imgurl=https%3A%2F%2Fwallpaperaccess.com%2Ffull%2F2777454.jpg&imgrefurl=https%3A%2F%2Fwallpaperaccess.com%2Fday&tbnid=P3iZyslWn4nxbM&vet=12ahUKEwi0tsjVwqH0AhW_oUsFHYrICxwQMyg3egQIARBc..i&docid=jfWmY13qJ4NQ_M&w=1920&h=1200&q=day%20background%20image&ved=2ahUKEwi0tsjVwqH0AhW_oUsFHYrICxwQMyg3egQIARBc").into(backIV);
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
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();

                String cityName = getCityName(longi , lat);
                cityNameTV.setText(cityName);
                getWeatherInfo(cityName);
            } else {
                Toast.makeText(this, "Unable to find your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
        private String getCityName(double longitude , double latitude){
        String city_Name = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext() , Locale.getDefault());
        try{
            List<Address> addresses = gcd.getFromLocation(latitude , longitude , 10);
            for(Address address:addresses) {
                if (address != null) {
                    String city = address.getLocality();
                    if (city != null && !city.equals("")) {
                        city_Name = city;

                    } else {
                        Toast.makeText(this, "User City Not Found..", Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return city_Name;
    }

}