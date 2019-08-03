package com.example.stockviewer.ui.main;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.util.LogTime;
import com.example.stockviewer.WatchlistActivity;
import com.example.stockviewer.WatchlistAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private Context context;
    private SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private RecyclerView recyclerView;
    private WatchlistAdapter adapter;
    private static final String TAG = "DBManager";
    private Cursor cursor;

    public DbManager(Context c) {
        this.context = c;
    }

    public DbManager open() throws SQLException {
        this.dbHelper = new DataBaseHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public boolean addToWatchlist(Company company) {
        open();
        Cursor cursor =getWatchlist();

        if(cursor.moveToFirst()){
            do{
                if(company.getSymbol().equals(cursor.getString(cursor.getColumnIndex(DataBaseHelper.COMPANY_SYMBOL))))
                {
                    Toast.makeText(this.context, "Already exists in Favorites", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }while (cursor.moveToNext());
        }
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.COMPANY_SYMBOL, company.getSymbol());
        values.put(DataBaseHelper.COMPANY_NAME, company.getName());
        values.put(DataBaseHelper.INDUSTRY, company.getIndustry());
        values.put(DataBaseHelper.IPO, company.getIpoyear());
            values.put(DataBaseHelper.MARKET_CAPITAL, company.getMarketCap());
            values.put(DataBaseHelper.SUMMARY_QUOTE, company.getSummaryQuote());
            values.put(DataBaseHelper.SECTOR, company.getSector());

        this.database.insert(DataBaseHelper.TABLE_NAME, null, values);
        close();
        return true;
    }

    public void deleteFavorite(Company company) {
        this.database.delete(DataBaseHelper.DATABASE_NAME, DataBaseHelper.COMPANY_SYMBOL+ "=" + company.getSymbol(), null);
    }

    public Cursor getWatchlist() {

        String[] Columns = {DataBaseHelper.COMPANY_NAME, DataBaseHelper.COMPANY_SYMBOL, DataBaseHelper.SECTOR, DataBaseHelper.IPO,
                DataBaseHelper.MARKET_CAPITAL, DataBaseHelper.SUMMARY_QUOTE, DataBaseHelper.INDUSTRY,
        };

        cursor = this.database.query(DataBaseHelper.TABLE_NAME, Columns, null, null, null, null, null);

        return cursor;
    }
}