package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentry.model.AnonymousTip;
import com.example.sentry.model.SelfDefense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AnonymousTipActivity extends AppCompatActivity {
    Button seeTip,makeTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_tip);
        seeTip=findViewById(R.id.see_tip);
        makeTip=findViewById(R.id.make_tip);
        seeTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),SeeAnonymousTip.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        makeTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),MakeAnonymousTip.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });

    }

}