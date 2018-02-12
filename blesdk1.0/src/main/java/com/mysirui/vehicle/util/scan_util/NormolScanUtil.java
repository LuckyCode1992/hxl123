package com.mysirui.vehicle.util.scan_util;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.mysirui.vehicle.util.AndroidUtil;
import com.mysirui.vehicle.util.BleUtil;
import com.mysirui.vehicle.util.ScanUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * creat by lucky_code at 2017/11/27
 */
public class NormolScanUtil extends ScanUtil {

    @Override
    protected void stopScan() {
        BleUtil.getInstance().getmBluetoothAdapter().cancelDiscovery();
        AndroidUtil.getApplication().unregisterReceiver(registerReceiver);
    }


    protected void startScan(String mac) {
        BleUtil.getInstance().getmBluetoothAdapter().startDiscovery();
        AndroidUtil.getApplication().registerReceiver(registerReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public Observable<BluetoothDevice> scanFor(final Context mContext, final String mac, final int timetou) {
        return Observable.create(new Observable.OnSubscribe<BluetoothDevice>() {
            @Override
            public void call(Subscriber<? super BluetoothDevice> subscriber) {
                mMac = mac.replaceAll(":", "").toUpperCase();
                mSub = subscriber;
                Log.i("是否打开蓝牙", "" + BleUtil.getInstance().isBleEnabled());
                startScan(mac);
                ScanUtil.timer.schedule(cancelTask, timetou);
            }
        });
    }
    BroadcastReceiver registerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                handle((BluetoothDevice) intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            }
        }
    };
}
