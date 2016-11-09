package com.example.flashtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dave.shi on 2016/5/26.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootReceiver", "onReceive: received boot broadcast");
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("isBootCompleted",true);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
