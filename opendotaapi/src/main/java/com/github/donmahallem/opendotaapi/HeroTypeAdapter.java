package com.github.donmahallem.opendotaapi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created on 13.10.2018.
 */
public class HeroTypeAdapter extends TypeAdapter<Hero> {
    private final static String TAG_NAME="name";
    private final static String TAG_ID="id";
    private final static String TAG_PRIMARY_ATTRIBUTE="primary_attr";
    private final static String TAG_LOCALIZED_NAME="localized_name";
    @Override
    public void write(JsonWriter out, Hero value) throws IOException {

    }

    @Override
    public Hero read(JsonReader in) throws IOException {
        if(in.peek()==JsonToken.NULL){
            in.skipValue();
            return null;
        }
        Hero.Builder builder=new Hero.Builder();
        in.beginObject();
        while(in.hasNext()) {
            final String name=in.nextName();
            switch (name){
                case TAG_NAME:
                    builder.setName(in.nextString());
                    break;
                case TAG_ID:
                    builder.setId(in.nextInt());
                    break;
                case TAG_PRIMARY_ATTRIBUTE:
                    builder.setPrimaryAttribute(in.nextString());
                    break;
                case TAG_LOCALIZED_NAME:
                    builder.setLocalizedName(in.nextString());
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return builder.build();
    }
}
