package com.example.stockviewer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;


import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsArticleActivity extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.progress)
    ProgressBar progress;

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_article);
        ButterKnife.bind(this);

        //getIntentExtras();
        url=StockNewsActivity.articleURL;

        AnimationDrawable ad = getProgressBarAnimation();
        progress.setIndeterminateDrawable(ad);

        webView.getSettings().setUserAgentString("Android");
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            }

        });

    }

    private void getIntentExtras() {

        if (getIntent().getStringExtra(StockNewsActivity.articleURL) != null) {
            url = getIntent().getStringExtra(StockNewsActivity.articleURL);
        } else {
            url = "";
        }

        if (getIntent().getStringExtra(StockNewsActivity.articleTitle) != null) {
            title = getIntent().getStringExtra(StockNewsActivity.articleTitle);
        } else {
            title = "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private AnimationDrawable getProgressBarAnimation() {

        GradientDrawable rainbow1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.MAGENTA, Color.WHITE});

        GradientDrawable rainbow2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{Color.WHITE, Color.MAGENTA});

        GradientDrawable[] gds = new GradientDrawable[]{rainbow1, rainbow2};

        AnimationDrawable animation = new AnimationDrawable();
        for (GradientDrawable gd : gds)
            animation.addFrame(gd, 500);

        animation.setOneShot(false);
        return animation;
    }
}

