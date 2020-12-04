package com.example.sentry.NearbyPlacesHelper;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    String TAG = "DataParser.java";

    //here we pass the array because legs is in the form og array
    private HashMap<String,String> getDuration(JSONArray googleDirectionJson) {
        HashMap<String, String> googleDirectionsMap = new HashMap<>();
        //the parameters we need in return
        String duration = "";
        String distance = "";
//        Log.v("bhai","the respone"+googleDirectionJson.toString());
        try {
            duration=googleDirectionJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distance=googleDirectionJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration",duration);
            googleDirectionsMap.put("distance",distance);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleDirectionsMap;
    }

    //here object because results is in the form of ovject
    //hash map works on key and value  first we input the data type of key and value
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson)
    {
        //object of hash map
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        //we are going to get all these parameters
        String placeName="-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String reference="";
        String rating="";
        String open="";
        String icon="";
        Log.v(TAG, googlePlaceJson.toString());

        try {
            // we are checking if the json file is not null
            if (!googlePlaceJson.isNull("name")) {
                //here we are getting the value from json object and storing them in the variables
                placeName = googlePlaceJson.getString("name");
            }
            //idhar mere ko object mil gaya hain ham usmien se ek ek karkien data nikal rahe hain
            if(!googlePlaceJson.isNull("vicinity"))
            {
                //here we are getting the value from json object
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("rating"))
            {
                //here we are getting the value from json object
                rating = googlePlaceJson.getString("rating");
            }
            if(!googlePlaceJson.isNull("opening_hours"))
            {
                //here we are getting the value from json object
                open = googlePlaceJson.getJSONObject("opening_hours").getString("open_now");
            }
            if(!googlePlaceJson.isNull("icon"))
            {
                //here we are getting the value from json object
                icon = googlePlaceJson.getString("icon");
            }
            //open=googlePlaceJson.getJSONObject("opening_hours").getJSONObject("open_now").getString("open_now");
            //"geometry" : {
            //            "location" : {
            //               "lat" : -33.8599358,
            //               "lng" : 151.2090295
            //            },
           // we have the jason of latitude and longitude in this form hence we got to that location
            //hence we write get jason object to first go to geometry then location and then we get the string that is latitude and longitude
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            Log.v(TAG, icon);

            if(!googlePlaceJson.isNull("reference"))
            {
                //here we are getting the value from json object
                reference=googlePlaceJson.getString("reference");
            }
//
            //after fetching the data putting the data in the hash map in the form of key and value
            googlePlaceMap.put("place_name",placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);
            googlePlaceMap.put("rating",rating);
            googlePlaceMap.put("open",open);
            googlePlaceMap.put("icon",icon);
            //on printing the data the hash map does not print it in order keep that in mind
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //returning the map
        return googlePlaceMap;

    }
    //get place will return hash map for each place
    //here we are creating a list reference to the hash map one hash map contains data for one place by the list we show all the different places
    // get places will store all the   hash map.in a list
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String,String>> placesList = new ArrayList<>();
        //hash map to store each place that we fetch
        HashMap<String,String> placeMap = null;

        for(int i=0;i<count;i++)
        {
            try {
                //here we are using the get place method to get 1 place and then add that hash map in the list
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    //this return a list of hash maps
    //when we create the data parser we call the parse method which will send the data to get places  where the jason array is taken which is passed
    //to get for loop where get place method is called and it will fetch each element from the jason array for each place and it gets
    //stored in the place list in the form of elements which is returned back to parse method which is returned to the origin
    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return getPlaces(jsonArray);
    }
    //here we are passing the destination details and parsing that json file
    public HashMap<String,String> parseDirections(String jasonData)
    //public String[] parseDirections(String jasonData)
    {
        //in this jason file routes(array) in which we have the kegs array which has distance and duration
        //object which has two elements that we need the  has
        //polyline stores multiple latitude and longitude
        JSONArray jsonArray = null;
        JSONArray jsonArray2=null;
        //JSONArray jsonArray1 = null;
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jasonData);
            Log.v(TAG, "line 139");
            Log.v(TAG, jasonData);
            Log.v(TAG, jsonObject.toString());
            Log.v(TAG, jsonObject.getJSONArray("routes").toString());
            if(jsonObject.getJSONArray("routes").length() > 0) {
                jsonArray  = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                //jsonArray2 = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            } else {
//                Toast.makeText(this, "Route data not available", Toast.LENGTH_LONG).show();
                Log.v(TAG, "Data unavailable");
            }
            Log.v(TAG, "line 141");
        } catch (JSONException e) {
            e.printStackTrace();
        }

       //return getPaths(jsonArray);
         return getDuration(jsonArray);
    }
    public String[] parseDirections_path(String jasonData)
    {
        //in this jason file routes(array) in which we have the kegs array which has distance and duration
        //object which has two elements that we need the  has
        //polyline stores multiple latitude and longitude
        JSONArray jsonArray = null;
        //JSONArray jsonArray1 = null;
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jasonData);
            Log.v(TAG, "line 139");
            Log.v(TAG, jasonData);
            Log.v(TAG, jsonObject.toString());
            Log.v(TAG, jsonObject.getJSONArray("routes").toString());
            if(jsonObject.getJSONArray("routes").length() > 0) {
                //jsonArray  = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
                jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            } else {
//                Toast.makeText(this, "Route data not available", Toast.LENGTH_LONG).show();
                Log.v(TAG, "Data unavailable");
            }
            Log.v(TAG, "line 141");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPaths(jsonArray);
        // return getDuration(jsonArray);
    }
    public String[] getPaths(JSONArray googleStepsJson)
    {
        int count = googleStepsJson.length();
        String[] polylines =new String[count];

        for(int i =0;i<count;i++)
        {
            try {
                polylines[i]=getPath(googleStepsJson.getJSONObject(i));
               // Log.v(TAG,"THE LATITUDE longitude is"+polylines[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polylines;
    }

    public String getPath(JSONObject googlePathJason)
    {
        String polyline=" ";
        try {
             polyline = googlePathJason.getJSONObject("polyline").getString("points");
            //storing the value iin array of strings
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
