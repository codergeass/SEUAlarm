package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.edu.seu.cse.seualarm.dao.AlarmInfoDao;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.MyAlarmManager;
import cn.edu.seu.cse.seualarm.util.PrefUtil;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm", "Boot收到广播");
        context.startService(new Intent(context, DaemonService.class));
    }
}
