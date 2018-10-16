package com.github.donmahallem.opendotaapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OpenDotaApi {
    @GET("heroes")
    Call<List<Hero>> getHeroes();
}
