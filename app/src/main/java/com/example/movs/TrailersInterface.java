package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrailersInterface {
    @GET("{movie_id}/videos")
    Call<TrailersAll> getTrailers(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
