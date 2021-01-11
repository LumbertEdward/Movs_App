package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int ITEM = 1;
    private static final int LOADING = 0;
    private boolean isLoadingAdded = false;
    private List<NowPlaying> nowPlayingList;
    private List<NowPlaying> nowPlayingListFiltered;
    private Context context;
    private static final int FADE_DURATION = 1000;
    private selectedItemInformationDetailsInterface detailsInterface;

    public SearchAdapter(Context context) {
        this.context = context;
        nowPlayingList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                View v = layoutInflater.inflate(R.layout.search_item, parent, false);
                viewHolder = new NowShowingVH(v);
                break;
            case LOADING:
                View load = layoutInflater.inflate(R.layout.loading_progress, parent, false);
                viewHolder = new LoadingVH(load);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM:
                NowShowingVH nowShowingVH = (NowShowingVH) holder;
                nowShowingVH.title.setText(nowPlayingList.get(position).getTitle());
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load("https://image.tmdb.org/t/p/w342//" + nowPlayingList.get(position).getPoster_path()).into(nowShowingVH.imageView);
                nowShowingVH.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detailsInterface.getNowShowingSelectedItemInformation(nowPlayingList.get(position));
                    }
                });
                scaleAnim(nowShowingVH.itemView);
                break;
            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                loadingVH.progressBar.setVisibility(View.VISIBLE);
                break;
        }

    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        detailsInterface = (selectedItemInformationDetailsInterface) context;
    }

    protected class NowShowingVH extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        LinearLayout linearLayout;
        public NowShowingVH(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtSearch);
            imageView = (ImageView) itemView.findViewById(R.id.imgSearch);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearSearch);
        }
    }
    protected class LoadingVH extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingVH(@NonNull View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressAll);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return (position == nowPlayingList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

        @Override
    public int getItemCount() {
        return nowPlayingList == null ? 0:nowPlayingList.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<NowPlaying> mFilter = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    mFilter.addAll(nowPlayingListFiltered);
                }
                else {
                    String mCharacter = constraint.toString().toLowerCase().trim();
                    for (NowPlaying nowPlaying: nowPlayingListFiltered){
                        if (nowPlaying.getTitle().toLowerCase().contains(mCharacter)){
                            mFilter.add(nowPlaying);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nowPlayingList.clear();
                nowPlayingList.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
    }



    public void add(NowPlaying playing){
        nowPlayingList.add(playing);
        notifyItemInserted(nowPlayingList.size() - 1);
    }
    public void addAll(List<NowPlaying> playings){
        for (NowPlaying nP: playings){
            nowPlayingList.add(nP);
        }
        nowPlayingListFiltered = new ArrayList<>(playings);
    }
    public void remove(NowPlaying playingsNow){
        int position = nowPlayingList.indexOf(playingsNow);
        if (position > -1){
            nowPlayingList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public NowPlaying getItem(int position){
        return nowPlayingList.get(position);
    }
    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new NowPlaying());
    }
    public void removeLoadingFooter(){
        isLoadingAdded = false;
        int position = nowPlayingList.size() - 1;
        NowPlaying now = getItem(position);
        if (now != null){
            nowPlayingList.remove(position);
            notifyItemRemoved(position);
        }

    }
    private void scaleAnim(View view){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(FADE_DURATION);
        view.startAnimation(scaleAnimation);
    }
}
