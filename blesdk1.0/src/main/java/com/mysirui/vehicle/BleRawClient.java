package com.mysirui.vehicle;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;

import com.mysirui.vehicle.constants.BleErrorEnum;
import com.mysirui.vehicle.constants.SRGattAttributes;
import com.mysirui.vehicle.framework.AsyncBleIdleWork;
import com.mysirui.vehicle.framework.AsyncBleWork;
import com.mysirui.vehicle.framework.BleAsyncWork;
import com.mysirui.vehicle.framework.ChannelListener;
import com.mysirui.vehicle.framework.IBleMsgCoder;
import com.mysirui.vehicle.framework.RawChannel;
import com.mysirui.vehicle.util.BleUtil;
import com.mysirui.vehicle.util.EnvUtil;
import com.mysirui.vehicle.util.LogUtil;
import com.mysirui.vehicle.util.RxUtil;
import com.mysirui.vehicle.util.ScanUtil;
import com.mysirui.vehicle.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import rx.functions.Action1;

/**
 * Created by marlin on 2017/5/23.
 * 基础的TCP功能封装
 * 1.连接、断开、发送byte数据、接收byte数据以及 connect disconnect receiveBytes消息
 * 2.封装所有操作到 单独一个线程中执行
 */

public class BleRawClient<T> extends RawChannel<T> {

    private Map<String,BluetoothGattCharacteristic> characteristicMap = new HashMap<String,BluetoothGattCharacteristic>();

    private String mac;
    private BluetoothGatt mBluetoothGatt;

    public void setMac(String mac) {
        boolean isSame = StringUtil.isNoneEmpty(mac) && mac.equals(this.mac);
        this.mac = mac;
        if(!isSame){
            this.dicConn();
        }
    }

    private IBleMsgCoder<T> bleCode = null;

    public BleRawClient(String mac, ChannelListener<T> listener, IBleMsgCoder<T> coder) {
        super(listener,coder);
        bleCode = coder;
    }

    ScanUtil scanUtil =null;

