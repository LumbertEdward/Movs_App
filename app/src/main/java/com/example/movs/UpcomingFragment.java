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
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movs.AllConstants.Constants.api_key;

public class UpcomingFragment extends Fragment {
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 20;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private RecyclerView recyclerView;
    private UpcomingAdapter adapter;
    private GridLayoutManager layoutManager;
    private UpcomingAllInterface upcomingAllInterface;
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
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.upcomingRecycler);
        layoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new UpcomingAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        searchData();
        getPopular();
        return v;
    }

    private void searchData() {
        //select.setSearchData(adapter);
    }

    private void getPopular() {
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
        upcomingAllInterface = upcomingRetrofitClass.getUpcomingRetrofit().create(UpcomingAllInterface.class);
        Call<upcomingAll> upcomingAllCall = upcomingAllInterface.getAllUpcomingData(api_key, language, CURRENT_PAGE);
        upcomingAllCall.enqueue(new Callback<upcomingAll>() {
            @Override
            public void onResponse(Call<upcomingAll> call, Response<upcomingAll> response) {
                progressBar.setVisibility(View.GONE);
                getFirstData(response.body().getUpcomingList());
            }

            @Override
            public void onFailure(Call<upcomingAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFirstData(List<Upcoming> upcomingList) {
        adapter.addAll(upcomingList);
        if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void loadNextItem() {
        upcomingAllInterface = upcomingRetrofitClass.getUpcomingRetrofit().create(UpcomingAllInterface.class);
        Call<upcomingAll> upcomingAllCall = upcomingAllInterface.getAllUpcomingData(api_key, language, CURRENT_PAGE);
        upcomingAllCall.enqueue(new Callback<upcomingAll>() {
            @Override
            public void onResponse(Call<upcomingAll> call, Response<upcomingAll> response) {
                progressBar.setVisibility(View.GONE);
                getNextData(response.body().getUpcomingList());
            }

            @Override
            public void onFailure(Call<upcomingAll> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), R.string.network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNextData(List<Upcoming> upcomingList) {
        adapter.removeLoadingFooter();
        isLoading = false;
        adapter.addAll(upcomingList);
        if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
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
