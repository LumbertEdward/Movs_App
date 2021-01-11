package com.example.movs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchRetrofit {
    private static Retrofit retrofit;
    private static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    public static Retrofit getSearchRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
