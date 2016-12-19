package cn.edu.seu.cse.seualarm.module;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.RandomAccess;

import cn.edu.seu.cse.seualarm.dao.AlarmInfoDao;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/9.
 */
public class AlarmInfo implements Serializable, Comparable {
    private int hour;
    private int minute;
    private int enable;
    private int vibrate;
    private int rain;
    private String ringtone;
    private String ringResId;
    private String label;
    private String sid;
    private int[] dayOfWeek;

    public AlarmInfo() {
        this.hour = 20;
        this.minute = 30;
        this.enable = 0;
        this.vibrate = 1;
        this.rain = 0;
        this.ringtone = Constants.DEFAULT_RING;
        this.ringResId = Constants.DEFAULT_RES_ID;
        this.label = "闹钟";
        this.dayOfWeek = new int[0];
        this.sid = "" + new Date().getTime();
    }

    public AlarmInfo(int hour, int minute, int enable, int vibrate, int rain, String ringtone, String ringResId, String label, int[] dayOfWeek) {
        this.hour = hour;
        this.minute = minute;
        this.enable = enable;
        this.vibrate = vibrate;
        this.rain = rain;
        this.ringtone = ringtone;
        this.ringResId = ringResId;
        this.label = label;
        this.dayOfWeek = dayOfWeek;
        this.sid = "" + new Date().getTime();
    }

    @Override
    public String toString() {
        return "AlarmInfo{" +
                " hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", label='" + label + '\'' +
                ", ringtone='" + ringtone + '\'' +
                ", enable=" + enable +
                ", vibrate=" + vibrate +
                " }";
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof AlarmInfo) {
            AlarmInfo alarmInfo = (AlarmInfo) o;
            if (hour > alarmInfo.getHour())
                return 1;
            else if (hour == alarmInfo.getHour()) {
                int compare = minute - alarmInfo.getMinute();
                return compare == 0 ? 0 : compare > 0 ? 1 : -1;
            }
            else
                return -1;
        }
        return -1;
    }

    public String getRingResId() {
        return ringResId;
    }

    public void setRingResId(String ringResId) {
        this.ringResId = ringResId;
    }

    public String getSId() {
        return sid;
    }

    public void setSId(String sid) {
        this.sid = sid;
    }

    public int getRain() {
        return rain;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int[] getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int[] dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
