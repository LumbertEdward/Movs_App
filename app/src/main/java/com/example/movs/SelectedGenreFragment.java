package com.example.movs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.movs.AllGenres.GenreNowInterface;
import com.example.movs.AllGenres.GenreNowRetrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedGenreFragment extends Fragment {
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";
    private static final int TOTAL_PAGES = 51;
    private int START_PAGE = 1;
    private int CURRENT_PAGE = START_PAGE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private GenreNowInterface genreNowInterface;
    private NowShowingAdapter adapter;
    private Genres genres;

    private List<NowPlaying> nowPlayingArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null){
            genres = args.getParcelable("Genre");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_selected_genre, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progressSelected);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.selectedGenreItemRecycler);

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new NowShowingAdapter(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        getNowShowingData();
        //dispData();
        return v;
    }

    private void dispData() {
        if (!nowPlayingArrayList.isEmpty()){
            adapter.addAll(nowPlayingArrayList);
        }
    }

    private void getNowShowingData() {
        recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
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
        genreNowInterface = GenreNowRetrofit.getNowShowingRetrofit().create(GenreNowInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = genreNowInterface.getNowPlaying(api_key,language, CURRENT_PAGE);
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
        genreNowInterface = GenreNowRetrofit.getNowShowingRetrofit().create(GenreNowInterface.class);
        Call<NowPlayingAll> nowPlayingAllCall = genreNowInterface.getNowPlaying(api_key, language, CURRENT_PAGE);
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
        int sId = genres.getId();
        for (int i = 0; i < nowPlayingList.size(); i++){
            for (int j = 0; j < nowPlayingList.get(i).getGenreIds().size(); j++){
                if (nowPlayingList.get(i).getGenreIds().get(j) == sId) {
                    String title = nowPlayingList.get(i).getTitle();
                    String original_title = nowPlayingList.get(i).getOriginal_title();
                    Double vote_average = nowPlayingList.get(i).getVote_average();
                    String backdrop_path = nowPlayingList.get(i).getBackdrop_path();
                    Integer id = nowPlayingList.get(i).getId();
                    Double popularity = nowPlayingList.get(i).getPopularity();
                    boolean video = nowPlayingList.get(i).isVideo();
                    String poster_path = nowPlayingList.get(i).getPoster_path();
                    boolean adult = nowPlayingList.get(i).isAdult();
                    String overview = nowPlayingList.get(i).getOverview();
                    String release_date = nowPlayingList.get(i).getRelease_date();
                    String original_language = nowPlayingList.get(i).getOriginal_language();
                    List<Integer> genreIds = nowPlayingList.get(i).getGenreIds();
                    Integer voteCount = nowPlayingList.get(i).getVoteCount();

                    NowPlaying np = new NowPlaying(title, original_title, vote_average, backdrop_path, id, popularity, video, poster_path, adult,
                            overview, release_date, original_language, genreIds, voteCount);
                    nowPlayingArrayList.add(np);
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    adapter.addAll(nowPlayingArrayList);
                    if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }
            }
        }


    }

    private void getData(List<NowPlaying> nowPlayingList) {
        int sId = genres.getId();
        for (int i = 0; i < nowPlayingList.size(); i++){
            for (int j = 0; j < nowPlayingList.get(i).getGenreIds().size(); j++){
                if (nowPlayingList.get(i).getGenreIds().get(j) == sId){
                    String title = nowPlayingList.get(i).getTitle();
                    String original_title = nowPlayingList.get(i).getOriginal_title();
                    Double vote_average = nowPlayingList.get(i).getVote_average();
                    String backdrop_path = nowPlayingList.get(i).getBackdrop_path();
                    Integer id = nowPlayingList.get(i).getId();
                    Double popularity = nowPlayingList.get(i).getPopularity();
                    boolean video = nowPlayingList.get(i).isVideo();
                    String poster_path = nowPlayingList.get(i).getPoster_path();
                    boolean adult = nowPlayingList.get(i).isAdult();
                    String overview = nowPlayingList.get(i).getOverview();
                    String release_date = nowPlayingList.get(i).getRelease_date();
                    String original_language = nowPlayingList.get(i).getOriginal_language();
                    List<Integer> genreIds = nowPlayingList.get(i).getGenreIds();
                    Integer voteCount = nowPlayingList.get(i).getVoteCount();

                    NowPlaying np = new NowPlaying(title, original_title, vote_average, backdrop_path, id, popularity, video, poster_path, adult,
                            overview, release_date, original_language, genreIds, voteCount);
                    nowPlayingArrayList.add(np);
                    adapter.addAll(nowPlayingArrayList);
                    if (CURRENT_PAGE != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }
            }
        }

    }
}
