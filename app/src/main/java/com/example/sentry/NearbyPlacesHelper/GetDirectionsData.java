package com.example.sentry.NearbyPlacesHelper;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDirectionsData extends AsyncTask<Object,String,String> {


    String googleDirectionData;
    GoogleMap mMap;
    String url;
    String duration,distance;
    LatLng latLng;
    String TAG = "GetDirectionsData";

    @Override
    protected String doInBackground(Object... objects) {
        //object is the parameter that we pass here
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng=(LatLng) objects[2];
        //creating a new url
        download_url d_url = new download_url();
        try {
            //passing the url
            Log.v(TAG, url);
            googleDirectionData = d_url.readUrl(url);

        } catch (IOException e) {
            Log.v(TAG, url);
            e.printStackTrace();
        }
        Log.v(TAG, googleDirectionData);
        return googleDirectionData;
    }
    @Override
    protected void onPostExecute(String s) {
        //we need only one hash map here because only one place
        HashMap<String,String> directionsList=null;
        //made an object of data parser
        DataParser parser = new DataParser();
        //the destination is parsed to data parser
        //we parse the file to take out things we need

        Log.v(TAG, s);

        directionsList = parser.parseDirections(s);
        //we get the map in the direction list and fetching values of duration and distance from it
        duration=directionsList.get("duration");
        distance=directionsList.get("distance");
        mMap.clear();
        MarkerOptions moo = new MarkerOptions();
        moo.position(latLng);
        moo.draggable(true);
        moo.title(("Duration= "+duration));
        moo.snippet("Distance= "+distance);

        mMap.addMarker(moo);

        //pass the destination*/
        String[] directionsList_path;
        DataParser parser_path = new DataParser();
        directionsList_path = parser_path.parseDirections_path(s);
        displayDirection(directionsList_path);
    }
    public void displayDirection(String[] directionsList_path)
    {
        int count=directionsList_path.length;
        for(int i=0;i<count;i++)
        {
            PolylineOptions polylineOptions=new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(10);
            polylineOptions.addAll(decodePoly(directionsList_path[i]));

            mMap.addPolyline(polylineOptions);
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
