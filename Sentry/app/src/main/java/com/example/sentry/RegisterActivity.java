package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.sentry.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference rootRef, demoRef;
    Button insertion;
    User push;        //object to be pushed to firebase
    EditText e1,e2;
    String uniqueKey;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        uniqueKey = sharedPreferences.getString(KEY_NAME,null);
        if(uniqueKey!=null)
        {
            Intent intent= new Intent(getApplicationContext(),Dashboard.class);
            intent.putExtra("key", uniqueKey);
            startActivity(intent);
        }
        else
        {
            push = new User();
            rootRef = FirebaseDatabase.getInstance().getReference();
            uniqueKey = rootRef.child("Users").push().getKey();
            demoRef = rootRef.child("Users").child(uniqueKey);
            insertion = findViewById(R.id.registerButton);
            e1=findViewById(R.id.username);
            e2=findViewById(R.id.password);
            insert_data();
        }
    }

    public void set_data()
    {
        push.setName(e1.getText().toString());
        push.setPassword(e2.getText().toString());
    }
    public void insert_data(){

        insertion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_data();
                demoRef.setValue(push);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME,uniqueKey);
                editor.apply();
                Intent intent= new Intent(getApplicationContext(),Dashboard.class);
                intent.putExtra("key", uniqueKey);
                startActivity(intent);
            }
        });
    }
}