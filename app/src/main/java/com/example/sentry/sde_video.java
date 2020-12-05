package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class sde_video extends AppCompatActivity {
    private RecyclerView recyclerView;
    String Tag ="my recycler data";
    String vid_title[],vid_description[],vid_links[];
    int selfDefenseImages[]={R.drawable.active_self_protection,R.drawable.aja_dang,R.drawable.ando_mierzwa,R.drawable.contact_combat_india,
            R.drawable.final_round_training,R.drawable.gracie_breakdown,
            R.drawable.janice_huang,R.drawable.lex_fitness,R.drawable.master_shailash,R.drawable.master_wong,
            R.drawable.oren_mellul,R.drawable.pib_india,R.drawable.special_training_service};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sde_video);
        recyclerView = findViewById(R.id.rec_view);
        vid_title=getResources().getStringArray(R.array.video_title);
        vid_description=getResources().getStringArray(R.array.video_description);
        vid_links=getResources().getStringArray(R.array.the_link);
        Log.v(Tag,"The code working again "+vid_links[0]);
        SelfDefenseAdaptor self_defense_adaptor =new SelfDefenseAdaptor(this,vid_title,vid_description,vid_links,selfDefenseImages);
        recyclerView.setAdapter(self_defense_adaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}