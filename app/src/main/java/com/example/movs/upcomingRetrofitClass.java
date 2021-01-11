package com.example.movs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class upcomingRetrofitClass {
    private static Retrofit retrofit;
    private static final String upcomingUrl ="https://api.themoviedb.org/3/movie/";
    public static Retrofit getUpcomingRetrofit(){
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(upcomingUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
