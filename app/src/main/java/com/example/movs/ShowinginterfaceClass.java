package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShowinginterfaceClass {
    @GET("now_playing")
    Call<NowPlayingAll> getNowPlaying(
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
