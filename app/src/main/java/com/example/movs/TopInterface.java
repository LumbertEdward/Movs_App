package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopInterface {
    @GET("top_rated")
    Call<TopRatedAll> getTopRated(
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );
}
