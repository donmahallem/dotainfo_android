package com.github.donmahallem.stickerstudio.indexing;


import android.content.Context;
import android.content.Intent;

import com.github.donmahallem.opendotaapi.Hero;
import com.github.donmahallem.opendotaapi.OpenDotaClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

public class AppIndexingUpdateService extends JobIntentService {

    // Job-ID must be unique across your whole app.
    private static final int UNIQUE_JOB_ID = 42;

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // TODO Insert your Indexable objects — for example, the recipe notes look as follows:
        Timber.d("Updating items");
        ArrayList<Indexable> indexableNotes = new ArrayList<>();
        OpenDotaClient client = new OpenDotaClient();
        Call<List<Hero>> heroCall = client.getApi().getHeroes();
        try {
            Response<List<Hero>> resp = heroCall.execute();
            if (resp.isSuccessful()) {
                for (Hero hero : resp.body()) {
                    Timber.d("Got hero %s",hero.toString());
                    if (hero.getName() == null) {
                        continue;
                    }
                    final String shortName = hero.getName().replace("npc_dota_hero_", "");
                    String imageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/" + shortName + "_full.png";
                    String webUrl = "https://www.dota2.com/hero/" + shortName + "/";
                    Indexable noteToIndex = new Indexable.Builder()
                            .setName(hero.getName())
                            .setUrl(webUrl)
                            .setImage(imageUrl)
                            .set
                            .setKeywords("dota2", "hero", hero.getLocalizedName(), hero.getName())
                            .build();

                    indexableNotes.add(noteToIndex);
                }
            } else {
                Timber.e("Couldnt retrieve %s", resp.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (indexableNotes.size() > 0) {
            Indexable[] notesArr = new Indexable[indexableNotes.size()];
            notesArr = indexableNotes.toArray(notesArr);

            // batch insert indexable notes into index
            FirebaseAppIndex.getInstance().update(notesArr)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Timber.e(e);
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            Timber.d("Updated heros");
                        }
                    });
        }
    }

}