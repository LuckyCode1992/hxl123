package com.mysirui.vehicle.constants;

import java.util.HashMap;
import java.util.UUID;

public class SRGattAttributes {
	private static HashMap<String, String> attributes = new HashMap<String, String>();

	//原来的协议
//    public final static String SERVICE 					= "0000fff0-0000-1000-8000-00805f9b34fb";
//    public final static String BLE_WRITE_DATA 			= "0000fff3-0000-1000-8000-00805f9b34fb";
//    public final static String TERMINAL_WRITE_DATA 		= "0000fff5-0000-1000-8000-00805f9b34fb";
//    public final static String READ_DATA 				= "0000fff6-0000-1000-8000-00805f9b34fb";
//    public final static String NOTIFICATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";

	public final static String SERVICE 					= "0000fff0-0000-1000-8000-00805f9b34fb";
	public final static String BLE_WRITE_DATA 			= "0000fff4-0000-1000-8000-00805f9b34fb";
	public final static String TERMINAL_WRITE_DATA 		= "0000fff4-0000-1000-8000-00805f9b34fb";
	public final static String READ_DATA 				= "0000fff5-0000-1000-8000-00805f9b34fb";
	public final static String NOTIFICATION_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";


	//todo 升级所需通道
	public static final String oadService_UUID = 	"f000ffc0-0451-4000-b000-000000000000";
	public static final String imageAService_UUID = "f000ffd0-0451-4000-b000-000000000000";

    public final static UUID UUID_SERVICE       		= UUID.fromString(SRGattAttributes.SERVICE);
    public final static UUID UUID_CHA_WRITE_BLE       	= UUID.fromString(SRGattAttributes.BLE_WRITE_DATA);
    public final static UUID UUID_CHA_WRITE_TERMINAL   	= UUID.fromString(SRGattAttributes.TERMINAL_WRITE_DATA);
    public final static UUID UUID_CHA_BLE_READ      	= UUID.fromString(SRGattAttributes.READ_DATA);
    public final static UUID UUID_CHA_BLE_DES       	= UUID.fromString(SRGattAttributes.NOTIFICATION_DESCRIPTOR);

}
