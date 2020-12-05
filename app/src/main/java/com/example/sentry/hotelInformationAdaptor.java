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

public class hotelInformationAdaptor  extends RecyclerView.Adapter<hotelInformationAdaptor.MyViewHolder> {
    String data1[],data2[],data3[],data4[],data5[],data6[],data7[],data8[];
    int Images[];
    String Tag="hotel single row";
    Context context;


    public hotelInformationAdaptor(Context ct, String hotel_name[], String hotel_rating[], String hotel_price[], String location[],String hotel_details[],String City_name[],String hotel_number[],String link[], int img[]){
        context=ct;
        data1=hotel_name;
        data2=hotel_rating;
        data3=hotel_price;
        data4=location;
        data5=link;
        data6=hotel_details;
        data7=City_name;
        data8=hotel_number;
        Images=img;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //we are setting that the view will be what layout we have created
        View view=inflater.inflate(R.layout.hotel_row,parent,false);
        return new hotelInformationAdaptor.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.hotel_title.setText(data1[position]);
        //holder.hotel_rating.setText(data2[position]);
        //holder.vid_description.setMovementMethod(new ScrollingMovementMethod());
        //holder.hotel_price.setText(data3[position]);
        holder.hotel_city.setText(data7[position]);
        //holder.link.setText(data5[position]);
        //holder.vid_link.setMovementMethod(LinkMovementMethod.getInstance());
        holder.hotel_img.setImageResource(Images[position]);
        //setting listener
        holder.law_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(Tag,"i am entering button");
                Intent intent =new Intent(context, HotelDetailPresent.class);
                intent.putExtra("data1",data1[position]);
                intent.putExtra("data2",data2[position]);
                intent.putExtra("data3",data3[position]);
                intent.putExtra("data4",data4[position]);
                intent.putExtra("data5",data5[position]);
                intent.putExtra("data6",data6[position]);
                intent.putExtra("data8",data8[position]);
                intent.putExtra("myImageView",Images[position]);

                //intent is starting
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView hotel_title,hotel_price,hotel_rating,location,link,hotel_de,hotel_num,hotel_city;
        ImageView hotel_img;
        ConstraintLayout law_layout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotel_title=itemView.findViewById(R.id.hotel_title);
            //hotel_price=itemView.findViewById(R.id.hotel_price);
            //hotel_rating=itemView.findViewById(R.id.hotel_rating);
            hotel_city=itemView.findViewById(R.id.hotel_location);
            //link=itemView.findViewById(R.id.link_to_see_more);
            hotel_img=itemView.findViewById(R.id.hotel_img);
            law_layout = itemView.findViewById(R.id.hotelMainLayout);
        }
    }
}
