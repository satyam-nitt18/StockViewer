package com.example.stockviewer;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.SupportActionModeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;

import com.example.stockviewer.ui.main.SectionsPagerAdapter;

import butterknife.BindView;
import android.support.v7.view.ActionMode.Callback;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity{

    public static MainActivity getInstance(){
        MainActivity mainActivity=new MainActivity();
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //android.view.ActionMode.Callback frameworkActionMode =
          //      new SupportActionModeWrapper.CallbackWrapper(context, supportActionMode);
        //toplevel.startActionMode (frameworkActionMode);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

}