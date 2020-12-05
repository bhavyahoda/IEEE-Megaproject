package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentry.model.AnonymousTip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MakeAnonymousTip extends AppCompatActivity {
    private TextView stay_strong;
    private EditText tip_loc,tip_detail,tip_topic;
    private FirebaseFirestore data_storage;
    FirebaseUser user;
    Button save;
    String userId;
    AnonymousTip push_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_anonymous_tip);
        user = FirebaseAuth.getInstance().getCurrentUser();
        stay_strong=(TextView)findViewById(R.id.call_the_police);
        tip_topic= findViewById(R.id.subject_tip);
        tip_loc = findViewById(R.id.city_state_tip);
        tip_detail = findViewById(R.id.tip_description);
        save = findViewById(R.id.save);
        userId = user.getUid();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerTip();
            }
        });
    }

    private void registerTip(){
        if (TextUtils.isEmpty(tip_topic.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Tip!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(tip_detail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Tip Details!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(tip_loc.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Tip Location!!", Toast.LENGTH_LONG).show();
            return;
        }
        data_storage = FirebaseFirestore.getInstance();
        DocumentReference documentReference = data_storage.collection("tip").document(userId);
        push_data = new AnonymousTip();
        set_data();
        documentReference.set(push_data);
        tip_loc.setText("");
        tip_detail.setText("");
        tip_topic.setText("");
        finish();
    }
    public void set_data(){
        push_data.setTipLocation(tip_loc.getText().toString());
        push_data.setTipDescription(tip_detail.getText().toString());
        push_data.setTipTopic(tip_topic.getText().toString());
    }

}