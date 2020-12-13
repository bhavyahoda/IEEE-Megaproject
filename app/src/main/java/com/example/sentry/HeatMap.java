package com.example.sentry;


import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sentry.directionhelpers.FetchURL;
import com.example.sentry.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HeatMap extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    Button getDirection,swapper;
    private Polyline currentPolyline;
    TileOverlay overlay;
    int flag=0;
    EditText where_place,to_place;
    LatLng latLng,latLng2;
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
        where_place =findViewById(R.id.From);
        to_place=findViewById(R.id.To);
        Places.initialize(getApplicationContext(), getResources().getString(R.string.map_key));
        where_place.setFocusable(false);
        where_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize places field list
                //zet the field to specify which type of place data to return after the user has made decision
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                //create autocomplete intent

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(HeatMap.this);
                //start activity result
                startActivityForResult(intent, 100);

            }
        });
        to_place.setFocusable(false);
        to_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize places field list
                //zet the field to specify which type of place data to return after the user has made decision
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                //create autocomplete intent

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
                        , fieldList).build(HeatMap.this);
                //start activity result
                startActivityForResult(intent, 101);

            }
        });

        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = where_place.getText().toString();
                String location2 = to_place.getText().toString();
                //storing the results in a list
                List<Address> where_places = null;
                List<Address> to_places = null;
                MarkerOptions mo = new MarkerOptions();
                //check if the user entered something
                if (!location.equals("")) { //this means that user has entered something
                    //now we use the geocoder class to get the latitude nad longitude of the entered text
                    Geocoder geocoder = new Geocoder(HeatMap.this);
                    try {
                        where_places = geocoder.getFromLocationName(location, 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //putting a marker in the location
                    for (int i = 0; i < where_places.size(); i++) {
                        Address add = where_places.get(i);
                        latLng = new LatLng(add.getLatitude(), add.getLongitude());
                        mo.position(latLng);
                        mo.title("You are here " + location);
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        mMap.addMarker(mo);
                        //showing camera  focus on last result
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
                if (!location2.equals("")) { //this means that user has entered something
                    //now we use the geo-coder class to get the latitude nad longitude of the entered text
                    Geocoder geocoder = new Geocoder(HeatMap.this);
                    try {
                        to_places = geocoder.getFromLocationName(location2, 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //putting a marker in the location
                    for (int i = 0; i < to_places.size(); i++) {
                        Address add = to_places.get(i);
                        latLng2 = new LatLng(add.getLatitude(), add.getLongitude());
                        mo.position(latLng2);
                        mo.title("You want to go here " + location2);
                        mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                        mMap.addMarker(mo);
                        //showing camera  focus on last result
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));
                    }
                }
                new FetchURL(HeatMap.this).execute(getUrl(latLng,latLng2, "driving"), "driving");
            }
        });
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng NYC = new LatLng(40.7128, -74.0060);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NYC, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(NYC)      // Sets the center of the map to Mountain View
                .zoom(11)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            //when success
            //we initialize that  place as our final place on the text editor
            Place place = Autocomplete.getPlaceFromIntent(data);
            where_place.setText(place.getAddress());
            //the address the user select gets set on the edit text
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //when error we handle the error
            Status status = Autocomplete.getStatusFromIntent(data);
            //display toast here the message get printed on the ui
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
        } else if (resultCode == RESULT_CANCELED) {
            //THE USER CANCELLED THE REQUEST
        }
        if (requestCode == 101 && resultCode == RESULT_OK) {
            //when success
            //we initialize that  place as our final place on the text editor
            Place place = Autocomplete.getPlaceFromIntent(data);
            to_place.setText(place.getAddress());
            //the address the user select gets set on the edit text
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            //when error we handle the error
            Status status = Autocomplete.getStatusFromIntent(data);
            //display toast here the message get printed on the ui
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
        } else if (resultCode == RESULT_CANCELED) {
            //THE USER CANCELLED THE REQUEST
        }
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
        Toast.makeText(getApplicationContext(),"The Heat map has loaded",Toast.LENGTH_LONG).show();
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
