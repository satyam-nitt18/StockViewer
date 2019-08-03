package com.example.stockviewer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.stockviewer.ui.main.AlphaVantage;
import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.ItemTouchListener;
import com.example.stockviewer.ui.main.TimeSeriesResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.util.stream.Collectors.toMap;

public class TopMoversActivity extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private Button advanced;
    private RecyclerView recyclerView;
    private RadioButton radioGain, radioLose;
    private ArrayList<Company> topCompanies, bottomCompanies, companies;
    private static final String TAG = "TopMoversActivity";
    private MoversAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_movers, container, false);
        recyclerView=view.findViewById(R.id.moversRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        radioGain=view.findViewById(R.id.gainers);
        radioLose=view.findViewById(R.id.losers);
        advanced=view.findViewById(R.id.advanced);
        topCompanies=new ArrayList<>();
        companies=new ArrayList<>();
        bottomCompanies=new ArrayList<>();

       /* AlphaVantage alphaVantage = new AlphaVantage();
        for(int i=0;i<ViewStocksActivity.mCompanySymbols.size()-2802;i++) {
            final int index=i;
            alphaVantage.fetchDaily(ViewStocksActivity.mCompanySymbols.get(i))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(d -> {
                                System.out.println("Symbol" + d.getMetaData().get("2. Symbol"));
                                getChange(d, index);
                                //  progress.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Loading successful", Toast.LENGTH_SHORT).show();
                            },
                            e -> {
                                Toast.makeText(getContext(), "Loading Unsuccessful", Toast.LENGTH_SHORT).show();
                                ;
                                Log.d(TAG, "Error in fetching");
                            },
                            () -> Log.d(TAG, "No Error in fetching")
                    );
        }

       /* Collections.sort(companies, new Comparator<Company>() {
            @Override
            public int compare(Company u1, Company u2) {
                return u1.getClose().compareTo(u2.getClose());
            }
        });
         for(int i=0;i<30;i++){
            Company c=companies.get(i);
            topCompanies.add(c);
            Company cl=companies.get(companies.size()-i-1);
            bottomCompanies.add(cl);
        }*/

        advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
    private void getChange(TimeSeriesResponse d, int index){
        double cToday = d.getStockData().get(0).getClose();
        double cYesterday = d.getStockData().get(1).getClose();
        float diff= (float)(cToday-cYesterday);
        Company c=new Company();
        c.setSymbol(ViewStocksActivity.mCompanySymbols.get(index));
        //c.setClose(String.valueOf(diff));
        companies.add(c);
    }




    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ArrayList<Company> accessCompanies=new ArrayList<>();
        switch (checkedId) {
            case R.id.gainers:
                //accessCompanies=topCompanies;
                break;
            case R.id.losers:
                //accessCompanies=bottomCompanies;
                break;
        }
        adapter = new MoversAdapter(getContext(), accessCompanies);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ItemTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getActivity(), CompanyStockDetails.class);
                StocksAdapter.companyName = adapter.get(position).getName();
                StocksAdapter.companySymbol = adapter.get(position).getSymbol();
                i.putExtra("from", "watchlist");
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

}
