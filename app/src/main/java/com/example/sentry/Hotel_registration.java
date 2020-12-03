package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sentry.model.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Hotel_registration extends AppCompatActivity {
    private EditText Name,hotel_location_link,hotel_loc_detail,min_price,rating_verified,hotel_feature;
    private FirebaseFirestore data_storage;
    FirebaseUser user;
    Button save;
    String userId;
    Hotel push_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_registration);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Name= findViewById(R.id.name_video);
        hotel_location_link= findViewById(R.id.location_hotel_link);
        hotel_loc_detail= findViewById(R.id.location_of_hotel);
        min_price= findViewById(R.id.min_room_price);
        rating_verified= findViewById(R.id.hotel_rating_itslef);
        hotel_feature= findViewById(R.id.features_hotel);
            save = findViewById(R.id.save_button);
            userId = user.getUid();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    registerHotelInfo();
                }
            });
        }

        private void registerHotelInfo(){
            if (TextUtils.isEmpty(Name.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter name!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(hotel_location_link.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter google link!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(hotel_loc_detail.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter Location!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(min_price.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter min Price!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(rating_verified.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter Rating!!", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(hotel_feature.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please enter hotel features!!", Toast.LENGTH_LONG).show();
                return;
            }

            data_storage = FirebaseFirestore.getInstance();
            CollectionReference documentReference = data_storage.collection("users").document(userId).collection("hotels");
            push_data = new Hotel();
            set_data();
            documentReference.add(push_data);
            Name.setText("");
            hotel_location_link.setText("");
            hotel_loc_detail.setText("");
            min_price.setText("");
            rating_verified.setText("");
            hotel_feature.setText("");
            finish();
        }

        public void set_data(){
            push_data.setName(Name.getText().toString());
            push_data.setLocationLink(hotel_location_link.getText().toString());
            push_data.setLocationDetail(hotel_loc_detail.getText().toString());
            push_data.setMinimumPrice(min_price.getText().toString());
            push_data.setRating(rating_verified.getText().toString());
            push_data.setFeatureDetail(hotel_feature.getText().toString());
        }
    }