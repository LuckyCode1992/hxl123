package fuzik.com.myapplication.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import fuzik.com.myapplication.MainActivity;

/**
 * creat by lucky_code at 2018/1/30
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION)) {
            Log.d("123456","123456");
            Intent intent1 = new Intent();
            intent1.setClass(context, MainActivity.class);
            context.startActivity(intent1);
        }
    }
}
