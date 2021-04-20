package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sentry.model.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class awarenessArticleRegister extends AppCompatActivity {
    private EditText nameTextView, emailTextView, titleTextView, briefTextView, linkTextView;
    private FirebaseFirestore data_storage;
    FirebaseUser user;
    Button save;
    String userId;
    Article push_data;
    String Tag="articleuploadshow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_article_register);
        user = FirebaseAuth.getInstance().getCurrentUser();
        nameTextView = findViewById(R.id.name_video);
        emailTextView = findViewById(R.id.email_aware);
        titleTextView = findViewById(R.id.title_of_article);
        briefTextView = findViewById(R.id.description);
        linkTextView = findViewById(R.id.article_link);
        save = findViewById(R.id.save_article);
        userId = user.getUid();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerArticleInfo();
            }
        });
    }

    private void registerArticleInfo(){
        if (TextUtils.isEmpty(nameTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter name!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(emailTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(titleTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter Title of the article!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(briefTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter brief description of your article!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(linkTextView.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter link to your article!!", Toast.LENGTH_LONG).show();
            return;
        }

        data_storage = FirebaseFirestore.getInstance();
        DocumentReference documentReference = data_storage.collection("articles").document();
        Log.v(Tag,"ya i am entering");
        push_data = new Article();
        set_data();
        documentReference.set(push_data);
        nameTextView.setText("");
        linkTextView.setText("");
        emailTextView.setText("");
        briefTextView.setText("");
        titleTextView.setText("");
        finish();
    }

    public void set_data(){
        push_data.setName(nameTextView.getText().toString());
        push_data.setEmail(emailTextView.getText().toString());
        push_data.setTitle(titleTextView.getText().toString());
        push_data.setDescription(briefTextView.getText().toString());
        push_data.setArticle_Link(linkTextView.getText().toString());
    }
}