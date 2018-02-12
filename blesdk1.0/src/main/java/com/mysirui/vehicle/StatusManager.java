package com.mysirui.vehicle;

import com.mysirui.vehicle.constants.StatusConstant;
import com.mysirui.vehicle.constants.BleTagConstant;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.EmptyChannelListener;
import com.mysirui.vehicle.framework.IStatusListener;
import com.mysirui.vehicle.dataModel.StatusItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆状态处理
 * Created by marlin on 2017/7/18.
 */

public class StatusManager extends EmptyChannelListener<BleData> {

    BleRawClient<BleData> rawClient ;
    BLEChannel channel;
    Map<Integer,StatusItem> statusItemMap = new HashMap<Integer,StatusItem>();

    List<IStatusListener> listenerList = new ArrayList<IStatusListener>();

    public void addStatusListener(IStatusListener listener){
        if(null!=listener){
            listenerList.add(listener);
        }
    }

    public void removeStatusListener(IStatusListener listener){
        if(null!=listener){
            listenerList.remove(listener);
        }
    }

    public StatusManager(BleRawClient<BleData> rawClient, BLEChannel channel) {
        this.rawClient = rawClient;
        this.channel = channel;

        //基础状态
        channel.addMsgListener(new BleTagListener(BleTagConstant.BasicStatus_B301) {
            @Override
            public void handle(BleData msg) {
                handleBasicStatuc(msg);
            }
        });
        channel.addMsgListener(new BleTagListener(BleTagConstant.Mileage_B311) {
            @Override
            public void handle(BleData msg) {
                handleMileage(msg);
            }
        });
        channel.addMsgListener(new BleTagListener(BleTagConstant.LeftOil_B312) {
            @Override
            public void handle(BleData msg) {
                handleLeftOil(msg);
            }
        });
        channel.addMsgListener(new BleTagListener(BleTagConstant.LeftElec_B313) {
            @Override
            public void handle(BleData msg) {
                handleLeftElec(msg);
            }
        });
        channel.addMsgListener(new BleTagListener(BleTagConstant.Elec_B310) {
            @Override
            public void handle(BleData msg) {
                handleElec(msg);
            }
        });
    }

    @Override
    public void onConnect() {
        statusItemMap.clear();
    }

    @Override
    public void onDisconnect() {
        statusItemMap.clear();
    }


    /*
    B301	Q	P
    Set数组，	acc+on+引擎+行驶+IG1+油路
	Set数组，	门边 左前+右前+左后+右后+后箱+总门边+门灯
	Set数组，	门锁 左前+右前+左后+右后+总门锁+防盗状态主机锁状态
	Set数组，	窗关 左前+右前+左后+右后+天窗+总4门玻璃
	Set数组，	灯亮 大灯+小灯+刹车（总线）+刹车（线接）
	Set，	    防盗设防+侵入
	Set，	    告警 告警编码 0.没有错误 1CAN错误 2.按键错误 3.IG1错误 4.拨码不支持 5.外部晶体工作错误 6.系统重启
	Set，	    CAN  总线错误+CAN休眠
	Set，	设置信息 静音+展车模式+当前钥匙（范围0-1）+深度休眠
	Hex	允许启动时间*10MS
     */
    private void handleBasicStatuc(BleData msg){
        String[] set = split(msg);

        addB301MixStatus(StatusConstant.S_DOOR,set,1,StatusConstant.VALUE_ON,StatusConstant.VALUE_OFF);
        addB301Status(StatusConstant.S_DOOR_LF,set,1,0);
        addB301Status(StatusConstant.S_DOOR_RF,set,1,2);
        addB301Status(StatusConstant.S_DOOR_LB,set,1,1);
        addB301Status(StatusConstant.S_DOOR_RB,set,1,3);
        addB301Status(StatusConstant.S_DOOR_TRUNCK,set,1,4);

        addB301MixStatus(StatusConstant.S_LOCK,set,2,StatusConstant.VALUE_ON,StatusConstant.VALUE_OFF);


        addB301MixStatus(StatusConstant.S_WINDOW,set,3,StatusConstant.VALUE_ON,StatusConstant.VALUE_OFF);
        addB301Status(StatusConstant.S_WINDOW_LF,set,3,0);
        addB301Status(StatusConstant.S_WINDOW_LB,set,3,1);
        addB301Status(StatusConstant.S_WINDOW_RF,set,3,2);
        addB301Status(StatusConstant.S_WINDOW_RB,set,3,3);
        addB301Status(StatusConstant.S_WINDOW_SKY,set,3,4);

        addB301Status(StatusConstant.S_ACC,set,0,0);
        addB301Status(StatusConstant.S_ON,set,0,1);
        addB301Status(StatusConstant.S_ENGINE,set,0,2);

        runStatusListener(
                StatusConstant.S_ACC,
                StatusConstant.S_ON,
                StatusConstant.S_ENGINE,
                StatusConstant.S_LOCK,
                StatusConstant.S_DOOR,
                StatusConstant.S_DOOR_LF,
                StatusConstant.S_DOOR_LB,
                StatusConstant.S_DOOR_RF,
                StatusConstant.S_DOOR_RB,
                StatusConstant.S_DOOR_TRUNCK,
                StatusConstant.S_WINDOW,
                StatusConstant.S_WINDOW_LF,
                StatusConstant.S_WINDOW_LB,
                StatusConstant.S_WINDOW_RF,
                StatusConstant.S_WINDOW_RB,
                StatusConstant.S_WINDOW_SKY
        );

    }

