package fuzik.com.myapplication;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


/**
 * creat by lucky_code at 2017/12/9
 */
public class MyApp extends Application {
    public Application instance;
    CountDownTimer timer;
    boolean isLiang;
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;

    int mm = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("-------", "开机了");
        instance = this;

        mm = new Random().nextInt(8) + 10;
        Toast.makeText(instance, "开机了"+mm, Toast.LENGTH_SHORT).show();
        PowerManager powerManager = (PowerManager) this
                .getSystemService(Context.POWER_SERVICE);
        isLiang = powerManager.isScreenOn();
        timer = new CountDownTimer(24 * 60 * 60 * 1000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("-------", "10秒过去了");
                Calendar calendar = Calendar.getInstance(Locale.CHINA);
                int week = calendar.get(Calendar.DAY_OF_WEEK);
                /**
                 * 周日 = 1
                 * 周一 = 2
                 * 周二 = 3
                 * 周三 = 4
                 * 周四 = 5
                 * 周五 = 6
                 * 周六 = 7
                 */
                Log.d("-------", "week=" + week);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                Log.d("-------", "hour=" + hour);
                int min = calendar.get(Calendar.MINUTE);
                Log.d("-------", "min=" + min);
                Log.d("-------", "mm=" + mm);
                int mill = calendar.get(Calendar.MILLISECOND);
                Log.d("-------", "mill=" + mill);
                    if (hour == 8) {
                        if (min >= mm && min <= 29) {//30就迟到了，当然不用再去操作了
                            registerScreenActionReceiver();
                            亮屏解锁();
                            启动脚本();
                        }
                    }
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();

    }

    private void 启动脚本() {
        // TODO: 2017/12/9
        Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage("com.alibaba.android.rimet");
        startActivity(LaunchIntent);
    }

    public void 亮屏解锁() {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁

    }

    boolean isregiste;

    private void registerScreenActionReceiver() {

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(receiver, filter);
        isregiste = true;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.d("-------", "锁屏了");
                isLiang = false;
                Toast.makeText(instance, "锁屏了", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.d("-------", "亮屏了");
                isLiang = true;
                启动脚本();
                // 屏幕解锁
                Toast.makeText(instance, "亮屏了", Toast.LENGTH_SHORT).show();

            }
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.d("-------", "解锁了");
                isLiang = true;
                Toast.makeText(instance, "解锁了", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
