package com.mysirui.vehicle;

import android.util.Log;

import com.mysirui.vehicle.constants.BleErrorEnum;
import com.mysirui.vehicle.constants.BleTagConstant;
import com.mysirui.vehicle.constants.MsgConstant;
import com.mysirui.vehicle.constants.SRBleResultCode;
import com.mysirui.vehicle.constants.TerminalConstants;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.AlwaysMsgListener;
import com.mysirui.vehicle.framework.IMsgListener;
import com.mysirui.vehicle.framework.MsgChannel;
import com.mysirui.vehicle.framework.MsgCoder;
import com.mysirui.vehicle.framework.MsgResult;
import com.mysirui.vehicle.util.BleCodeUtil;
import com.mysirui.vehicle.util.ByteUtil;
import com.mysirui.vehicle.util.RxUtil;
import com.mysirui.vehicle.util.StringUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by marlin on 2017/7/3.
 */

public class BLEChannel extends MsgChannel<BleData> {

    static String TAG = "BLEChannel";

    private String id;
    private String key;

    /**
     * 设备存储的ID值
     */
    private String deviceRandomID;
    /**
     * 设置ID号
     */
    private int locbusID;

    private boolean isIDSynced() {
        return null != deviceRandomID
                && (deviceRandomID.toLowerCase().endsWith(id.toLowerCase()) || id.toLowerCase().endsWith(deviceRandomID.toLowerCase()));
    }

    private BleRawClient<BleData> bleClient;

    public BLEChannel(BleRawClient<BleData> client, MsgCoder<BleData> coder) {
        super(client, coder);
        bleClient = client;

        //通用的消息处理机制
        this.addMsgListener(hearMsgHandler); //心跳
        this.addMsgListener(responseHandler);//应答

    }

    IMsgListener<BleData> responseHandler = new AlwaysMsgListener<BleData>() {
        @Override
        public boolean isMyMsg(BleData msg) {
            return msg.getOperationType() == BleTagConstant.Operation_Heart_A608;
        }

        @Override
        public void handle(BleData msg) {
            sendMsg(BleData.buildHeart());
        }
    };

    IMsgListener<BleData> hearMsgHandler = new AlwaysMsgListener<BleData>() {
        @Override
        public boolean isMyMsg(BleData msg) {
            return msg.getMessageType() == MsgConstant.Message_Publish;
        }

        @Override
        public void handle(BleData msg) {
            sendMsg(coder.buildResponse(msg));
        }
    };


    public void setIdAndKey(String id, String key) {
        this.id = id;
        if (key.length() <= 6) {
            StringBuffer sb = new StringBuffer();

            for (char c : key.toCharArray()) {
                sb.append(Integer.toHexString(c));
            }
            key = sb.toString();
        }
        this.key = key;
    }

    public Observable<MsgResult<BleData>> sendControl(final int vehicleID, final int command) {

        if (!client.isConnected()) {  //未链接
            return RxUtil.flushResult(MsgResult.notConnected2Server);
        } else if (null == deviceRandomID) {  //未认证
            return RxUtil.flushResult(MsgResult.notAuthorized);
        } else if (isIDSynced()) {
            return sendMsg(BleData.queryControlNumberReq(), timeout_login)//查询B502查询指令
                    .flatMap(new Func1<MsgResult<BleData>, Observable<MsgResult<BleData>>>() {
                        @Override
                        public Observable<MsgResult<BleData>> call(MsgResult<BleData> bleDataMsgResult) {
                            if (bleDataMsgResult.isSucc()) {                 //发送B502控制指令
                                BleData commandMsg = BleData.controlReq(command,
                                        bleDataMsgResult.getResultEntity().getOperationParamenter(),
                                        id, key);
                                return sendMsg(commandMsg, timeout_login);
                            } else {
                                return RxUtil.flushResult(MsgResult.TIME_OUT);
                            }
                        }
                    }).map(new Func1<MsgResult<BleData>, MsgResult<BleData>>() {  //重组结果
                        @Override
                        public MsgResult<BleData> call(MsgResult<BleData> bleDataMsgResult) {
                            if (bleDataMsgResult.isSucc()) {                        //将返回结果直接
                                int code = StringUtil.safeParse(bleDataMsgResult.getResultEntity().getOperationParamenter().split(",")[1]);
                                if (code <= 2) { //1、2都表示成功
                                    code = 0;
                                }
                                String rMsg = SRBleResultCode.stringValue(code);
                                bleDataMsgResult.setResultCode(code);
                                bleDataMsgResult.setMsg(rMsg);
                            }
                            return bleDataMsgResult;
                        }
                    });
        } else {
            return RxUtil.flushResult(MsgResult.IDKeyNotCorrect);
        }

    }

