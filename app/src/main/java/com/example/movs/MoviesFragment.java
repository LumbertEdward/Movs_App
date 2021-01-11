package com.example.movs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 1;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private MoviesInterface moviesInterface;
    private ShowinginterfaceClass showinginterfaceClass;
    private upcomingInterfaceClass upcomingInterface;
    private PopularInterfaceClass popularInterfaceClass;
    private selectedItemInformationDetailsInterface select;
    private TopRatedInterface topRatedInterface;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textView;

    private RecyclerView recyclerViewNow;
    private TopItemAdapter topItemAdapter;
    private RecyclerView.LayoutManager layoutManagerNow;

    private TextView txtUpcoming;
    private RecyclerView recyclerViewUpcoming;
    private UpcomingFrontItemsAdapter upcomingFrontItemsAdapter;
    private RecyclerView.LayoutManager layoutManagerUpcoming;

    private TextView txtPopular;
    private RecyclerView recyclerViewPopular;
    private PopularFrontAdapter popularFrontAdapter;
    private RecyclerView.LayoutManager layoutManagerPopular;

    private TextView txtTop;
    private RecyclerView recyclerViewTop;
    private TopRatedFrontAdapter topRatedFrontAdapter;
    private RecyclerView.LayoutManager layoutManagerTop;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView txtHeaderTop;
    private TextView txtHeaderPopular;
    private TextView txtHeaderUpcoming;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies, container, false);
        txtHeaderTop = (TextView) v.findViewById(R.id.txtHeaderTopRated);
        txtHeaderTop.setVisibility(View.GONE);
        txtHeaderPopular = (TextView) v.findViewById(R.id.txtHeaderPopular);
        txtHeaderPopular.setVisibility(View.GONE);
        txtHeaderUpcoming = (TextView) v.findViewById(R.id.txtHeaderUpcoming);
        txtHeaderUpcoming.setVisibility(View.GONE);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerGenre);

        recyclerViewNow = (RecyclerView) v.findViewById(R.id.recyclerNowShowing2);

        txtUpcoming = (TextView) v.findViewById(R.id.txtHeaderUpcoming2);
        txtUpcoming.setVisibility(View.GONE);
        recyclerViewUpcoming = (RecyclerView) v.findViewById(R.id.recyclerUpcoming);

        txtPopular = (TextView) v.findViewById(R.id.txtHeaderPopular2);
        txtPopular.setVisibility(View.GONE);
        recyclerViewPopular = (RecyclerView) v.findViewById(R.id.recyclerPopular);

        txtTop = (TextView) v.findViewById(R.id.txtHeaderTopRated2);
        txtTop.setVisibility(View.GONE);
        recyclerViewTop = (RecyclerView) v.findViewById(R.id.recyclerTopRated);

        progressBar = (ProgressBar) v.findViewById(R.id.progressMovies);
        progressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeMovies);

        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_dark,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionMovies();
            }
        });
        genreData();
        nowShowingData();
        upcomingData();
        popularData();
        getTopRatedData();
        return v;
    }

    private void getTopRatedData() {
        txtTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getTopRatedMore();
            }
        });
        topRatedInterface = TopRatedRetrofit.getRetrofit().create(TopRatedInterface.class);
        Call<TopRatedAll> topRatedAllCall = topRatedInterface.getTopRated(api_key, language);
        topRatedAllCall.enqueue(new Callback<TopRatedAll>() {
            @Override
            public void onResponse(Call<TopRatedAll> call, Response<TopRatedAll> response) {
                txtHeaderTop.setVisibility(View.VISIBLE);
                txtTop.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                getTopResponse(response.body().getTopRatedList());
            }

            @Override
            public void onFailure(Call<TopRatedAll> call, Throwable t) {
                txtHeaderTop.setVisibility(View.GONE);
                txtTop.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTopResponse(List<TopRated> topRatedList) {
        topRatedFrontAdapter = new TopRatedFrontAdapter(topRatedList, getContext());
        layoutManagerTop = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTop.setAdapter(topRatedFrontAdapter);
        recyclerViewTop.setLayoutManager(layoutManagerTop);

    }

    private void actionMovies() {

        swipeRefreshLayout.setRefreshing(false);

    }

    private void popularData() {
        txtPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getPopularMore();
            }
        });
        popularInterfaceClass = popularRetrofit.getPopularRetrofit().create(PopularInterfaceClass.class);
        Call<PopularAll> popularAllCall = popularInterfaceClass.getPopular(api_key, language);
        popularAllCall.enqueue(new Callback<PopularAll>() {
            @Override
            public void onResponse(Call<PopularAll> call, Response<PopularAll> response) {
                txtHeaderPopular.setVisibility(View.VISIBLE);
                txtPopular.setVisibility(View.VISIBLE);
                getPopularData(response.body().getPopularList());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PopularAll> call, Throwable t) {
                txtHeaderPopular.setVisibility(View.GONE);
                txtPopular.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getPopularData(List<Popular> popularList) {
        popularFrontAdapter = new PopularFrontAdapter(popularList, getContext());
        layoutManagerPopular = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopular.setLayoutManager(layoutManagerPopular);
        recyclerViewPopular.setAdapter(popularFrontAdapter);
    }

    private void upcomingData() {
        txtUpcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.getUpcomingMore();
            }
        });
        upcomingInterface = upcomingRetrofitClass.getUpcomingRetrofit().create(upcomingInterfaceClass.class);
        Call<upcomingAll> upcomingAllCall = upcomingInterface.getUpcoming(api_key, language);
        upcomingAllCall.enqueue(new Callback<upcomingAll>() {
            @Override
            public void onResponse(Call<upcomingAll> call, Response<upcomingAll> response) {
                txtHeaderUpcoming.setVisibility(View.VISIBLE);
                txtUpcoming.setVisibility(View.VISIBLE);
                getUpcoming(response.body().getUpcomingList());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<upcomingAll> call, Throwable t) {
                txtHeaderUpcoming.setVisibility(View.GONE);
                txtUpcoming.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);

            }
        });


    }

    private void getUpcoming(List<Upcoming> upcomingList) {
        layoutManagerUpcoming = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        upcomingFrontItemsAdapter = new UpcomingFrontItemsAdapter(upcomingList, getContext());
        recyclerViewUpcoming.setAdapter(upcomingFrontItemsAdapter);
        recyclerViewUpcoming.setLayoutManager(layoutManagerUpcoming);

    }

    private void nowShowingData() {
        showinginterfaceClass = retrofitNowCreation.getNowShowingRetrofit().create(ShowinginterfaceClass.class);
        Call<NowPlayingAll> nowPlayingAllCall = showinginterfaceClass.getNowPlaying(api_key, language);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                if (response.body() != null){
                    getShowingData(response.body().getNowPlayingList());
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }


    private void getShowingData(List<NowPlaying> nowPlayingList) {
        layoutManagerNow = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topItemAdapter = new TopItemAdapter(nowPlayingList, getContext());
        recyclerViewNow.setAdapter(topItemAdapter);
        recyclerViewNow.setLayoutManager(layoutManagerNow);

    }

    private void genreData(){
        moviesInterface = retrofitCreation.getRetrofit().create(MoviesInterface.class);
        Call<GenresAll> genresAllCall = moviesInterface.getGenres(api_key, language);
        genresAllCall.enqueue(new Callback<GenresAll>() {
            @Override
            public void onResponse(Call<GenresAll> call, Response<GenresAll> response) {
                getRecyclerData(response.body().getGenresList());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GenresAll> call, Throwable t) {
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        });

    }

    private void getRecyclerData(List<Genres> genresList) {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new GenreAdapter(genresList, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof selectedItemInformationDetailsInterface){
            select = (selectedItemInformationDetailsInterface) context;
        }
        else {
            throw new ClassCastException(context.toString() + "Must implement the interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        select = null;
    }
}
