package com.ahmedcancels.news;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ahmedcancels.news.api.ApiClient;
import com.ahmedcancels.news.api.ApiInterface;
import com.ahmedcancels.news.models.Article;
import com.ahmedcancels.news.models.News;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public static final String API_KEY="be9b8cb656844016b61d2d9a14ab26d0";
    private  static final String BASE_NEWS="Bitcoin";



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private  ImageView imageView;
    private Button button;
    private MenuItem searchItem;
    private SearchView searchView;



    public String keyWordcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        relativeLayout=findViewById(R.id.erorlayout);
        imageView=findViewById(R.id.errorimg);
        button=findViewById(R.id.errbtn);


        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                //refreshing if we search also by checking the keywordxheck

                if (keyWordcheck==""){

                    loadjson( "");

                }else{

                    loadjson( keyWordcheck);

                }


            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        loadjson( "");
    }

    public void loadjson(String keyWord){


        relativeLayout.setVisibility(View.GONE);
        keyWordcheck=keyWord;
        swipeRefreshLayout.setRefreshing(true);




        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Call<News> call;




        if (keyWord.length() > 0){

            call=apiInterface.getNewsSearch(keyWord,API_KEY);

        }
        else {

            call=apiInterface.getNews(BASE_NEWS,API_KEY);



        }


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }



                    articles=response.body().getArticle();
                    adapter = new Adapter(articles, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    swipeRefreshLayout.setRefreshing(false);
                    initListener();





                }
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    errorFunction();



                }

                }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

                Log.e("onFailure ", t.toString());
                swipeRefreshLayout.setRefreshing(false);
                errorFunction();




            }
        });




    }

    private void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetalisActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());



                    startActivity(intent);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_main,menu);

         searchItem=menu.findItem(R.id.search);
         searchView= (SearchView) menu.findItem(R.id.search).getActionView();

         SearchManager searchManager= (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


         searchView.setQueryHint("search news ...");
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() >2 ){

                    loadjson(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }


        });

        searchItem.getIcon().setVisible(false,false);


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        searchItem.collapseActionView();
        searchView.clearFocus();

        //Intent i =new Intent(MainActivity.this,MainActivity.class);
        //startActivity(i);
    }


    //view error layout when connection to internet doesnot exist
    private void errorFunction(){

        relativeLayout.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadjson("");
            }
        });
    }
}
