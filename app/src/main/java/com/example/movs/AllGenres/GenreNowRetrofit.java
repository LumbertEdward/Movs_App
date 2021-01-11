package com.example.movs.AllGenres;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreNowRetrofit {
    private static final String Url = "https://api.themoviedb.org/3/movie/";
    private static Retrofit retrofit;
    public static Retrofit getNowShowingRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
