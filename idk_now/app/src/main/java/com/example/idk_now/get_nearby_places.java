package com.example.idk_now;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
//basically async task does things in the background and prepares the app to do that in the ui and connect this sub class to the main adtivity
public class get_nearby_places  extends AsyncTask<Object,String,String> {


    String google_places_data;
    GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        //object is the parameter that we pass here
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        //creating a new url
        download_url d_url = new download_url();
        try {
            //passing the url
            google_places_data = d_url.readUrl(url);
            String bhavya = "bhavya";
            Log.d("bhai", "yeh dekh" + bhavya);

        } catch (IOException e) {
            System.out.println("Their was no url provided");
            e.printStackTrace();
        }
        return google_places_data;
    }

    @Override
    protected void onPostExecute(String s) {
        //list bulaya
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();
        //the list is then parsed with the help of the parser
        nearbyPlaceList = parser.parse(s);
        // it passes the list
        Log.d("bhai", "yeh dekh" + nearbyPlaceList.size());
        showNearbyPlaces(nearbyPlaceList);
    }

    //it is going to take a lit of hash maps
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList) {
        //here nearbyPlaceList.size will store the number of elements that is returned to us through the jason array it
        // will have the data of each place in the form pf hash maps
        int l;
        l = nearbyPlaceList.size();
        for (int i = 0; i < l; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            //here to each hash map there will be a marker added
            //here one by one the ith element is fetched and stored in the hash map like for a place all the details are store in this kash map
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            //fetching the place name and the vicinity and all other details
            //for lat lang the googleplace.get("lat") returns the value in string we want it in double format hence used that
            //
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            Log.d("gadha", "yeh dekh" + lat);
            LatLng latLng = new LatLng(lat, lng);
           // mMap.setOnMarkerClickListener(this);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }
}
//used for multi threading
// we have ui thread when user using a app you click a button for some specific task what we an do we do with the help of ui thread
// it shows you next activity on clicking a new button and for all this to actually happen we need data data is not stored in the app it is stored in the server
// request for them it will take some time to get response by that time my ui thread is waiting for the response and i can do nothing on the app
// therefore instead of ui thread creating the request we can create a new thread
//which will be busy with the threads and the ui thread ca be free we can scroll down and down up '
//to achieve this thread we use async task :here we create a inner class and extend it with the help of async task
//we have four methods and three parameters to work with the type o parameter
//1.params::parameter the url and stuff we pass
//2. the progress:updates our progress
//3.results in tenno ka data type likhata hain <> iske anadar
//not passing any parameters then we write void void void
//1.method::on pre execute-::
//2 method::do in background-::whenever we work in this method we have tp pass parameters .when i send a request i am still on my ui thread what should happen on the background
//3 method ::on progress update -:: here we have to pass the updates i have send the request and i am downloading files i have to show the user ki kuch ho raha hain background mien kuch and i have to shoe=w the user that their is some progress like 25%done 50%done aise karkien
//it is done here
//4 methods:: on post execute-::here we have to pass the results i have got the response and i want that result to be processed it is done here.
//before sending the request we have to do some prerequisites that is covered in on pre execute and it is taken care of by the ui thread

//executers are used when bahut time lageaga