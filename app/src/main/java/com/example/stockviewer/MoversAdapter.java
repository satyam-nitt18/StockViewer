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
import com.example.stockviewer.ui.main.TimeSeriesResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoversAdapter extends RecyclerView.Adapter<MoversAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Company> companies;
    private double cToday, cYesterday;
    private static final String TAG = "MoversAdapter";


    public MoversAdapter(Context context, ArrayList<Company> companies){
        this.context = context;
        this.companies = companies;
    }

    @Override
    public MoversAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mover_item,parent,false);
        return new MoversAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MoversAdapter.ViewHolder holder, int position) {

        try {
            holder.companyNameTv.setText(companies.get(position).getName());
            holder.companySymbolTv.setText(companies.get(position).getSymbol());

                fetch();

                double diff = cToday - cYesterday;
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                holder.closeTv.setText(decimalFormat.format(cToday));
                holder.changeTv.setText(String.valueOf(diff));

                if (diff < 0) {
                    holder.changeTv.setTextColor(Color.RED);
                    holder.percentageChangeTv.setText("-" + decimalFormat.format(Math.abs(diff / cYesterday)) + "%");
                    holder.percentageChangeTv.setTextColor(Color.RED);
                }
                if (diff > 0) {
                    holder.changeTv.setTextColor(Color.GREEN);
                    holder.percentageChangeTv.setText("+" + decimalFormat.format(Math.abs(diff /cYesterday)) + "%");
                    holder.percentageChangeTv.setTextColor(Color.GREEN);
                }


        }catch (NullPointerException npe){
            npe.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "companies "+companies.size());
        if(companies== null)
            return 0;
        return companies.size();
    }

    public Company get(int position){
        return companies.get(position);
    }

    public void setData(TimeSeriesResponse d){

        cToday = d.getStockData().get(0).getClose();
        cYesterday = d.getStockData().get(1).getClose();
    }


    public void fetch() {
        AlphaVantage alphaVantage=new AlphaVantage();
        alphaVantage.fetchDaily(StocksAdapter.companySymbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d -> {
                            System.out.println("Symbol" + d.getMetaData().get("2. Symbol"));
                            setData(d);
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
        @BindView(R.id.change)
        TextView changeTv;

        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
