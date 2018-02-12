package com.mysirui.vehicle.constants;

import java.util.HashMap;

public class SRBleResultCode {
	public static final int RESP_NULL 						= 0x00;
	public static final int RESP_OK 						= 0x01; //成功
	public static final int RESP_EXE 						= 0x02; //正在执行
	public static final int RESP_FAIL 						= 0x03; //失败:通用
	public static final int RESP_F04_CANCEL 				= 0x04; //失败:取消
	public static final int RESP_F05_DELAY 					= 0x05; //失败:延时执行
	public static final int RESP_F06_UNSUPPORT 				= 0x06; //失败:设备不支持
	public static final int RESP_F07_TIMOUT 				= 0x07; //失败:有效期超时
	public static final int RESP_F08_CONFIG 				= 0x08; //失败:配置不支持
	public static final int RESP_F09_PARAM 					= 0x09; //失败:参数非法
	public static final int RESP_F0A_BUSY 					= 0x0A; //失败:系统繁忙
	public static final int RESP_F0B_STATE 					= 0x0B; //失败:状态不支持
	public static final int RESP_F0C_DUPCMD 				= 0x0C; //失败:指令正在执行时再次接收相同指令
	public static final int RESP_F0D_DUPSTATE 				= 0x0D; //失败:状态已更新
	public static final int RESP_F0E_START_ON 				= 0x0E; //失败:启动检测到状态ON
	public static final int RESP_F0F_START_OPEN 			= 0x0F; //失败:启动检测到门开
	public static final int RESP_F10_START_WIN 				= 0x10; //失败:启动检测到升窗
	public static final int RESP_F11_START_WASH 			= 0x11; //失败:启动检测到洗车模式
	public static final int RESP_F12_STOP_ON 				= 0x12; //失败:熄火检测到档位ON
	public static final int RESP_F13_STOP_DRIVE 			= 0x13; //失败:熄火检测到行驶
	public static final int RESP_F14_PANIC_ON 				= 0x14; //失败:寻车检测到状态ON
	public static final int RESP_F15_LOCK_OPEN 				= 0x15; //失败:关锁检测到门开
	public static final int RESP_F16_START_UNLK 			= 0x16; //失败:PPKE解锁禁止遥控启动
	public static final int RESP_F17_STOP_GERA 				= 0x17; //失败:熄火检测到档位非P档
	public static final int RESP_F18_STOP_MANUAL	 		= 0x18; //失败:熄火检测到手动启动
	public static final int RESP_F19_BTK_ID 				= 0x19; //失败:钥匙错误
	public static final int RESP_F1A_BTK_SEQ 				= 0x1A; //失败:滚动码错误
	public static final int RESP_F1B_Vehicle_Unkown 		= 0x1B; //失败:车型拨码错误
	public static final int RESP_F1C_App_Unsupport 			= 0x1C; //失败:业务不支持
	public static final int RESP_F1D_STOP_LF_CLOSE 			= 0x1D; //失败:熄火时必须打开主门
	public static final int RESP_F1E_BTK_AUTH 				= 0x1E; //失败:蓝牙签权错误
	public static final int RESP_F1F_PKE_OFF 				= 0x1F; //失败:在off状态下遥控启动
	public static final int RESP_F20_OTA_PAUSE 				= 0x20; //失败:升级暂停
	public static final int RESP_F21_CRC_ERROR 				= 0x21; //失败:CRC错误 
	public static final int RESP_F22_FLASH_WRITE_ERR 		= 0x22; //失败:写FLASH失败
	public static final int RESP_F23_RESET 					= 0x23; //失败:升级期间重启
	public static final int RESP_F24_RESTIMEOUT 			= 0x24; //失败:接收超时达到最大次数
	public static final int RESP_F25_CRC_ERROR_FRAG 		= 0x25; //错误:B808返回的片校验与B806返回的片校验不一致
	public static final int RESP_F26_CRC_DATA 				= 0x26; //错误:上传将要写入的FLASH 的片校验
	public static final int RESP_F27_RX_CRC 				= 0x27; //错误:接收数据CRC错误
	public static final int RESP_F28_SET 					= 0x28; //错误:参数设置1
	public static final int RESP_F29_RESET 					= 0x29; //错误:参数清零
	public static final int RESP_F2A_UPDATE_ERR_ON 			= 0x2A; //错误:在ON档在线升级
	public static final int RESP_F2B_BKOPEN_ON 				= 0x2B; //错误:ON无法开启后备箱
	public static final int RESP_F2C_KEYFIND_OK 			= 0x2C; //结果:找到遥控器
	public static final int RESP_F2D_KEYFIND_FAIL 			= 0x2D; //结果:未找到遥控器
	public static final int RESP_F2E_BLOCK_ERASE_FINISHED 	= 0x2E; //结果:块擦除完成
	public static final int RESP_F2F_MANURMT_NOT_READY 		= 0x2F; //手动档遥控启动状态未满足
	public static final int RESP_F30_MANUBRAKE_ERR 			= 0x30; //手刹状态错误
	public static final int RESP_F31_BTU_ID_0				= 0x31; //PPKE或OST 存储的btu的ID 为0
	public static final int RESP_F32_BTU_PSW_ERR			= 0x32; //btu的密码错误
	
	private static HashMap<String, String> resultStr = new HashMap<String, String>();

