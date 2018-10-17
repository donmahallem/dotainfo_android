package com.github.donmahallem.stickerstudio.indexing;


import android.content.Context;
import android.content.Intent;

import com.github.donmahallem.dota2gamefileapi.BaseHero;
import com.github.donmahallem.opendotaapi.GithubDotaClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.StickerBuilder;
import com.google.firebase.appindexing.builders.StickerPackBuilder;

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
    private final String STICKER_PACK_NAME = "Dota2HeroStickerPack";

    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, UNIQUE_JOB_ID, new Intent());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        // TODO Insert your Indexable objects — for example, the recipe notes look as follows:
        Timber.d("Updating items");
        ArrayList<Indexable> indexableNotes = new ArrayList<>();
        GithubDotaClient client = new GithubDotaClient(getCacheDir());
        Call<List<com.github.donmahallem.dota2gamefileapi.BaseHero>> heroCall = client.getApi().getHeroes();
        try {
            Response<List<BaseHero>> resp = heroCall.execute();
            if (resp.isSuccessful()) {
                createHeroIndexables(indexableNotes, resp.body());
                createStickerPack(indexableNotes, resp.body());
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
        }else{
            Timber.d("No types found");
        }
    }

    private void createHeroIndexables(List<Indexable> indexables, List<BaseHero> heroes) {

        for (BaseHero hero : heroes) {
            if(hero.getWorkshopGuideName()==null){
                continue;
            }
            Timber.d("Got hero %s", hero.toString());
            if (hero.getWorkshopGuideName() == null) {
                continue;
            }
            final String shortName = hero.getNpcHeroName().replace("npc_dota_hero_", "");
            String imageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/" + shortName + "_full.png";
            String webUrl = "https://www.dota2.com/hero/" + shortName + "/";
            final Indexable.Builder builder = new Indexable.Builder()
                    .setName(hero.getWorkshopGuideName())
                    .setUrl(webUrl)
                    .setImage(imageUrl);
            if (hero.getNameAliases() != null) {
                builder.setKeywords("dota2", "hero", hero.getWorkshopGuideName(), hero.getNameAliases());
            } else {
                builder.setKeywords("dota2", "hero", hero.getWorkshopGuideName());
            }

            indexables.add(builder.build());
        }
    }

    private StickerBuilder createSticker(BaseHero baseHero) {
        final String shortName = baseHero.getNpcHeroName().replace("npc_dota_hero_", "");
        String imageUrl = "http://cdn.dota2.com/apps/dota2/images/heroes/" + shortName + "_full.png";
        String webUrl = "https://www.dota2.com/hero/" + shortName + "/";
        StickerBuilder stickerBuilder = Indexables.stickerBuilder()
                .setName(baseHero.getWorkshopGuideName())
                .setImage(imageUrl)
                .setUrl(String.format(STICKER_URL_PATTERN, baseHero.getHeroID()))
                .setDescription("content description")
                .setIsPartOf(Indexables.stickerPackBuilder()
                        .setName(STICKER_PACK_NAME))
                .put("dota2", baseHero.getWorkshopGuideName());
        return stickerBuilder;
    }

    private static final String STICKER_URL_PATTERN = "mystickers://sticker/%s";
    private static final String STICKER_PACK_URL_PATTERN = "mystickers://sticker/pack/%s";
    private void createStickerPack(List<Indexable> indexables, List<BaseHero> heroes) {
        List<StickerBuilder> stickerBuilders = new ArrayList<>();
        for (BaseHero hero : heroes) {
            if(hero.getWorkshopGuideName()==null){
                continue;
            }
            StickerBuilder builder=createSticker(hero);
            indexables.add(builder.build());
            stickerBuilders.add(builder);

        }
        StickerPackBuilder stickerPackBuilder = Indexables.stickerPackBuilder()
                .setName(STICKER_PACK_NAME)
                // Firebase App Indexing unique key that must match an intent-filter.
                // (e.g. mystickers://sticker/pack/0)
                .setUrl(String.format(STICKER_PACK_URL_PATTERN, 0))
                // Defaults to the first sticker in "hasSticker". Used to select between sticker
                // packs so should be representative of the sticker pack.
                .setHasSticker(stickerBuilders.toArray(new StickerBuilder[stickerBuilders.size()]))
                .setDescription("content description");
        indexables.add(stickerPackBuilder.build());
    }
}