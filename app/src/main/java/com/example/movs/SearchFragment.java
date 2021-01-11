package com.example.movs;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 51;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private ImageView back;
    private EditText txt;
    private ImageView cancel;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SearchAdapter searchAdapter;
    private SearchInterface searchInterface;
    private selectedItemInformationDetailsInterface select;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerSearch);
        linearLayoutManager = new LinearLayoutManager(getContext());
        back = (ImageView) v.findViewById(R.id.searchBack);
        txt = (EditText) v.findViewById(R.id.searchText);
        cancel =(ImageView) v.findViewById(R.id.searchCancel);
        cancel.setVisibility(View.GONE);
        searchAdapter = new SearchAdapter(getContext());
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.onBackPressed();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("");
            }
        });
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchAdapter.getFilter().filter(s);
                cancel.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getData();
        return v;
    }

    private void getData() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;
                loadNexItem();
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
        searchInterface = SearchRetrofit.getSearchRetrofit().create(SearchInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = searchInterface.getNowShowingData(api_key, language, CURRENT_PAGE);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                getAllData(response.body().getNowPlayingList());
            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getAllData(List<NowPlaying> nowPlayingList) {
        searchAdapter.addAll(nowPlayingList);
        if (CURRENT_PAGE != TOTAL_PAGES) searchAdapter.addLoadingFooter();
        else isLastPage = true;

    }

    private void loadNexItem() {
        searchInterface = SearchRetrofit.getSearchRetrofit().create(SearchInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = searchInterface.getNowShowingData(api_key, language, CURRENT_PAGE);
        nowPlayingAllCall.enqueue(new Callback<NowPlayingAll>() {
            @Override
            public void onResponse(Call<NowPlayingAll> call, Response<NowPlayingAll> response) {
                getNextData(response.body().getNowPlayingList());
            }

            @Override
            public void onFailure(Call<NowPlayingAll> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getNextData(List<NowPlaying> nowPlayingList) {
        searchAdapter.removeLoadingFooter();
        isLoading = false;
        searchAdapter.addAll(nowPlayingList);
        if (CURRENT_PAGE != TOTAL_PAGES) searchAdapter.addLoadingFooter();
        else isLastPage = true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        select = (selectedItemInformationDetailsInterface) context;
    }
}