    @Override
    protected boolean conn_internal() {
        if(isConnectedOrConnecting()){
            return false;
        }

        if(null!=scanUtil){
            scanUtil.cancel();
        }

        changeStateTo(STATE_CONNTCTING);
        if(StringUtil.isEmpty(mac) || !BleUtil.getInstance().isBleEnabled()){
            LogUtil.ble("断开原因"+"mac为null");
            changeStateTo(STATE_DISCONNTCTED);
            return false;
        }


        scanUtil.scan(EnvUtil.getApplication(),mac,SRBleSDK.time).subscribe(new Action1<BluetoothDevice>() {
            @Override
            public void call(final BluetoothDevice device) {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(null!=device && shouldConn){
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mBluetoothGatt = device.connectGatt(EnvUtil.getApplication(), false, bluetoothGattCallback);
                                }
                            });
                            LogUtil.ble("开始连接"+device+Thread.currentThread());
                        }else {
                            LogUtil.ble("断开原因"+"scanUtil");
                            changeStateTo(STATE_DISCONNTCTED);
                        }
                    }
                });
            }
        },RxUtil.emptyErrAction);
        return true;
    }

    static Handler mHandler = new Handler();

    @Override
    protected void close_internal() {
        if(null!=scanUtil){
            scanUtil.cancel();
        }
        if(null!=mBluetoothGatt){
            mBluetoothGatt.disconnect();
        }
    }


    @Override
    protected void send_internal(T msg) {
        String uuid = bleCode.getUUIDOfChar(msg);
        BluetoothGattCharacteristic cha = characteristicMap.get(uuid);
        write(coder.buildMsg(msg).array(),cha,BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,0,200);
    }

    private ConcurrentLinkedQueue<BleAsyncWork> msgQueue = new ConcurrentLinkedQueue<BleAsyncWork>();
    boolean bleProcessing = false;
    private void processCommands() {
        Runnable command = msgQueue.poll();
        if(null!=command){
            bleProcessing = true;
            command.run();
        }
    }
    // command finished, queue the next command
    private void commandCompleted() {
        bleProcessing = false;
        processCommands();
    }
    // add a new command to the queue
    private void queueCommand(BleAsyncWork command) {
        msgQueue.add(command);
        if (!bleProcessing) {
            processCommands();
        }
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                  final int status) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        disConnWhenError(BleErrorEnum.FAIL_OPENNOTIFY);
                    }else{
                        changeStateTo(STATE_CONNTCTED);
                    }
                }
            });
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                      final BluetoothGattCharacteristic characteristic,final int status) {
            final boolean succ = status == BluetoothGatt.GATT_SUCCESS;
            latestSendTime = System.currentTimeMillis();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (!succ) {
                        disConnWhenError(BleErrorEnum.FAIL_WRITE);
                    }else{
                        commandCompleted();
                    }
                    LogUtil.ble("发送蓝牙数据"+ (succ?"成功":"失败")+":"+new String(characteristic.getValue()));
                }
            });
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            final byte[] msg = characteristic.getValue();
            latestReceiveTime = System.currentTimeMillis();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    LogUtil.ble("蓝牙数据"+new String(msg));
                    List<T> msgs =   coder.parseMsg(msg);
                    if(null==msgs){
                        return;
                    }
                    for(T msg:msgs){
                        listener.onReceive(msg);
                    }
                }
            });
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt,final int status,final int newState) {
            LogUtil.ble("蓝牙状态变更为" + newState+"  状态:"+status+" "+Thread.currentThread());
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (newState == BluetoothProfile.STATE_CONNECTED && !isConnected()) {
                        if(!isConnected()){
                            gatt.discoverServices();
                            LogUtil.ble("开始发现Service");
                        }
                    }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                        mBluetoothGatt.close();
                        //mBluetoothGatt = null;
                        LogUtil.ble("清理mBluetoothGatt");
                        changeStateTo(STATE_DISCONNTCTED);
                    }
                }
            });

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status){
            if (status != BluetoothGatt.GATT_SUCCESS) {
                disConnWhenError(BleErrorEnum.NOTFOUNE_SERVICE);
            }else{
                BluetoothGattService service = gatt.getService(SRGattAttributes.UUID_SERVICE);
                if(null==service){
                    disConnWhenError(BleErrorEnum.NOTFOUNE_SERVICE);
                    return;
                }
                boolean hasReadChar = false;
                for(BluetoothGattCharacteristic characteristic:service.getCharacteristics()){
                    characteristicMap.put(characteristic.getUuid().toString(),characteristic);
                    if(characteristic.getUuid().equals(SRGattAttributes.UUID_CHA_BLE_READ)){
                        gatt.setCharacteristicNotification(characteristic, true);
                        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SRGattAttributes.NOTIFICATION_DESCRIPTOR));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(descriptor);
                        }
                        hasReadChar = true;
                    }
                }
                if(!hasReadChar){
                    disConnWhenError(BleErrorEnum.NOTFOUNE_CHAR);
                }
            }
        }
    };


    private static final int kMaxSendSize = 20;
    private boolean write(byte[] bytes,final BluetoothGattCharacteristic cha,int writeType,int intervalOf20byte,int intervalOfMsg){
        if(!isConnected() || null==cha){
            return false;
        }
        LogUtil.ble("准备发送逻辑数据"+new String(bytes)+cha);
        int position = 0;
        do{
            final byte[] WriteBytes = new byte[kMaxSendSize];
            for(int index=0;index<kMaxSendSize;index++){
                WriteBytes[index] = 0;
            }
            int lastLength = bytes.length-position;
            if(0==position){ //片首加0x00
                System.arraycopy(bytes, position, WriteBytes, 1, lastLength<WriteBytes.length-1?lastLength:WriteBytes.length-1);
                position += (WriteBytes.length-1);
            }else{
                System.arraycopy(bytes, position, WriteBytes, 0, lastLength<WriteBytes.length?lastLength:WriteBytes.length);
                position += WriteBytes.length;
            }

            addBleMsgSendCommmand(WriteBytes,cha,writeType); //发送20字节到发送队列
            addActionIntervalCommand(intervalOf20byte); //增加休息时间

        }while(position <= bytes.length);

        addActionIntervalCommand(intervalOfMsg); //增加消息处理间隔
        return true;
    }

    void addBleMsgSendCommmand(final byte[] bytes20, final BluetoothGattCharacteristic cha, final int dwriteType){
        queueCommand(new AsyncBleWork(mBluetoothGatt,cha,bytes20,dwriteType));
    }

    void addActionIntervalCommand(final int milseconds){
        if(milseconds>0){
            queueCommand(buildActionInterval(milseconds));
        }
    }


    BleAsyncWork buildActionInterval(final int milseconds){
        return  new AsyncBleIdleWork(mBluetoothGatt,milseconds) {
            @Override
            public void doAfterTime() {
                commandCompleted();
            }
        };
    }

    @Override
    protected  void resetResource(){
        super.resetResource();
        BleAsyncWork work = null;
        while ( null!= (work = msgQueue.poll())){
            work.cancel();
        }
        msgQueue.clear();
        bleProcessing = false;
    }

}