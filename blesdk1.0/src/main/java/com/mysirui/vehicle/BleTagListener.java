package com.mysirui.vehicle;

import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.IMsgListener;

/**
 * 监听某个Tag
 * Created by marlin on 2017/7/18.
 */

public abstract class BleTagListener implements IMsgListener<BleData> {

    private int tag;

    public BleTagListener(int tag) {
        this.tag = tag;
    }

    @Override
    public boolean isMyMsg(BleData msg) {
        return msg.getOperationType() == tag;
    }

    @Override
    public boolean willUnReg() {
        return false;
    }

}
