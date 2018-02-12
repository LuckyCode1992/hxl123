package com.mysirui.vehicle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.mysirui.vehicle.businessModel.LockThenCloseOilBusiness;
import com.mysirui.vehicle.constants.CommandEnum;
import com.mysirui.vehicle.constants.StatusConstant;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.dataModel.StatusItem;
import com.mysirui.vehicle.framework.ChannelListener;
import com.mysirui.vehicle.framework.DefaultChannelListener;
import com.mysirui.vehicle.framework.IStatusListener;
import com.mysirui.vehicle.framework.MsgResult;
import com.mysirui.vehicle.util.AndroidUtil;
import com.mysirui.vehicle.util.EnvUtil;
import com.mysirui.vehicle.util.ScanUtil;

import java.util.Map;

import rx.Observable;
import rx.functions.Action0;

/**
 * 蓝牙SDK调用封装
 * Created by marlin on 2017/8/18.
 */

public class SRBleSDK {

    public static void iniWithApplication(Application application) {
        AndroidUtil.setApplication(application);
    }

    /*
    //247189d2f147
    //String mac = "a0:e6:f8:84:c9:6b";
    String mac = "24:71:89:d2:f1:47";
    //String mac = "12345";
    String idStr = "3c8bd1";
    //String ketStr = "5051fa";
    String ketStr = "353035316661";
     */

    //是否上锁断油, 解锁通油模式 共享车专用
    private boolean lockThenCloseOilMode = false;
    public static int leBleTimes = 3;

    public static final int NORMOL = 1;
    public static final int FAST_Lottipop = 3;
    public static final int FAST = 2;
    public static int time = 10000;

    /**
     * 新增上锁断开油路，解锁恢复油路功能
     * 注意：此设置需要在开始start方法前调用，否则将无效，如果，不需要此功能，可以设置为false或者，不写。
     * 此功能，只需要调用上锁，解锁指令，即可以实现：上锁断开油路，解锁恢复油路
     */
    public SRBleSDK setLockThenCloseOilMode(boolean lockThenCloseOilMode) {
        this.lockThenCloseOilMode = lockThenCloseOilMode;
        return this;
    }

    //默认车辆ID值
    private int vid = 1;

    //蓝牙生命周期上下文
    private Activity mContext;

    //程序切换动后台后多少秒后断开链接，默认30秒
    private int stopIntervalAfterInBackground = 30;

    //蓝牙客户端
    private static SRBleClient client = new SRBleClient();

    private SRBleSDK() {
    }

    //是否立即断开连接
    private boolean releaseImmediately = false;

    public SRBleSDK setReleaseImmediately(boolean releaseImmediately) {
        this.releaseImmediately = releaseImmediately;
        return this;
    }

    /**
     * 蓝牙监听
     */
    ChannelListener channelListener = new DefaultChannelListener() {
        @Override
        public void onConnect() {
            if (null != onConnAction) {
                onConnAction.call();
            }
        }

        @Override
        public void onLogin() {
            if (null != onLoginAction) {
                onLoginAction.call();
            }
        }

        @Override
        public void onDisconnect() {
            if (null != onDisConnAction) {
                onDisConnAction.call();
            }
        }

        @Override
        public void onConnecting() {
            if (null != onConnectingAction) {
                onConnectingAction.call();
            }
        }

        @Override
        public void onReceive(Object receiveiMsg) {

        }
    };

