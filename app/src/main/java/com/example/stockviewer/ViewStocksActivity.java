package com.example.stockviewer;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.ItemTouchListener;
import com.example.stockviewer.ui.main.Stock;
import com.example.stockviewer.ui.main.Tools;
import com.example.stockviewer.ui.main.WatchlistEntry;
import android.support.v7.view.ActionMode.Callback;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ViewStocksActivity extends Fragment {

    private RecyclerView recyclerView;
    private StocksAdapter adapter;
    private ArrayList<String> mCompanyNames;
    public  static ArrayList<String> mCompanySymbols;
    private ActionMode actionMode;
    private ActionMode.Callback actionModeCallback;
    private boolean isMultiSelect = false;
    private List<String> selectedIds = new ArrayList<>();
    private Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_stocks, container, false);

        mCompanyNames= new ArrayList<>();
        mCompanySymbols=new ArrayList<>();
        recyclerView=view.findViewById(R.id.stocksRecycler);
        //ViewStocksActivity viewStocksActivity=new ViewStocksActivity();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter=new StocksAdapter();
        recyclerView.setAdapter(adapter);

        /*Window window = dialog.getWindow();
        View toplevel = window.getDecorView();
        if (toplevel == null) { return; }

        android.view.ActionMode.Callback frameworkActionMode =
                new CallbackWrapper (context, supportActionMode);
        toplevel.startActionMode (frameworkActionMode);*/
        InputStream is = getResources().openRawResource(R.raw.nasdaq_stocks);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";


        ArrayList<Company> companies=new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\",\"");

                Company c = new Company();
                c.setSymbol(tokens[0].replaceAll("^\"|\"$", ""));
                c.setName(tokens[1].replaceAll("^\"|\"$", ""));
                //c.setMarketCap(tokens[3].replaceAll("^\"|\"$", ""));
                //c.setIPOyear(tokens[4].replaceAll("^\"|\"$", ""));
                //c.setSector(tokens[5].replaceAll("^\"|\"$", ""));
                //c.setIndustry(tokens[6].replaceAll("^\"|\"$", ""));
                //c.setSummaryQuote(tokens[7].replaceAll("^\"|\"$", ""));
                mCompanyNames.add(tokens[1].replaceAll("^\"|\"$", ""));
                mCompanySymbols.add(tokens[0].replaceAll("^\"|\"$", ""));
                companies.add(c);
            }
            //dbManager.setCompanies(companies);
            System.out.println(getClass().getSimpleName() + " " + companies.size() + " companies added");

            adapter = new StocksAdapter(mCompanyNames, mCompanySymbols, getContext());

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            // recyclerView.scrollToPosition(news.getArticles().size() - 1);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            recyclerView.setNestedScrollingEnabled(false);

            adapter.setOnClickListener(new StocksAdapter.OnClickListener() {
                @Override
                public void onItemClick(View view, int pos) {
                    if(adapter.getSelectedItemsCount()>0)
                        enableActionMode(pos);
                    else{
                        adapter.startIntent(pos);
                    }
                }

                @Override
                public void onItemLongClick(View view, int pos) {

                    enableActionMode(pos);
                }
            });

            actionModeCallback=new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                   // Tools.setSystemBarColor(getActivity(), R.color.colorPrimaryDark);
                    mode.getMenuInflater().inflate(R.menu.compare_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if(item.getItemId()==R.id.compare){
                        //CompareActivity
                        Toast.makeText(getContext(), "comparison", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.clearSelection();
                    actionMode=null;
                    //Tools.setSystemBarColor(getActivity(), R.color.colorPrimary);
                }
            };

        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            MainActivity.getInstance().finish();
        }
        else
            Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    public void enableActionMode(int position){
       if(actionMode==null)

           actionMode=MainActivity.getInstance().startActionMode(actionModeCallback);
       toggleSelection(position);
   }
    public void toggleSelection(int pos){
        adapter.toggleSelection(pos);
        int count=adapter.getSelectedItemsCount();
        if(count==0){
            actionMode.finish();
        }else{
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }


}
