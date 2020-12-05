package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentry.model.Contact;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.local.QueryEngine;

public class SendingLocation extends FragmentActivity implements OnMapReadyCallback,
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener {
	private FirebaseFirestore data_storage;
	String userID;
	private FirebaseUser mAuth;
	private GoogleMap mMap;
	private  GoogleApiClient Client;
	private LocationRequest locationRequest;
	private Location last_location;
	//stores the last location
	private Marker curr_location;
	public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
	//this code is for the other places on the map it is to check the about the permission.


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sending_location);
		//get a handle of the fragment by calling the fragment manager through id
		//get the support map fragment and request notification when the map is ready
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkLocationPermission();
		}
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
		                                 .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
		case PERMISSION_REQUEST_LOCATION_CODE:
			if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					if (Client == null) {
						buildGoogleApiClient();
					}
					mMap.setMyLocationEnabled(true);
				}
			} else {
				Toast.makeText(this, "Permission denied" , Toast.LENGTH_LONG).show();
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
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}
	@Override
	public void onLocationChanged(@NonNull Location location) {

		last_location = location;
		//there is already set to another place we remove that
		if (curr_location != null) {
			//if there was place already on that maker remove
			curr_location.remove();
		}
		LatLng lat_lang = new LatLng(location.getLatitude(), location.getLongitude());

		MarkerOptions markerOptions = new MarkerOptions();
		//set properties to the marker
		markerOptions.position(lat_lang);

		SmsManager sms = SmsManager.getDefault();
		mAuth = FirebaseAuth.getInstance().getCurrentUser();
		data_storage = FirebaseFirestore.getInstance();
		userID=mAuth.getUid();
		DocumentReference documentReference=data_storage.collection("contacts").document(userID);
		documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
			@Override
			public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
				String lat_long="I AM IN A PROBLEM. GIVEN ARE MY CURRENT LOCATION COORDINATES, PLEASE COME AND HELP ME OUT!!!!!!!\n";
				lat_long += String.valueOf(lat_lang.latitude).concat(" ").concat(String.valueOf(lat_lang.longitude));
				if(value.getString("number5")!=null) {
					sms.sendTextMessage(value.getString("number5"), null, lat_long, null, null);
				}
				if(value.getString("number4")!=null) {
					sms.sendTextMessage(value.getString("number4"), null, lat_long, null, null);
				}
				if(value.getString("number3")!=null) {
					sms.sendTextMessage(value.getString("number3"), null, lat_long, null, null);
				}
				if(value.getString("number2")!=null) {
					sms.sendTextMessage(value.getString("number2"), null, lat_long, null, null);
				}
				if(value.getString("number1")!=null) {
					sms.sendTextMessage(value.getString("number1"), null, lat_long, null, null);
				}
			}
		});
		markerOptions.title(("You are here now"));
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		//current marker has been assigned the marker option declared above
		curr_location = mMap.addMarker(markerOptions);

		//camera specification moving camera to the current location of the user

		mMap.moveCamera(CameraUpdateFactory.newLatLng(lat_lang));
		mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

		//stopping the updates after we get the current location of ourselves
		if (Client != null) {

			LocationServices.FusedLocationApi.removeLocationUpdates(Client, this);
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
		locationRequest.setInterval(120 * 1000);
		//if a location is available sooner you get it
		//If you are the only app which use the location services you will continue to receive updates approximately every 60 seconds. If another app is using the location services with a higher rate of updates, you will get more location updates (but no more frequently that every 10 seconds).
		locationRequest.setFastestInterval(40 * 1000);
		//we want to get the updated location after certain interval of time but the power impact has to be lower
		locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

		//we have to check whether the user has given permission for location access or not
		//so if statement

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			LocationServices.FusedLocationApi.requestLocationUpdates(Client, locationRequest, this);
		}
	}

	public boolean checkLocationPermission() {
		//first checking the permission was granted or not
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			//if permission is not granted we ask for the permission an educational message is shown as to why the permission is required
			//this will return true if the user had asked for the permission before and denied for it,and is now trying to access the feature again
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);

			}
			else {
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
			}
			return false;

		} else
			return true;

	}

	@Override
	public void onConnectionSuspended(int i) {

	}
}
