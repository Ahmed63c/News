package com.ahmedcancels.news.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//return Retrofit object that be used wirh creating call object from api interface....



public class ApiClient {


    public static final String BASE_URL="https://newsapi.org/v2/";
    public static Retrofit retrofit;

    public static Retrofit getApiClient(){


        if(retrofit==null){

            retrofit=new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }

}

