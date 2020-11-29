package com.example.sentry;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//passing  parameter and in that creating a inner class
public class LawInfoAdapter  extends RecyclerView.Adapter<LawInfoAdapter.MyViewHolder> {

    String data1[],data2[];
    int images[];
    String  Tag="Law info activity";
    Context context;
    //adding constructor with 4 parameters

    public LawInfoAdapter(Context ct,String sec_code[],String sec_name[],int img[]){
        context=ct;
        data1=sec_code;
        data2=sec_name;
        images=img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //we are setting that the view will be what layout we have created
        View view=inflater.inflate(R.layout.my_row_law_adapter,parent,false);
        return new MyViewHolder(view);
    }
    //here it is communicating with my view holder class
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //dynamically adding the data
        holder.section_code.setText(data1[position]);
        holder.section_heading.setText(data2[position]);
        holder.law_image.setImageResource(images[position]);
        //setting listener
        holder.law_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Tag,"i am entering button");
                Intent intent =new Intent(context,LawDetailActivity.class);
                intent.putExtra("data1",data1[position]);
                intent.putExtra("data2",data2[position]);
                intent.putExtra("myImageView",images[position]);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //creating a constructor
        //we are receiving view from the above
        TextView section_code, section_heading;
        ImageView law_image;
        ConstraintLayout law_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            section_code = itemView.findViewById(R.id.sec_code);
            section_heading = itemView.findViewById(R.id.sec_heading);
            law_image = itemView.findViewById(R.id.law_img_1);
            law_layout = itemView.findViewById(R.id.la_main_layout);
        }
    }
}
