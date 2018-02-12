package com.mysirui.vehicle;

import android.content.Context;

import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.ChannelConnectionManager;
import com.mysirui.vehicle.framework.ChannelEventListenerContainer;
import com.mysirui.vehicle.framework.ChannelListener;
import com.mysirui.vehicle.framework.IBleMsgCoder;
import com.mysirui.vehicle.framework.MsgResult;
import com.mysirui.vehicle.dataModel.StatusItem;
import com.mysirui.vehicle.util.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by marlin on 2017/7/3.
 * 思锐蓝牙接口已经功能组装
 */

public class SRBleClient {

    int vehicleID;
    BleRawClient<BleData> rawClient;
    BLEChannel msgChannel;
    ChannelEventListenerContainer<BleData> container;
    StatusManager statusManager;

    public SRBleClient() {

        IBleMsgCoder<BleData> coder = new BleMsgCoder();
        container = new ChannelEventListenerContainer<BleData>();
        rawClient = new BleRawClient<BleData>(null, container, coder);

        //连接管理
        container.add(new ChannelConnectionManager(rawClient, coder));

        //消息交互
        msgChannel = new BLEChannel(rawClient, coder);
        container.add(msgChannel);

        //车辆状态维护
        statusManager = new StatusManager(rawClient, msgChannel);
        container.add(statusManager);

    }

    /**
     * 查询状态
     *
     * @param tags
     */
    public Observable<Map<Integer, StatusItem>> queryStatus(final int... tags) {


        //final int[] queryTags = new int[]{0xb301,0xb311,0xb312,0xb313};

        return Observable.create(new Observable.OnSubscribe<Map<Integer, StatusItem>>() {
            @Override
            public void call(final Subscriber<? super Map<Integer, StatusItem>> subscriber) {
                Observable<MsgResult<BleData>> observable = msgChannel.sendMsgWithDefaultTimeout(BleData.bulidQuery(tags[0]));
                for (int index = 1; index < tags.length; index++) {
                    final int tag = tags[index];
                    observable = observable.flatMap(new Func1<MsgResult<BleData>, Observable<MsgResult<BleData>>>() {
                        @Override
                        public Observable<MsgResult<BleData>> call(MsgResult<BleData> bleDataMsgResult) {
                            return msgChannel.sendMsgWithDefaultTimeout(BleData.bulidQuery(tag));
                        }
                    });
                }
                observable.subscribe(new Action1<MsgResult<BleData>>() {
                    @Override
                    public void call(MsgResult<BleData> bleDataMsgResult) {
                        RxUtil.finish(subscriber, statusManager.statusItemMap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        subscriber.onError(throwable);
                    }
                });
            }
        });
    }

    public SRBleClient removeListener(ChannelListener listener) {
        container.remove(listener);
        return this;
    }

    public SRBleClient addListener(ChannelListener listener) {
        container.add(listener);
        return this;
    }

    public SRBleClient addListenerFirst(ChannelListener listener) {
        container.addFirst(listener);
        return this;
    }

    /**
     * 启动思锐蓝牙客户端
     *
     * @param vehcileID 车辆ID
     * @param mac       设备mac
     * @param id        设备ID
     * @param key       设备Key
     */
    public void start(int vehcileID, String mac, String id, String key) {
        start(null, vehcileID, mac, id, key);
    }

    List<Context> contextList = new ArrayList<Context>();

    /**
     * 关联上下文启动
     *
     * @param mContext
     * @param vehcileID
     * @param mac
     * @param id
     * @param key
     */
    public void start(Context mContext, int vehcileID, String mac, String id, String key) {
        if (null != mContext) {
            contextList.add(mContext);
        }
        this.vehicleID = vehcileID;
        msgChannel.setIdAndKey(id, key);
        rawClient.setMac(mac);
        rawClient.start();
        startTime = System.currentTimeMillis();
    }


    /**
     * 关闭蓝牙
     */
    public void stop() {
        rawClient.stop();
    }

    public void stop(Context mContext) {
        stop(mContext, false);
    }

    public void stop(Context mContext, boolean force) {
        if (null != mContext) {
            contextList.remove(mContext);
        }

        if (force) {
            stop(); //立即断开
        } else if (contextList.isEmpty()) {
            delayStopWithNoContext();   //延时断开
        }
    }

    long startTime = 0;
    static Timer timer = new Timer();

    private void delayStopWithNoContext() {
        final long lastStopTime = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (lastStopTime > startTime && contextList.isEmpty()) {
                    stop();
                }
            }
        }, 5000);
    }

    /**
     * 发送消息
     *
     * @param msg     发送的消息
     * @param timeOut 超时时间 秒
     * @return
     */
    public Observable<MsgResult<BleData>> sendMsg(BleData msg, int timeOut) {
        return msgChannel.sendMsg(msg, timeOut);
    }

    /**
     * 发送消息
     *
     * @param msg     发送的消息, 默认超时时间5秒
     * @return
     */
    public Observable<MsgResult<BleData>> sendMsg(BleData msg) {
        return msgChannel.sendMsg(msg, 5000);
    }

    /**
     * 发送控制消息
     *
     * @param vehicleID 车辆ID
     * @param command   控制指令
     * @return
     */
    public Observable<MsgResult<BleData>> sendControl(final int vehicleID, final int command) {
        return msgChannel.sendControl(vehicleID, command).observeOn(AndroidSchedulers.mainThread());
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public boolean isConnected() {
        return rawClient.isConnected();
    }

    public StatusManager getStatusManager() {
        return statusManager;
    }
}
