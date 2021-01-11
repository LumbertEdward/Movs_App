package com.example.movs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopRatedFragment extends Fragment {
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 390;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private TopRatedAdapter topRatedAdapter;
    private TopInterface topInterface;
    private ProgressBar progressBar;
    private selectedItemInformationDetailsInterface select;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_top_rated, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressTop);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerTop);
        layoutManager = new GridLayoutManager(getContext(), 3);
        topRatedAdapter = new TopRatedAdapter(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(topRatedAdapter);
        progressBar.setVisibility(View.VISIBLE);
        getSearchData();
        getTopData();
        return v;
    }

    private void getSearchData() {
        //select.setSearchTopData(topRatedAdapter);
    }

    private void getTopData() {
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
        topInterface = TopRetrofit.getRetrofit().create(TopInterface.class);
        Call<TopRatedAll> topRatedAllCall = topInterface.getTopRated(api_key, language, CURRENT_PAGE);
        topRatedAllCall.enqueue(new Callback<TopRatedAll>() {
            @Override
            public void onResponse(Call<TopRatedAll> call, Response<TopRatedAll> response) {
                progressBar.setVisibility(View.GONE);
                getFirstData(response.body().getTopRatedList());
            }

            @Override
            public void onFailure(Call<TopRatedAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFirstData(List<TopRated> topRatedList) {
        topRatedAdapter.addAll(topRatedList);
        if (CURRENT_PAGE != TOTAL_PAGES) topRatedAdapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void loadNextItem() {
        topInterface = TopRetrofit.getRetrofit().create(TopInterface.class);
        Call<TopRatedAll> topRatedAllCall = topInterface.getTopRated(api_key, language, CURRENT_PAGE);
        topRatedAllCall.enqueue(new Callback<TopRatedAll>() {
            @Override
            public void onResponse(Call<TopRatedAll> call, Response<TopRatedAll> response) {
                progressBar.setVisibility(View.GONE);
                getNextData(response.body().getTopRatedList());
            }

            @Override
            public void onFailure(Call<TopRatedAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getNextData(List<TopRated> topRatedList) {
        topRatedAdapter.removeLoadingFooter();
        isLoading = false;
        topRatedAdapter.addAll(topRatedList);
        if (CURRENT_PAGE != TOTAL_PAGES) topRatedAdapter.addLoadingFooter();
        else isLastPage = true;
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
