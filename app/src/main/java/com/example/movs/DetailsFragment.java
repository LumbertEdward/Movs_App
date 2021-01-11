package com.example.movs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {
    private static final String api_key= "bfb9dfa7c6ccc29b4bdee1ec785dcb7c";
    private static final String language = "en-US";
    private ImageView img;
    private ImageView imgB;
    private TextView txtTitle;
    private TextView rate;
    private TextView overview;
    private TextView releaseDate;
    private LikeButton likeButton;
    private RelativeLayout rel;
    private ProgressBar progressBar;
    private DetailsInterface detailsInterface;
    private ImageView imgBack;
    private Button btn;
    private NowPlaying nowPlaying;
    private Popular popular;
    private Upcoming upcoming;
    private TopRated topRated;
    private selectedItemInformationDetailsInterface select;

    private DetailsGenresAdapter genresAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private DetailsGenresInterface detailsGenresInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            nowPlaying = bundle.getParcelable("Now");
            popular = bundle.getParcelable("Popular");
            upcoming = bundle.getParcelable("Upcoming");
            topRated = bundle.getParcelable("TopRated");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        img = (ImageView) v.findViewById(R.id.imgDetailsNow);
        txtTitle = (TextView) v.findViewById(R.id.titleDetails);
        rate = (TextView) v.findViewById(R.id.detailsRate);
        overview = (TextView) v.findViewById(R.id.txtOverview);
        releaseDate = (TextView) v.findViewById(R.id.releaseDate);
        imgBack = (ImageView) v.findViewById(R.id.imgBack);
        imgB = (ImageView) v.findViewById(R.id.imgB);
        progressBar = (ProgressBar) v.findViewById(R.id.progressDetails);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.genreRecycler);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        getDetails();
        return v;
    }

    private void getDetails() {
        if (nowPlaying != null) {
            progressBar.setVisibility(View.GONE);
            txtTitle.setText(nowPlaying.getTitle());
            rate.setText(String.valueOf(nowPlaying.getVote_average()));
            overview.setText(nowPlaying.getOverview());
            releaseDate.setText(nowPlaying.getRelease_date());
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load("https://image.tmdb.org/t/p/w342//" + nowPlaying.getPoster_path())
                    .into(img);
            imgBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select.onBackPressed();
                }
            });
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = nowPlaying.getId();
                    detailsInterface.getDetailsId(id);
                }
            });

            getGenreData();

        }
        else if (popular != null) {
            progressBar.setVisibility(View.GONE);
            txtTitle.setText(popular.getTitle());
            rate.setText(String.valueOf(popular.getVote_average()));
            overview.setText(popular.getOverview());
            releaseDate.setText(popular.getRelease_date());
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load("https://image.tmdb.org/t/p/w342//" + popular.getPoster_path())
                    .into(img);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = popular.getId();
                    detailsInterface.getDetailsId(id);
                }
            });
            getGenreData();

        }
        else if (upcoming != null){
            progressBar.setVisibility(View.GONE);
            txtTitle.setText(upcoming.getTitle());
            rate.setText(String.valueOf(upcoming.getVote_average()));
            overview.setText(upcoming.getOverview());
            releaseDate.setText(upcoming.getRelease_date());
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load("https://image.tmdb.org/t/p/w342//" + upcoming.getPoster_path())
                    .into(img);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = upcoming.getId();
                    detailsInterface.getDetailsId(id);
                }
            });
            getGenreData();
        }
        else if (topRated != null){
            progressBar.setVisibility(View.GONE);
            txtTitle.setText(topRated.getTitle());
            rate.setText(String.valueOf(topRated.getVote_average()));
            overview.setText(topRated.getOverview());
            releaseDate.setText(topRated.getRelease_date());
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load("https://image.tmdb.org/t/p/w342//" + topRated.getPoster_path())
                    .into(img);
            imgB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = topRated.getId();
                    detailsInterface.getDetailsId(id);
                }
            });
            getGenreData();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        }

    }

    private void getGenreData(){
        detailsGenresInterface = DetailsGenresRetrofit.getRetrofit().create(DetailsGenresInterface.class);
        Call<GenresAll> genresAllCall = detailsGenresInterface.getGenres(api_key, language);
        genresAllCall.enqueue(new Callback<GenresAll>() {
            @Override
            public void onResponse(Call<GenresAll> call, Response<GenresAll> response) {
                compareGenre(response.body().getGenresList());
            }

            @Override
            public void onFailure(Call<GenresAll> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void compareGenre(List<Genres> genresList) {
        if (nowPlaying != null){
            List<Genres> genres = new ArrayList<>();
            for (int i = 0; i <genresList.size(); i++){
                for (int j = 0; j < nowPlaying.getGenreIds().size(); j++){
                    if (genresList.get(i).getId() == nowPlaying.getGenreIds().get(j)){
                        int a = genresList.get(i).getId();
                        String nm = genresList.get(i).getName();
                        Genres genres1 = new Genres(a, nm);
                        genres.add(genres1);
                        genresAdapter = new DetailsGenresAdapter(genres, getContext());
                        recyclerView.setAdapter(genresAdapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }
        }
        else if (popular != null){
            List<Genres> genres = new ArrayList<>();
            for (int i = 0; i <genresList.size(); i++){
                for (int j = 0; j < popular.getGenreIds().size(); j++){
                    if (genresList.get(i).getId() == popular.getGenreIds().get(j)){
                        int a = genresList.get(i).getId();
                        String nm = genresList.get(i).getName();
                        Genres genres1 = new Genres(a, nm);
                        genres.add(genres1);
                        genresAdapter = new DetailsGenresAdapter(genres, getContext());
                        recyclerView.setAdapter(genresAdapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }

        }
        else if (topRated != null){
            List<Genres> genres = new ArrayList<>();
            for (int i = 0; i <genresList.size(); i++){
                for (int j = 0; j < topRated.getGenreIds().size(); j++){
                    if (genresList.get(i).getId() == topRated.getGenreIds().get(j)){
                        int a = genresList.get(i).getId();
                        String nm = genresList.get(i).getName();
                        Genres genres1 = new Genres(a, nm);
                        genres.add(genres1);
                        genresAdapter = new DetailsGenresAdapter(genres, getContext());
                        recyclerView.setAdapter(genresAdapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }

        }
        else if (upcoming != null){
            List<Genres> genres = new ArrayList<>();
            for (int i = 0; i <genresList.size(); i++){
                for (int j = 0; j < upcoming.getGenreIds().size(); j++){
                    if (genresList.get(i).getId() == upcoming.getGenreIds().get(j)){
                        int a = genresList.get(i).getId();
                        String nm = genresList.get(i).getName();
                        Genres genres1 = new Genres(a, nm);
                        genres.add(genres1);
                        genresAdapter = new DetailsGenresAdapter(genres, getContext());
                        recyclerView.setAdapter(genresAdapter);
                        recyclerView.setLayoutManager(linearLayoutManager);
                    }
                }
            }

        }
        else {
            Toast.makeText(getContext(), "No Genres", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DetailsInterface) {
            detailsInterface = (DetailsInterface) context;
        }
        else if (context instanceof selectedItemInformationDetailsInterface){
            select = (selectedItemInformationDetailsInterface) context;
        }
        else {
            throw new ClassCastException(context.toString() + "Must implement selectInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        detailsInterface = null;
        select = null;
    }

}
