package cn.edu.seu.cse.seualarm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class AlarmInfoDbHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_ALARMINFO="create table "+ Constants.ALARM_TABLE +
            "(id integer primary key autoincrement,"+
            Constants.ALARM_HOUR + " integer,"+
            Constants.ALARM_MINUTE + " integer ,"+
            Constants.ALARM_ENABLE + " integer,"+
            Constants.ALARM_VIBRATE + " integer,"+
            Constants.ALARM_RAIN + " integer,"+
            Constants.ALARM_RINGTONE + " text,"+
            Constants.ALARM_RES_ID + " text,"+
            Constants.ALARM_LABEL + " text,"+
            Constants.ALARM_SID + " text,"+
            Constants.ALARM_REPEAT_DAY + " text"+
            ")";

    public AlarmInfoDbHelper(Context context) {
        super(context, Constants.SQLDB_NAME, null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个数据库
        db.execSQL(CREATE_ALARMINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

