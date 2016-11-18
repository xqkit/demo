package com.example.flashtools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Desc:
 * Date:    2016/11/17 17:36
 * Email:   frank.xiong@zeusis.com
 */

public class MyService extends Service {

    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNm;
    private final static int mId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        NotificationCompat.Builder mBuilder = getBinder("MyService");
        mNm.notify(mId, mBuilder.build());
        startForeground(mId, mBuilder.build());
        return new MyBinder();
    }

    private NotificationCompat.Builder getBinder(String myService) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pI = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(this).
                setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Flashtools").
                setContentText(myService).setContentIntent(pI);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mBuilder != null) {
            mBuilder.setContentTitle("射频测试结束");
            mNm.notify(mId, mBuilder.build());
            mBuilder.setAutoCancel(true);
        }
        return super.onUnbind(intent);
    }
}
