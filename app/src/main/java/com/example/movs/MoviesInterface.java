package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesInterface {
    @GET("movie/list")
    Call<GenresAll> getGenres(
            @Query("api_key") String api_key,
            @Query("language") String language
    );


}
