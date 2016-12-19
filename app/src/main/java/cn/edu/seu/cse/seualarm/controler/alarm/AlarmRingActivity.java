package cn.edu.seu.cse.seualarm.controler.alarm;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;

/**
 * Created by Coder Geass on 2016/12/15.
 */

public class AlarmRingActivity extends Activity{
    private AlarmInfo alarmInfo;
    private WeatherInfo weatherInfo;
    private boolean showWeather;
    private AlarmRingDialog dialog;
    private Intent alarmRingService;


    //public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//关键代码

        Bundle bundle = getIntent().getExtras();
        alarmInfo = (AlarmInfo) bundle.getSerializable(Constants.ALARM_INFO);
        weatherInfo = (WeatherInfo) bundle.getSerializable(Constants.WEATHER_INFO);
        showWeather = bundle.getBoolean(Constants.SHOW_WEATHER);

        Log.d("alarm", "alarmringactivity get alarminfo");

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm_ring);

        alarmRing();
    }

    @Override
    public void onBackPressed() {
        // 禁用back键
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
//            stopService(alarmRingService);
//            dialog.dismiss();
//            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if (alarmRingService != null)
            stopService(alarmRingService);
        finish();
        super.onPause();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void alarmRing() {
        String label = alarmInfo.getLabel();
        label = label.length() > 0 ? "标签:" + label : "闹钟";

        String hour = alarmInfo.getHour() + "";
        String minute = alarmInfo.getMinute() + "";
        if(alarmInfo.getHour() < 10) hour = "0" + alarmInfo.getHour();
        if(alarmInfo.getMinute() < 10) minute = "0" + alarmInfo.getMinute();
        String time = hour + " : " + minute;

        Log.d("alarm", "alarminfo ringresid" + alarmInfo.getRingResId());

        alarmRingService = new Intent(this, AlarmRingService.class);
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable(Constants.ALARM_INFO, alarmInfo);
        alarmRingService.putExtras(bundle1);
        startService(alarmRingService);

        dialog = new AlarmRingDialog(this);
        dialog.setTitle(label).setTime(time).setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(alarmRingService);
                dialog.dismiss();
                finish();
            }
        });
        if (showWeather) dialog.setWeatherInfo(weatherInfo);
        else dialog.disWeatherInfo();

        dialog.showDialog();
//        Display display = getWindowManager().getDefaultDisplay();
//        dialog.setAttributes(display);
    }
}
