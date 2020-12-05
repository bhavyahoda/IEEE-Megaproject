package com.example.sentry;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelfDefenseAdaptor extends RecyclerView.Adapter<SelfDefenseAdaptor.MyViewHolder> {
    String info1[],info2[],info3[];
    int photo[];
    String Tag="hi i am there";
    Context context;

    //constructor
    public SelfDefenseAdaptor(Context ct, String vid_title[], String vid_description[], String vid_links[], int selfDefenseImages[])
    {
        context=ct;
        info1=vid_title;
        info2=vid_description;
        info3=vid_links;
        photo=selfDefenseImages;
    }

    //creating view here in the view we are calling elements from thr view holder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //layout inflater creates view
        LayoutInflater inflater = LayoutInflater.from(context);
        //we are setting that the view will be what layout we have created
        View view=inflater.inflate(R.layout.selfdefenserow,parent,false);
        //here the references  of the elements in each row is added
        return new MyViewHolder(view);
    }

    //references passed from on create to here where the objects are created data is feaded here
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.vid_title.setText(info1[position]);
        holder.vid_description.setText(info2[position]);
        holder.vid_link.setText("Click It");
        holder.self_vid_image.setImageResource(photo[position]);
        holder.vid_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(info3[position]));
                context.startActivity(browseIntent);
                Log.v(Tag, info3[position]);
            }
        });
        Log.v(Tag,"I entered on view holder");
    }

    @Override
    public int getItemCount() {

        return info1.length;
    }
//the elements in the xml file have reference are taken here
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vid_title, vid_link,vid_description;
        ImageView self_vid_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            vid_title=itemView.findViewById(R.id.vid_title);
            vid_description=itemView.findViewById(R.id.vid_description);
            vid_description.setMovementMethod(new ScrollingMovementMethod());
            vid_link=itemView.findViewById(R.id.self_vid_link);
            //vid_link.setMovementMethod(LinkMovementMethod.getInstance());
            self_vid_image=itemView.findViewById(R.id.self_vid_image);
        }
    }
}
