package com.example.movs;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private String key = "AIzaSyCEuBhYMutH-fNlaiJgWNH96NNDhjep8uk";
    private String url = "https://www.youtube.com/watch?v=";
    private YoutubeInterface youtubeInterface;
    private List<TrailersInfo> trailersInfoList;
    Context context;

    public TrailerAdapter(List<TrailersInfo> trailersInfoList, Context context) {
        this.trailersInfoList = trailersInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.trailer_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(trailersInfoList.get(position).getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeInterface.getYouTubeVideo(trailersInfoList.get(position));
            }
        });
        holder.youTubeThumbnailView.initialize(key, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(trailersInfoList.get(position).getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Toast.makeText(context, "Error loading", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(context, "Error loading thumbnail", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        youtubeInterface = (YoutubeInterface) context;
    }

    @Override
    public int getItemCount() {
        return trailersInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private YouTubeThumbnailView youTubeThumbnailView;
        private TextView name;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.thumbnail);
            name = (TextView) itemView.findViewById(R.id.trailerHeading);
            cardView = (CardView) itemView.findViewById(R.id.cardTrailer);
        }
    }
}
