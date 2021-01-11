package com.example.movs.AllGenres;

import com.example.movs.NowPlayingAll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenreNowInterface {
    @GET("now_playing")
    Call<NowPlayingAll> getNowPlaying(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );
}
