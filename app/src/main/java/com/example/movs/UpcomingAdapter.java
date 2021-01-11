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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int ITEM = 1;
    private static final int LOADING = 0;
    private boolean isLoadingAdded = false;
    private List<Upcoming> upcomingList;
    private Context context;
    private List<Upcoming> upcomingListFiltered;
    private selectedItemInformationDetailsInterface select;

    public UpcomingAdapter(Context context) {
        upcomingList = new ArrayList<>();
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
                viewHolder = new UpcomingVH(v);
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
        switch (getItemViewType(position)){
            case ITEM:
                UpcomingVH upcomingVH = (UpcomingVH) holder;
                upcomingVH.txtTitleUpcoming.setText(upcomingList.get(position).getTitle());
                upcomingVH.popularity.setText(String.valueOf(upcomingList.get(position).getVote_average()));
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load("https://image.tmdb.org/t/p/w342//" + upcomingList.get(position).getPoster_path())
                        .into(upcomingVH.imageView);
                upcomingVH.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select.getUpcomingSelectedItem(upcomingList.get(position));
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
        return upcomingList == null ? 0:upcomingList.size();
    }



    @Override
    public int getItemViewType(int position) {
        return (position == upcomingList.size() - 1 && isLoadingAdded) ? LOADING:ITEM;
    }

    protected class UpcomingVH extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitleUpcoming;
        TextView popularity;
        LinearLayout linearLayout;
        public UpcomingVH(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgUpcoming);
            txtTitleUpcoming = (TextView) itemView.findViewById(R.id.titleUpcoming);
            popularity = (TextView) itemView.findViewById(R.id.UpcomingPopularity);
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
                List<Upcoming> uFilter = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    uFilter.addAll(upcomingListFiltered);
                }
                else {
                    String charUpcoming = constraint.toString().toLowerCase().trim();
                    for (Upcoming upcoming: upcomingListFiltered){
                        if (upcoming.getTitle().toLowerCase().contains(charUpcoming)){
                            uFilter.add(upcoming);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = uFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                upcomingList.clear();
                upcomingList.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
    }
    public void add(Upcoming upcoming){
        upcomingList.add(upcoming);
        notifyItemInserted(upcomingList.size() - 1);
    }
    public void addAll(List<Upcoming> upcomings){
        for (Upcoming up: upcomings){
            add(up);
        }
        upcomingListFiltered = new ArrayList<>(upcomings);

    }
    public void remove(Upcoming upR){
        int position = upcomingList.indexOf(upR);
        if (position > -1){
            upcomingList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Upcoming getItem(int position){
        return upcomingList.get(position);
    }
    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new Upcoming());
    }
    public void removeLoadingFooter(){
        isLoadingAdded = false;
        int position = upcomingList.size() - 1;
        Upcoming upcomingR = getItem(position);
        if (upcomingR != null){
            upcomingList.remove(position);
            notifyItemRemoved(position);
        }
    }

}
