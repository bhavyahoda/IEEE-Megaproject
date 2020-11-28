package com.example.idk_now;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDirectionsData extends AsyncTask<Object,String,String> {


    String googleDirectionData;
    GoogleMap mMap;
    String url;
    String duration,distance;
    LatLng latLng;

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
            String rra = "wtfff";
            Log.d("mansiii","i am here"+rra);
            googleDirectionData = d_url.readUrl(url);

        } catch (IOException e) {
            System.out.println("Their was no url provided");
            String rra = "wtfff";
            Log.d("mansiii","i am here"+rra);
            e.printStackTrace();
        }
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
        directionsList = parser.parseDirections(s);
        //we get the map in the direction list and fetching values of duration and distance from it
        duration=directionsList.get("duration");
        distance=directionsList.get("distance");
        mMap.clear();
        MarkerOptions moo = new MarkerOptions();
        moo.position(latLng);
        moo.draggable(true);
        moo.title(("duration="+duration));
        moo.snippet("distance="+distance);

        mMap.addMarker(moo);

        //pass the destination
    }

}
