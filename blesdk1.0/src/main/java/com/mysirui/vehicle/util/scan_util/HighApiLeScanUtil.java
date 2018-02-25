package com.mysirui.vehicle.util.scan_util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.mysirui.vehicle.util.BleUtil;
import com.mysirui.vehicle.util.ScanUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * creat by lucky_code at 2017/11/27
 */
public class HighApiLeScanUtil extends ScanUtil {

    protected void startScan(String mac) {
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.trim();
            String MAC;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length(); i++) {
                sb.append(mac.charAt(i));
                if ((i + 1) % 2 == 0) {//i+1  为 2,4,6,8 偶数
                    if (i + 1 < mac.length()) {
                        sb.append(":");
                    }
                }
            }
            MAC = sb.toString().toUpperCase();
            List<ScanFilter> bleScanFilters = new ArrayList<>();
            bleScanFilters.add(
                    new ScanFilter.Builder().setDeviceAddress(MAC).build()//setServiceUuid(new ParcelUuid(SRGattAttributes.UUID_SERVICE)
            );
            ScanSettings bleScanSettings = new ScanSettings.Builder().build();
            BleUtil.getInstance().getmBluetoothLeScanner().startScan(bleScanFilters, bleScanSettings, scanCallback);//bleScanFilters, bleScanSettings,
        } else {
            BleUtil.getInstance().getmBluetoothLeScanner().startScan(scanCallback);
        }
    }

    @Override
    protected void stopScan() {
        /**
         * 解释一下：
         *     mBluetoothAdapter.getBluetoothLeScanner() 这个方法可能会得不到scanner，所以需要作非空判断
         */
        if (BleUtil.getInstance().getmBluetoothLeScanner() != null)
            BleUtil.getInstance().getmBluetoothLeScanner().stopScan(scanCallback);
    }

    public Observable<BluetoothDevice> scanFor(final Context mContext, final String mac, final int timetou) {
        return Observable.create(new Observable.OnSubscribe<BluetoothDevice>() {
            @Override
            public void call(Subscriber<? super BluetoothDevice> subscriber) {
                if (!TextUtils.isEmpty(mac)) {
                    mMac = mac.trim().replaceAll(":", "").toUpperCase();
                }
                mSub = subscriber;
                Log.i("是否打开蓝牙", "" + BleUtil.getInstance().isBleEnabled());
                startScan(mac);
                ScanUtil.timer.schedule(cancelTask, timetou);
            }
        });
    }

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            handle(device);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };
}
