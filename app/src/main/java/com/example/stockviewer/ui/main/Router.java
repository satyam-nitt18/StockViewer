package com.example.stockviewer.ui.main;

import android.database.Observable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Router {

    @GET("everything")
    Call<News> getNews(
            @Query("q") String q,
            @Query("from") String from,
            @Query("sortBy") String sortBy,
            @Query("language") String language,
            @Query("apikey") String apiKey);
}
