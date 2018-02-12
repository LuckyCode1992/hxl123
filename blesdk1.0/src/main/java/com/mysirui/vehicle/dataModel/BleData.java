package com.mysirui.vehicle.dataModel;

import com.mysirui.vehicle.constants.CommandEnum;
import com.mysirui.vehicle.constants.MsgConstant;
import com.mysirui.vehicle.constants.BleTagConstant;
import com.mysirui.vehicle.constants.PacketConstants;
import com.mysirui.vehicle.constants.TerminalConstants;
import com.mysirui.vehicle.util.BleCodeUtil;
import com.mysirui.vehicle.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BleData {


	private boolean isLockOrUnlockCmd = false;

	public boolean isLockOrUnlockCmd() {
		return isLockOrUnlockCmd;
	}

	private final static String TAG = BleData.class.getSimpleName();
	
	private static final int kMaxPackageSize = 560; //每包最大60字节
	
	private int terminalType;
	private int otherTarget = 0;

	public void setOtherTarget(int otherTarget) {
		this.otherTarget = otherTarget;
	}

	private int messageType;
	private int operationType;
	private String operationParamenter;

	public BleData(int terminal, int message, int operation, String paramenter) {
		this.terminalType = terminal;
		this.messageType = message;
		this.operationType = operation;
		this.operationParamenter = paramenter;
	}

	public static BleData dataFromString(String string) {

		if(null==string || !string.startsWith("*")){ //错误
            return null;
		}

		String[] ss = string.split("\\*");
		if(ss.length > 2){
			string = "*"+ss[ss.length-1];
		}

		int crcLoc = string.indexOf("#");
		int crc = Integer.parseInt(string.substring(1, crcLoc), 16);
		
		int crcCount = 0;
		String sub = string.substring(crcLoc+1);
		for(char cha : sub.toCharArray())
			crcCount += cha;
		
		crcCount &= 0xff;
		
		if(crcCount != crc) {
			return null;
		}
		
		String[] temp = sub.split("#");
		int terminalType = Integer.parseInt(temp[0], 16);
		int messageType = Integer.parseInt(temp[1], 16);
		
		int operationType;
		String operationParamenter = "";
		int seperatorLoc = temp[2].indexOf(",");
		if(seperatorLoc > 0) {
			operationType = Integer.parseInt(temp[2].substring(0, seperatorLoc), 16);
			String operationParamenterTemp = temp[2].substring(seperatorLoc + 1);

			for (byte b: operationParamenterTemp.getBytes()) {
				if (b != 0) {
					operationParamenter += (char)b;
				}
			}

		} else {
			operationType = Integer.parseInt(temp[2], 16);
		}

		return new BleData(terminalType, messageType, operationType, operationParamenter);
	}

	public static BleData buildQueryAuthcode(){
		return new BleData(TerminalConstants.Terminal_Ble,
				MsgConstant.Message_Query,
				BleTagConstant.Operation_A605,
				null);
	}


	public static BleData buildAuth(BleData data){
		return  BleData.bleAuthDataRsp(Integer.parseInt(data.getOperationParamenter().split(",")[0]));
	}

	//蓝牙认证消息
	public static BleData bleAuthDataRsp(int authCode) {
		return new BleData(TerminalConstants.Terminal_Ble,
				MsgConstant.Message_Excute,
				BleTagConstant.Operation_A606,
				BleCodeUtil.authStringWithID(authCode));
	}
	
	//大数据发送申请
	public static BleData bleBigDataReq(){
		return new BleData(TerminalConstants.Terminal_Ble,
				MsgConstant.Message_Publish,
				BleTagConstant.Operation_A607,
				null);
	}
	
	//设备连接状态
	public static BleData terminalConnectionReq(){
		return new BleData(TerminalConstants.Terminal_Ble,
				MsgConstant.Message_Query,
				BleTagConstant.Operation_A304,
				null);
	}


    public static BleData buildStartUpgradeReq(int termID, String operationParamenter){
        return new BleData(termID,
                MsgConstant.Message_Excute,
                BleTagConstant.Operation_B801,
                operationParamenter);
    }
	
	//查询ID机密钥
	public static BleData buildQueryIDKeyMsg(){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Query,
				BleTagConstant.Operation_B203,
				null);
	}

	public static BleData bulidQuery(int tag){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Query,
				tag,
				null);
	}

	//设置ID及密钥
	public static BleData configEncryptionInfoReq(String idStr, String keyStr){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Config,
				BleTagConstant.Operation_B203,
				idStr+","+keyStr);
	}

	//查询控制滚动码
	public static BleData queryControlNumberReq(){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Query,
				BleTagConstant.Operation_B502,
				null);
	}

	//控制
	public static BleData controlReq(int instruction, String controlNumber, String idStr, String keyStr){
		String operationParamenter = BleCodeUtil.controlDataEncyption(instruction,
				Integer.parseInt(controlNumber, 16), idStr, keyStr);
		BleData r = new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Publish,
				BleTagConstant.Operation_B502,
				operationParamenter);
		if(CommandEnum.LOCK.getCommandID() == instruction ||
				CommandEnum.UNLOCK.getCommandID() == instruction
				){
			r.isLockOrUnlockCmd = true;
		}
		return r;
	}

	//状态查询
	public static BleData queryStatusReq(){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Query,
				BleTagConstant.BasicStatus_B301,
				null);
	}

	//终端基本信息查询
	public static BleData queryTerminalBasicInfoReq(){
		return new BleData(TerminalConstants.Terminal_Broadcast,
				MsgConstant.Message_Query,
				BleTagConstant.Operation_B101,
				null);
	}

	//回复
	public static BleData ackRsp(int terminalType, int operationType){
		return new BleData(terminalType,
				MsgConstant.Message_Publich_Confirm,
				operationType,
				null);
	}

	//升级固件
	
	//升级分片回应
	
	public String toString() {
		List<String> list = new ArrayList<String>();
//		list.add("#"+ Integer.toHexString(terminalType&0xff));
		list.add(""+ Integer.toHexString(terminalType&0xff));
		/*if(
				terminalType!=TerminalConstants.Terminal_Broadcast
				&& otherTarget != TerminalConstants.Terminal_Broadcast
				&& otherTarget != terminalType
				){
			list.add(","+Integer.toHexString(otherTarget&0xff));
		}*/
		list.add("#"+ Integer.toHexString(messageType&0xff));
		list.add("#"+ Integer.toHexString(operationType&0xffff));
		if(!StringUtil.isEmpty(operationParamenter))
			list.add(","+ operationParamenter);
		list.add("#");
		
		String preCrcStr = StringUtil.join(list, "");
		
		int crcCount = 0;
		for(char cha : preCrcStr.toCharArray()){
			crcCount += cha;
			crcCount &= 0xff;
		}
		
		String crcStr = "*"+ Integer.toHexString(crcCount)+"#"+preCrcStr;
        
		return crcStr;
	}
	
	public byte[] toBytes() {
		byte[] bytes = toString().getBytes();

		int packetLength = kMaxPackageSize - 2;
	    int total = bytes.length/packetLength + (bytes.length%packetLength==0?0:1);
	    int newLength = bytes.length + total*2;
	    byte[] newBytes = new byte[newLength];
	    
	    int index = 0;
	    int position = 0;
	    do {
	    	int packetFlag;
	    	if (index == 0 && index == total-1) { //只有一片
	    		packetFlag = PacketConstants.PacketFlag_Start_End;
	    	} else if (index == 0) {             //启始片
	    		packetFlag = PacketConstants.PacketFlag_Start;
	    	} else if (index == total-1) {       //结尾片
	    		packetFlag = PacketConstants.PacketFlag_End;
	    	} else {                             //中间片
	    		packetFlag = PacketConstants.PacketFlag_Middle;
	    	}
	    	
	    	int length = index*packetLength+packetLength>bytes.length?(bytes.length-index*packetLength):packetLength;
	    	byte[] tempBytes = new byte[length+2];
	    	//分片标志
	    	tempBytes[0] = Integer.toHexString(packetFlag).getBytes()[0];
	    	//数据
	    	System.arraycopy(bytes, position, tempBytes, 1, length);
	    	//结束符
	    	tempBytes[length+1] = 0x00;
	    	
	    	System.arraycopy(tempBytes, 0, newBytes, index*kMaxPackageSize, tempBytes.length);

	    	++index;
	    	position += packetLength;
	    } while(position < bytes.length);
	    
		return newBytes;
	}

	public static BleData buildHeart(){
		return new BleData(TerminalConstants.Terminal_Ble,
				MsgConstant.Message_Excute,
				BleTagConstant.Operation_Heart_A608,
				null);
	}

	public boolean isBigData(){
		return toBytes().length > kMaxPackageSize;
	}
	
	public int getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(int terminalType) {
		this.terminalType = terminalType;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public int getOperationType() {
		return operationType;
	}
	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}
	public String getOperationParamenter() {
		return operationParamenter;
	}
	public void setOperationParamenter(String operationParamenter) {
		this.operationParamenter = operationParamenter;
	}
}
