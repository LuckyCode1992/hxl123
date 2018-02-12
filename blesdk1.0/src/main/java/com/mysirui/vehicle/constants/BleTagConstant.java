package com.mysirui.vehicle.constants;
//操作常量
public class BleTagConstant {
	//BTU tag
	public static final int Operation_A605 = 0xA605;    //蓝牙认证输入随机码
	public static final int Operation_A606 = 0xA606;    //蓝牙认证输出激活字符串
	public static final int Operation_A607 = 0xA607;    //蓝牙大数据传输申请
	public static final int Operation_Heart_A608 = 0xA608;    //心跳Tag
	public static final int Operation_A304 = 0xA304;    //终端与蓝牙的会话状态
    
	public static final int Operation_B101 = 0xB101;    //产品名称、软件信息、硬件信息、车型信息
	public static final int Operation_B102 = 0xB102;    //软件版本
	public static final int Operation_B103 = 0xB103;    //硬件信息
	public static final int Operation_B104 = 0xB104;    //固件扇区
	public static final int Operation_B105 = 0xB105;    //重启序号、重启原因
    
    //配置
	public static final int Operation_B201 = 0xB201;    //蓝牙钥匙ID、密钥
	public static final int Operation_B202 = 0xB202;    //蓝牙连接PIN码
	public static final int Operation_B203 = 0xB203;    //配置蓝牙加密ID及密钥
    
    //状态
    //状态 acc+on+引擎+行驶;(门)左前+右前+左后+右后+后箱;(锁)左前+右前+左后+右后;(窗)左前+右前+左后+右后+天窗;大灯+小灯;设防+侵入;告警编码
	public static final int BasicStatus_B301 = 0xB301;
	public static final int Operation_B302 = 0xB302;    //通信模块与网关的会话状态
	public static final int Operation_B303 = 0xB303;    //蓝牙模块与手机的会话状态
	public static final int Operation_B304 = 0xB304;    //终端与蓝牙的会话状态

	public static final int Elec_B310 = 0xB310;    		//备用电压温度
	public static final int Mileage_B311 = 0xB311;    	//总里程
	public static final int LeftOil_B312 = 0xB312;    	//剩余油量
	public static final int LeftElec_B313 = 0xB313;    	//剩余电量
    
    //事件
	public static final int Operation_B401 = 0xB401;    //业务回应
	public static final int Operation_B402 = 0xB402;    //蓝牙回应
	public static final int Operation_B422 = 0xB422;    //升级回应
	public static final int Operation_B443 = 0xB443;    //未知标签
    
    //业务
	public static final int Operation_B501 = 0xB501;    //业务指令
	public static final int Operation_B502 = 0xB502;    //蓝牙指令
    
    //指令
	public static final int Operation_B601 = 0xB601;    //重启：立即
	public static final int Operation_B602 = 0xB602;    //修改引导扇区编号并重启
	public static final int Operation_B605 = 0xB605;    //同步滚动码到OTU
    //会话
	public static final int Operation_B701 = 0xB701;    //透传：命令行指令
	public static final int Operation_B702 = 0xB702;    //透传：命令行回显
	public static final int Operation_B703 = 0xB703;    //透传：日志使能
	public static final int Operation_B704 = 0xB704;    //透传：日志回显
	public static final int Operation_B705 = 0xB705;    //总线捕获请求
	public static final int Operation_B706 = 0xB706;    //总线捕获回应
	public static final int Operation_B707 = 0xB707;    //相邻总线，设备ID，设备回显
	public static final int Operation_B708 = 0xB708;    //相邻总线，设备ID，设备回显
    
    //升级
	public static final int Operation_B801 = 0xB801;    //启动升级
	public static final int Operation_B802 = 0xB802;    //停止升级
	public static final int Operation_B803 = 0xB803;    //固件请求，扇区编号、扇区当前软件版本、分片长度
	public static final int Operation_B804 = 0xB804;    //固件回应，字节总数、分片总数、校验和
	public static final int Operation_B805 = 0xB805;    //分片请求，编号
	public static final int Operation_B806 = 0xB806;    //分片回应，编号、校验和、数据
	public static final int Operation_B807 = 0xB807;
	public static final int Operation_B808 = 0xB808;
}
