package com.github.donmahallem.stickerstudio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.donmahallem.dota2gamefileapi.BaseHero;
import com.github.donmahallem.opendotaapi.GithubDotaClient;
import com.github.donmahallem.stickerstudio.indexing.AppIndexingUpdateService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class HeroDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtHeroName;
    private String articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        this.mTxtHeroName=findViewById(R.id.txtHeroName);
        Timber.d(this.getIntent().getData().toString());
    }
    @Override
    public void onStart() {
        super.onStart();
        if (articleId == null) return;
        final Uri BASE_URL = Uri.parse("https://www.example.com/articles/");
        final String APP_URI = BASE_URL.buildUpon().appendPath(articleId).build().toString();
        GithubDotaClient client = new GithubDotaClient(getCacheDir());
        Call<List<BaseHero>> heroCall = client.getApi().getHeroes();
        heroCall.enqueue(new Callback<List<BaseHero>>() {
            @Override
            public void onResponse(Call<List<BaseHero>> call, Response<List<BaseHero>> response) {
                if(response.isSuccessful()){
                    for(BaseHero baseHero:response.body()){
                        if(baseHero.getHeroID()==articleId){
                            Indexable articleToIndex = new Indexable.Builder()
                                    .setName(baseHero.getWorkshopGuideName())
                                    .setUrl(APP_URI)
                                    .setDescription("Good article")
                                    .setKeywords(baseHero.getWorkshopGuideName())
                                    .setMetadata(new Indexable.Metadata.Builder()
                                            .setWorksOffline(false))
                                    .build();

                            Task<Void> task = FirebaseAppIndex.getInstance().update(articleToIndex);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BaseHero>> call, Throwable t) {

            }
        });
/*
        Indexable articleToIndex = new Indexable.Builder()
                .setName(TITLE)
                .setUrl(APP_URI)
                .setDescription("Good article")
                .setKeywords("a","long","keyword")
                .setMetadata(new Indexable.Metadata.Builder()
                .setWorksOffline(false))
                .build();

        Task<Void> task = FirebaseAppIndex.getInstance().update(articleToIndex);
*/
    }

    @Override
    public void onStop() {
        super.onStop();
/*
        if (articleId == null) return;
        final Uri BASE_URL = Uri.parse("https://www.example.com/articles/");
        final String APP_URI = BASE_URL.buildUpon().appendPath(articleId).build().toString();

        Task<Void> actionTask = FirebaseUserActions.getInstance().end(Actions.newView(TITLE,
                APP_URI));

        actionTask.addOnSuccessListener(HeroDetailActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully ended view action on " + TITLE);
            }
        });

        actionTask.addOnFailureListener(HeroDetailActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Timber.e( "App Indexing API: Failed to end view action on " + TITLE + ". "
                        + exception.getMessage());
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
