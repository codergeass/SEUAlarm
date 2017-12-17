package cn.edu.seu.cse.seualarm.controler.alarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.pwittchen.weathericonview.WeatherIconView;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.AlarmInfo;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.Constants;
import cn.edu.seu.cse.seualarm.util.PrefUtil;

/**
 * Created by Coder Geass on 2016/12/15.
 */

public class AlarmRingDialog extends Dialog {
    private View view;
    private Window window;
    private Button ok_btn;
//    private WeatherIconView weatherIconView;
    private ImageView weatherIconView;
    private ImageView alarmIconView;
    private TextView tv_temp, tv_wet, tv_hum, tv_light, tv_pm, tv_title, tv_time;
    private TextView tv_temp_show, tv_wet_show, tv_hum_show, tv_light_show, tv_pm_show;
    private LinearLayout ll_weather;
    private View.OnClickListener onClickListener;
    private Context context;
    private int src;

    public AlarmRingDialog(Context context) {
        super(context, R.style.AarmRingDialog);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_alarm_ring, null);
        ok_btn = (Button) view.findViewById(R.id.alarm_ring_ok);
//        weatherIconView = (WeatherIconView) view.findViewById(R.id.weather_icon);
        weatherIconView = (ImageView) view.findViewById(R.id.weather_icon);
        alarmIconView = (ImageView) view.findViewById(R.id.clock_view);

        tv_temp = (TextView) view.findViewById(R.id.tv_weather_info_temp);
        tv_wet = (TextView) view.findViewById(R.id.tv_weather_info_wet);
        tv_hum = (TextView) view.findViewById(R.id.tv_weather_info_hum);
        tv_pm = (TextView) view.findViewById(R.id.tv_weather_info_pm);
        tv_light = (TextView) view.findViewById(R.id.tv_weather_info_light);

        tv_temp_show = (TextView) view.findViewById(R.id.tv_weather_info_temp_show);
        tv_wet_show = (TextView) view.findViewById(R.id.tv_weather_info_wet_show);
        tv_hum_show = (TextView) view.findViewById(R.id.tv_weather_info_hum_show);
        tv_pm_show = (TextView) view.findViewById(R.id.tv_weather_info_pm_show);
        tv_light_show = (TextView) view.findViewById(R.id.tv_weather_info_light_show);

        tv_title = (TextView) view.findViewById(R.id.dialog_title);
        tv_time = (TextView) view.findViewById(R.id.tv_alarm_ring_time);
        ll_weather = (LinearLayout) view.findViewById(R.id.ll_weather);

        src = PrefUtil.getInt(context, Constants.WEATHER_SRC, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        show();
//        final Window win = getWindow();
//        win.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);

        setContentView(view);
    }

    // 禁用返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 设置确定键的监听事件
    public AlarmRingDialog setClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
        ok_btn.setOnClickListener(listener);
        return this;
    }


    public void showDialog() {
        window = getWindow();   //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim);       //设置窗口弹出动画

//        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        setCanceledOnTouchOutside(false);
        show();

        WindowManager.LayoutParams wl = window.getAttributes();
        window.setAttributes(wl);
    }

    public void setAttributes(Display display) {
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.height = (int)(display.getHeight() * 0.7);

        window = getWindow();
        window.setAttributes(wl);
    }

    // 设置天气信息 在时间设置之后
    public AlarmRingDialog setWeatherInfo(WeatherInfo weatherInfo) {
        tv_temp.setText(String.valueOf(weatherInfo.getmTemp()));
        tv_wet.setText(String.valueOf(weatherInfo.getmWet()));
        tv_hum.setText(String.valueOf(weatherInfo.getmHum()));
        tv_light.setText(String.valueOf(weatherInfo.getmLight()));
        tv_pm.setText(String.valueOf(weatherInfo.getmPM()));

        if (src == 2) {
            tv_pm_show.setText("PM2.5");
            tv_hum_show.setText("风向");
            tv_light_show.setText("风力");
        } else {
            tv_pm_show.setText("颗粒物");
            tv_hum_show.setText("气压");
            tv_light_show.setText("照度");
        }

        int hour = Integer.valueOf(tv_time.getText().toString().substring(0,2));
        Log.d("alarm", "hour " + hour);

        if (!weatherInfo.getmRain().contains("无") && weatherInfo.getmRain().contains("雨")) {
            Log.d("alarm", "weather rain");
            if (hour >= 6 && hour <= 18) {
                weatherIconView.setImageResource(R.drawable.weather_rain);
//                weatherIconView.setIconResource(context.getResources().getString(R.string.wi_day_rain));
            }
            else {
                weatherIconView.setImageResource(R.drawable.weather_rain);
//                weatherIconView.setIconResource(context.getResources().getString(R.string.wi_night_rain));
            }
        } else if (weatherInfo.getmRain().contains("雪")) {
            weatherIconView.setImageResource(R.drawable.weather_snow);
        } else if (weatherInfo.getmRain().contains("雷")) {
            weatherIconView.setImageResource(R.drawable.weather_thunder);
        } else if (weatherInfo.getmRain().contains("风")) {
            weatherIconView.setImageResource(R.drawable.weather_windy);
        } else {
            Log.d("alarm", "weather sunny");
            if (hour >= 6 && hour <= 18) {
                weatherIconView.setImageResource(R.drawable.weather_day);
//                weatherIconView.setIconResource(context.getResources().getString(R.string.wi_day_sunny));
            }
            else {
                weatherIconView.setImageResource(R.drawable.weather_night);
//                weatherIconView.setIconResource(context.getResources().getString(R.string.wi_night_clear));
            }
        }

        return this;
    }

    // 不显示天气信息
    public AlarmRingDialog disWeatherInfo() {
        tv_temp.setVisibility(View.INVISIBLE);
        tv_wet.setVisibility(View.INVISIBLE);
        tv_hum.setVisibility(View.INVISIBLE);
        tv_light.setVisibility(View.INVISIBLE);
        tv_pm.setVisibility(View.INVISIBLE);

        tv_temp_show.setVisibility(View.INVISIBLE);
        tv_wet_show.setVisibility(View.INVISIBLE);
        tv_hum_show.setVisibility(View.INVISIBLE);
        tv_light_show.setVisibility(View.INVISIBLE);
        tv_pm_show.setVisibility(View.INVISIBLE);

        weatherIconView.setVisibility(View.INVISIBLE);
        ll_weather.setVisibility(View.GONE);
        alarmIconView.setVisibility(View.VISIBLE);

        return this;
    }

    // 设置闹钟标题
    public AlarmRingDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    // 设置时间
    public AlarmRingDialog setTime(String time) {
        tv_time.setText(time);
        return this;
    }
}
