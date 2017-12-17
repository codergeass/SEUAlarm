package cn.edu.seu.cse.seualarm.controler.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.MyAlarmManager;
import cn.edu.seu.cse.seualarm.util.NetworkUtil;
import cn.edu.seu.cse.seualarm.util.AsyncHttpClient;
import cn.edu.seu.cse.seualarm.util.PrefUtil;
import cn.edu.seu.cse.seualarm.util.WeatherInfoClient;

/**
 * Created by Coder Geass on 2016/12/10.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmInfo alarmInfo;
    private WeatherInfo weatherInfo;
    private Context mContext;
    private boolean showWeather;
    private boolean isRain;
    private boolean getRain;
    private Bundle bundle1;
    private int src;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        Bundle bundle = intent.getExtras();

        bundle1 = new Bundle();
        alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
        src = PrefUtil.getInt(context, Constants.WEATHER_SRC, 0);

        getRain = false;
        if (alarmInfo != null)
            getRain = alarmInfo.getRain() == 1;

        if (alarmInfo == null)
            Log.d("alarm", "alarm receiver alarminfo null");

        Log.d("alarm", "alarmreceiver ring resid" + alarmInfo.getRingResId());

        isRain = false;

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

        boolean alarmweather = PrefUtil.getBoolean(context, Constants.ALARM_WEATHER, true);
        // 闹铃提醒
        // 先获取天气信息
        if (alarmweather && NetworkUtil.isNetworkAvailable(context)) {
            Log.d("alarm", "get weatherinfo");
            if (src == 1)
                WeatherInfoClient.getLocalWeatherInfo(
                        PrefUtil.getString(context, Constants.IP_WEB, "223.3.173.237"));
            else if (src == 2)
                WeatherInfoClient.getPublicWeatherInfo(
                        PrefUtil.getString(context, Constants.CITY_NAME, "南京"));
            else
                WeatherInfoClient.getWeathInfo();
            //sleep();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (WeatherInfoClient.resCode) {
                        weatherInfo = WeatherInfoClient.weatherInfo;
                        showWeather = true;
                        isRain = WeatherInfoClient.isRain();

                        bundle1.putSerializable(Constants.ALARM_INFO, alarmInfo);
                        bundle1.putSerializable(Constants.WEATHER_INFO, weatherInfo);
                        bundle1.putBoolean(Constants.SHOW_WEATHER, showWeather);

                        // 判断是否提醒
                        if (!isRain || !getRain) {
                            Intent alarmRingItent = new Intent(mContext, AlarmRingActivity.class);
                            alarmRingItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            alarmRingItent.putExtras(bundle1);

                            mContext.startActivity(alarmRingItent);
                        }
                    }
                }
            }, 2000);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (WeatherInfoClient.resCode) {
//                        weatherInfo = WeatherInfoClient.weatherInfo;
//                        showWeather = true;
//                        isRain = WeatherInfoClient.isRain();
//                    }
//                }
//            });
        } else {
            weatherInfo = new WeatherInfo();
            showWeather = false;
            Log.d("alarm", "network error");

            bundle1.putSerializable(Constants.ALARM_INFO, alarmInfo);
            bundle1.putSerializable(Constants.WEATHER_INFO, weatherInfo);
            bundle1.putBoolean(Constants.SHOW_WEATHER, showWeather);

            // 判断是否提醒
            if (!isRain || !getRain) {
                Intent alarmRingItent = new Intent(context, AlarmRingActivity.class);
                alarmRingItent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmRingItent.putExtras(bundle1);

                context.startActivity(alarmRingItent);
            }
        }
    }
}
