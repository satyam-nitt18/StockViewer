package com.example.stockviewer.ui.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    public static final String DATABASE_NAME= "StockWatchList.db";



    public static final String TABLE_NAME="watchlist";
    public static final String COMPANY_SYMBOL = "CompanySymbol";
    public static final String COMPANY_NAME = "CompanyName";
    public static final String MARKET_CAPITAL="MarketCapital";
    public static final String SECTOR="Sector";
    public static final String INDUSTRY = "Industry";
    public static final String IPO = "IPO";
    public static final String SUMMARY_QUOTE = "SummaryQuote";

    public static final String TABLE_CREATE= "create table watchlist ( "+
            "CompanySymbol text, CompanyName text, MarketCapital text, Sector text, "+
            "IPO text, Industry text, SummaryQuote text);";

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }
}
