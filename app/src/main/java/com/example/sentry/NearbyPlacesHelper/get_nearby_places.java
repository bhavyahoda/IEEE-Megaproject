package com.example.sentry.NearbyPlacesHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.example.sentry.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
//basically async task does things in the background and prepares the app to do that in the ui and connect this sub class to the main adtivity

public class get_nearby_places  extends AsyncTask<Object,String,String> {


    String google_places_data;
    GoogleMap mMap;
    String url;
    String markerType;
    String TAG = "get_nearby_places";
    private Context mContext;
    private TextView textMarker;
    private TextView open_now;
    private TextView rat;
    private TextView vic;
    private ImageView ic;
    private CardView cardView;

    public get_nearby_places(Context context, TextView tv,TextView open_now_marker,TextView ratingMarker,TextView VicMarker,ImageView icon,ImageView pic_place,CardView card_view) {
        mContext = context;
        textMarker = tv;
        open_now = open_now_marker;
        rat=ratingMarker;
        vic=VicMarker;
        ic=icon;
        cardView=card_view;
        Log.v(TAG,"picture,icon added");
    }

    @Override
    protected String doInBackground(Object... objects) {
        //object is the parameter that we pass here
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        markerType = (String) objects[2];
        //creating a new url
        download_url d_url = new download_url();
        try {
            //passing the url
            google_places_data = d_url.readUrl(url);

        } catch (IOException e) {
            System.out.println("Their was no url provided");
            e.printStackTrace();
        }
        return google_places_data;
    }

    @Override
    protected void onPostExecute(String s) {
        //list called
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser = new DataParser();
        //the list is then parsed with the help of the parser
        nearbyPlaceList = parser.parse(s);
        // it passes the list
        Log.v(TAG, markerType);
//        MainActivity.showNearbyPlacesMain(nearbyPlaceList, markerType, mMap);
//        return nearbyPlaceList;
        showNearbyPlaces(nearbyPlaceList, markerType);
    }

    //it is going to take a lit of hash maps
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList, String markerType) {
        //here nearbyPlaceList.size will store the number of elements that is returned to us through the jason array it
        // will have the data of each place in the form pf hash maps
        int l;
        l = nearbyPlaceList.size();
        l = l > 5 ? 5: l;
        for (int i = 0; i < l; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            //here to each hash map there will be a marker added
            //here one by one the ith element is fetched and stored in the hash map like for a place all the details are store in this kash map
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            //fetching the place name and the vicinity and all other details
            //for lat lang the google place.get("lat") returns the value in string we want it in double format hence used that
            //
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String rating = googlePlace.get("rating");
            String open = googlePlace.get("open");
            String icon = googlePlace.get("icon");
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            LatLng latLng = new LatLng(lat, lng);
            /*Icon icon_hospital={
                        url: "../res/sit_marron.png", // url
                        scaledSize: new google.maps.Size(width, height), // size
                        origin: new google.maps.Point(0,0), // origin
                        anchor: new google.maps.Point(anchor_left, anchor_top) // anchor
 };

            }*/
           // mMap.setOnMarkerClickListener(this);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            switch (markerType) {
                case "hospital" : //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.hos_icon));
                                break;

                case "police" : //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pol_icon));
                    break;

                case "fire_station" : //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire_stat_icon));
                    break;

            }


            mMap.addMarker(markerOptions);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    cardView.setVisibility(View.VISIBLE);
                    Log.v(TAG, marker.getTitle());
                    textMarker.setText(marker.getTitle());
                    //rating null hua toh kya
                    Log.v(TAG,"i am printed!"+rating);
                    rat.setText("Ratings "+rating);
                    Log.v(TAG,"i am printed!"+vicinity);
                    vic.setText("Location "+vicinity);
                    if(open.equals("true"))
                    {
                        Log.v(TAG,"open now");
                        open_now.setText("open now");
                    }
                    else {
                        Log.v(TAG,"close");
                        open_now.setText("closed now");
                    }
                    Log.v(TAG,"the icon is::"+icon);

                    Picasso.get().load(icon)
                            .resize(400,400)
                            .centerCrop()
                            .rotate(0)
                            .into(ic);

                    Log.v(TAG,"the icon picture printed::"+icon);



//                 Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    cardView.setVisibility(View.INVISIBLE);
                }
            });
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        }
    }
}