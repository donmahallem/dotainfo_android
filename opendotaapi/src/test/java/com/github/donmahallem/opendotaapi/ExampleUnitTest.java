package com.github.donmahallem.opendotaapi;

import com.github.donmahallem.dota2gamefileapi.BaseHero;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void addition_isCorrect2() throws IOException {
        GithubDotaClient client=new GithubDotaClient();
        Call<List<BaseHero>> call=client
                .getApi()
                .getHeroes();
        Response<List<BaseHero>> a=call.execute();
        System.out.println("Result; "+a.code()+" - "+a.isSuccessful());
        System.out.println("Heroes: "+a.body().size());
    }
}