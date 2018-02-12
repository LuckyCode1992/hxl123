package com.mysirui.vehicle.businessModel;

import com.mysirui.vehicle.BleRawClient;
import com.mysirui.vehicle.SRBleClient;
import com.mysirui.vehicle.constants.BleTagConstant;
import com.mysirui.vehicle.constants.MsgConstant;
import com.mysirui.vehicle.constants.TerminalConstants;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.ChannelListener;
import com.mysirui.vehicle.framework.DefaultChannelListener;
import com.mysirui.vehicle.util.RxUtil;

/**
 * Created by marlin on 2017/11/2.
 */

public class LockThenCloseOilBusiness extends DefaultChannelListener<BleData> {


    SRBleClient client ;

    public LockThenCloseOilBusiness(SRBleClient client) {
        this.client = client;
    }

    //收到主机滚动码后  广播给OTU
    @Override
    public void onReceive(BleData receiveiMsg) {

        //主机滚动码应答  b502
        if(BleTagConstant.Operation_B502 == receiveiMsg.getOperationType()
           && MsgConstant.Message_Query_Ack == receiveiMsg.getMessageType()
                ){
            BleData data = new BleData(TerminalConstants.Terminal_OTU,
                    MsgConstant.Message_Excute,
                    BleTagConstant.Operation_B605,
                    receiveiMsg.getOperationParamenter());

            client.sendMsg(data).subscribe(RxUtil.emptyResultAction,RxUtil.emptyErrAction);
        }
    }

    @Override
    public void willSend(BleData sendMsg) {
        //开关锁控制指令
        if(sendMsg.getOperationType() == BleTagConstant.Operation_B502
           && sendMsg.getMessageType() == MsgConstant.Message_Publish
           && isLockOrUnlock(sendMsg)  ){
           sendMsg.setOtherTarget(TerminalConstants.Terminal_OTU);
        }
    }


    public boolean isLockOrUnlock(BleData msg){
        return msg.isLockOrUnlockCmd();
    }

}
