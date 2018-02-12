package com.mysirui.vehicle.util;

/**
 * Created by marlin on 2017/7/5.
 */

public class ByteUtil {

    public static boolean isAllZero(String str){
        if(StringUtil.isEmpty(str)){
            return true;
        }
        for(char c:str.toCharArray()){
            if(c != '0'){
                return false;
            }
        }
        return true;
    }

}
