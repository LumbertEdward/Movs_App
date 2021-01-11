package com.example.movs;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movs.AllConstants.Constants.api_key;

public class NowShowingFragment extends Fragment {
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 51;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private NowShowingAdapter adapter;
    private ProgressBar progressBar;
    private NowShowingInterface nowShowingInterface;
    private selectedItemInformationDetailsInterface select;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_now_showing, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.showRecycler);
        progressBar = (ProgressBar) v.findViewById(R.id.progressShow);
        progressBar.setVisibility(View.VISIBLE);
        layoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new NowShowingAdapter(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        performSearch();
        getNowShowingData();
        return v;
    }

    private void performSearch() {
        //select.setSearchNowData(adapter);
    }

    private void getNowShowingData() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;
                loadNextItem();

            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        loadFirstItem();
    }

    private void loadFirstItem() {
        nowShowingInterface = retrofitNowCreation.getNowShowingRetrofit().create(NowShowingInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = nowShowingInterface.getNowShowingData(api_key, language, CURRENT_PAGE);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                progressBar.setVisibility(View.GONE);
                getData(response.body().getNowPlayingList());
            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadNextItem() {
        nowShowingInterface = retrofitNowCreation.getNowShowingRetrofit().create(NowShowingInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = nowShowingInterface.getNowShowingData(api_key, language, CURRENT_PAGE);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                progressBar.setVisibility(View.GONE);
                getNextData(response.body().getNowPlayingList());
            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getNextData(List<NowPlaying> nowPlayingList) {
        adapter.removeLoadingFooter();
        isLoading = false;
        adapter.addAll(nowPlayingList);
        if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void getData(List<NowPlaying> nowPlayingList) {
        adapter.addAll(nowPlayingList);
        if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof selectedItemInformationDetailsInterface){
            select = (selectedItemInformationDetailsInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        select = null;
    }
}
