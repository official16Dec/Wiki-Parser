package com.sundroid.wikiparser.adapters;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sundroid.wikiparser.R;
import com.sundroid.wikiparser.models.Content;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    private Context context;
    private List<Content> clist;

    public ArticleAdapter(Context context, List<Content> clist){
        this.context= context;
        this.clist = clist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.article_adapter,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.article_title.setText((position+1)+". "+clist.get(position).getTitle());
        Spanned sp = Html.fromHtml(clist.get(position).getRevisions().get(0).getContentText());
        holder.article_info.setText(sp);
    }

    @Override
    public int getItemCount() {
        if(clist != null ){
            return clist.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView article_title, article_info;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            article_title = itemView.findViewById(R.id.article_title);
            article_info = itemView.findViewById(R.id.article_info);
        }
    }
}
