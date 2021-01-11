package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TopRatedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int ITEM = 1;
    private static final int LOADING = 0;
    private boolean isLoadingAdded = false;
    private List<TopRated> topRatedList;
    private List<TopRated> topRatedListFiltered;
    private selectedItemInformationDetailsInterface select;
    private Context context;

    public TopRatedAdapter(Context context) {
        topRatedList = new ArrayList<>();
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                View v = layoutInflater.inflate(R.layout.upcoming_item, parent, false);
                viewHolder = new TopRatedVH(v);
                break;
            case LOADING:
                View loading = layoutInflater.inflate(R.layout.loading_progress, parent, false);
                viewHolder = new LoadingVH(loading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                TopRatedVH topRatedVH = (TopRatedVH) holder;
                topRatedVH.txtTitle.setText(topRatedList.get(position).getTitle());
                topRatedVH.txtPopularity.setText(String.valueOf(topRatedList.get(position).getVote_average()));
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load("https://image.tmdb.org/t/p/w342//" + topRatedList.get(position).getPoster_path())
                        .into(topRatedVH.imageView);
                topRatedVH.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select.getTopRatedItem(topRatedList.get(position));
                    }
                });
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
        select = (selectedItemInformationDetailsInterface) context;
    }


    @Override
    public int getItemCount() {
        return topRatedList == null ? 0: topRatedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == topRatedList.size() - 1 && isLoadingAdded) ? LOADING:ITEM;
    }

    protected class TopRatedVH extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtPopularity;
        private LinearLayout linearLayout;
        public TopRatedVH(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgUpcoming);
            txtTitle = (TextView) itemView.findViewById(R.id.titleUpcoming);
            txtPopularity = (TextView) itemView.findViewById(R.id.UpcomingPopularity);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearUpcoming);
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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<TopRated> topRateds = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    topRateds.addAll(topRatedListFiltered);
                }
                else {
                    String topChar = constraint.toString().toLowerCase().trim();
                    for (TopRated topRated: topRatedListFiltered){
                        if (topRated.getTitle().toLowerCase().contains(topChar)){
                            topRateds.add(topRated);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = topRateds;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                topRatedList.clear();
                topRatedList.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
    }
    public void add(TopRated topRated){
        topRatedList.add(topRated);
        notifyItemInserted(topRatedList.size() - 1);
    }
    public void addAll(List<TopRated> topRateds){
        for (TopRated top: topRateds){
            add(top);
        }
        topRatedListFiltered = new ArrayList<>(topRateds);
    }
    public void remove(TopRated topR){
        int position = topRatedList.indexOf(topR);
        if (position > -1){
            topRatedList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public TopRated getItem(int position){
        return topRatedList.get(position);
    }
    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new TopRated());
    }
    public void removeLoadingFooter(){
        isLoadingAdded = false;
        int position = topRatedList.size() - 1;
        TopRated tP = getItem(position);
        if (tP != null){
            topRatedList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
