package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HotelInfo extends AppCompatActivity {
    Button hot_info,hotel_regis,hotel_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);
        hot_info=findViewById(R.id.hot_info);
        hotel_regis=findViewById(R.id.hotel_regis);
        hotel_show=findViewById(R.id.hotel_show);
        hot_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), HotelInformation.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        hotel_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), HotelRegistration.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        hotel_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), HotelShow.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
    }
}