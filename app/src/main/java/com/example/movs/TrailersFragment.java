package com.example.movs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movs.AllConstants.Constants.api_key;

public class TrailersFragment extends Fragment {
    private static final String language = "en-US";
    private VideoView videoView;
    private TextView textView;
    private MediaController mediaController;
    private TrailersInterface trailersInterface;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int id;
    private ImageView imgBack;
    private ProgressBar progressBar;
    private selectedItemInformationDetailsInterface select;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            id = bundle.getInt("MyId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trailers, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.trailerRecycler);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressTrailers);
        progressBar.setVisibility(View.VISIBLE);
        imgBack = (ImageView) v.findViewById(R.id.getBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onBackPressed();
            }
        });
        getTrailer();
        return v;
    }

    private void getTrailer() {
        trailersInterface = TrailersRetrofit.getTrailersRetrofit().create(TrailersInterface.class);
        Call<TrailersAll> trailersCall = trailersInterface.getTrailers(id, api_key, language);
        trailersCall.enqueue(new Callback<TrailersAll>() {
            @Override
            public void onResponse(Call<TrailersAll> call, Response<TrailersAll> response) {
                progressBar.setVisibility(View.GONE);
                getData(response.body().getTrailersInfoList());
            }

            @Override
            public void onFailure(Call<TrailersAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getData(List<TrailersInfo> trailersInfoList) {
        adapter = new TrailerAdapter(trailersInfoList, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof selectedItemInformationDetailsInterface){
            select = (selectedItemInformationDetailsInterface) context;
        }
        else {
            throw new ClassCastException(context.toString() + "Must implement selectedItemInformationDetailsInterface Interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        select = null;
    }
}