    /**
     * 蓝牙关联的SDK
     * Activity 结束后会清理所有资源
     *
     * @param activity
     * @return
     */
    public static SRBleSDK with(Activity activity) {
        EnvUtil.setApplication(activity.getApplication());
        SRBleSDK result = new SRBleSDK();
        result.mContext = activity;
        activity.getApplication().registerActivityLifecycleCallbacks(result.lifecycleCallbacks);
        return result;
    }

    Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity == mContext) {
                whenActivityDestory();
            }
        }
    };

    private void whenActivityDestory() {

        client.stop(mContext, releaseImmediately);
        client.getStatusManager().removeStatusListener(statusListener);
        client.removeListener(channelListener);
        if (null != mContext) {
            Log.d("蓝牙SDK", mContext + "销毁时清空资源");
            mContext.getApplication().unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
            mContext = null;
        }
    }

    /**
     * 设置程序切入后台后多少秒断开蓝牙
     *
     * @param timeou
     * @return
     */
    public SRBleSDK stopTimeAfterInBackground(int timeou) {
        stopIntervalAfterInBackground = timeou;
        return this;
    }

    private Action0 onLoginAction;

    /**
     * 监听蓝牙认证成功事件
     *
     * @param action
     * @return
     */
    public SRBleSDK onLogin(Action0 action) {
        onLoginAction = action;
        return this;
    }

    private Action0 onConnAction;

    /**
     * 监听蓝牙连接上事件
     *
     * @param action
     * @return
     */
    public SRBleSDK onConnect(Action0 action) {
        onConnAction = action;
        return this;
    }


    private Action0 onDisConnAction;

    /**
     * 监听蓝牙断开事件
     *
     * @param action
     * @return
     */
    public SRBleSDK onDisconn(Action0 action) {
        onDisConnAction = action;
        return this;
    }

    private Action0 onConnectingAction;

    /**
     * 监听蓝牙连接中事件
     */
    public SRBleSDK onConnecting(Action0 action) {
        onConnectingAction = action;
        return this;
    }


    ChannelListener lockThenCloseOilListener = new LockThenCloseOilBusiness(client);

    /**
     * 开始连接蓝牙
     *
     * @param mac 蓝牙Mac地址
     * @param id  蓝牙ID
     * @param key 蓝牙Key
     * @return
     */
    public SRBleSDK start(String mac, String id, String key) {
        client.removeListener(channelListener);
        client.addListener(channelListener);

        client.removeListener(lockThenCloseOilListener);
        if (lockThenCloseOilMode) {
            client.addListenerFirst(lockThenCloseOilListener);
        }

        client.start(mContext, vid, mac, id, key);
        return this;
    }

    /**
     * 设置蓝牙低功率连接次数
     * 不调用，默认连接失败3次后开始调用正常功率
     * 其他自然数，表示第几次后，开始调用正常功率连接
     */
    public SRBleSDK setLeBleTimes(int times) {
        SRBleSDK.leBleTimes = times;
        return this;
    }

    /**
     * 断开蓝牙
     */
    public void stop() {
        client.stop(mContext);
    }

    /**
     * 监听车辆状态变更
     *
     * @return
     */
    public SRBleSDK onStatusChange(IStatusListener statusListener) {
        client.getStatusManager().removeStatusListener(statusListener);
        this.statusListener = statusListener;
        client.getStatusManager().addStatusListener(statusListener);
        return this;
    }

    /**
     * 通过蓝牙控制车辆
     *
     * @param command 控制住指令
     * @return
     */
    public Observable<MsgResult<BleData>> command(CommandEnum command) {
        return client.sendControl(vid, command.getCommandID());
    }

    /**
     * 通过蓝牙控制车辆
     *
     * @param commandId 控制住指令
     * @param vid       车辆id
     */
    public Observable<MsgResult<BleData>> command(int vid, int commandId) {
        return client.sendControl(vid, commandId);
    }

    /**
     * 蓝牙启动
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_start() {
        return client.sendControl(vid, CommandEnum.START.getCommandID());
    }

    /**
     * 蓝牙熄火
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_stop() {
        return client.sendControl(vid, CommandEnum.STOP.getCommandID());
    }

    /**
     * 蓝牙解锁
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_unlock() {
        return client.sendControl(vid, CommandEnum.UNLOCK.getCommandID());
    }

    /**
     * 蓝牙上锁
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_lock() {
        return client.sendControl(vid, CommandEnum.LOCK.getCommandID());
    }

    /**
     * 蓝牙寻车
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_call() {
        return client.sendControl(vid, CommandEnum.CALL.getCommandID());
    }

    /**
     * 蓝牙寻车
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_openWindow() {
        return client.sendControl(vid, CommandEnum.OPEN_WINDOW.getCommandID());
    }

    /**
     * 蓝牙寻车
     *
     * @return
     */
    public Observable<MsgResult<BleData>> command_closeWindow() {
        return client.sendControl(vid, CommandEnum.CLOES_WINDOW.getCommandID());
    }

    static final int TAG_BASIC = 0xb301;

    /**
     * 查询车辆基本状态
     * ACC、ON、门、窗、锁
     *
     * @return
     */
    public Observable<Map<Integer, StatusItem>> queryBasicStatus() {
        //基础状态直接查询
        if (client.statusManager.statusItemMap.containsKey(StatusConstant.S_LOCK)) {
            return Observable.just(client.statusManager.statusItemMap);
        }
        return client.queryStatus(TAG_BASIC);
    }

    /**
     * 通过标签（TAG）查询车辆状态
     *
     * @param tags
     * @return
     */
    public Observable<Map<Integer, StatusItem>> queryStatus(int... tags) {
        return client.queryStatus(tags);
    }

    /**
     * 蓝牙是否连接上
     *
     * @return
     */
    public boolean isConnected() {
        return client.isConnected();
    }


    /**
     * 获取车辆状态表
     *
     * @return
     */
    public Map<Integer, StatusItem> getStatusItemMap() {
        return client.getStatusManager().getStatusItemMap();
    }

    /**
     * 车辆状态监听
     */
    IStatusListener statusListener = null;

    /**
     * 设置扫描模式 可以不设置。不设置，就会根据系统版本，自动选择模式，
     * 但部分手机，蓝牙可能和版本不匹配或其他缘故,故开放出来，在部分手机无法正常连接的情况下
     * 分别尝试SRBleSDK.FAST_Lottipop，SRBleSDK.FAST，SRBleSDK.NORMOL，，通常，只需要用到SRBleSDK.FAST_Lottipop和SRBleSDK.NORMOL
     */
    public SRBleSDK setMode(int mode) {
        ScanUtil.setMode(mode);
        return this;
    }
}
