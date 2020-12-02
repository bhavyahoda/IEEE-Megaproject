package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sentry.model.Article;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class articleUploadedShow extends AppCompatActivity {

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_uploaded_show);
        firebaseFirestore=FirebaseFirestore.getInstance();
        mFirestoreList=findViewById(R.id.firestore_list);
        Query query=firebaseFirestore.collection("users");
        FirestoreRecyclerOptions<Article>options=new FirestoreRecyclerOptions.Builder<Article>()
                .setQuery(query, Article.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Article, ArticlesViewHolder>(options) {
            @NonNull
            @Override
            public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_tvac_studio,parent,false);
                return new ArticlesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position, @NonNull Article model) {
                    holder.list_name.setText(model.getName());
                    holder.list_price.setText(model.getEmail()+"");
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }

    private class ArticlesViewHolder extends RecyclerView.ViewHolder{
        private TextView list_name;
        private TextView list_price;
        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name=findViewById(R.id.list_name);
            list_price=findViewById(R.id.list_price);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}