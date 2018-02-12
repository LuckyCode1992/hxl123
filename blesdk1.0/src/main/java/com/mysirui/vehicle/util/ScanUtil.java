package com.mysirui.vehicle.util;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.mysirui.vehicle.SRBleSDK;
import com.mysirui.vehicle.util.scan_util.HighApiLeScanUtil;
import com.mysirui.vehicle.util.scan_util.LeSCanUtil;
import com.mysirui.vehicle.util.scan_util.NormolScanUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by marlin on 2017/8/14.
 */

public abstract class ScanUtil {

    static final String TAG = "ScanUtil";

    static Map<String, BluetoothDevice> deviceMap = new HashMap<String, BluetoothDevice>();
    protected Subscriber<? super BluetoothDevice> mSub = null;

    protected String mMac;
    protected static Timer timer = new Timer();
    static int mode = 3;

    public static void setMode(int mm) {
        mode = mm;
    }

    public void cancel() {
        if (null != mSub) {
            stopScan();
            RxUtil.finish(mSub);
            mSub = null;
        }
    }

    public static Observable<BluetoothDevice> scan(Context mContext, String mac, int timeout) {
        if (mode == SRBleSDK.NORMOL) {
            return new NormolScanUtil().scanFor(mContext, mac, timeout);
        }
        if (mode == SRBleSDK.FAST) {
            return new LeSCanUtil().scanFor(mContext, mac, timeout);
        }
        if (mode == SRBleSDK.FAST_Lottipop && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new HighApiLeScanUtil().scanFor(mContext, mac, timeout);
        }
        return new LeSCanUtil().scanFor(mContext, mac, timeout);
    }

    protected void handle(BluetoothDevice device) {
        Log.d(TAG, "扫描到" + device.getName() + "=" + device);
        if (StringUtil.isNoneEmpty(device.getName()) && device.getName().toUpperCase().endsWith(mMac)) {
            deviceMap.put(mMac, device);
            finish(device);
        }
    }

    protected TimerTask cancelTask = new TimerTask() {
        @Override
        public void run() {
            finish(null);
        }
    };

    private void finish(BluetoothDevice device) {
        if (null == mSub) {
            return;
        }
        Log.i(TAG, "扫描结束:" + device);
        stopScan();
        RxUtil.finish(mSub, device);
        mSub = null;

    }

    /**
     * 停止扫描，取消
     */
    protected abstract void stopScan();
}
