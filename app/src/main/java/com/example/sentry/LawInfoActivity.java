package com.example.sentry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

public class LawInfoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    //Button bt_law;
    String sec_code[],sec_name[];
    String Tag ="myrecyclerdata";
    int images[]={R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img,R.drawable.law_img};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law_info);
        //bt_law = findViewById(R.id.)
        recyclerView = findViewById(R.id.recycler_view);
        sec_code=getResources().getStringArray(R.array.Section_code_number);
        Log.v(Tag,"The code working ");
        sec_name=getResources().getStringArray(R.array.Section_heading);
        Log.v(Tag,"The code working again ");

        //creating my adapter object
        //passing the parameter first is the context which is main activity therefor we type this
        LawInfoAdapter LawInfoAdapter=new LawInfoAdapter(this,sec_code,sec_name,images);
        //setting my adaptor
        recyclerView.setAdapter(LawInfoAdapter);
        //layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}