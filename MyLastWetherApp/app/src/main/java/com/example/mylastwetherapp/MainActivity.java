package com.example.mylastwetherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//main is the starting point of an app’s execution
public class MainActivity extends AppCompatActivity implements  LocationListener {
    TextView view_city;
    TextView view_temp;
    TextView view_desc;
    private LocationManager locationManager;
    private Location lastknownLocation;
    private double longitude;
    private double latitude;
    TextView LatitudeTextview,LongitudeTextview;



    ImageView view_weather;
    EditText search;
    FloatingActionButton search_floating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Perform initialization of all fragments

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LatitudeTextview = (TextView)findViewById(R.id.Latitudetextview);
        LongitudeTextview = (TextView)findViewById(R.id.LongitudeTextview);

        if (savedInstanceState != null) {
            latitude = savedInstanceState.getDouble("lat");
            longitude = savedInstanceState.getDouble("long");
            Log.d("sj","hoise");

            LatitudeTextview.setText("Lat" + latitude);
            LongitudeTextview.setText(("Long" + longitude));

        }

        view_city=findViewById(R.id.town);
        view_city.setText("");
        view_temp=findViewById(R.id.temp);
        view_temp.setText("");
        view_desc=findViewById(R.id.desc);
        view_desc.setText("");

        view_weather=findViewById(R.id.weather_image);
        search=findViewById(R.id.search_edit);
        search_floating=findViewById(R.id.floating_search);

        search_floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
                api_key(String.valueOf(search.getText()));
            }
        });


    }



    public void startGPS(View v) {
        // first place to put permission for api is manifest, 2nd on runtime
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]    {Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    0);

            return;
        }
        //when given permission we can access the user's location
        lastknownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);


    }

    private void api_key(final String City) {
        OkHttpClient client =new OkHttpClient();
       //calling an api request

        Request request = new Request.Builder()
                // this is your url, where you want to send a request and get a response
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=0962947df96b3b645c5f3ed191b416d3&units=metric")
                .get()
                .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    //volly response

                    String responseData = response.body().string();
                    try {

                        JSONObject json = new JSONObject(responseData);  //this is your JSON data which will be used when you send the post request
                        JSONArray array = json.getJSONArray("weather");
                        JSONObject object = array.getJSONObject(0);

                        String description =object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject tem1 = json.getJSONObject("main");
                        Double Temperature = tem1.getDouble("temp");

                        setText(view_city,City);

                        String temps = Math.round(Temperature)+ " °C";
                        setText(view_temp,temps);
                        setText(view_desc,description);
                        setImage(view_weather,icons);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();

        }


    }
    private void setText(final TextView text, final String value){
        //Sets the text displayed in the output box
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setImage(final ImageView imageView, final String value){
        // sets the weather image
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //switch to show weather images according to the weather aka the value
                switch (value){
                    case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "01n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d01d));
                        break;
                    case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "02n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d02d));
                        break;
                    case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "03n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d03d));
                        break;
                    case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "04n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d04d));
                        break;
                    case "06d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d06d));
                        break;
                    case "06n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d06d));
                        break;
                    case "10d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "10n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d10d));
                        break;
                    case "11d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "11n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d11d));
                        break;
                    case "13d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));
                        break;
                    case "13n":imageView.setImageDrawable(getResources().getDrawable(R.drawable.d13d));


                }



            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        //location functionality
        lastknownLocation= location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();



        LatitudeTextview.setText("Lat" + latitude);
        LongitudeTextview.setText(("Long" + longitude));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
     //Save all appropriate fragment state

        super.onSaveInstanceState(outState);
        outState.putDouble("lat",latitude);
        outState.putDouble("long",longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
