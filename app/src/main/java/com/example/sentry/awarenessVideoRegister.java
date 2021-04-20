package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sentry.model.Videos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class awarenessVideoRegister extends AppCompatActivity {
    private EditText nameTextView, qualificationTextView, topicTextView, timeTextView, linkTextView, reasonTextView,PriceTextView;
    private FirebaseFirestore data_storage;
    FirebaseUser user;
    Button save;
    String userId;
    Videos push_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_video_register);
        user = FirebaseAuth.getInstance().getCurrentUser();
        nameTextView = findViewById(R.id.name_video);
        qualificationTextView = findViewById(R.id.email_aware);
        topicTextView = findViewById(R.id.title_of_article);
        timeTextView = findViewById(R.id.description);
        linkTextView = findViewById(R.id.article_link);
        reasonTextView = findViewById(R.id.reason);
        PriceTextView=findViewById(R.id.price_video);
        save = findViewById(R.id.save_article);
        userId = user.getUid();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVideoInfo();
            }
        });
    }

    private void registerVideoInfo(){
        if (TextUtils.isEmpty(nameTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(qualificationTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Qualification!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(topicTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter topic of your video!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(timeTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter time the event is going to take place!!", Toast.LENGTH_LONG).show();
           return;
        }
        if (TextUtils.isEmpty(linkTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter link !!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(PriceTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Price !!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(reasonTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter reason why anyone should attend!!", Toast.LENGTH_LONG).show();
            return;
        }

        data_storage = FirebaseFirestore.getInstance();
        DocumentReference documentReference = data_storage.collection("counselling").document(userId);
        push_data = new Videos();
        set_data();
        documentReference.set(push_data);
        nameTextView.setText("");
        linkTextView.setText("");
        qualificationTextView.setText("");
        timeTextView.setText("");
        reasonTextView.setText("");
        topicTextView.setText("");
        PriceTextView.setText("");
        finish();
    }

    public void set_data(){
        push_data.setName(nameTextView.getText().toString());
        push_data.setLink(linkTextView.getText().toString());
        push_data.setQualification(qualificationTextView.getText().toString());
        push_data.setTime(timeTextView.getText().toString());
        push_data.setReason(reasonTextView.getText().toString());
        push_data.setPrice(PriceTextView.getText().toString());
        push_data.setTopic(topicTextView.getText().toString());
    }
}