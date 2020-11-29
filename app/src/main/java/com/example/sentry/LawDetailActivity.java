package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LawDetailActivity extends AppCompatActivity {


        ImageView main_img ;
        TextView title,description;
        String Tag="i am getting the data";
        String data1,data2;
        int myImageView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_law_detail);

            main_img = findViewById(R.id.main_image);
            title = findViewById(R.id.Title);
            description= findViewById(R.id.Descrip);
            getData();
            setData();
        }

        private void getData()
        {
            Log.v(Tag,"i am entering get data");
            //setting if block to check if data exists
            if(getIntent().hasExtra("myImageView") && getIntent().hasExtra("data1") && getIntent().hasExtra("data2")){

                data1 = getIntent().getStringExtra("data1");
                Log.v(Tag,"i am there"+data1);
                data2 = getIntent().getStringExtra("data2");
                myImageView = getIntent().getIntExtra("myImageView",1);
                Log.v(Tag,"i am getting image");

            }else{
                Toast.makeText(this,"NO INFORMATION AVAILABLE",Toast.LENGTH_SHORT).show();

            }
        }
        private void setData()
        {
            Log.v(Tag,"i am entering set data");
            title.setText(data1);
            description.setText(data2);
            main_img.setImageResource(myImageView);
        }
    }