package com.example.stockviewer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.DataBaseHelper;
import com.example.stockviewer.ui.main.DbManager;
import com.example.stockviewer.ui.main.ItemTouchListener;

import java.util.ArrayList;

public class WatchlistActivity extends Fragment {

    private RecyclerView recyclerView;
    private WatchlistAdapter adapter;
    private static final String TAG = "WatchlistActivity";
    public DbManager dbManager;
    public Cursor cursor;
    public TextView dummy;
    public static ArrayList<Company> watchlistCompanies = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        setHasOptionsMenu(true);

        dummy=view.findViewById(R.id.dummy);
        recyclerView = view.findViewById(R.id.watchlistRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new WatchlistAdapter(getContext(),"No stocks added to Watchlist");
        recyclerView.setAdapter(adapter);

        watchlistCompanies.clear();
        dbManager = new DbManager(getContext());
        dbManager.open();

        cursor = dbManager.getWatchlist();

        if (cursor.moveToFirst()) {
            do {
                Company c = new Company();
                c.setName(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMPANY_NAME)));
                c.setSymbol(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMPANY_SYMBOL)));
                c.setIndustry(cursor.getString(cursor.getColumnIndex(DataBaseHelper.INDUSTRY)));
                c.setIPOyear(cursor.getString(cursor.getColumnIndex(DataBaseHelper.IPO)));
                c.setMarketCap(cursor.getString(cursor.getColumnIndex(DataBaseHelper.MARKET_CAPITAL)));
                c.setSector(cursor.getString(cursor.getColumnIndex(DataBaseHelper.SECTOR)));
                c.setSummaryQuote(cursor.getString(cursor.getColumnIndex(DataBaseHelper.SUMMARY_QUOTE)));

                watchlistCompanies.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbManager.close();

        Log.d(TAG, "watchlist size " + watchlistCompanies.size());

        if(watchlistCompanies.size()>0)
            dummy.setVisibility(View.GONE);
        adapter = new WatchlistAdapter(getContext(), watchlistCompanies);

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
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.refresh_button){

            watchlistCompanies.clear();
            adapter=new WatchlistAdapter(getContext(), watchlistCompanies);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        return true;
    }
}

