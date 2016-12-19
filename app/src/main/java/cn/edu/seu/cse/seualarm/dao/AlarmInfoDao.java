package cn.edu.seu.cse.seualarm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class AlarmInfoDao {
    private AlarmInfoDbHelper mHelper;

    public AlarmInfoDao(Context mContext) {
        mHelper = new AlarmInfoDbHelper(mContext);
    }

    public void addAlarmInfo(AlarmInfo alarmInfo) {
        //添加一个闹钟
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.ALARM_HOUR, alarmInfo.getHour());
        values.put(Constants.ALARM_MINUTE, alarmInfo.getMinute());
        values.put(Constants.ALARM_ENABLE, alarmInfo.getEnable());
        values.put(Constants.ALARM_VIBRATE, alarmInfo.getVibrate());
        values.put(Constants.ALARM_RAIN, alarmInfo.getRain());
        values.put(Constants.ALARM_RINGTONE, alarmInfo.getRingtone());
        values.put(Constants.ALARM_RES_ID, alarmInfo.getRingResId());
        values.put(Constants.ALARM_LABEL, alarmInfo.getLabel());
        values.put(Constants.ALARM_REPEAT_DAY, getDataDayofWeek(alarmInfo.getDayOfWeek()));
        values.put(Constants.ALARM_SID, alarmInfo.getSId());
        db.insert(Constants.ALARM_TABLE, null, values);
        //Toast.makeText(mContext, "闹钟设置成功", Toast.LENGTH_SHORT).show();

        if (db != null) {
            db.close();
            values.clear();
        }
    }

    public AlarmInfo findById(String alarmId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.ALARM_TABLE, null, Constants.ALARM_SID + "=?", new String[]{alarmId}, null, null, null);
        AlarmInfo alarmInfo = new AlarmInfo();
        if (cursor != null) {
            //Log.d("alarm","cusor不为空");
            if (cursor.moveToNext()) {

                alarmInfo.setHour(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_HOUR)));
                alarmInfo.setMinute(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_MINUTE)));
                alarmInfo.setEnable(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_ENABLE)));
                alarmInfo.setVibrate(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_VIBRATE)));
                alarmInfo.setRain(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_RAIN)));
                alarmInfo.setRingtone(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RINGTONE)));
                alarmInfo.setRingResId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RES_ID)));
                alarmInfo.setLabel(cursor.getString(cursor.getColumnIndex(Constants.ALARM_LABEL)));
                alarmInfo.setSId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_SID)));
                String dayOfWeek = cursor.getString(cursor.getColumnIndex(Constants.ALARM_REPEAT_DAY));

                Log.d("alarm", dayOfWeek);
                int[] day = getAlarmDayofWeek(dayOfWeek);
                if (day != null) {
                    // Log.d("alarm","数据库中重复天数不为空");
                } else {
                    // Log.d("alarm","数据库中重复天数为空");
                }
                alarmInfo.setDayOfWeek(day);
            }
        }
        return alarmInfo;
    }

    public AlarmInfo findByOnlyId(String alarmId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.ALARM_TABLE, null, "id=?",
                new String[]{Constants.ALARM_SID}, null, null, null);

        AlarmInfo alarmInfo = new AlarmInfo();
        if (cursor != null) {
            while (cursor.moveToNext()) {

                alarmInfo.setHour(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_HOUR)));
                alarmInfo.setMinute(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_MINUTE)));
                alarmInfo.setEnable(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_ENABLE)));
                alarmInfo.setVibrate(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_VIBRATE)));
                alarmInfo.setRain(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_RAIN)));
                alarmInfo.setRingtone(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RINGTONE)));
                alarmInfo.setRingResId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RES_ID)));
                alarmInfo.setSId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_SID)));
                alarmInfo.setLabel(cursor.getString(cursor.getColumnIndex(Constants.ALARM_LABEL)));
                String dayOfWeek = cursor.getString(cursor.getColumnIndex(Constants.ALARM_REPEAT_DAY));

                Log.d("alarm", dayOfWeek);
                int[] day = getAlarmDayofWeek(dayOfWeek);
                alarmInfo.setDayOfWeek(day);
            }
        }
        return alarmInfo;
    }

    public List<AlarmInfo> getAllInfo() {
        List<AlarmInfo> list = new ArrayList<AlarmInfo>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.ALARM_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setHour(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_HOUR)));
                alarmInfo.setMinute(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_MINUTE)));
                alarmInfo.setEnable(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_ENABLE)));
                alarmInfo.setVibrate(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_VIBRATE)));
                alarmInfo.setRain(cursor.getInt(cursor.getColumnIndex(Constants.ALARM_RAIN)));
                alarmInfo.setRingtone(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RINGTONE)));
                alarmInfo.setRingResId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_RES_ID)));
                alarmInfo.setLabel(cursor.getString(cursor.getColumnIndex(Constants.ALARM_LABEL)));
                alarmInfo.setSId(cursor.getString(cursor.getColumnIndex(Constants.ALARM_SID)));
                String dayOfWeek = cursor.getString(cursor.getColumnIndex(Constants.ALARM_REPEAT_DAY));

                Log.d("alarm", dayOfWeek);
                int[] day = getAlarmDayofWeek(dayOfWeek);
                alarmInfo.setDayOfWeek(day);
                list.add(alarmInfo);
            }
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return list;
    }

    /*
    *删除闹钟
    */
    public void deleteAlarm(AlarmInfo alarmInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(Constants.ALARM_TABLE, Constants.ALARM_SID + " = ?", new String[]{alarmInfo.getSId()});

        //Toast.makeText(mContext, "闹钟删除成功", Toast.LENGTH_SHORT).show();
        db.close();
    }

    /*
    *编辑闹钟
    */
    public void updateAlarm(AlarmInfo alarmInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.ALARM_HOUR, alarmInfo.getHour());
        values.put(Constants.ALARM_MINUTE, alarmInfo.getMinute());
        values.put(Constants.ALARM_ENABLE, alarmInfo.getEnable());
        values.put(Constants.ALARM_VIBRATE, alarmInfo.getVibrate());
        values.put(Constants.ALARM_RAIN, alarmInfo.getRain());
        values.put(Constants.ALARM_RINGTONE, alarmInfo.getRingtone());
        values.put(Constants.ALARM_RES_ID, alarmInfo.getRingResId());
        values.put(Constants.ALARM_LABEL, alarmInfo.getLabel());
        values.put(Constants.ALARM_REPEAT_DAY, getDataDayofWeek(alarmInfo.getDayOfWeek()));
        values.put(Constants.ALARM_SID, alarmInfo.getSId());
        db.updateWithOnConflict(Constants.ALARM_TABLE, values, Constants.ALARM_SID + " = ?",
                new String[]{alarmInfo.getSId()}, SQLiteDatabase.CONFLICT_IGNORE);

        //Toast.makeText(mContext, "闹钟修改成功", Toast.LENGTH_SHORT).show();
        db.close();

        Log.d("alarm", "AlarmDao update完成");
    }

    public int getOnlyId(AlarmInfo alarmInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = -1;
        Cursor cusor = db.query(Constants.ALARM_TABLE, new String[]{"id"}, Constants.ALARM_SID + " = ?",
                new String[]{alarmInfo.getSId()}, null, null, null);
        if (cusor != null) {
            if (cusor.moveToNext()) {
                id = cusor.getInt(cusor.getColumnIndex("id"));
            }
        }
        if (db != null) {
            db.close();
        }
        cusor.close();
        return id;
    }

    public static int[] getAlarmDayofWeek(String dayOfWeek) {
        if (dayOfWeek.equals(""))
            return new int[0];

        String[] change = dayOfWeek.split(",");
        int[] Day = new int[change.length];
        for (int i = 0; i < change.length; i++) {
            Day[i] = Integer.parseInt(change[i]);
        }
        return Day;
    }

    public static String getDataDayofWeek(int[] Day) {
        String dayOfWeek = "";

        //将重复的天数从数组变为字符串
        for (int i = 0; i < Day.length; i++) {
            int day = Day[i];
            if (i == Day.length - 1) {
                dayOfWeek = dayOfWeek + day;
            } else {
                dayOfWeek = dayOfWeek + day + ",";
            }
        }
        return dayOfWeek;
    }
}
