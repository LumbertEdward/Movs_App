package com.example.movs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailersRetrofit {
    private static Retrofit retrofit;
    private static final String TrailerUrl = "https://api.themoviedb.org/3/movie/";

    public static Retrofit getTrailersRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(TrailerUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
