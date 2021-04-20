package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sentry.model.SelfDefense;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class selfDefenseForm extends AppCompatActivity {
    private EditText nameTextView, AboutTextView, PhoneTextView, DateTextView, linkTextView,WorkTextView,priceTextView;
    private FirebaseFirestore data_storage;
    FirebaseUser user;
    Button save;
    String userId;
    SelfDefense push_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_defense_form);
        user = FirebaseAuth.getInstance().getCurrentUser();
        nameTextView = findViewById(R.id.name_of_person);
        AboutTextView = findViewById(R.id.about);
        PhoneTextView = findViewById(R.id.Phone_number);
        DateTextView = findViewById(R.id.date);
        linkTextView = findViewById(R.id.Link_location);
        WorkTextView = findViewById(R.id.Reference_work);
        priceTextView =findViewById(R.id.price_self_def);
        save = findViewById(R.id.save_info);
        userId = user.getUid();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                registerSelfDefense();
            }
        });
    }

    private void registerSelfDefense(){
        if (TextUtils.isEmpty(nameTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(AboutTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Details!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(PhoneTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter phone number!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(DateTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter the date for the event!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(linkTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter link !!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(priceTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Price !!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(WorkTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter reference work!!", Toast.LENGTH_LONG).show();
            return;
        }

        data_storage = FirebaseFirestore.getInstance();
        DocumentReference documentReference = data_storage.collection("self_defense").document();
        push_data = new SelfDefense();
        set_data();
        documentReference.set(push_data);
        nameTextView.setText("");
        linkTextView.setText("");
        AboutTextView.setText("");
        PhoneTextView.setText("");
        WorkTextView.setText("");
        DateTextView.setText("");
        priceTextView.setText("");
        finish();
    }

    public void set_data(){
        push_data.setName(nameTextView.getText().toString());
        push_data.setLink(linkTextView.getText().toString());
        push_data.setAbout(AboutTextView.getText().toString());
        push_data.setDate(DateTextView.getText().toString());
        push_data.setWork(WorkTextView.getText().toString());
        push_data.setPrice(priceTextView.getText().toString());
        push_data.setPhone(PhoneTextView.getText().toString());
    }
}