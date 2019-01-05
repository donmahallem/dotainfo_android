package com.github.donmahallem.opendotaapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import androidx.annotation.Nullable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class GithubDotaClient {

    private final OkHttpClient mOkHttp;
    private final Gson mGson;
    private final Retrofit mRetrofit;

    public GithubDotaClient(@Nullable File cache) {
        if (cache != null) {
            this.mOkHttp = new OkHttpClient.Builder()
                    .cache(new Cache(cache, 1024 * 1024 * 30))
                    .build();
        } else {
            this.mOkHttp = new OkHttpClient.Builder()
                    .build();
        }
        this.mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new DotaTypeAdapterFactory()).create();
        this.mRetrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/SteamDatabase/GameTracking-Dota2/master/")
                .client(this.mOkHttp)
                .addConverterFactory(new GithubTypeAdapterFactory())
                .build();
    }

    public GithubDotaApi getApi() {
        return this.mRetrofit.create(GithubDotaApi.class);
    }
}
