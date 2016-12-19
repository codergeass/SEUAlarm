package cn.edu.seu.cse.seualarm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/16.
 */

public class RunInfoDbHelper extends SQLiteOpenHelper {
    private Context mContext;
    public static final String CREATE_RUNINFO ="create table "+ Constants.RUNINFO_TABLE +
            "(id integer primary key autoincrement,"+
            Constants.RUNINFO_ID + " text,"+
            Constants.RUNINFO_DATE + " text ,"+
            Constants.RUNINFO_MEMO + " text,"+
            Constants.RUNINFO_TIMES + " integer,"+
            Constants.RUNINFO_ISRUN + " integer,"+
            Constants.RUNINFO_WEATHER_RAIN + " text,"+
            Constants.RUNINFO_WEATHER_TEMP + " text,"+
            Constants.RUNINFO_WEATHER_HUM + " text,"+
            Constants.RUNINFO_WEATHER_WET + " text,"+
            Constants.RUNINFO_WEATHER_LIGHT + " text,"+
            Constants.RUNINFO_WEATHER_PM + " text"+
            ")";

    public RunInfoDbHelper(Context context) {
        super(context, Constants.RUNINFO_SQLDB_NAME, null, 1);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建一个数据库
        db.execSQL(CREATE_RUNINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
