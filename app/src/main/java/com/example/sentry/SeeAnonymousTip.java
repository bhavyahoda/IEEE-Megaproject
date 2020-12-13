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
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sentry.model.AnonymousTip;
import com.example.sentry.model.SelfDefense;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SeeAnonymousTip extends AppCompatActivity {
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    String Tag = "i am in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_anonymous_tip);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.tip_list_rec);
        Query query=firebaseFirestore.collection("tip");
        Log.v(Tag,"query printed "+query.toString());
        FirestoreRecyclerOptions<AnonymousTip> options=new FirestoreRecyclerOptions.Builder<AnonymousTip>()
                .setQuery(query, AnonymousTip.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<AnonymousTip,SeeAnonymousTip.TipViewHolder>(options) {
            @NonNull
            @Override
            public SeeAnonymousTip.TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_update_row,parent,false);
                return new SeeAnonymousTip.TipViewHolder(view);

            }
            @Override
            protected void onBindViewHolder(@NonNull SeeAnonymousTip.TipViewHolder holder, int position, @NonNull AnonymousTip model) {
                holder.tip_loc.setText((model.getTipLocation()).toUpperCase());
                holder.tip_loc.setTextSize(TypedValue.COMPLEX_UNIT_SP,35f);
                holder.tip_loc.setTextColor(Color.parseColor("#000000"));
                holder.tip_loc.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                holder.tip_sub.setText((model.getTipTopic()).toUpperCase());
                holder.tip_sub.setTextSize(TypedValue.COMPLEX_UNIT_SP,35f);
                holder.tip_sub.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                holder.tip_sub.setTextColor(Color.parseColor("#000000"));
                holder.tip_description.setText(model.getTipDescription());
                holder.tip_description.setTextSize(TypedValue.COMPLEX_UNIT_SP,25f);
                holder.tip_description.setTextColor(Color.parseColor("#000000"));
                Log.v(Tag,"entering view holder ");
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }
    public class TipViewHolder extends RecyclerView.ViewHolder{
        private TextView tip_loc;
        private TextView tip_sub;
        private TextView tip_description;
        AnonymousTip push;
        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            tip_loc = (TextView) itemView.findViewById(R.id.tip_location);
            tip_sub = (TextView) itemView.findViewById(R.id.tip_subject);
            tip_description = (TextView) itemView.findViewById(R.id.tip_see_description);
            push = new AnonymousTip();
            push.setTipLocation(tip_loc.getText().toString());
            push.setTipTopic(tip_sub.getText().toString());
            push.setTipDescription(tip_description.getText().toString());
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