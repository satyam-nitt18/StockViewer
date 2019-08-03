package com.example.stockviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.stockviewer.ui.main.AlphaVantage;
import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.TimeSeriesResponse;
import com.example.stockviewer.ui.main.WatchlistEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenActivity extends AppCompatActivity implements CheckNetwork {
    @Override
    public void handleNetworkUnavailable() {
        Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
    }

    public ImageView imageView;
    public ProgressBar progressBar;
    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        imageView = findViewById(R.id.image);
        progressBar = findViewById(R.id.progress_wheel);

        if (isOnline(this)) {

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    moveToMain();
                    finish();
                }
            }, 2000);
        } else {
            Toast.makeText(this, " Sorry. Can't load Data. " +
                    "\n PLease check your network connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public void moveToMain() {
        startActivity(new Intent(SplashScreenActivity.this, SubscribeActivity.class));
       finish();
    }
}
