package com.example.movs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailsGenresAdapter extends RecyclerView.Adapter<DetailsGenresAdapter.MyViewHolder> {
    private List<Genres> genresList;
    private Context context;
    private selectedItemInformationDetailsInterface select;

    public DetailsGenresAdapter(List<Genres> genresList, Context context) {
        this.genresList = genresList;
        this.context = context;
    }

    @NonNull
    @Override
    public DetailsGenresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.detail_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsGenresAdapter.MyViewHolder holder, int position) {
        holder.btn.setText(genresList.get(position).getName());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getGenre(genresList.get(position));
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
        return genresList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button btn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn = (Button) itemView.findViewById(R.id.genre1);
        }
    }
}
