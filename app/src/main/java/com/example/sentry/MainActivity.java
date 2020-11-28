package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button login,register;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        register = findViewById(R.id.Register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
    }
}