package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HotelDetailPresent extends AppCompatActivity {

    ImageView main_img ;
    TextView name,description,rating,price,link,phoneNumber,location;
    String Tag="i am getting the data";
    String data1,data2,data3,data4,data5,data6,data8;
    private boolean isClickingLink = false;
    int myImageView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail_present);

        main_img= findViewById(R.id.hotel_image);
        name= findViewById(R.id.hotel_name);
        description=findViewById(R.id.hotel_description);
        rating=findViewById(R.id.rating);
        price=findViewById(R.id.price_hotel);
        link=findViewById(R.id.detail_link);
        phoneNumber=findViewById(R.id.phone_hotel);
        location=findViewById(R.id.location_hotel);

        getData();
        setData();
        //Log.v(Tag,"the linkkkkkkkk!!!!!!"+data5);
       link.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent in = new Intent(Intent.ACTION_VIEW,Uri.parse(data5));
               startActivity(in);
               
           }
       });
    }
    private void getData() {
        Log.v(Tag, "i am entering get data");
        //setting if block to check if data exists
        if (getIntent().hasExtra("myImageView") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2") && getIntent().hasExtra("data3")
                && getIntent().hasExtra("data4") && getIntent().hasExtra("data5") && getIntent().hasExtra("data6") && getIntent().hasExtra("data8")) {

            data1 = getIntent().getStringExtra("data1");
            //Log.v(Tag, "i am there" + data1);
            data2 = getIntent().getStringExtra("data2");
            data3 = getIntent().getStringExtra("data3");
            data4 = getIntent().getStringExtra("data4");
            data5 = getIntent().getStringExtra("data5");
            data6 = getIntent().getStringExtra("data6");
            data8 = getIntent().getStringExtra("data8");
            //data2 = getIntent().getStringExtra("data2");
            myImageView = getIntent().getIntExtra("myImageView", 1);
            //Log.v(Tag, "i am getting image");

        } else {
            Toast.makeText(this, "NO INFORMATION AVAILABLE", Toast.LENGTH_SHORT).show();

        }
    }
       /* link.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isClickingLink) {
                Log.w("log", "not clicking link");
            }
            else {
                Log.v(Tag, "I am entering the on click");
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data5));
                startActivity(browseIntent);
                Log.v(Tag, data5);
            }
        }
    });*/

    private void setData()
    {
        name.setText(data1);
        description.setText(data6);
        description.setMovementMethod(new ScrollingMovementMethod());
        rating.setText(data2);
        price.setText(data3);
        link.setText("Click to see more details");
        Log.v(Tag,"the link is:"+data5);
        //link.setMovementMethod(LinkMovementMethod.getInstance());
        location.setText(data4);
        phoneNumber.setText(data8);
        main_img.setImageResource(myImageView);
    }

}
