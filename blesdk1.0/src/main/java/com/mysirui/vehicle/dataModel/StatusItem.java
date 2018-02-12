package com.mysirui.vehicle.dataModel;

import com.mysirui.vehicle.constants.StatusConstant;

/**
 * Created by marlin on 2017/6/15.
 * 状态项
 */

public class StatusItem {

    public StatusItem(int statusKey) {
        this.statusKey = statusKey;
    }

    public StatusItem(int statusKey, int status, String value) {
        this.statusKey = statusKey;
        this.status = status;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    private String name;

    //状态ID，如 引擎、电压等
    private int statusKey;

    //当前状态
    private int status = 0;

    //状态分级、如GMS分级的信号强度
    private int level;

    //状态值，如温度值、电压值、速度值等
    private String value;

    public boolean isON(){
        return StatusConstant.VALUE_ON == status;
    }

    public boolean isValid(){
        return StatusConstant.VALUE_ON == status;
    }

    public int getStatusKey() {
        return statusKey;
    }

    public void setStatusKey(int statusKey) {
        this.statusKey = statusKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static StatusItem buildEmpty(int tag){
        return new StatusItem(tag);
    }
    public static StatusItem buildOffline(int tag){
        StatusItem result = new StatusItem(tag);
        result.setStatus(StatusConstant.VALUE_OFF);
        return result;
    }

    @Override
    public String toString() {
        return "key:"+statusKey+" status:"+status+" value:"+value;
    }
}
