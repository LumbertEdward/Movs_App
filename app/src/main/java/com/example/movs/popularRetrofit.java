package com.example.movs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class popularRetrofit {
    private static Retrofit retrofit;
    private static final String popularUrl = "https://api.themoviedb.org/3/movie/";
    public static Retrofit getPopularRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(popularUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
