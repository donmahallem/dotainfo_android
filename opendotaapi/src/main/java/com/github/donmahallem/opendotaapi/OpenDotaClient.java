package com.github.donmahallem.opendotaapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenDotaClient {

    private final OkHttpClient mOkHttp;
    private final Gson mGson;
    private final Retrofit mRetrofit;

    public OpenDotaClient(){
        this.mOkHttp=new OkHttpClient.Builder()
                .build();
        this.mGson=new GsonBuilder()
                .registerTypeAdapterFactory(new DotaTypeAdapterFactory()).create();
        this.mRetrofit=new Retrofit.Builder()
                .baseUrl("https://api.opendota.com/api/")
                .client(this.mOkHttp)
                .addConverterFactory( GsonConverterFactory.create(this.mGson))
                .build();
    }

    public OpenDotaApi getApi(){
        return this.mRetrofit.create(OpenDotaApi.class);
    }
}
