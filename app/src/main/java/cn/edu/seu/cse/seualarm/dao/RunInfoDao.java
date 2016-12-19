package cn.edu.seu.cse.seualarm.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.module.RunInfo;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/16.
 */

public class RunInfoDao {
    private RunInfoDbHelper mHelper;
    private static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public RunInfoDao(Context mContext) {
        mHelper = new RunInfoDbHelper(mContext);
    }

    public void addRunInfo(RunInfo runInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        WeatherInfo weatherInfo = runInfo.getRunWeather();

        values.put(Constants.RUNINFO_ID, runInfo.getRunID());
        values.put(Constants.RUNINFO_MEMO, runInfo.getRunMem());
        values.put(Constants.RUNINFO_DATE, YYYY_MM_DD.format(runInfo.getRunDate()));
        values.put(Constants.RUNINFO_TIMES, runInfo.getRunTimes());
        values.put(Constants.RUNINFO_ISRUN, runInfo.getIsRun());
        values.put(Constants.RUNINFO_WEATHER_RAIN, weatherInfo.getmRain());
        values.put(Constants.RUNINFO_WEATHER_TEMP, weatherInfo.getmTemp());
        values.put(Constants.RUNINFO_WEATHER_WET, weatherInfo.getmWet());
        values.put(Constants.RUNINFO_WEATHER_HUM, weatherInfo.getmHum());
        values.put(Constants.RUNINFO_WEATHER_LIGHT, weatherInfo.getmLight());
        values.put(Constants.RUNINFO_WEATHER_PM, weatherInfo.getmPM());

        db.insert(Constants.RUNINFO_TABLE, null, values);

        if (db != null) {
            db.close();
            values.clear();
        }
    }

    public RunInfo findById(String runId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.ALARM_TABLE, null, Constants.RUNINFO_ID + "=?", new String[]{runId}, null, null, null);
        RunInfo runInfo = new RunInfo();
        WeatherInfo weatherInfo = new WeatherInfo();

        if (cursor != null) {
            if (cursor.moveToNext()) {

                runInfo.setRunID(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_ID)));
                runInfo.setRunMem(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_MEMO)));
                runInfo.setRunTimes(cursor.getInt(cursor.getColumnIndex(Constants.RUNINFO_TIMES)));
                runInfo.setIsRun(cursor.getInt(cursor.getColumnIndex(Constants.RUNINFO_ISRUN)));

                String sdate = cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_DATE));
                Date date = new Date();
                try {
                    date = YYYY_MM_DD.parse(sdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                runInfo.setRunDate(date);

                weatherInfo.setmRain(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_RAIN)));
                weatherInfo.setmTemp(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_TEMP)));
                weatherInfo.setmWet(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_WET)));
                weatherInfo.setmHum(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_HUM)));
                weatherInfo.setmLight(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_LIGHT)));
                weatherInfo.setmPM(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_PM)));
                runInfo.setRunWeather(weatherInfo);
            }
        }
        return runInfo;
    }

    public List<RunInfo> getAllRunInfo() {
        List<RunInfo> list = new ArrayList<RunInfo>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(Constants.RUNINFO_TABLE, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RunInfo runInfo = new RunInfo();
                WeatherInfo weatherInfo = new WeatherInfo();
                runInfo.setRunID(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_ID)));
                runInfo.setRunMem(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_MEMO)));
                runInfo.setRunTimes(cursor.getInt(cursor.getColumnIndex(Constants.RUNINFO_TIMES)));
                runInfo.setIsRun(cursor.getInt(cursor.getColumnIndex(Constants.RUNINFO_ISRUN)));

                String sdate = cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_DATE));
                Date date = new Date();
                try {
                    date = YYYY_MM_DD.parse(sdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                runInfo.setRunDate(date);

                weatherInfo.setmRain(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_RAIN)));
                weatherInfo.setmTemp(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_TEMP)));
                weatherInfo.setmWet(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_WET)));
                weatherInfo.setmHum(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_HUM)));
                weatherInfo.setmLight(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_LIGHT)));
                weatherInfo.setmPM(cursor.getString(cursor.getColumnIndex(Constants.RUNINFO_WEATHER_PM)));
                runInfo.setRunWeather(weatherInfo);

                list.add(runInfo);
            }
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        return list;
    }

    public void updateRunInfo(RunInfo runInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        WeatherInfo weatherInfo = runInfo.getRunWeather();

        values.put(Constants.RUNINFO_ID, runInfo.getRunID());
        values.put(Constants.RUNINFO_MEMO, runInfo.getRunMem());
        values.put(Constants.RUNINFO_DATE, YYYY_MM_DD.format(runInfo.getRunDate()));
        values.put(Constants.RUNINFO_TIMES, runInfo.getRunTimes());
        values.put(Constants.RUNINFO_ISRUN, runInfo.getIsRun());
        values.put(Constants.RUNINFO_WEATHER_RAIN, weatherInfo.getmRain());
        values.put(Constants.RUNINFO_WEATHER_TEMP, weatherInfo.getmTemp());
        values.put(Constants.RUNINFO_WEATHER_WET, weatherInfo.getmWet());
        values.put(Constants.RUNINFO_WEATHER_HUM, weatherInfo.getmHum());
        values.put(Constants.RUNINFO_WEATHER_LIGHT, weatherInfo.getmLight());
        values.put(Constants.RUNINFO_WEATHER_PM, weatherInfo.getmPM());

        db.updateWithOnConflict(Constants.RUNINFO_TABLE, values, Constants.RUNINFO_ID + " = ?",
                new String[]{runInfo.getRunID()}, SQLiteDatabase.CONFLICT_IGNORE);

        db.close();
    }

    public int getOnlyId(RunInfo runInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int id = -1;
        Cursor cusor = db.query(Constants.ALARM_TABLE, new String[]{"id"}, Constants.RUNINFO_ID + " = ?",
                new String[]{runInfo.getRunID()}, null, null, null);
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
}