    private void handleMileage(BleData msg){
        addHexValue(StatusConstant.S_MILEAGE,split(msg));
        runStatusListener(StatusConstant.S_MILEAGE);
    }

    private void handleLeftOil(BleData msg){
        addSimpleStatusAndHexValue(StatusConstant.S_LEFT_OIL,split(msg));
        runStatusListener(StatusConstant.S_LEFT_OIL);
    }

    private void handleLeftElec(BleData msg){
        addSimpleStatusAndHexValue(StatusConstant.S_LEFT_ELEC,split(msg));
        runStatusListener(StatusConstant.S_LEFT_ELEC);
    }

    private void runStatusListener(int ... tags){
        List<StatusItem> statusItems = new ArrayList<StatusItem>();
        for(int tag:tags){
            StatusItem item = statusItemMap.get(tag);
            if(null!=item){
                statusItems.add(item);
            }
        }
        for (IStatusListener listener:listenerList){
            listener.onStatusChange(statusItems);
        }
    }

    private void addB301MixStatus(int key,String[] set,int index,int openValue,int closeValue ){
        addStatus(key,parseMixStatusDoorOrLockOrWindow(set,index,openValue,closeValue));
    }

    private int parseMixStatusDoorOrLockOrWindow(String[] set,int index,int openValue,int closeValue){
        String sSet = set[index].substring(0,4);
        if(sSet.contains(String.valueOf(openValue))){
            return openValue;
        }else if(sSet.contains(String.valueOf(closeValue))){
            return closeValue;
        }else {
            return StatusConstant.VALUE_UNKONW;
        }
    }

    private void addB301Status(int key,String[] set,int setIndex,int sstatusIndex){
        addStatus(key,parseB301Status(set,setIndex,sstatusIndex));
    }

    private int parseB301Status(String[] statusSet, int setIndex,int statusIndex){
        return Integer.parseInt(statusSet[setIndex].substring(statusIndex,statusIndex+1));
    }


    private void handleElec(BleData msg){

    }



    private void addStatusAndHexValue(int key,String status,String value){
        try {
            addStatusAndValue(key,Integer.parseInt(status),String.valueOf(Integer.parseInt(value,16)));
        }catch (Exception e){
            addStatusAndValue(key,StatusConstant.VALUE_UNKONW,null);
        }

    }

    private void addSimpleStatusAndValue(int key,String[] sv){
        addStatusAndIntValue(key,sv[0],sv[1]);
    }

    private void addHexValue(int key,String[] sv){
        addStatusAndValue(key,StatusConstant.VALUE_ON,parseHex2Int(sv[0]));
    }

    private String parseHex2Int(String hex){
        try {
            return String.valueOf(Integer.parseInt(hex,16));
        }catch (Exception ex){
            return "0";
        }
    }

    private void addSimpleStatusAndHexValue(int key,String[] sv){
        try {
            addStatusAndHexValue(key,sv[0],sv[1]);
        }catch (Exception e){
            addStatusAndValue(key,StatusConstant.VALUE_UNKONW,null);
        }

    }

    private void addStatusAndIntValue(int key,String status,String value){
        addStatusAndValue(key,Integer.parseInt(status),value);
    }

    private void addStatus(int key,int status){
        statusItemMap.put(key,new StatusItem(key,status,null));
    }

    private void addStatusAndValue(int key,int status,String value){
        statusItemMap.put(key,new StatusItem(key,status,value));
    }

    private String[] split(BleData msg){
        return msg.getOperationParamenter().split(",");
    }

    private int getInt(String[] paras,int index){
        return Integer.parseInt(paras[index]);
    }

    private int getHex(String[] paras,int index){
        return Integer.parseInt(paras[index],16);
    }

    public Map<Integer, StatusItem> getStatusItemMap() {
        return statusItemMap;
    }
}