    @Override
    public void onConnect() {
        super.onConnect();
        deviceRandomID = null;
        loginProcess();
    }

    public void sendMsg(final BleData msg) {
        autoSetTarget(msg);
        super.sendMsg(msg);
    }

    private void autoSetTarget(BleData msg) {
        if (0 == msg.getTerminalType() && 0 != locbusID && !msg.isLockOrUnlockCmd() && msg.getOperationType() != BleTagConstant.Operation_B203) {
            msg.setTerminalType(locbusID);
        }
    }

    public Observable<MsgResult<BleData>> sendMsg(final BleData msg, final int timeOut) {
        autoSetTarget(msg);
        return super.sendMsg(msg, timeOut);
    }

    public Observable<MsgResult<BleData>> sendMsgWithDefaultTimeout(final BleData msg) {
        autoSetTarget(msg);
        return super.sendMsg(msg, timeout_login);
    }

    /**
     * 登录超时时间
     */
    int timeout_login = 5 * 1000;

    /**
     * 登录处理流程
     * -> A605
     * <- A605
     * -> A606
     */
    private void loginProcess() {
        sendMsg(coder.buildLoginMsg(), timeout_login)
                .subscribe(new Action1<MsgResult<BleData>>() {
                    @Override
                    public void call(MsgResult<BleData> bleDataMsgResult) {
                        if (!bleDataMsgResult.isSucc()) {
                            client.disConnWhenError(BleErrorEnum.NORESPONSE_LOGIN);
                        } else {
                            sendMsg(BleData.buildAuth(bleDataMsgResult.getResultEntity())); //发送A606认证
                            //处理密钥流程
                            idKeySyncProcess();
                        }
                    }
                }, err);
    }

    /**
     * ID Key同步处理流程
     */
    private void idKeySyncProcess() {
        sendMsg(BleData.buildQueryIDKeyMsg(), timeout_login) //B203 查询ID Key
                .flatMap(new Func1<MsgResult<BleData>, Observable<MsgResult<BleData>>>() {
                    @Override
                    public Observable<MsgResult<BleData>> call(MsgResult<BleData> bleDataMsgResult) {
                        if (bleDataMsgResult.isSucc()) {
                            String deviceIDAndCRC = bleDataMsgResult.getResultEntity().getOperationParamenter();
                            if (bleDataMsgResult.getResultEntity().getTerminalType() > TerminalConstants.Terminal_OTU) {
                                locbusID = bleDataMsgResult.getResultEntity().getTerminalType();
                            }
                            String[] idAndCRC = deviceIDAndCRC.split(",");
                            deviceRandomID = idAndCRC[0];
                            if (BleCodeUtil.isIDKeyCorrect(id, key, deviceRandomID, idAndCRC[1])) {  //配置OK
                                Log.i(TAG, "认证通过");
                                return RxUtil.flushResult(MsgResult.SUCC);
                            } else if (ByteUtil.isAllZero(deviceRandomID)) {         //下发配置
                                Log.i(TAG, "认证配置初始化");
                                deviceRandomID = id;
                                return sendMsg(BleData.configEncryptionInfoReq(id, key), timeout_login);
                            } else {                                                 //配置不一致
                                Log.i(TAG, "认证失败");
                                return RxUtil.flushResult(MsgResult.IDKeyNotCorrect);
                            }
                        } else {
                            return RxUtil.flushResult(bleDataMsgResult);
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<MsgResult<BleData>>() {
                    @Override
                    public void call(MsgResult<BleData> bleDataMsgResult) {                    //配置应答消息
                    /*
                    if(bleDataMsgResult.isSucc()){
                        if(null!=bleDataMsgResult.getResultEntity()){
                            String deviceIDAndCRC = bleDataMsgResult.getResultEntity().getOperationParamenter();
                            deviceRandomID = deviceIDAndCRC.split(",")[1];
                        }
                    } else {
                        client.disConnWhenError(BleErrorEnum.NORESPONSE);
                    }*/
                        //B203完成后设置为登录
                        bleClient.setLogined();
                    }
                }, err);
    }

    Action1<Throwable> err = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            throwable.printStackTrace();
            Log.d("ble","真正原因"+throwable.getMessage());
            client.disConnWhenError(BleErrorEnum.UNKNOWEXCEPTION);
        }
    };


}
