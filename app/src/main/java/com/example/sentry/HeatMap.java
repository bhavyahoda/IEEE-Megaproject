package com.example.sentry;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sentry.directionhelpers.FetchURL;
import com.example.sentry.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HeatMap extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    Button getDirection,swapper;
    private Polyline currentPolyline;
    TileOverlay overlay;
    int flag=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        getDirection = findViewById(R.id.btnGetDirection);
        swapper=findViewById(R.id.Swapper);
        flag =0;
        swapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    flag = 1;
                    addHeatMap();
                }
                else{
                    flag=0;
                    overlay.remove();
                }
            }
        });
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(HeatMap.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });
        place1 = new MarkerOptions().position(new LatLng(40.707397, -73.933278)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(40.732751, -73.955001)).title("Location 2");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(place1);
        mMap.addMarker(place2);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        String alternate = "alternatives=true";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&" + alternate;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=YOUR API KEY HERE";
        return url;
    }

    private void addHeatMap() {
        List<WeightedLatLng> latLngs = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            latLngs = readItems(R.raw.final_dataset_nyc);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }
        int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.001f, 0.01f
        };

        Gradient gradient = new Gradient(colors, startPoints);

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                .weightedData(latLngs)
                .gradient(gradient)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        overlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(provider));
    }

    private List<WeightedLatLng> readItems(@RawRes int resource) throws JSONException {
        List<WeightedLatLng> result = new ArrayList<>();
        InputStream inputStream = getApplicationContext().getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("Latitude");
            double lng = object.getDouble("Longitude");
            double offence_description = object.getDouble("Offence Description");
            result.add(new WeightedLatLng(new LatLng(lat, lng),offence_description));
        }
        return result;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
