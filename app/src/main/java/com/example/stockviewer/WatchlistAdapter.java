package com.example.stockviewer;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stockviewer.ui.main.AlphaVantage;
import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.Daily;
import com.example.stockviewer.ui.main.TimeSeriesResponse;
import com.example.stockviewer.ui.main.WatchlistEntry;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Company> watchList;
    private boolean watchlistState;
    private double cToday, cYesterday;
    private static final String TAG = "WatchlistAdapter";

    public WatchlistAdapter(Context context, String s){
        this.context=context;
        Company c=new Company();
        c.setName(s);
        c.setSymbol("Try Refreshing");
        this.watchList=new ArrayList<>();
        watchList.add(c);
        Log.d(TAG, "Default added");
        watchlistState=false;
    }

        public WatchlistAdapter(Context context, ArrayList<Company> watchList){
            this.context = context;
            this.watchList = watchList;
            watchlistState=true;
        }

        @Override
        public WatchlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_item,parent,false);
            return new ViewHolder(v);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(@NonNull WatchlistAdapter.ViewHolder holder, int position) {

            try {
                holder.companyNameTv.setText(watchList.get(position).getName());
                holder.companySymbolTv.setText(watchList.get(position).getSymbol());

                if(watchlistState) {
                    fetch();

                    double diff = cToday - cYesterday;
                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                    holder.closeTv.setText(decimalFormat.format(cToday));

                    if (diff < 0) {
                        holder.percentageChangeTv.setText("-" + decimalFormat.format(Math.abs(diff / cYesterday)) + "%");
                        holder.percentageChangeTv.setTextColor(Color.RED);
                    }
                    if (diff > 0) {
                        holder.percentageChangeTv.setText("+" + decimalFormat.format(Math.abs(diff /cYesterday)) + "%");
                        holder.percentageChangeTv.setTextColor(Color.GREEN);
                    }
                }

            }catch (NullPointerException npe){
                npe.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
        Log.d(TAG, "watchlist size "+watchList.size());
        if(watchList== null)
            return 0;
            return watchList.size();
        }

        public Company get(int position){
            return watchList.get(position);
        }

        public void setWatchlist(TimeSeriesResponse d){

            cToday = d.getStockData().get(0).getClose();
            cYesterday = d.getStockData().get(1).getClose();
        }

        public void update(ArrayList<Company> watchList) {
            this.watchList = watchList;
        }

        public void fetch() {
            AlphaVantage alphaVantage=new AlphaVantage();
            alphaVantage.fetchDaily(StocksAdapter.companySymbol)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(d -> {
                                System.out.println("Symbol" + d.getMetaData().get("2. Symbol"));
                                setWatchlist(d);
                               // Toast.makeText(context, "Loading successful", Toast.LENGTH_SHORT).show();
                            },
                            e -> {
                                Toast.makeText(context, "Loading Unsuccessful", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Error in fetching");
                            },
                            () -> Log.d(TAG, "No Error in fetching")
                    );
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.company_name)
            TextView companyNameTv;
            @BindView(R.id.company_symbol)
            TextView companySymbolTv;
            @BindView(R.id.close)
            TextView closeTv;
            @BindView(R.id.percentage_change)
            TextView percentageChangeTv;

            public ViewHolder(View v){
                super(v);
                ButterKnife.bind(this,v);
            }
        }
    }

