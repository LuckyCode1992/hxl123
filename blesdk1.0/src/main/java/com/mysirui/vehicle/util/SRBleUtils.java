package com.mysirui.vehicle.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint({ "InlinedApi", "UseSparseArrays" })
public class SRBleUtils {

    private static String getHashMapValue(HashMap<Integer, String> hashMap, int number){
    	String result =hashMap.get(number);
    	if(TextUtils.isEmpty(result)){
    		List<Integer> numbers = getElement(number);
    		result="";
    		for(int i=0;i<numbers.size();i++){
    			result+=hashMap.get(numbers.get(i))+"|";
    		}
    	}
    	return result;
    }

    /**
     * 位运算结果的反推函数10 -> 2 | 8;
     */
    static private List<Integer> getElement(int number){
    	List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < 32; i++){
            int b = 1 << i;
            if ((number & b) > 0) 
            	result.add(b);
        }
        
        return result;
    }
    
    
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {  
            return null;  
        }  
        for (int i = 0; i < src.length; i++) {  
            int v = src[i] & 0xFF;  
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {  
                stringBuilder.append(0);  
            }  
            stringBuilder.append(hv);  
        }  
        return stringBuilder.toString();  
    }  
    
    /** 
     * 把16进制字符串转换成字节数组 
     * @param hex 
     * @return 
     */ 
    public static byte[] hexStringToByte(String hex) {
    	if (hex == null || hex.equals("")) {
            return null;
        }
    	hex = hex.toUpperCase();
        int len = (hex.length() / 2); 
        byte[] result = new byte[len]; 
        char[] achar = hex.toCharArray(); 
        for (int i = 0; i < len; i++) { 
         int pos = i * 2; 
         result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
        } 
        return result; 
    }
    
    private static byte toByte(char c) { 
        byte b = (byte) "0123456789ABCDEF".indexOf(c); 
        return b; 
    }


}
