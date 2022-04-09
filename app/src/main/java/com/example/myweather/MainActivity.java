package com.example.myweather;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    EditText city;
    TextView description;
    TextView temp;
    TextView pressure;
    TextView humidity;
    TextView speed;
    TextView cityAndRegion;
    TextView dateAndTime;
    TextView feelsLike;

    private final TextWatcher cityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (city.getText().length() != 0) {
                UpdateWeather(city.getText().toString().trim());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city=findViewById(R.id.cityText);

        cityAndRegion=findViewById(R.id.nameCityAndRegionText);
        dateAndTime=findViewById(R.id.dateAndTimeText);
        temp=findViewById(R.id.tempText);
        description=findViewById(R.id.descriptionText);
        feelsLike=findViewById(R.id.feelsLikeText);
        humidity=findViewById(R.id.humidityText);
        pressure=findViewById(R.id.pressureText);
        speed=findViewById(R.id.speedText);

        city.addTextChangedListener(cityWatcher);

        UpdateWeather("Omsk");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void UpdateWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=b1b35bba8b434a28a0be2a3e1071ae5b&units=metric&lang=ru";
        @SuppressLint("SetTextI18n") JsonObjectRequest jo = new JsonObjectRequest(Request.Method.GET, url, null, obj -> {
            try {
                JSONObject main = obj.getJSONObject("main");
                JSONObject wind = obj.getJSONObject("wind");
                JSONObject coord = obj.getJSONObject("coord");
                JSONObject sys = obj.getJSONObject("sys");
                JSONArray weather = obj.getJSONArray("weather");
                JSONObject desc = weather.getJSONObject(0);

                cityAndRegion.setText(obj.getString("name") + ", " + sys.getString("country"));

                temp.setText(Math.round(Float.parseFloat(main.getString("temp"))) + "°C");
                feelsLike.setText(Math.round(Float.parseFloat(main.getString("feels_like"))) + "°C");
                humidity.setText("Видимость\n" + main.getString("humidity") + "%");
                pressure.setText("Давление\n" + main.getString("pressure") + " мм");
                speed.setText("Скорость\n" + wind.getString("speed") + " м/с");
                description.setText(desc.getString("description"));

                updateMap(
                        new LatLng(coord.getDouble("lat"), coord.getDouble("lon")),
                        obj.getString("name") + ", " + sys.getString("country")
                );

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate date = LocalDate.now();
                    Locale localeRu = new Locale("ru", "RU");
                    dateAndTime.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL,localeRu).substring(0, 1).toUpperCase() + date.getDayOfWeek().getDisplayName(TextStyle.FULL,localeRu).substring(1) + ", " + date.format(DateTimeFormatter.ofPattern("dd")) + " " + date.getMonth().getDisplayName(TextStyle.FULL,localeRu).substring(0, 1).toUpperCase() + date.getMonth().getDisplayName(TextStyle.FULL,localeRu).substring(1) + ", " + date.getYear());
                }

            } catch (JSONException e) {temp.setText(e.getMessage()); } }, error -> { });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jo);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

    }

    private void updateMap(LatLng latLng, String cityName) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        mMap.clear();

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(cityName)
                .snippet("Ваша реклама");
        mMap.addMarker(options).showInfoWindow();
    }
}