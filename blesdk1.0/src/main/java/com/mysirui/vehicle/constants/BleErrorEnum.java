package com.mysirui.vehicle.constants;

/**
 * Created by marlin on 2017/8/9.
 */

public enum BleErrorEnum {



    UNKNOWEXCEPTION(1,"未知系统异常"),
    NOTOPEN(2,"蓝牙未打开"),
    NOTCONNECTED(3,"还未连接上蓝牙设备"),
    NOTFOUNE_SERVICE(4,"蓝牙服务未找到"),
    NOTFOUNE_CHAR(5,"蓝牙特性未找到"),
    FAIL_OPENNOTIFY(6,"打开读通知失败"),
    FAIL_WRITE(7,"蓝牙发送数据失败"),
    NORESPONSE(8,"蓝牙消息应答超时"),
    FAIL_AUTHORIZE(9,"主机认证授权失败"),
    NORESPONSE_AUTHORIZE(10,"主机未响应认证消息，LOCBUS通信错误"),
    NORESPONSE_LOGIN(11,"蓝牙未响应登录请求"),
    NOT_AUTHORIZE(12,"主机还未认证授权"),
    TAG_NOTSUPPORT(13,"不支持的标签"),
    TAG_KEY_ERROR(19,"秘钥错误"),
    ;



    private int code;
    private String msg;

    BleErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "错误码:"+code+" 内容:"+msg;
    }
}
