package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AwarenessCounselling extends AppCompatActivity {
    Button video_show,video_register,article_show,article_register,delete;
    private FirebaseFirestore data_storage;
    String userId;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_counselling);
        user = FirebaseAuth.getInstance().getCurrentUser();
        data_storage = FirebaseFirestore.getInstance();
        video_show=findViewById(R.id.aw_co_ar_show);
        video_register=findViewById(R.id.aw_co_regis);
        article_show=findViewById(R.id.art_show);
        article_register=findViewById(R.id.aw_co_ar_regis);
        delete = findViewById(R.id.delete);
        userId = user.getUid();
        video_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), VideoUploadedshow.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        video_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AwarenessVideoRegister.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        article_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), ArticleUploadedShow.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        article_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), AwarenessArticleRegister.class);
                //startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
                startActivity(intent);
            }
        });
        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data_storage.collection("users").document(userId).delete();
            }
        });*/
    }

}