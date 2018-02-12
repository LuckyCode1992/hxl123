package com.mysirui.vehicle;

import com.mysirui.vehicle.constants.MsgConstant;
import com.mysirui.vehicle.constants.BleTagConstant;
import com.mysirui.vehicle.constants.TerminalConstants;
import com.mysirui.vehicle.constants.SRGattAttributes;
import com.mysirui.vehicle.dataModel.BleData;
import com.mysirui.vehicle.framework.IBleMsgCoder;
import com.mysirui.vehicle.util.ByteUtil;
import com.mysirui.vehicle.util.StringUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marlin on 2017/7/3.
 */

public class BleMsgCoder implements IBleMsgCoder<BleData> {

    static BleData heart = BleData.buildHeart();
    @Override
    public BleData buildHeartMsg() {
        return heart;
    }

    static BleData loginQuery = BleData.buildQueryAuthcode();

    @Override
    public BleData buildLoginMsg() {
        return loginQuery;
    }

    //1K缓存
    ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    @Override
    public List<BleData> parseMsg(byte[] byteMsg) {
        rBuffer.put(byteMsg);

        if(0!=byteMsg[byteMsg.length-1]){ //未结束
            return null;
        }

        rBuffer.flip();
        BleData data =  BleData.dataFromString(byteBufferToString(rBuffer));
        rBuffer.clear();
        if(null==data){
            return null;
        }
        return Arrays.asList(data);

    }
    public static String byteBufferToString(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void resetResource() {
        rBuffer.clear();
    }
    @Override
    public ByteBuffer buildMsg(BleData msg) {
        if(null==msg){
            return null;
        }
        byte[] bytes = msg.toBytes();
        ByteBuffer result = ByteBuffer.allocate( bytes.length);
        result.put(bytes);
        return result;
    }

    @Override
    public boolean isResponseOf(BleData sendMsg, BleData receiveiMsg) {
        if(null==sendMsg || null==receiveiMsg){
            return false;
        }

        //7#b502控制响应为7#b402
        if(sendMsg.getMessageType() == MsgConstant.Message_Publish &&
            sendMsg.getOperationType() == BleTagConstant.Operation_B502
                ){
            return receiveiMsg.getMessageType() == MsgConstant.Message_Publish &&
                    receiveiMsg.getOperationType() == BleTagConstant.Operation_B402;
        }

        //不支持标签应答
        if(receiveiMsg.getOperationType() == BleTagConstant.Operation_B443){
            int ivalidTag = StringUtil.hex2IntValue(receiveiMsg.getOperationParamenter());
            if(sendMsg.getOperationType() == ivalidTag && sendMsg.getTerminalType() == receiveiMsg.getTerminalType()){
                return true;
            }
        }

        return sendMsg.getOperationType() == receiveiMsg.getOperationType() &&
                sendMsg.getMessageType() + 1 == receiveiMsg.getMessageType() ; //参见SRBleMessageConstants
    }

    @Override
    public boolean isNotSupportAck(BleData sendMsg,BleData receiveiMsg) {
        return receiveiMsg.getOperationType() == BleTagConstant.Operation_B443
                && sendMsg.getTerminalType() == receiveiMsg.getTerminalType();
    }

    @Override
    public BleData buildResponse(BleData pushMsg) {
        if( null==pushMsg || pushMsg.getMessageType()%2!=1 ){ //参见SRBleMessageConstants
            return null;
        }
        return new BleData(pushMsg.getTerminalType(),pushMsg.getMessageType()+1,pushMsg.getOperationType(),null);
    }

    @Override
    public String getUUIDOfChar(BleData msg) {
        return msg.getTerminalType() == TerminalConstants.Terminal_Ble? SRGattAttributes.BLE_WRITE_DATA:SRGattAttributes.TERMINAL_WRITE_DATA;
    }
}
