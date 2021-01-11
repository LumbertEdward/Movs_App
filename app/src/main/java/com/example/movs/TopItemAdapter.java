package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopItemAdapter extends RecyclerView.Adapter<TopItemAdapter.MyViewHolder> {
    private List<NowPlaying> nowPlayingList;
    private Context context;
    private selectedItemInformationDetailsInterface select;

    public TopItemAdapter(List<NowPlaying> nowPlayingList, Context context) {
        this.nowPlayingList = nowPlayingList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.top_now_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://image.tmdb.org/t/p/w342//" + nowPlayingList.get(position).getPoster_path()).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getNowShowingSelectedItemInformation(nowPlayingList.get(position));
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        select = (selectedItemInformationDetailsInterface) context;
    }

    @Override
    public int getItemCount() {
        return nowPlayingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardTopItem);
            imageView = (ImageView) itemView.findViewById(R.id.imgFullTop);
        }
    }
}
