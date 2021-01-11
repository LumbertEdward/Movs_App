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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private static final int ITEM = 1;
    private static final int LOADING = 0;
    private boolean isLoadingAdded = false;
    private List<Popular> popularList;
    private List<Popular> popularListFiltered;
    private Context context;
    private selectedItemInformationDetailsInterface select;

    public PopularAdapter(Context context) {
        popularList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case ITEM:
                View v = inflater.inflate(R.layout.upcoming_item, parent, false);
                viewHolder = new PopularVH(v);
                break;
            case LOADING:
                View loading = inflater.inflate(R.layout.loading_progress, parent, false);
                viewHolder = new LoadingVH(loading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case ITEM:
                PopularVH popularVH = (PopularVH) holder;
                popularVH.txtTitle.setText(popularList.get(position).getTitle());
                popularVH.txtPopularity.setText(String.valueOf(popularList.get(position).getVote_average()));
                Picasso.Builder builder = new Picasso.Builder(context);
                builder.downloader(new OkHttp3Downloader(context));
                builder.build().load("https://image.tmdb.org/t/p/w342//" + popularList.get(position).getPoster_path())
                        .into(popularVH.imgPopular);
                popularVH.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select.getPopularSelectedItem(popularList.get(position));
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
        return popularList == null ? 0:popularList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == popularList.size() - 1 && isLoadingAdded) ? LOADING:ITEM;
    }

    protected class PopularVH extends RecyclerView.ViewHolder{
        ImageView imgPopular;
        TextView txtTitle;
        TextView txtPopularity;
        LinearLayout linearLayout;
        public PopularVH(@NonNull View itemView) {
            super(itemView);
            imgPopular = (ImageView) itemView.findViewById(R.id.imgUpcoming);
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
                List<Popular> populars = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    populars.addAll(popularListFiltered);
                }
                else {
                    String popularChar = constraint.toString().toLowerCase().trim();
                    for (Popular popular: popularListFiltered){
                        if (popular.getTitle().toLowerCase().contains(popularChar)){
                            populars.add(popular);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = populars;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                popularList.clear();
                popularList.addAll((List) results.values);
                notifyDataSetChanged();

            }
        };
    }
    public void add(Popular popular){
        popularList.add(popular);
        notifyItemInserted(popularList.size() - 1);
    }
    public void addAll(List<Popular> populars){
        for (Popular pop: populars){
            add(pop);
        }
        popularListFiltered = new ArrayList<>(populars);
    }
    public void remove(Popular popRemove){
        int position = popularList.indexOf(popRemove);
        if (position > -1){
            popularList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public Popular getItem(int position){
        return popularList.get(position);
    }
    public void addLoadingFooter(){
        isLoadingAdded = true;
        add(new Popular());
    }
    public void removeLoadingFooter(){
        isLoadingAdded = false;
        int position = popularList.size() - 1;
        Popular popularRemove = getItem(position);
        if (popularRemove != null){
            popularList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
