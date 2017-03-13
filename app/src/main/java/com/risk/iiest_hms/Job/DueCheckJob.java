package com.risk.iiest_hms.Job;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.risk.iiest_hms.Helper.AsyncTasks;
import com.risk.iiest_hms.Helper.Constants;
import com.risk.iiest_hms.Helper.PageParser;
import com.risk.iiest_hms.HomeActivity;
import com.risk.iiest_hms.R;

public class DueCheckJob extends JobService {

    private AsyncTasks mAuthTask = null;
    private int respFlag = 0;
    private String loginUrl = "http://iiesthostel.iiests.ac.in/students/login_students";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        String sid = jobParameters.getExtras().getString(Constants.Job.SID);
        String password = jobParameters.getExtras().getString(Constants.Job.PASSWORD);
        final Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("sid", sid)
                .appendQueryParameter("password", password)
                .appendQueryParameter("type", "2")
                .appendQueryParameter("login", "");
        String query = builder.build().getEncodedQuery();
        String type = "application/x-www-form-urlencoded";
        mAuthTask = new AsyncTasks(query, type, new AsyncTasks.AsyncTasksListener() {
            @Override
            public void onPostExecute(String response) {
                mAuthTask = null;
                if (response != null) {
                    respFlag = 1;
                    PageParser p = new PageParser(response);
                    if (p.checkDues()) {
                        int notificationId = 1;
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setAutoCancel(true)
                                .setSound(alarmSound)
                                .setSmallIcon(R.drawable.ic_notif)
                                .setContentTitle("You have pending dues")
                                .setContentText("Please pay your bills at the earliest to avoid fines");
                        Intent intent = new Intent(DueCheckJob.this, HomeActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(DueCheckJob.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(notificationId, mBuilder.build());
                    }
                }
            }

            @Override
            public void onCancelled() {
                mAuthTask = null;
            }
        });
        mAuthTask.execute(loginUrl);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return respFlag == 0;
    }
}
