package com.mysirui.vehicle.constants;

/**
 * Created by marlin on 2017/6/15.
 * 车辆状态常量
 */

public class StatusConstant {

    //状态常量
    public static final int VALUE_UNKONW = 0;
    public static final int VALUE_ON = 1;
    public static final int VALUE_OFF = 2;

    private static  int S_INDEX = 1;

    //引擎状态
    public static final int S_ACC = S_INDEX++;
    public static final int S_ON = S_INDEX++;
    public static final int S_ENGINE = S_INDEX++;

    //锁状态
    public static final int S_LOCK = S_INDEX++;

    //门状态
    public static final int S_DOOR = S_INDEX++;
    public static final int S_DOOR_LF = S_INDEX++;
    public static final int S_DOOR_RF = S_INDEX++;
    public static final int S_DOOR_LB = S_INDEX++;
    public static final int S_DOOR_RB = S_INDEX++;
    public static final int S_DOOR_TRUNCK = S_INDEX++;

    //窗状态
    public static final int S_WINDOW = S_INDEX++;
    public static final int S_WINDOW_LF = S_INDEX++;
    public static final int S_WINDOW_LB = S_INDEX++;
    public static final int S_WINDOW_RF = S_INDEX++;
    public static final int S_WINDOW_RB = S_INDEX++;
    public static final int S_WINDOW_SKY = S_INDEX++;

    //电压状态
    public static final int S_ELEC = S_INDEX++;

    //里程 单位公里
    public static final int S_MILEAGE = S_INDEX++;

    //剩余油量
    public static final int S_LEFT_OIL = S_INDEX++;

    //剩余电量
    public static final int S_LEFT_ELEC = S_INDEX++;


}
