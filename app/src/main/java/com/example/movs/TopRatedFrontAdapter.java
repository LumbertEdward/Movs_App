package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopRatedFrontAdapter extends RecyclerView.Adapter<TopRatedFrontAdapter.MyViewHolder> {
    private List<TopRated> topRatedList;
    private Context context;
    private selectedItemInformationDetailsInterface select;

    public TopRatedFrontAdapter(List<TopRated> topRatedList, Context context) {
        this.topRatedList = topRatedList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.upcoming_front_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(topRatedList.get(position).getTitle());
        holder.rate.setText(String.valueOf(topRatedList.get(position).getVote_average()));
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://image.tmdb.org/t/p/w342//" + topRatedList.get(position).getPoster_path()).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getTopRatedItem(topRatedList.get(position));
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
        return topRatedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private ImageView imageView;
        private TextView textView;
        private TextView rate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.titleUpcomingFront);
            rate = (TextView) itemView.findViewById(R.id.UpcomingPopularityFront);
            imageView = (ImageView) itemView.findViewById(R.id.imgUpcomingFront);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.up_front);
        }
    }
}
