package com.mysirui.vehicle.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marlin on 2016/7/7.
 */
public class BleUtil {

    private static BleUtil instance = new BleUtil();
    public static BleUtil getInstance(){
        return instance;
    }

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothManager bluetoothManager = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;
    public BluetoothAdapter getmBluetoothAdapter(){
        return mBluetoothAdapter;
    }
    public BluetoothLeScanner getmBluetoothLeScanner(){
        mBluetoothLeScanner =mBluetoothAdapter.getBluetoothLeScanner();
        return mBluetoothLeScanner;
    }

    public BluetoothManager getBluetoothManager(){
    return bluetoothManager;
}
    private BleUtil(){
            bluetoothManager = (BluetoothManager) AndroidUtil.getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();

    }



    List<BluetoothDevice> scanedDevices = new ArrayList<BluetoothDevice>();
    private void startDiscory(){
        scanedDevices.clear();
        bluetoothManager.getAdapter().startDiscovery();
    }

    private BluetoothDevice hasDevice(String mac){
        for(BluetoothDevice device:scanedDevices){
            if(device.getAddress().equals(mac)){
                return device;
            }
        }
        return null;
    }

    /*
    是否支持蓝牙4.0
     */
    public boolean isSupportBle(){
        return null!=mBluetoothAdapter;
    }


    public boolean isBleEnabled() {
        return null!=mBluetoothAdapter && mBluetoothAdapter.isEnabled();
    }

    public boolean enableBle() {
        try {
            return isBleEnabled()?true:null!=mBluetoothAdapter&&mBluetoothAdapter.enable();
        }catch (Exception e){
            return  false;
        }

    }

    public void openSetting(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_BLUETOOTH_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            EnvUtil.getApplication().startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

