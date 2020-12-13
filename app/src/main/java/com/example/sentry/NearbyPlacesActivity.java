package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentry.NearbyPlacesHelper.GetDirectionsData;
import com.example.sentry.NearbyPlacesHelper.get_nearby_places;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NearbyPlacesActivity extends FragmentActivity implements OnMapReadyCallback,
	GoogleApiClient.ConnectionCallbacks,
	GoogleApiClient.OnConnectionFailedListener,
	LocationListener,
	GoogleMap.OnMarkerClickListener,
	GoogleMap.OnMarkerDragListener {

	private GoogleMap mMap;
	private  GoogleApiClient Client;
	private LocationRequest locationRequest;
	private Location last_location;
	//stores the last location
	private Marker curr_location;
	public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
	LocationRequest mLocationRequest;
	int PROXIMITY_RADIUS = 5000;
	//current location
	double latitude;
	double longitude;
	// destination location
	double end_latitude;
	double end_longitude;
	String TAG = "mainActivity";
	Button bt_search,bt_hospital,bt_police,bt_fire,bt_to;
	TextView tv;
	TextView open_now_marker;
	TextView ratingMarker;
	TextView VicMarker;
	ImageView icon;
	ImageView pic_place;
	CardView cardView;
	EditText loc_name;
	//this code is for the other places on the map it is to check the about the permission.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby_places);
		//get a handle of the fragment by calling the fragment manager through id
		//get the support map fragment and request notification when the map is ready
//        textMarker = (TextView) findViewById(R.id.textMarker);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkLocationPermission();
		}
		Places.initialize(getApplicationContext(), getResources().getString(R.string.map_key));

		loc_name = (EditText) findViewById(R.id.search_text);
		Log.v(TAG, "101");

		//set edit text non focusable
		loc_name.setFocusable(false);
		loc_name.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Initialize places field list
				//zet the field to specify which type of place data to return after the user has made decision
				List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
				                              Place.Field.LAT_LNG, Place.Field.NAME);

				//create autocomplete intent
				Log.v(TAG, fieldList.toString());

				Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY
				        , fieldList).build(NearbyPlacesActivity.this);
				//start activity result
				startActivityForResult(intent, 100);

			}
		});


		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
		                                 .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
		case PERMISSION_REQUEST_LOCATION_CODE:
			if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//permission is granted
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					if (Client == null) {
						buildGoogleApiClient();
					}
					mMap.setMyLocationEnabled(true);
				}
			} else { //permission denied
				Toast.makeText(this, "Permission denied" , Toast.LENGTH_LONG).show();
			}
			return;
		}
	}
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.clear();
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
			buildGoogleApiClient();

		mMap.setMyLocationEnabled(true);
		mMap.setOnMarkerClickListener(this);
		mMap.setOnMarkerDragListener(this);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v(TAG, "t have entered on activity");
		Log.v(TAG, String.valueOf(requestCode));
		if (requestCode == 100 && resultCode == RESULT_OK) {
			//when success
			//we initialize that  place as our final place on the text editor
			Place place = Autocomplete.getPlaceFromIntent(data);
			Log.v(TAG, "the place autocomplete" + place.getAddress());
			loc_name.setText(place.getAddress());
			//the address the user select gets set on the edit text
		} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
			//when error we handle the error
			Status status = Autocomplete.getStatusFromIntent(data);
			Log.i(TAG, status.getStatusMessage());
			//display toast here the message get printed on the ui
			Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_LONG).show();
		} else if (resultCode == RESULT_CANCELED) {
			//THE USER CANCELLED THE REQUEST
		}
	}
	public void onClick(View v) {
		//check if user has selected the button
		//object created
		//an object of get nearby place
		tv = (TextView) findViewById(R.id.textMarker);
		open_now_marker = (TextView) findViewById(R.id.open_now_marker);
		ratingMarker = (TextView) findViewById(R.id.rating_marker);
		VicMarker = (TextView) findViewById(R.id.vicinityMarker);
		icon = (ImageView) findViewById(R.id.icon);
		cardView = (CardView) findViewById(R.id.cardView);
		//get_nearby_places gns = new get_nearby_places(this, tv, open_now_marker, ratingMarker, VicMarker, icon, pic_place, cardView);
		bt_search = findViewById(R.id.button_search);
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				//take the text the user had entered
				//converting the text into string
				String location = loc_name.getText().toString();
				//storing the results in a list
				List<Address> searched_places = null;
				MarkerOptions mo = new MarkerOptions();
				//check if the user entered something
				if (!location.equals("")) { //this means that user has entered something
					//now we use the geocoder class to get the latitude nad longitude of the entered text
					Geocoder geocoder = new Geocoder(getApplicationContext());
					try {
						searched_places = geocoder.getFromLocationName(location, 5);
					} catch (IOException e) {
						e.printStackTrace();
					}

					//putting a marker in the location
					for (int i = 0; i < searched_places.size(); i++) {
						Address add = searched_places.get(i);
						LatLng latLng = new LatLng(add.getLatitude(), add.getLongitude());
						mo.position(latLng);
						mo.title("You Searched For " + location);
						mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
						mMap.addMarker(mo);
						//showing camera  focus on last result
						mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
					}
				}
			}
		});
		bt_hospital = findViewById(R.id.b_hospital);
		bt_hospital.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				String hospital = "hospital";
				String url = getUrl(latitude, longitude, hospital);
				Object dataTransfer[] = new Object[3];
				get_nearby_places gns = new get_nearby_places(getApplicationContext(), tv, open_now_marker, ratingMarker, VicMarker, icon, cardView);
				//creating an object which is storing two objects the map and the url
				dataTransfer[0] = mMap;
				dataTransfer[1] = url;
				dataTransfer[2] = hospital;
				//here the execute will call the nearby places api server and execute the methods to show the data performed on our request
				gns.execute(dataTransfer);
				Toast.makeText(getApplicationContext(), "Showing Nearby Hospitals", Toast.LENGTH_LONG).show();
			}
		});

		bt_fire = findViewById(R.id.b_fire_station);
		bt_fire.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				String fireStation = "fire_station";
				String url = getUrl(latitude, longitude, fireStation);
				Object dataTransfer[] = new Object[3];
				get_nearby_places gns = new get_nearby_places(getApplicationContext(), tv, open_now_marker, ratingMarker, VicMarker, icon, cardView);
				dataTransfer[0] = mMap;
				dataTransfer[1] = url;
				dataTransfer[2] = fireStation;
				gns.execute(dataTransfer);
				Toast.makeText(getApplicationContext(), "Showing Nearby Fire Stations", Toast.LENGTH_LONG).show();

			}
		});
		bt_police = findViewById(R.id.b_police_station);
		bt_police.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				String policeStation = "police";
				String url = getUrl(latitude, longitude, policeStation);
				Object dataTransfer[] = new Object[3];
				get_nearby_places gns = new get_nearby_places(getApplicationContext(), tv, open_now_marker, ratingMarker, VicMarker, icon, cardView);
				dataTransfer[0] = mMap;
				dataTransfer[1] = url;
				dataTransfer[2] = policeStation;
				gns.execute(dataTransfer);
				Toast.makeText(getApplicationContext(), "Showing Nearby Police Stations ", Toast.LENGTH_LONG).show();
			}
		});

		bt_to = findViewById(R.id.bt_to);
		bt_to.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				Object data_transfer[] = new Object[3];
				String url = getDirectionsUrl();
				//object of the class
				GetDirectionsData gdd = new GetDirectionsData();
				data_transfer[0] = mMap;
				data_transfer[1] = url;
				//passing the latitude and longitude value of the destination location
				data_transfer[2] = new LatLng(end_latitude, end_longitude);
				gdd.execute(data_transfer);
				Toast.makeText(getApplicationContext(), "Showing Distance and Duration Between The Current Location and The Destination", Toast.LENGTH_LONG).show();


			}
		});

	}

	private String getDirectionsUrl() {

		StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json");
		googleDirectionsUrl.append("?origin=" + last_location.getLatitude() + "," + last_location.getLongitude());
		googleDirectionsUrl.append("&destination=" + end_latitude + "," + end_longitude);
		//does not give location that has ferry
		googleDirectionsUrl.append("&avoid=ferries");
		googleDirectionsUrl.append("&key=" + getResources().getString(R.string.map_key));

		return googleDirectionsUrl.toString();

	}

	private String getUrl(double latitude, double longitude , String nearbyPlace) {
		latitude = last_location.getLatitude();
		longitude = last_location.getLongitude();
		StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json");
		googlePlaceUrl.append("?location=" + latitude + "," + longitude);
		googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
		googlePlaceUrl.append("&types=" + nearbyPlace);
		googlePlaceUrl.append("&sensor=true");
		googlePlaceUrl.append("&key=" + getResources().getString(R.string.map_key));
//converting string builder to string
		Log.v(TAG, googlePlaceUrl.toString());
		return googlePlaceUrl.toString();
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
		markerOptions.title(("You are here now."));
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		//current marker has been assigned the marker option declared above
		curr_location = mMap.addMarker(markerOptions);

		//camera specification moving camera to the current location of the user

		curr_location = mMap.addMarker(markerOptions);


		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_lang, 15));
		mMap.animateCamera(CameraUpdateFactory.zoomIn());
		mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(lat_lang)      // Sets the center of the map to Mountain View
				.zoom(14)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(0)                   // Sets the tilt of the camera to 30 degrees
				.build();                   // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		//stopping the updates after i get the current location of myself
		if (Client != null) {
			//if client is null means there is no location now that we got
			//not equal to null means some location has been administered by the client
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
				//after reading the message the user knows why we need permission now the diluoge box appears to
				//to the user to choose the permission and this time we check it with the request code to match that the permision is granted for the work we want to o ith the permission
				//new string will take a array of permission that we have to give

				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);

			}

			else {
				//if we dont want to show the explaination then we directly request for the permission
				ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION_CODE);
			}
			//permission denied hogaya ab they cannot access the current location
			//if the permission was asked before by the app and the user had choosen the dont ask again vala option
			return false;

		}
		//if the permission is granted directly true bejh dega
		else
			return true;

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		//marker is draggable on clicking
		marker.setDraggable(true);
		return false;
	}

	@Override
	public void onMarkerDragStart(Marker marker) {

	}

	@Override
	public void onMarkerDrag(Marker marker) {

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {

		//setting latitude and longitude of that marker
		end_latitude = marker.getPosition().latitude;
		end_longitude = marker.getPosition().longitude;

	}
}