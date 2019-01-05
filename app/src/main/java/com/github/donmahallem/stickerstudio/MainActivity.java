package com.github.donmahallem.stickerstudio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.donmahallem.stickerstudio.indexing.AppIndexingUpdateService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TITLE = "Random article";
    private final static String TAG = "JJJ";
    private final static String articleId = "12349";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addStickersBtn = findViewById(R.id.btnAddStickers);
        final FirebaseAppIndex firebaseAppIndex = FirebaseAppIndex.getInstance();
        addStickersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, AppIndexingService.class));
            }
        });
        Button clearStickersBtn = findViewById(R.id.btnRemoveStickers);
        clearStickersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppIndexingUtil.clearStickers(MainActivity.this, firebaseAppIndex);
            }
        });
        this.findViewById(R.id.btnStart).setOnClickListener(this);
        this.findViewById(R.id.btnStop).setOnClickListener(this);
        FirebaseRemoteConfig.getInstance()
                .setDefaults(R.xml.app_config_defaults);
        FirebaseRemoteConfig.getInstance()
                .fetch()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        FirebaseRemoteConfig.getInstance()
                                .activateFetched();
                    }
                });
        Timber.d("Default version: %s", FirebaseRemoteConfig.getInstance()
                .getString("current_patch_version"));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (articleId == null) return;
        final Uri BASE_URL = Uri.parse("https://www.example.com/articles/");
        final String APP_URI = BASE_URL.buildUpon().appendPath(articleId).build().toString();

        Indexable articleToIndex = new Indexable.Builder()
                .setName(TITLE)
                .setUrl(APP_URI)
                .setDescription("Good article")
                .setKeywords("a", "long", "keyword")
                .setMetadata(new Indexable.Metadata.Builder()
                        .setWorksOffline(false))
                .build();

        Task<Void> task = FirebaseAppIndex.getInstance().update(articleToIndex);

        // If the Task is already complete, a call to the listener will be immediately
        // scheduled
        task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully added " + TITLE + " to index");
            }
        });

        task.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "App Indexing API: Failed to add " + TITLE + " to index. " + exception
                        .getMessage());
            }
        });
        // log the view action
        Task<Void> actionTask = FirebaseUserActions.getInstance().start(Actions.newView(TITLE,
                APP_URI));

        actionTask.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully started view action on " + TITLE);
            }
        });

        actionTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "App Indexing API: Failed to start view action on " + TITLE + ". "
                        + exception.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        if (articleId == null) return;
        final Uri BASE_URL = Uri.parse("https://www.example.com/articles/");
        final String APP_URI = BASE_URL.buildUpon().appendPath(articleId).build().toString();

        Task<Void> actionTask = FirebaseUserActions.getInstance().end(Actions.newView(TITLE,
                APP_URI));

        actionTask.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "App Indexing API: Successfully ended view action on " + TITLE);
            }
        });

        actionTask.addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Timber.e("App Indexing API: Failed to end view action on " + TITLE + ". "
                        + exception.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                startProgress();
                break;
            case R.id.btnStop:
                stopProgress();
                break;
        }
    }

    private void stopProgress() {

        AppIndexingUpdateService.enqueueWork(this);
    }

    private void startProgress() {
        final Action.Builder builder = new Action.Builder(Action.Builder.LIKE_ACTION);
        builder.setActionStatus(Action.Builder.STATUS_TYPE_COMPLETED);
        final Uri BASE_URL = Uri.parse("https://www.example.com/articles/");
        final String APP_URI = BASE_URL.buildUpon().appendPath(articleId).build().toString();
        builder.setUrl(APP_URI);
        builder.setName("Example article");
        FirebaseUserActions.getInstance()
                .start(builder.build())
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Timber.d("Logged successfully");
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Timber.e(e);
                    }
                });

    }
}
