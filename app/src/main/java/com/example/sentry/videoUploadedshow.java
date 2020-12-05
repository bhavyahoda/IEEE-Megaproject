package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sentry.model.Videos;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VideoUploadedshow extends AppCompatActivity {

    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    String Tag = "i am in";
    TextView video_name;
    TextView video_qualification;
    TextView video_fees;
    TextView video_topic;
    TextView video_time;
    TextView video_link;
    TextView video_reason_to_attend;
    //Videos push;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_uploadedshow);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.video_list_rec);
        Query query=firebaseFirestore.collection("counselling");
        Log.v(Tag,"query printed "+query.toString());
        FirestoreRecyclerOptions<Videos>options=new FirestoreRecyclerOptions.Builder<Videos>()
                .setQuery(query, Videos.class)
                .build();


            adapter = new FirestoreRecyclerAdapter<Videos, VideoUploadedshow.VideosViewHolder>(options) {
                @NonNull
                @Override
                public VideoUploadedshow.VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_uploaded_show_row,parent,false);
                    return new VideoUploadedshow.VideosViewHolder(view);

                }

                @Override
                protected void onBindViewHolder(@NonNull VideoUploadedshow.VideosViewHolder holder, int position, @NonNull Videos model) {
                    holder.vi_name.setText(model.getName());
                    holder.vi_qualification.setText(model.getQualification());
                    holder.vi_fees.setText(model.getPrice());
                    holder.vi_topic.setText(model.getTopic());
                    holder.vi_time.setText(model.getTime());
                    holder.vi_link.setText(model.getLink());
                    holder.vi_reason_to_attend.setText(model.getReason());
                    Log.v(Tag,"entering view holder ");
                }
            };
            mFirestoreList.setHasFixedSize(true);
            mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
            mFirestoreList.setAdapter(adapter);

        }

        private class VideosViewHolder extends RecyclerView.ViewHolder{
            private TextView vi_name;
            private TextView vi_qualification;
            private TextView vi_fees;
            private TextView vi_topic;
            private TextView vi_time;
            private TextView vi_link;
            private TextView vi_reason_to_attend;
            Videos push;
            public VideosViewHolder(@NonNull View itemView) {
                super(itemView);
                vi_name=(TextView)itemView.findViewById(R.id.video_name);
                vi_qualification=(TextView)itemView.findViewById(R.id.qualification);
                vi_fees=(TextView)itemView.findViewById(R.id.fees);
                vi_topic=(TextView)itemView.findViewById(R.id.video_topic);
                vi_time=(TextView)itemView.findViewById(R.id.video_time);
                vi_link=(TextView)itemView.findViewById(R.id.video_link);
                vi_reason_to_attend=(TextView)itemView.findViewById(R.id.video_reason);
                push=new Videos();
                push.setName(vi_name.getText().toString());
                push.setQualification(vi_qualification.getText().toString());
                push.setPrice(vi_fees.getText().toString());
                push.setTopic(vi_topic.getText().toString());
                push.setTime(vi_time.getText().toString());
                push.setLink(vi_link.getText().toString());
                push.setReason(vi_reason_to_attend.getText().toString());
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