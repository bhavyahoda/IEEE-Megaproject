package com.example.sentry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sentry.model.SelfDefense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SelfDefenseShow extends AppCompatActivity {
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    String click_link;
    String Tag = "i am in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_defense_show_class);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.self_defense_list_rec);
        Query query=firebaseFirestore.collection("self_defense");
        Log.v(Tag,"query printed "+query.toString());
        FirestoreRecyclerOptions<SelfDefense> options=new FirestoreRecyclerOptions.Builder<SelfDefense>()
                .setQuery(query, SelfDefense.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SelfDefense, SelfDefenseShow.SelfDefenseViewHolder>(options) {
            @NonNull
            @Override
            public SelfDefenseShow.SelfDefenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.self_defense_uploaded_show_row,parent,false);
                return new SelfDefenseShow.SelfDefenseViewHolder(view);

            }
            @Override
            protected void onBindViewHolder(@NonNull SelfDefenseShow.SelfDefenseViewHolder holder, int position, @NonNull SelfDefense model) {
                holder.sd_name.setText(model.getName());
                holder.sd_about.setText(model.getAbout());
                holder.sd_reference.setText(model.getWork());
                holder.sd_number.setText(model.getPhone());
                holder.sd_loc_link.setText("click to get the link for google class/google maps for location");
                click_link=model.getLink();
                Log.v(Tag,"the link is"+click_link);
                holder.sd_loc_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(click_link));
                        startActivity(in);
                    }
                });
                holder.sd_date.setText(model.getDate());
                holder.sd_fees.setText(model.getPrice());
                Log.v(Tag,"entering view holder ");
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }
    private class SelfDefenseViewHolder extends RecyclerView.ViewHolder{
        private TextView sd_name;
        private TextView sd_about;
        private TextView sd_reference;
        private TextView sd_number;
        private TextView sd_loc_link;
        private TextView sd_date;
        private TextView sd_fees;
        SelfDefense push;
        public SelfDefenseViewHolder(@NonNull View itemView) {
            super(itemView);
            sd_name = (TextView) itemView.findViewById(R.id.self_def_name_see);
            sd_about = (TextView) itemView.findViewById(R.id.self_def_about_see);
            sd_reference = (TextView) itemView.findViewById(R.id.self_def_reference_see);
            sd_number = (TextView) itemView.findViewById(R.id.sd_see_number);
            sd_loc_link = (TextView) itemView.findViewById(R.id.self_def_loc_link_see);
            sd_date = (TextView) itemView.findViewById(R.id.self_def_date_see);
            sd_fees = (TextView) itemView.findViewById(R.id.self_date_def_fees);
            push = new SelfDefense();
            push.setName(sd_name.getText().toString());
            push.setAbout(sd_about.getText().toString());
            push.setWork(sd_reference.getText().toString());
            push.setPhone(sd_number.getText().toString());
            push.setLink(sd_loc_link.getText().toString());
            push.setDate(sd_date.getText().toString());
            push.setPrice(sd_fees.getText().toString());

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