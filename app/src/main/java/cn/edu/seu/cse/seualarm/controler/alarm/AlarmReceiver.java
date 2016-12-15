package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.MyAlarmManager;
import cn.edu.seu.cse.seualarm.util.NetworkUtil;
import cn.edu.seu.cse.seualarm.util.WeatherInfoClient;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        AlarmInfo alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
        boolean getRain = false;
        if (alarmInfo != null)
            getRain = alarmInfo.getRain() == 1;

        if (alarmInfo == null)
            Log.d("alarm", "alarm receiver alarminfo null");

        boolean isRain = false;

        if (alarmInfo != null) {
            if (alarmInfo.getEnable() == 0)
                MyAlarmManager.cancelAlarmClock(context, alarmInfo);
            // 一次性闹铃
            if (alarmInfo.getDayOfWeek().length == 0) {
                MyAlarmManager.cancelOneAlarm(context, alarmInfo);
            } else {
                // 重复闹铃
                MyAlarmManager.startAlarmClock(context, alarmInfo);
            }
        }

        // 闹铃提醒
        // 先获取天气信息
        if (NetworkUtil.isNetworkAvailable(context)) {
            isRain = WeatherInfoClient.isRain();
        }

        // 判断是否提醒
        if (!isRain || !getRain) {
            Intent clockIntent = new Intent(context, AlarmRingActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putSerializable(Constants.ALARM_INFO, alarmInfo);
            clockIntent.putExtras(bundle1);
            clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(clockIntent);
        }
    }
}
