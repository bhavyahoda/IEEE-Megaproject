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

import com.example.sentry.model.Hotel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

    public class HotelShow extends AppCompatActivity {
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    String Tag = "i am in";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_show);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.hotel_list_rec);
        Query query=firebaseFirestore.collection("hotels");
        Log.v(Tag,"query printed "+query.toString());
        FirestoreRecyclerOptions<Hotel> options=new FirestoreRecyclerOptions.Builder<Hotel>()
                .setQuery(query, Hotel.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Hotel, HotelShow.HotelsViewHolder>(options) {
            @NonNull
            @Override
            public HotelShow.HotelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_uploaded_show_row,parent,false);
                return new HotelShow.HotelsViewHolder(view);

            }
            @Override
            protected void onBindViewHolder(@NonNull HotelShow.HotelsViewHolder holder, int position, @NonNull Hotel model) {
                holder.ho_name.setText(model.getName());
                holder.ho_city_details.setText(model.getLocationDetail());
                holder.ho_loc_link.setText(model.getLocationLink());
                holder.ho_min_price.setText(model.getMinimumPrice());
                holder.ho_rating.setText(model.getRating());
                holder.ho_feature.setText(model.getFeatureDetail());
                Log.v(Tag,"entering view holder ");
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }
    private class HotelsViewHolder extends RecyclerView.ViewHolder{
        private TextView ho_name;
        private TextView ho_city_details;
        private TextView ho_loc_link;
        private TextView ho_min_price;
        private TextView ho_rating;
        private TextView ho_feature;
        Hotel push;
        public HotelsViewHolder(@NonNull View itemView) {
            super(itemView);
            ho_name=(TextView)itemView.findViewById(R.id.hotel_see_name);
            ho_city_details=(TextView)itemView.findViewById(R.id.hotel_see_c_c_s);
            ho_loc_link=(TextView)itemView.findViewById(R.id.hotel_see_link_loc);
            ho_min_price=(TextView)itemView.findViewById(R.id.hotel_see_min_price);
            ho_rating=(TextView)itemView.findViewById(R.id.hotel_see_rating);
            ho_feature=(TextView)itemView.findViewById(R.id.hotel_see_feature);
            push=new Hotel();
            push.setName(ho_name.getText().toString());
            push.setLocationDetail(ho_city_details.getText().toString());
            push.setLocationLink(ho_loc_link.getText().toString());
            push.setMinimumPrice(ho_min_price.getText().toString());
            push.setRating(ho_rating.getText().toString());
            push.setFeatureDetail(ho_feature.getText().toString());
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

