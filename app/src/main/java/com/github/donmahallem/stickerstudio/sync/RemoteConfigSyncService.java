package com.github.donmahallem.stickerstudio.sync;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.github.donmahallem.stickerstudio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import androidx.annotation.NonNull;

public class RemoteConfigSyncService extends JobService {
    public final static long SIX_HOUR = 6 * 3600 * 1000L;
    public final static int JOB_ID = 192;
    private Task<Void> mTask;

    public static JobInfo createJobInfo(ComponentName componentName) {
        final JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPeriodic(SIX_HOUR)
                .setPersisted(true);
        if (Build.VERSION.SDK_INT >= 28) {
            builder.setPrefetch(true)
                    .setImportantWhileForeground(false);
        }
        return builder
                .build();
    }

    public static void scheduleJob(@NonNull Context context) {
        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null)
            return;
        ComponentName componentName = new ComponentName(context.getApplicationContext(), RemoteConfigSyncService.class);
        final JobInfo info = createJobInfo(componentName);
        jobScheduler.schedule(info);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        config.setDefaults(R.xml.app_config_defaults);
        this.mTask = config.fetch();
        final OnCompleteListener<Void> completeListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    RemoteConfigSyncService.this
                            .jobFinished(jobParameters, false);
                } else {
                    RemoteConfigSyncService.this
                            .jobFinished(jobParameters, true);
                }
            }
        };
        if (Build.VERSION.SDK_INT >= 28) {
            this.mTask.addOnCompleteListener(this.getMainExecutor(), completeListener);
        } else {
            this.mTask.addOnCompleteListener(completeListener);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (this.mTask != null) {
        }
        return true;
    }
}
