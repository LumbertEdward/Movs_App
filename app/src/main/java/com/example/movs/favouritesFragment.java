package com.example.movs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class favouritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private favouritesAdapter adapter;
    private NowShowingAdapter nowShowingAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FavoritesNowInterface favoritesNowInterface;
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";

    private ArrayList<NowPlaying> playingArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerFavorite);
        layoutManager = new GridLayoutManager(getContext(), 3);
        getData();
        return v;
    }

    private void getData() {
        favoritesNowInterface = FavoritesRetrofit.getRetrofit().create(FavoritesNowInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = favoritesNowInterface.getNowShowingData(api_key, language);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                getMResponse(response.body().getNowPlayingList());
            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                Toast.makeText(getContext(), "None", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getMResponse(List<NowPlaying> nowPlayingList) {
        SharedPreferences preferences = getActivity().getSharedPreferences("Details", Context.MODE_PRIVATE);
        Set<String> stringSet = preferences.getStringSet("SAVED_CONNECTION", new HashSet<String>());
        if (playingArrayList != null){
            playingArrayList.clear();
        }
        for (NowPlaying nowPlaying: nowPlayingList) {
            if (stringSet.contains(nowPlaying.getTitle())) {
                playingArrayList.add(nowPlaying);
            }
        }

        if (nowShowingAdapter == null){
            nowShowingAdapter = new NowShowingAdapter(getContext());
            recyclerView.setAdapter(nowShowingAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
        }
        else {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }

    }
}
