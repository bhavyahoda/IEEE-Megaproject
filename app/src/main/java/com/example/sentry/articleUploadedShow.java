package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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
    String Tag = "i am in";
    String click_link;
    //Article push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_uploaded_show);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.firestore_list);
        Query query=firebaseFirestore.collection("articles");
        Log.v(Tag,"query printed "+query.toString());
        FirestoreRecyclerOptions<Article>options=new FirestoreRecyclerOptions.Builder<Article>()
                .setQuery(query, Article.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Article, ArticlesViewHolder>(options) {
            @NonNull
            @Override
            public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.article_uploaded_show_row,parent,false);
                return new ArticlesViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position, @NonNull Article model) {
                    holder.article_name.setText((model.getName()).toUpperCase());
                    holder.article_name.setTextSize(TypedValue.COMPLEX_UNIT_SP,35f);
                    holder.article_name.setTextColor(Color.parseColor("#000000"));
                    holder.article_name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    holder.article_email.setText(model.getEmail()+"");
                    holder.article_email.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
                    holder.article_email.setTextColor(Color.parseColor("#000000"));
                    holder.article_title.setText(model.getTitle());
                    holder.article_title.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f);
                    holder.article_title.setTextColor(Color.parseColor("#000000"));
                    holder.article_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    holder.article_brief.setText(model.getDescription());
                    holder.article_brief.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
                    holder.article_brief.setTextColor(Color.parseColor("#000000"));
                    holder.article_link.setText(Html.fromHtml("<u>CLICK TO READ THE WHOLE ARTICLE!</u>"));
                    holder.article_link.setTextColor(Color.parseColor("#00FFFF"));
                    holder.article_link.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
                    holder.article_link.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
               // holder.vid_link.setText(Html.fromHtml("<u>CLICK TO SEE!</u>"));
                    click_link=model.getArticle_Link();
                    Log.v(Tag,"the link is"+click_link);
                    holder.article_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(click_link));
                                        startActivity(in);
                                    }
                            });
                Log.v(Tag,"entering view holder ");
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
    }
    private class ArticlesViewHolder extends RecyclerView.ViewHolder{
        private TextView article_name;
        private TextView article_email;
        private TextView article_title;
        private TextView article_brief;
        private TextView article_link;
        Article push;
        public ArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            article_name=(TextView)itemView.findViewById(R.id.article_up_name);
            article_link=(TextView)itemView.findViewById(R.id.article_up_link);
            article_brief=(TextView)itemView.findViewById(R.id.article_up_Brief_Description);
            article_email=(TextView)itemView.findViewById(R.id.article_up_email);
            article_title=(TextView)itemView.findViewById(R.id.article_up_title);
            push=new Article();
            push.setName(article_name.getText().toString());
            push.setEmail(article_email.getText().toString());
            push.setTitle(article_title.getText().toString());
            push.setDescription(article_brief.getText().toString());
            push.setArticle_Link(article_link.getText().toString());
            Log.v(Tag,"printing brief"+article_brief);
            Log.v(Tag,"printing title"+article_title);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.v(Tag,"entering on stop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        Log.v(Tag,"entering on start");
    }
    }


