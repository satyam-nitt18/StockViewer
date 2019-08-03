package com.example.stockviewer;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.stockviewer.ui.main.DbManager;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.example.stockviewer.ui.main.AlphaVantage;
import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.Daily;
import com.example.stockviewer.ui.main.Stock;
import com.example.stockviewer.ui.main.TimeSeriesResponse;
import com.example.stockviewer.ui.main.Tooltip;
import com.example.stockviewer.ui.main.WatchlistEntry;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CompanyStockDetails extends AppCompatActivity implements CheckNetwork {


    public TextView companyNameTv,
            companySymbolTv, closeTv, currency, IPOTv, summaryQuote, changeTv, percentageChangeTv, timeTv;
    // @BindView(R.id.chart)
    //protected LineChart lineChart
    public TextView highTv;
    public TextView lowTv;
    public TextView openTv;
    public TextView marketCapitalTv;
    public TextView sectorTv;
    public TextView industryTv;
    public Toolbar toolbar;
    public Button watchButton;
    public LineChart lineChart;

    public TimeSeriesResponse response;

    public ProgressBar progress;
    public NestedScrollView mainView;
    private static final String TAG = "CompanyStockDetails";
    public Date mDate;
    public DateFormat mDataFormat;
    public DbManager dbManager;

    @Override
    public void handleNetworkUnavailable() {
        Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_stock_details);
        companyNameTv = findViewById(R.id.company_name);
        companySymbolTv = findViewById(R.id.company_symbol);
        highTv = findViewById(R.id.high);
        lowTv = findViewById(R.id.low);
        openTv = findViewById(R.id.open);
        marketCapitalTv = findViewById(R.id.market_capital);
        industryTv = findViewById(R.id.industry);
        sectorTv = findViewById(R.id.sector);
        toolbar = findViewById(R.id.toolbar);
        mainView = findViewById(R.id.main_view);
        progress = (ProgressBar) findViewById(R.id.progress);
        closeTv = findViewById(R.id.close);
        currency = findViewById(R.id.currency);
        IPOTv = findViewById(R.id.ipo);
        summaryQuote = findViewById(R.id.summary);
        changeTv = findViewById(R.id.change);
        percentageChangeTv = findViewById(R.id.percentage_change);
        timeTv = findViewById(R.id.time);
        watchButton = findViewById(R.id.watchButton);
        lineChart=findViewById(R.id.chart);

        dbManager=new DbManager(this);
        mDate=new Date();
        mDataFormat = new SimpleDateFormat("MMM yy", Locale.ENGLISH);

        String from=getIntent().getStringExtra("from");
        if(from.equals("watchlist"))
            watchButton.setVisibility(View.INVISIBLE);

        Load load = new Load();
        load.execute();



    }

    class Load extends AsyncTask<Boolean, Boolean, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            AlphaVantage alphaVantage = new AlphaVantage();
            Log.d(TAG, "company fetching " + StocksAdapter.companyName);
            alphaVantage.fetchDaily(StocksAdapter.companySymbol)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(d -> {
                        System.out.println("Symbol" + d.getMetaData().get("2. Symbol"));
                                setUI(d);
                                plotData(d);
                                progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Loading successful", Toast.LENGTH_SHORT).show();
                            },
                            e -> {
                                Toast.makeText(getApplicationContext(), "Loading Unsuccessful", Toast.LENGTH_SHORT).show();
                                ;
                                Log.d(TAG, "Error in fetching");
                            },
                            () -> Log.d(TAG, "No Error in fetching")
                    );
            return true;
        }
    }

    public void setUI(TimeSeriesResponse d) {

        double cToday = d.getStockData().get(0).getClose();
        double cYesterday = d.getStockData().get(1).getClose();
        double open = d.getStockData().get(0).getOpen();
        double high = d.getStockData().get(0).getHigh();
        double low = d.getStockData().get(0).getLow();


        double diff = cToday - cYesterday;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        closeTv.setText(decimalFormat.format(cToday));
        openTv.setText(decimalFormat.format(open));
        highTv.setText(decimalFormat.format(high));
        lowTv.setText(decimalFormat.format(low));

        changeTv.setText(decimalFormat.format(diff));
        percentageChangeTv.setText("(" + decimalFormat.format(Math.abs(diff / 10)) + "%)");

        if (diff < 0) {
            changeTv.setTextColor(Color.RED);
            percentageChangeTv.setTextColor(Color.RED);

        }
        if (diff > 0) {
            changeTv.setText("+" + decimalFormat.format(diff));
            changeTv.setTextColor(Color.GREEN);
            percentageChangeTv.setTextColor(Color.GREEN);

        }

        String s = d.getMetaData().get("3. Last Refreshed");

        DateTime t;
        try {
            t = DateTime.parse(s, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            t = DateTime.parse(s, DateTimeFormat.forPattern("yyyy-MM-dd"));
        }
        t = t.toDateTime(DateTimeZone.forID("US/Eastern"));
        t = t.toDateTime(DateTimeZone.forID("Africa/Accra"));

        timeTv.setText(t.toString("d MMMM, yyyy"));

        loadCompanyDetails(StocksAdapter.companySymbol);

    }

    public void loadCompanyDetails(String symbol) {
        //Company company=dbManager.getCompany(symbol);
        InputStream is = getResources().openRawResource(R.raw.nasdaq_stocks);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";

        Company companyToSave=new Company();
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\",\"");

                if (tokens[0].replaceAll("^\"|\"$", "").equals(symbol)) {

                    companySymbolTv.setText(tokens[0].replaceAll("^\"|\"$", ""));
                    companyNameTv.setText(tokens[1].replaceAll("^\"|\"$", ""));
                    marketCapitalTv.setText(tokens[3].replaceAll("^\"|\"$", ""));
                    IPOTv.setText(tokens[4].replaceAll("^\"|\"$", ""));
                    sectorTv.setText(tokens[5].replaceAll("^\"|\"$", ""));
                    industryTv.setText(tokens[6].replaceAll("^\"|\"$", ""));
                    summaryQuote.setText(tokens[7].replaceAll("^\"|\"$", ""));

                    companyToSave.setSymbol(tokens[0].replaceAll("^\"|\"$", ""));
                    companyToSave.setName(tokens[1].replaceAll("^\"|\"$", ""));
                    companyToSave.setMarketCap(tokens[3].replaceAll("^\"|\"$", ""));
                    companyToSave.setIPOyear(tokens[4].replaceAll("^\"|\"$", ""));
                    companyToSave.setSector(tokens[5].replaceAll("^\"|\"$", ""));
                    companyToSave.setIndustry(tokens[6].replaceAll("^\"|\"$", ""));
                    companyToSave.setSummaryQuote(tokens[7].replaceAll("^\"|\"$", ""));

                    Log.d(TAG, "loading company details of " + tokens[1].replaceAll("^\"|\"$", ""));

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        watchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded=dbManager.addToWatchlist(companyToSave);
                if(isAdded)
                Toast.makeText(getApplicationContext(), StocksAdapter.companyName+" added to Watchlist", Toast.LENGTH_SHORT).show();

            }
        });
    }

    long normalize(LocalDateTime subsequent, LocalDateTime reference) {
        return subsequent.toDate().getTime() / 1000 - reference.toDate().getTime() / 1000;
    }
    public void plotData (TimeSeriesResponse dailyData) {

        List<Stock> recordList = dailyData.getStockData();
        try {
            ArrayList<Entry> entries = new ArrayList<>();
            Stock first = recordList.get(recordList.size() - 1);

            Collections.reverse(recordList);
            for (Stock wr : recordList) {
                entries.add(new Entry(normalize(wr.getDate(), first.getDate()), (float) wr.getClose()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "");
            dataSet.setLineWidth(2f);
            dataSet.setDrawCircles(false);
//            dataSet.setDrawFilled(true);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.setDrawVerticalHighlightIndicator(false);
            LineData data = new LineData(dataSet);

            Tooltip tooltip = new Tooltip(this, R.layout.tool_tip);

            lineChart.setMarker(tooltip);

            lineChart.setPinchZoom(false);
            lineChart.setScaleEnabled(false);

            lineChart.setData(data);

            lineChart.getAxisRight().setEnabled(false);
            lineChart.getAxisLeft().setDrawAxisLine(false);
            lineChart.getAxisRight().setTextColor(Color.BLACK);
            lineChart.getAxisRight().setDrawLimitLinesBehindData(true);
            lineChart.getAxisLeft().setGridColor(Color.BLACK);
            lineChart.getAxisLeft().setTextColor(Color.BLACK);
            lineChart.getAxisRight().setZeroLineColor(Color.BLACK);

            lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getXAxis().setDrawAxisLine(false);
            lineChart.getXAxis().setTextColor(Color.BLACK);
            lineChart.setDrawBorders(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.getDescription().setEnabled(false);

            lineChart.setNoDataText("No data");
            lineChart.setNoDataTextColor(Color.RED);
            lineChart.fitScreen();

            lineChart.invalidate();


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

            lineChart.clear();
            if (lineChart.getData() != null) lineChart.clearValues();
            lineChart.setNoDataText("No data");
            lineChart.setNoDataTextColor(Color.RED);
            lineChart.fitScreen();
            lineChart.invalidate();

        }
    }
    public void handleServerError(){
       // Snackbar.make(coordinatorLayout,"Server Error",Snackbar.LENGTH_INDEFINITE)
         //       .setAction("RETRY", v->fetchData())
           //     .setActionTextColor(getResources().getColor(R.color.md_yellow_200))
             //   .show();

        Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
    }


}
