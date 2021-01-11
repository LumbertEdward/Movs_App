package com.example.movs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface upcomingInterfaceClass {
    @GET("upcoming")
    Call<upcomingAll> getUpcoming(
            @Query("api_key") String api_key,
            @Query("language") String language
    );
}
