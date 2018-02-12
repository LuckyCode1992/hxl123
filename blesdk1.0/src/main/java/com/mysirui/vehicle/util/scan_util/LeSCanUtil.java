package com.mysirui.vehicle.util.scan_util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.mysirui.vehicle.util.BleUtil;
import com.mysirui.vehicle.util.ScanUtil;

import rx.Observable;
import rx.Subscriber;

/**
 * creat by lucky_code at 2017/11/27
 */
public class LeSCanUtil extends ScanUtil{

    @Override
    protected void stopScan() {
        BleUtil.getInstance().getmBluetoothAdapter().stopLeScan(leScanCallback);
    }


    protected void startScan(String mac) {
        BleUtil.getInstance().getmBluetoothAdapter().startLeScan(leScanCallback);//new UUID[]{SRGattAttributes.UUID_SERVICE},
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
    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            handle(bluetoothDevice);
        }
    };
}
