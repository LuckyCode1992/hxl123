package com.mysirui.vehicle.framework;

import android.bluetooth.BluetoothGatt;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by marlin on 2017/8/9.
 */

public  abstract class AsyncBleIdleWork extends BleAsyncWork  {

    static Timer timer = new Timer();

    private BluetoothGatt mBluetoothGatt;
    private int delay;
    private boolean canceled = false;
    private TimerTask task =null;

    public AsyncBleIdleWork(BluetoothGatt mBluetoothGatt, int delay) {
        this.mBluetoothGatt = mBluetoothGatt;
        this.delay = delay;
    }



    @Override
    public void run() {
        if(canceled){
           return;
        }
        task = new TimerTask() {
            @Override
            public void run() {
                doAfterTime();
            }
        };
        timer.schedule(task,delay);
    }

    public abstract void doAfterTime();

    public  void cancel(){
        canceled = true;
        if(null!=task){
            task.cancel();
        }
    }

}
