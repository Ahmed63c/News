package com.ahmedcancels.news.api;

import com.ahmedcancels.news.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


//usein  documentation of "newsapi.org"  these annotaion and queries from the url in the site

public interface ApiInterface {

    @GET("everything")
    Call<News> getNews(

            @Query("q") String q,
            @Query("apiKey") String apiKey
    );




    @GET("everything")
    Call<News> getNewsSearch(


            @Query("q") String q,
            @Query("apiKey") String apiKey


    );



}
