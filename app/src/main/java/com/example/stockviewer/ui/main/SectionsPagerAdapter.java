package com.example.stockviewer.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.stockviewer.R;
import com.example.stockviewer.StockNewsActivity;
import com.example.stockviewer.TopMoversActivity;
import com.example.stockviewer.ViewStocksActivity;
import com.example.stockviewer.WatchlistActivity;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            {
                WatchlistActivity watchlistActivity=new WatchlistActivity();
                return  watchlistActivity;
            }
            case 1:
            {
                ViewStocksActivity viewStocksActivity=new ViewStocksActivity();
                return viewStocksActivity;
            }
            case 2:
            {
                StockNewsActivity stockNewsActivity=new StockNewsActivity();
                return stockNewsActivity;
            }
            case 3:
            {
                TopMoversActivity topMoversActivity=new TopMoversActivity();
                return topMoversActivity;
            }
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}