package com.mysirui.vehicle.framework;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.mysirui.vehicle.util.LogUtil;

/**
 * Created by marlin on 2017/8/9.
 */

public  class AsyncBleWork extends BleAsyncWork  {

    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic cha;
    private byte[] bytes20 ;
    private int writeType;

    public AsyncBleWork(BluetoothGatt mBluetoothGatt, BluetoothGattCharacteristic cha, byte[] msg, int writeType) {
        this.mBluetoothGatt = mBluetoothGatt;
        this.cha = cha;
        this.bytes20 = msg;
        this.writeType = writeType;
    }

    @Override
    public void run() {
        cha.setValue(bytes20);
        cha.setWriteType(writeType);
         if(mBluetoothGatt.writeCharacteristic(cha)){
                    LogUtil.ble("准备发送蓝牙数据"+new String(bytes20)+cha);
          }else {
                    //disConnWhenError(SRBleError.FAIL_WRITE);
            }
    }

    @Override
    public void cancel() {

    }
}
