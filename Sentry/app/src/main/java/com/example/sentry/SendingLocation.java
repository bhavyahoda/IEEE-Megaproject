package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.annotations.Nullable;

public class SendingLocation extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private  GoogleApiClient Client;
    private LocationRequest locationRequest;
    private Location last_location;
    //stores the last location
    private Marker curr_location;
    public static final int PERMISSION_REQUEST_LOCATION_CODE=99;
    //this code is for the other places on the map it is to check the about the permission.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_location);
        //get a handle of the fragment by calling the fragment manager through id
        //get the support map fragment and request notification when the map is ready
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case PERMISSION_REQUEST_LOCATION_CODE:
                if(grantResults.length>0  && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    //permission is granted
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                    {
                        if (Client==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else //permission denied
                {
                    Toast.makeText(this,"Permission denied" , Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       /* LatLng india = new LatLng(20.5937, 78.9629);
        eMap.addMarker(new MarkerOptions().position(india).title("marker in india"));
        eMap.moveCamera(CameraUpdateFactory.newLatLng(india));*/
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            buildGoogleApiClient();
        //set our location instead of giving longitude and latitude

        mMap.setMyLocationEnabled(true);
    }

    //created a google api client
    protected synchronized void buildGoogleApiClient() {
        Client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        Client.connect();
    }
    //to get the location of a moving person we have to update this object regularly we use the fused location provider api,velocity location sab chaiye therefore we get it from location object

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    @Override
    public void onLocationChanged(@NonNull Location location) {

        last_location=location;
        //there is already set to another place we remove that
        if(curr_location!=null)
        {
            //if there was place already on that maker remove
            curr_location.remove();
        }
        LatLng lat_lang=new LatLng(location.getLatitude(),location.getLongitude());

        MarkerOptions markerOptions =new MarkerOptions();
        //set properties to the marker
        markerOptions.position(lat_lang);

        SmsManager sms = SmsManager.getDefault();
        String phone="Receiver's Phone Number";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        String lat_long = String.valueOf(lat_lang.latitude).concat(" ").concat(String.valueOf(lat_lang.longitude));
        sms.sendTextMessage(phone, null, lat_long, null, null);
        markerOptions.title(("you are here now"));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        //current marker has been assigned the marker option declared above
        curr_location=mMap.addMarker(markerOptions);

        //camera specification moving camera to the current location of the user

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat_lang));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        //stopping the updates after i get the current location of myself
        if(Client!=null)
        {
            //if client is null means there is no location now that we got
            //not equal to null means some location has been administered by the client
            LocationServices.FusedLocationApi.removeLocationUpdates(Client,this);
        }






    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //location request object created
        locationRequest = new LocationRequest();
        // application high accuracy location creating a location request
        //this basically gives updated location at a small interval of time
        //set the interval in which you want to location
        //update getting after every 120 seconds
        locationRequest.setInterval(120*1000);
        //if a location is available sooner you get it
        //If you are the only app which use the location services you will continue to receive updates approximately every 60 seconds. If another app is using the location services with a higher rate of updates, you will get more location updates (but no more frequently that every 10 seconds).
        locationRequest.setFastestInterval(40*1000);
        //we want to get the updated location after certain interval of time but the power impact has to be lower
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //we have to check whether the user has given permission for location access or not
        //so if statement

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(Client, locationRequest, this);
        }
    }

    public boolean checkLocationPermission()
    {
        //first checking the permission was granted or not
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            //if permission is not granted we ask for the permission an educational message is shown as to why the permission is required
            //this will return true if the user had asked for the permission before and denied for it,and is now trying to access the feature again
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                //after reading the message the user knows why we need permission now the diluoge box appears to
                //to the user to choose the permission and this time we check it with the request code to match that the permision is granted for the work we want to o ith the permission
                //new string will take a array of permission that we have to give
              /* new AlertDialog.Builder(this)
                        .setTitle("Permission is needed!")
                        .setMessage("This permission id needed so that the app can show you your current location ")
                        .se*/
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE);

            }

            else
            {//if we dont want to show the explaination then we directly request for the permission
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_LOCATION_CODE);
            }
            //permission denied hogaya ab they cannot access the current location
            //if the permission was asked before by the app and the user had choosen the dont ask again vala option
            return false;

        }
        //if the permission is granted directly true bhej dega
        else
            return true;

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}