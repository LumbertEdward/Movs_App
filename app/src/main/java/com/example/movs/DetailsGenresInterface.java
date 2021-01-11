package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DetailsGenresInterface {
    @GET("movie/list")
    Call<GenresAll> getGenres(
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
