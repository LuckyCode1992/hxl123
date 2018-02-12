package com.mysirui.vehicle.util;

/**
 *   蓝牙交互协议中加密帮助类
 */
public class BleCodeUtil {

	public static void main(String[] args){
		String coded = authStringWithID(127819);
		System.out.println(coded);
		System.out.print(controlDataEncyption(12,12,"asdd","asdaasda"));
	}

	public final static String kBTUAuth                      = "2i0L1o5V0f8u2z9ik-@~";


	public static boolean isIDKeyCorrect(String idStr,String keyStr,String deviceRandomID,String keyCRC){


		if(StringUtil.isEmpty(idStr) || StringUtil.isEmpty(keyStr))
			return false;

		if(keyStr.length()<=6){
			StringBuffer sb = new StringBuffer();

			for(char c:keyStr.toCharArray()){
				sb.append(Integer.toHexString(c));
			}
			keyStr = sb.toString();
		}
		if(idStr.length()%2!=0)
			idStr = "0"+idStr;

		if(keyStr.length()%2!=0)
			keyStr = "0"+keyStr;

		int crcCount = 0;
		for(int i=0; i+2 <= idStr.length(); i+=2) {
			crcCount += Integer.parseInt(idStr.substring(i, i+2), 16);
			crcCount &= 0xff;
		}

		for(int i=0; i+2 <= keyStr.length(); i+=2) {
			crcCount += Integer.parseInt(keyStr.substring(i, i+2), 16);
			crcCount &= 0xff;
		}

		crcCount &= 0xff;

		return crcCount == Integer.parseInt(keyCRC, 16);
	}

	/**
	 * 将A605应答消息中授权码加密
	 * @param authID 授权码
	 * @return 加密后的授权码
	 */
	public static String authStringWithID(int authID) {
		if(authID==0) return null;
		
		authID += 0x13572468;
	    authID ^= 0x50A00A05;
	    
	    byte[] auth_unit = new byte[4];
	    for(int i = 0; i < auth_unit.length; i++) {
	        auth_unit[i] = (byte) (authID >> (8*i));
	    }
	  
	    byte[] keyBytes = kBTUAuth.getBytes();
	    int i, j;
	    for(i=0, j=0; i<keyBytes.length; i++, j++) {
	    	if (j >= 4) {j = 0;}
	    	keyBytes[i] -= auth_unit[j];
	    }
	    
	    return SRBleUtils.bytesToHexString(keyBytes);
	}


	/**
	 * 使用 ID Key对控制指令和流水号进行加密
	 * @param command
	 * @param serialNumber
	 * @param idStr
	 * @param keyStr
	 * @return
	 */
	public static String controlDataEncyption(int command, int serialNumber, String idStr, String keyStr) {


		if (idStr.length()%2!=0) {
			idStr = "0" + idStr;
		}
		
		byte[] idBytes = SRBleUtils.hexStringToByte(idStr);
		byte[] keyBytes = SRBleUtils.hexStringToByte(keyStr);
		
		byte[] data = new byte[8];

		
		//前3字节为密钥
		System.arraycopy(idBytes, 0, data, 0, 3);
		//最后2字节为滚动码
		data[6] = (byte) (serialNumber>>8&0xff);
		data[7] = (byte) (serialNumber&0xff);
		//第4个字节和7字节相同
		data[3] = data[7];
		//4字节==0字节
		data[4] = data[0];
		//5字节为指令
		data[5] = (byte) command;

		Ht2crypt_btk.Oneway1_ext(data, keyBytes);

		byte[] temp = new byte[4];  
		System.arraycopy(data, 4, temp, 0, 4);
		Ht2crypt_btk.Oneway2(temp,32);
		System.arraycopy(temp, 0, data, 4, 4);

		String result = SRBleUtils.bytesToHexString(data);

		return result;
	}

}