    static {
    	resultStr.put(String.valueOf(RESP_NULL), "无效");
    	resultStr.put(String.valueOf(RESP_OK), "成功");
    	resultStr.put(String.valueOf(RESP_EXE), "成功");
    	resultStr.put(String.valueOf(RESP_FAIL), "失败:通用");
    	resultStr.put(String.valueOf(RESP_F04_CANCEL), "失败:取消");
    	resultStr.put(String.valueOf(RESP_F05_DELAY), "失败:延时执行");
    	resultStr.put(String.valueOf(RESP_F06_UNSUPPORT), "失败:设备不支持");
    	resultStr.put(String.valueOf(RESP_F07_TIMOUT), "失败:有效期超时");
    	resultStr.put(String.valueOf(RESP_F08_CONFIG), "失败:配置不支持");
    	resultStr.put(String.valueOf(RESP_F09_PARAM), "失败:参数非法");
    	resultStr.put(String.valueOf(RESP_F0A_BUSY), "失败:系统繁忙");
    	resultStr.put(String.valueOf(RESP_F0B_STATE), "失败:状态不支持");
    	resultStr.put(String.valueOf(RESP_F0C_DUPCMD), "失败:指令正在执行时再次接收相同指令");
    	resultStr.put(String.valueOf(RESP_F0D_DUPSTATE), "失败:状态已更新");
    	resultStr.put(String.valueOf(RESP_F0E_START_ON), "失败:启动检测到状态ON");
    	resultStr.put(String.valueOf(RESP_F0F_START_OPEN), "失败:启动检测到门开");
    	resultStr.put(String.valueOf(RESP_F10_START_WIN), "失败:启动检测到升窗");
    	resultStr.put(String.valueOf(RESP_F11_START_WASH), "失败:启动检测到洗车模式");
    	resultStr.put(String.valueOf(RESP_F12_STOP_ON), "失败:熄火检测到档位ON");
    	resultStr.put(String.valueOf(RESP_F13_STOP_DRIVE), "失败:熄火检测到行驶");
    	resultStr.put(String.valueOf(RESP_F14_PANIC_ON), "失败:寻车检测到状态ON");
    	resultStr.put(String.valueOf(RESP_F15_LOCK_OPEN), "失败:关锁检测到门开");
    	resultStr.put(String.valueOf(RESP_F16_START_UNLK), "失败:PPKE解锁禁止遥控启动");
    	resultStr.put(String.valueOf(RESP_F17_STOP_GERA), "失败:熄火检测到档位非P档");
    	resultStr.put(String.valueOf(RESP_F18_STOP_MANUAL), "失败:熄火检测到手动启动");
    	resultStr.put(String.valueOf(RESP_F19_BTK_ID), "失败:钥匙错误");
    	resultStr.put(String.valueOf(RESP_F1A_BTK_SEQ), "失败:滚动码错误");
    	resultStr.put(String.valueOf(RESP_F1B_Vehicle_Unkown), "失败:车型拨码错误");
    	resultStr.put(String.valueOf(RESP_F1C_App_Unsupport), "失败:业务不支持");
    	resultStr.put(String.valueOf(RESP_F1D_STOP_LF_CLOSE), "失败:熄火时必须打开主门");
    	resultStr.put(String.valueOf(RESP_F1E_BTK_AUTH), "失败:蓝牙签权错误");
    	resultStr.put(String.valueOf(RESP_F1F_PKE_OFF), "失败:在off状态下遥控启动");
    	resultStr.put(String.valueOf(RESP_F20_OTA_PAUSE), "失败:升级暂停");
    	resultStr.put(String.valueOf(RESP_F21_CRC_ERROR), "失败:CRC错误 ");
    	resultStr.put(String.valueOf(RESP_F22_FLASH_WRITE_ERR), "失败:写FLASH失败");
    	resultStr.put(String.valueOf(RESP_F23_RESET), "失败:升级期间重启");
    	resultStr.put(String.valueOf(RESP_F24_RESTIMEOUT), "失败:接收超时达到最大次数");
    	resultStr.put(String.valueOf(RESP_F25_CRC_ERROR_FRAG), "错误:B808返回的片校验与B806返回的片校验不一致");
    	resultStr.put(String.valueOf(RESP_F26_CRC_DATA), "错误:上传将要写入的FLASH 的片校验");
    	resultStr.put(String.valueOf(RESP_F27_RX_CRC), "错误:接收数据CRC错误");
    	resultStr.put(String.valueOf(RESP_F28_SET), "错误:参数设置1");
    	resultStr.put(String.valueOf(RESP_F29_RESET), "错误:参数清零");
    	resultStr.put(String.valueOf(RESP_F2A_UPDATE_ERR_ON), "错误:在ON档在线升级");
    	resultStr.put(String.valueOf(RESP_F2B_BKOPEN_ON), "错误:ON无法开启后备箱");
    	resultStr.put(String.valueOf(RESP_F2C_KEYFIND_OK), "结果:找到遥控器");
    	resultStr.put(String.valueOf(RESP_F2D_KEYFIND_FAIL), "结果:未找到遥控器");
    	resultStr.put(String.valueOf(RESP_F2E_BLOCK_ERASE_FINISHED), "结果:块擦除完成");
    	resultStr.put(String.valueOf(RESP_F2F_MANURMT_NOT_READY), "手动档遥控启动状态未满足");
    	resultStr.put(String.valueOf(RESP_F30_MANUBRAKE_ERR), "手刹状态错误");
    	resultStr.put(String.valueOf(RESP_F31_BTU_ID_0), "PPKE或OST 存储的btu的ID 为0");
    	resultStr.put(String.valueOf(RESP_F32_BTU_PSW_ERR), "btu的密码错误");
    	
    }

    public static String stringValue(int resultCode) {
        String stringValue = resultStr.get(String.valueOf(resultCode));
        return stringValue != null ? stringValue : String.format("失败（%d）", resultCode);
    }
}
