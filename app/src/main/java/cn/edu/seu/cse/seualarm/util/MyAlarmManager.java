package cn.edu.seu.cse.seualarm.util;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;


import java.util.Calendar;

import cn.edu.seu.cse.seualarm.controler.alarm.AlarmReceiver;
import cn.edu.seu.cse.seualarm.dao.AlarmInfoDao;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class MyAlarmManager {
//    public static final String ALARM_OFF = "cn.edu.seu.cse.seualarm.ALARM_OFF";

    /**
     * 关闭一次性闹铃
     * @param context
     * @param alarmInfo
     */
    public static void cancelOneAlarm(Context context, AlarmInfo alarmInfo) {
        // 关闭闹铃
        alarmInfo.setEnable(0);
        AlarmInfoDao dao = new AlarmInfoDao(context);
        dao.updateAlarm(alarmInfo);

//        Intent it = new Intent(ALARM_OFF);
//        context.sendBroadcast(it);
    }

    /**
     * 取消闹铃
     * @param context
     * @param alarmInfo
     */
    public static void cancelAlarmClock(Context context, AlarmInfo alarmInfo) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmInfoDao dao = new AlarmInfoDao(context);
        int alarmIntId = dao.getOnlyId(alarmInfo);

        PendingIntent pi = PendingIntent.getBroadcast(context, alarmIntId, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * 开启闹钟
     *
     * @param context    context
     * @param alarmInfo 闹钟实例
     */
    @TargetApi(19)
    public static void startAlarmClock(Context context, AlarmInfo alarmInfo) {
        AlarmInfoDao dao = new AlarmInfoDao(context);

        // 向AlarmReceiver发送数据
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ALARM_INFO, alarmInfo);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtras(bundle);
        Log.d("MyAlarmManager", "start alarm");

        int id = dao.getOnlyId(alarmInfo);
        // FLAG_UPDATE_CURRENT：如果PendingIntent已经存在，保留它并且只替换它的extra数据。
        // FLAG_CANCEL_CURRENT：如果PendingIntent已经存在，那么当前的PendingIntent会取消掉，然后产生一个新的PendingIntent。
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 取得下次响铃时间
        long nextTime = calculateNextTime(alarmInfo);
        // 设置闹钟
        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }

    }

    /**
     * 取得下次设置时间 用于重复设置闹铃
     * @param alarmInfo
     * @return
     */
    public static long calculateNextTime(AlarmInfo alarmInfo) {
        // 当前系统时间
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, alarmInfo.getHour());
        calendar.set(Calendar.MINUTE, alarmInfo.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 下次响铃时间
        long nextTime = calendar.getTimeInMillis();
        int[] weeks = alarmInfo.getDayOfWeek();

        // 当单次响铃时
        if (weeks.length == 0) {
            // 当设置时间大于系统时间时
            if (nextTime > now) {
                return nextTime;
            } else {
                // 设置的时间加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        } else {
            nextTime = 0;
            // 临时比较用响铃时间
            long tempTime;
            // 取得响铃重复周期
            for (int week : weeks) {
                // 设置重复的周
                calendar.set(Calendar.DAY_OF_WEEK, week);
                tempTime = calendar.getTimeInMillis();
                // 当设置时间小于等于当前系统时间时
                if (tempTime <= now) {
                    // 设置时间加7天
                    tempTime += AlarmManager.INTERVAL_DAY * 7;
                }

                if (nextTime == 0) {
                    nextTime = tempTime;
                } else {
                    // 比较取得最小时间为下次响铃时间
                    nextTime = Math.min(tempTime, nextTime);
                }

            }

            return nextTime;
        }
    }
}

