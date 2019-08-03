package com.example.stockviewer;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.Toast;
import java.time.format.DateTimeFormatter;


import com.example.stockviewer.ui.main.Article;
import com.example.stockviewer.ui.main.Company;
import com.example.stockviewer.ui.main.ItemTouchListener;
import com.example.stockviewer.ui.main.News;
import com.example.stockviewer.ui.main.NewsAdapter;
import com.example.stockviewer.ui.main.Router;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.support.v7.app.AppCompatActivity;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import static android.support.constraint.Constraints.TAG;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;

public class StockNewsActivity extends Fragment implements SearchView.OnQueryTextListener{

    private Retrofit retrofit;
    private Router router;
    private Call <News> call;
    private static final String TAG = "StockNewsActivity";
    public static String articleURL=null;
    public static String articleTitle=null;
    public String searchParameter="";
    public static String newsApi="99c3506a15964ac6b6c1192f9a16d59c";

    private NewsAdapter adapter;
   // @BindView(R.id.newsRecycler)
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news, container, false);
        setHasOptionsMenu(true);
        recyclerView=view.findViewById(R.id.newsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter=new NewsAdapter();
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "calling retrofit");
        retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        router = retrofit.create(Router.class);
              Log.d(TAG, "search "+searchParameter+"date "+getDateFromBeginningOfMonth());
        call = router.getNews(searchParameter, getDateFromBeginningOfMonth(), "publishedAt",
                "en", newsApi);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                News news = response.body();
                if (news != null) {
                    Log.d(TAG, "posts not null");

                    adapter = new NewsAdapter(news, getContext());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

                    recyclerView.setNestedScrollingEnabled(false);

                    recyclerView.addOnItemTouchListener(new ItemTouchListener(getContext(), recyclerView, new ItemTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            final Article a = adapter.get(position);
                            Intent i = new Intent(getActivity(), NewsArticleActivity.class);
                            articleURL= a.getUrl();
                            articleTitle=a.getTitle();
                            startActivity(i);
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
                }
            }

            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDateFromBeginningOfMonth(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

       String[] dateFields={String.valueOf(mYear), String.valueOf(mMonth), String.valueOf(mDay)};
        String date = dateFields[0] + dateFields[1] + dateFields[2];
        Log.d(TAG, "date fetched "+date);

        return date;
    }
   // @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super .onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem=menu.findItem(R.id.search_button);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        searchParameter=s.toLowerCase();
        Log.d(TAG, "search parameter "+searchParameter);
        if (searchParameter.equals("")) {
            Log.d(TAG, "google accessed");
            searchParameter="google";
        }
        else {
            Log.d(TAG, "search pareameter "+searchParameter);
        }
        return true;
    }
}
