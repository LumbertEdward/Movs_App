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

import static com.example.movs.AllConstants.Constants.api_key;

public class PopularFragment extends Fragment {
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 500;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private PopularAdapter popularAdapter;
    private PopInterface popInterface;
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
        View v = inflater.inflate(R.layout.fragment_popular, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressPop);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerPop);
        layoutManager = new GridLayoutManager(getContext(), 3);
        popularAdapter = new PopularAdapter(getContext());
        recyclerView.setAdapter(popularAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        getData();
        getSearchData();
        return v;
    }

    private void getSearchData() {
        //select.setSearchPopularData(popularAdapter);
    }

    private void getData() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;
                loadNextPage();

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
        loadFirstPage();
    }

    private void loadFirstPage() {
        popInterface = PopRetrofit.getRetrofit().create(PopInterface.class);
        Call<PopularAll> popularAllCall = popInterface.getAllPopular(api_key, language, CURRENT_PAGE);
        popularAllCall.enqueue(new Callback<PopularAll>() {
            @Override
            public void onResponse(Call<PopularAll> call, Response<PopularAll> response) {
                progressBar.setVisibility(View.GONE);
                getFirstOut(response.body().getPopularList());
            }

            @Override
            public void onFailure(Call<PopularAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFirstOut(List<Popular> popularList) {
        popularAdapter.addAll(popularList);
        if (CURRENT_PAGE != TOTAL_PAGES) popularAdapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void loadNextPage() {
        popInterface = PopRetrofit.getRetrofit().create(PopInterface.class);
        Call<PopularAll> popularAllCall = popInterface.getAllPopular(api_key, language, CURRENT_PAGE);
        popularAllCall.enqueue(new Callback<PopularAll>() {
            @Override
            public void onResponse(Call<PopularAll> call, Response<PopularAll> response) {
                progressBar.setVisibility(View.GONE);
                getNextOut(response.body().getPopularList());
            }

            @Override
            public void onFailure(Call<PopularAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getNextOut(List<Popular> popularList) {
        popularAdapter.removeLoadingFooter();
        isLoading = false;
        popularAdapter.addAll(popularList);
        if (CURRENT_PAGE != TOTAL_PAGES) popularAdapter.addLoadingFooter();
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
