package cn.edu.seu.cse.seualarm.util;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class Constants {
    // 请求码与返回码
    public static final int SET_ALARM_DONE = 0;//闹钟设置成功
    public static final int SET_ALARM_CANCEL = 1;//闹钟设置取消
    public static final int UPDATE_ALARM = 2;//跳转到闹钟添加
    public static final int UPDATE_ALARM_DONE = 3;//闹钟设置成功
    public static final int ADD_ALARM = 4;//跳转到闹钟添加
    public static final int ADD_ALARM_DONE = 5;//跳转到闹钟添加
    public static final int ASK_RING_SET = 6;//跳转到闹钟添加
    public static final int RING_SET_DONE = 7;//跳转到闹钟添加
    public static final int ASK_CUSTOM_RING_SET = 8;//跳转到闹钟添加
    public static final int CUSTOM_RING_SET_DONE = 9;//跳转到闹钟添加

    // 活动间数据传递
    public static final String DEFAULT_RING = "everybody";
    public static final String DEFAULT_RES_ID = "everybody.mp3";
    public static final String RING_NAME = "RING_NAME";
    public static final String RING_ID = "RING_ID";
    public static final String CUSTOM_RING = "CUSTOM_RING";
    public static final String ALARM_INFO = "ALARM_INFO";
    public static final String ALARM_POSITION = "ALARM_POSITION";

    // 数据库
    public static final String SQLDB_NAME = "alarm.db";//闹钟信息数据库
    public static final String ALARM_TABLE = "tbAlarmInfo";//闹钟的信息表名
    public static final String ALARM_HOUR = "hour";//闹钟小时
    public static final String ALARM_MINUTE = "minute";//闹钟分钟
    public static final String ALARM_RINGTONE = "ringtone";//闹钟的铃声
    public static final String ALARM_RES_ID = "ringresid";//闹钟的铃声
    public static final String ALARM_VIBRATE = "vibrate";//是否震动
    public static final String ALARM_RAIN = "rain";//雨天是否取消
    public static final String ALARM_ENABLE = "enable";//是否生效
    public static final String ALARM_LABEL = "label";//闹钟标签
    public static final String ALARM_REPEAT_DAY = "repeatDays";//一周重复的天
    public static final String ALARM_SID = "alarmID";//闹钟ID
}
