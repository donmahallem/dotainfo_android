package com.github.donmahallem.stickerstudio;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.donmahallem.dota2gamefileapi.BaseHero;
import com.github.donmahallem.opendotaapi.GithubDotaClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private int articleId = -1;
    private BaseHero mHero;
    private String APP_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        this.mTxtHeroName = findViewById(R.id.txtHeroName);
        if (this.getIntent().getData() != null) {
            this.articleId = Integer.parseInt(this.getIntent().getData().getLastPathSegment());
        }
        Timber.d(this.getIntent().getData().toString());
    }

    private void setHero(BaseHero hero) {
        this.mHero = hero;
        final Uri BASE_URL = Uri.parse("https://www.dota2.com/heroes/");
        APP_URI = BASE_URL.buildUpon().appendPath("" + articleId).build().toString();
        Indexable articleToIndex = new Indexable.Builder()
                .setName(hero.getWorkshopGuideName())
                .setUrl(APP_URI)
                .setDescription("Good article")
                .setKeywords(hero.getWorkshopGuideName())
                .setMetadata(new Indexable.Metadata.Builder()
                        .setWorksOffline(false))
                .build();

        Task<Void> task = FirebaseAppIndex.getInstance().update(articleToIndex);
        Task<Void> actionTask = FirebaseUserActions.getInstance().start(Actions.newView(this.mHero.getWorkshopGuideName(),
                APP_URI));

        actionTask.addOnSuccessListener(HeroDetailActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.d("App Indexing API: Successfully started view action on ");
            }
        });

        actionTask.addOnFailureListener(HeroDetailActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Timber.e("App Indexing API: Failed to end view action on . "
                        + exception.getMessage());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (articleId < 0) return;
        GithubDotaClient client = new GithubDotaClient(getCacheDir());
        Call<List<BaseHero>> heroCall = client.getApi().getHeroes();
        heroCall.enqueue(new Callback<List<BaseHero>>() {
            @Override
            public void onResponse(Call<List<BaseHero>> call, Response<List<BaseHero>> response) {
                if (response.isSuccessful()) {
                    for (BaseHero baseHero : response.body()) {
                        if (baseHero.getHeroID() == articleId) {
                            setHero(baseHero);
                            return;
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

        if (mHero == null) return;

        Task<Void> actionTask = FirebaseUserActions.getInstance().end(Actions.newView(this.mHero.getWorkshopGuideName(),
                APP_URI));

        actionTask.addOnSuccessListener(HeroDetailActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.d("App Indexing API: Successfully ended view action on ");
            }
        });

        actionTask.addOnFailureListener(HeroDetailActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Timber.e("App Indexing API: Failed to end view action on . "
                        + exception.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

}
