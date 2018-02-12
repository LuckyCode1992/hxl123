package com.mysirui.vehicle.util;

import java.util.List;

/**
 * Created by marlin on 2017/6/15.
 */
public class StringUtil {

    public static boolean isEmpty(String mac) {
        return null == mac || 0 == mac.trim().length();
    }

    public static boolean isNoneEmpty(String mac) {
        return !isEmpty(mac);
    }

    public static String join(List<String> list, String split) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            if (null != split) {
                sb.append(split).append(s);
            } else {
                sb.append(s);
            }
        }
        return null == split ? sb.toString() : sb.substring(split.length());
    }

    public static int safeParse(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int hex2IntValue(String hex) {
        return Integer.parseInt(hex, 16);
    }
}
