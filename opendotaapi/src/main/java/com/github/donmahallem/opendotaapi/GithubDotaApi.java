package com.github.donmahallem.opendotaapi;

import com.github.donmahallem.dota2gamefileapi.BaseHero;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GithubDotaApi {
    @GET("game/dota/scripts/npc/npc_heroes.txt")
    Call<List<BaseHero>> getHeroes();
}
