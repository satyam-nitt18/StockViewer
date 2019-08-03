package com.example.stockviewer.ui.main;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlphavantageRouter {

    @GET("/query")
    Call<JsonObject> get(
            @Query("function") String function,
            @Query("symbol") String symbol,
            @Query("outputsize") String outputSize);

    @GET("/query")
    Observable<JsonObject> getStockData(
            @Query("function") String function,
            @Query("symbol") String symbol,
            @Query("outputsize") String outputSize,
            @Query("apikey") String apiKey);

    @GET("/query")
    Observable<JsonObject> getStockData(
            @Query("function") String function,
            @Query("symbol") String symbol,
            @Query("interval")String interval,
            @Query("outputsize") String outputSize,
            @Query("apikey") String apiKey);
}
