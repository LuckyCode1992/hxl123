package com.mysirui.vehicle.constants;

/**
 *  状态查询标签
 * Created by marlin on 2017/8/23.
 */

public class BleQueryTagConstant {

    /**
     * 车辆基础状态查询
     * ACC ON 门 后备箱 锁 窗 天窗 状态查询
     */
    public static final int BasicStatus_B301 = 0xB301;

    /**
     * 车辆里程查询标签
     */
    public static final int Mileage_B311 = 0xB311;    //总里程

    /**
     * 剩余油量查询标签
     */
    public static final int LeftOil_B312 = 0xB312;    //剩余油量

    /**
     * 剩余电量查询标签
     */
    public static final int LeftElec_B313 = 0xB313;    //剩余电量

}
