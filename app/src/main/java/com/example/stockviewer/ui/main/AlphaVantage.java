package com.example.stockviewer.ui.main;

import javax.inject.Inject;

import dagger.Provides;
import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlphaVantage {

    public  final String BASE_URL = "https://www.alphavantage.co";
    public  AlphavantageRouter router;

      public AlphaVantage() {
        router=(new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()).create(AlphavantageRouter.class);
       }


        public Observable<TimeSeriesResponse> fetchIntraDay(String ticker) {
            //Interval interval = Interval.FIVE_MIN;
            return Observable.fromCallable(() -> router.getStockData("TIME_SERIES_INTRADAY",
                    ticker, "5min", "compact")
                    .map(o -> IntraDay.from("5min", o)).blockingLast());
        }

        public Observable<TimeSeriesResponse> fetchDaily(String ticker) {
          return Observable.fromCallable(() -> router.getStockData("TIME_SERIES_DAILY", ticker, "compact", "5XZJ928JL4LSQ7MG")
                    .map(o -> Daily.from(o)).blockingLast());
        }

        public Observable<TimeSeriesResponse> fetchWeekly(String ticker) {

            return Observable.fromCallable(() -> router.getStockData("TIME_SERIES_WEEKLY", ticker, "compact", "5XZJ928JL4LSQ7MG")
                    .map(o -> Weekly.from(o)).blockingLast());
        }

        public Observable<TimeSeriesResponse> fetchMonthly(String ticker) {

            return Observable.fromCallable(() -> router.getStockData("TIME_SERIES_MONTHLY", ticker, "compact","5XZJ928JL4LSQ7MG")
                    .map(o -> Monthly.from(o)).blockingLast());
        }

}


