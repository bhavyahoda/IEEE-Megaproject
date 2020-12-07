package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sentry.model.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FillContactsActivity extends AppCompatActivity {
    private EditText contact1_name,contact1_number,contact2_name,contact2_number,contact3_name,contact3_number,contact4_name,contact4_number,contact5_name,contact5_number;
    private FirebaseFirestore data_storage;
    String userID;
    private FirebaseUser mAuth;
    Contact push;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_contacts);
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        data_storage = FirebaseFirestore.getInstance();
        contact1_name = findViewById(R.id.contact1_name);
        contact1_number = findViewById(R.id.contact1_number);
        contact2_name = findViewById(R.id.contact2_name);
        contact2_number = findViewById(R.id.contact2_number);
        contact3_name = findViewById(R.id.contact3_name);
        contact3_number = findViewById(R.id.contact3_number);
        contact4_name = findViewById(R.id.contact4_name);
        contact4_number = findViewById(R.id.contact4_number);
        contact5_name = findViewById(R.id.contact5_name);
        contact5_number = findViewById(R.id.contact5_number);
        userID = mAuth.getUid();
        button=findViewById(R.id.save_please);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_adder();
            }
        });
    }
    private void contact_adder(){
        DocumentReference documentReference = data_storage.collection("contacts").document(userID);
        push=new Contact();
        set_data();
        documentReference.set(push);
        // Validations for input email and password
        if (TextUtils.isEmpty(contact1_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 1st contact name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact1_number.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 1st contact number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact2_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 2nd contact name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact2_number.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 2nd contact number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact3_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 3rd contact name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact3_number.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 3rd contact number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact4_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 4th contact name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact4_number.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 4th contact number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact5_name.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 5th contact name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contact5_number.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter 5th contact number!!", Toast.LENGTH_LONG).show();
            return;
        }
        contact1_name.setText("");
        contact1_number.setText("");
        contact2_name.setText("");
        contact2_number.setText("");
        contact3_name.setText("");
        contact3_number.setText("");
        contact4_name.setText("");
        contact4_number.setText("");
        contact5_name.setText("");
        contact5_number.setText("");
        finish();
    }
    public void set_data(){
        push.setName1(contact1_name.getText().toString());
        push.setName2(contact2_name.getText().toString());
        push.setName3(contact3_name.getText().toString());
        push.setName4(contact4_name.getText().toString());
        push.setName5(contact5_name.getText().toString());
        push.setNumber1(contact1_number.getText().toString());
        push.setNumber2(contact2_number.getText().toString());
        push.setNumber3(contact3_number.getText().toString());
        push.setNumber4(contact4_number.getText().toString());
        push.setNumber5(contact5_number.getText().toString());

    }
}