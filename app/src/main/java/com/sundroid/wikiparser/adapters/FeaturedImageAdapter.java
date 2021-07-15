package com.sundroid.wikiparser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.models.FeaturedImage;

import java.util.List;

public class FeaturedImageAdapter extends RecyclerView.Adapter<FeaturedImageAdapter.MyViewHolder> {
    private Context context;
    private List<FeaturedImage> flist;
    CircularProgressDrawable circularProgressDrawable;

    public FeaturedImageAdapter(Context context, List<FeaturedImage> flist){
        this.context= context;
        this.flist = flist;
        circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.featured_adapter,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.image_title.setText(flist.get(position).getTitle());

        Glide.with(context)
                .load(flist.get(position).getImageinfo().get(0).getUrl())
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_sync)
                .into(holder.featured_image);
    }

    @Override
    public int getItemCount() {
        if(flist != null ){
            return flist.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView image_title;
        ImageView featured_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_title = itemView.findViewById(R.id.image_title);
            featured_image = itemView.findViewById(R.id.featured_image);
        }
    }
}
