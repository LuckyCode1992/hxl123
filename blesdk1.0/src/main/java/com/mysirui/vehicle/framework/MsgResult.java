package com.mysirui.vehicle.framework;

import com.mysirui.vehicle.constants.BleErrorEnum;

/**
 * Created by marlin on 2017/6/15.
 */
public class MsgResult<T> {

    public MsgResult(int resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "code:"+resultCode+" msg:"+msg;
    }

    public static final MsgResult SUCC = new MsgResult(0,"成功.");
    public static final MsgResult TIME_OUT = new MsgResult(BleErrorEnum.NORESPONSE);
    public static final MsgResult notAuthorized = new MsgResult(BleErrorEnum.NOT_AUTHORIZE);
    public static final MsgResult IDKeyNotCorrect = new MsgResult(BleErrorEnum.TAG_KEY_ERROR);
    public static final MsgResult notConnected2Server = new MsgResult(BleErrorEnum.NOTCONNECTED);
    public static final MsgResult notSupportTag = new MsgResult(BleErrorEnum.TAG_NOTSUPPORT);
    private int resultCode;
    private String msg;
    private T resultEntity;

    public MsgResult() {

    }

    private MsgResult(BleErrorEnum error){
        resultCode = error.getCode();
        msg = error.getMsg();
    }

    public boolean isSucc(){
        return  0 == resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResultEntity() {
        return resultEntity;
    }

    public void setResultEntity(T resultEntity) {
        this.resultEntity = resultEntity;
    }
}
