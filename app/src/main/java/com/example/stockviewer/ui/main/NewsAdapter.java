package com.example.stockviewer.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stockviewer.R;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private static final String TAG ="NewsAdapter" ;
    private News news;
    private Context context;

    public NewsAdapter(){

    }
    public NewsAdapter(News news, Context context){
        this.news = news;
        this.context = context;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_viewer,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        final Article a = get(position);

        DateTime dt = DateTime.parse(a.getPublishedAt(), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        String date = dt.toString("d MMMM, yyyy HH:mm a");
        holder.dateTv.setText(date);
        holder.descriptionTv.setText(a.getDescription());
        holder.sourceTv.setText(a.getSource().getName());
        holder.urlTv.setText(a.getUrl());
        holder.titleTv.setText(a.getTitle());

      // Glide.with(context)
        //        .asBitmap()
          //      .load(a.getUrlToImage())
            //    .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(news==null)
            return 0;
        else {
            Log.d(TAG, "count= " + news.getArticles().size());
            return news.getArticles().size();
        }
    }


    public Article get(int position){
        return news.getArticles().get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView titleTv;
        @BindView(R.id.url)
        TextView urlTv;
        @BindView(R.id.date)
        TextView dateTv;
        @BindView(R.id.source)
        TextView sourceTv;
        @BindView(R.id.description)
        TextView descriptionTv;
        @BindView(R.id.image)
        ImageView imageView;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}