package com.github.donmahallem.opendotaapi;

import android.util.Log;

import com.github.donmahallem.dota2gamefileapi.BaseHero;
import com.github.donmahallem.dota2gamefileapi.HeroesFileParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.Nullable;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class GithubTypeAdapterFactory extends Converter.Factory {
    private final Type test=TypeToken.getParameterized(List.class, BaseHero.class).getType();
    @Nullable
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        System.out.println("JJJJ " +type.toString());
        if(type.equals(test)){
            return new Converter<ResponseBody, List<BaseHero>>() {
                @Override
                public List<BaseHero> convert(ResponseBody value) throws IOException {
                    final HeroesFileParser parser=new HeroesFileParser(value.source());
                    parser.parse();
                    return parser.getHeroes();
                }
            };
        }else{
            return null;
        }
    }
}
