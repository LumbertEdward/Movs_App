package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class favouritesAdapter extends RecyclerView.Adapter<favouritesAdapter.MyViewHolder> {
    private List<NowPlaying> nowPlayingList;
    private List<NowPlaying> nowPlayingListFiltered;
    Context context;
    private selectedItemInformationDetailsInterface detailsInterface;

    public favouritesAdapter(List<NowPlaying> nowPlayingList, Context context) {
        this.nowPlayingList = nowPlayingList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.now_showing_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(nowPlayingList.get(position).getTitle());
        holder.rate.setText(String.valueOf(nowPlayingList.get(position).getPopularity()));
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://image.tmdb.org/t/p/w342//" + nowPlayingList.get(position).getPoster_path())
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsInterface.getNowShowingSelectedItemInformation(nowPlayingList.get(position));
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        detailsInterface = (selectedItemInformationDetailsInterface) context;
    }

    @Override
    public int getItemCount() {
        return nowPlayingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView rate;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titleShowing);
            rate = (TextView) itemView.findViewById(R.id.popularity);
            imageView = (ImageView) itemView.findViewById(R.id.imgNow);
            cardView = (CardView) itemView.findViewById(R.id.cardShowing);
        }
    }
}
