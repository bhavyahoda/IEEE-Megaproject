package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelfDefense extends AppCompatActivity {
    Button sd_regis,sd_vid,sd_show_class;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_defense);
        sd_regis=findViewById(R.id.sd_regis);
        sd_vid=findViewById(R.id.sd_vid);
        sd_show_class=findViewById(R.id.sd_show_class);
        sd_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), SelfDefenseForm.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        sd_vid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),sde_video.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        sd_show_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), SelfDefenseShow.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
    }

}