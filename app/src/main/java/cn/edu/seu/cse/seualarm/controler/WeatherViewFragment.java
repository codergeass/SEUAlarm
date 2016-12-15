package cn.edu.seu.cse.seualarm.controler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class WeatherViewFragment extends Fragment {
    public static final int REFRESH_DELAY = 2000;
    private PullToRefreshView mPullToRefreshView;
    public WeatherView mWeatherView;
    private TextView mTemp;
    private TextView mRain;
    private TextView mHum;
    private TextView mWet;
    private TextView mPM;
    private TextView mLight;
    private WeatherInfo mWeatherInfo;

    public final String tempEnd = "℃";
    public final String humEnd = "hPa";
    public final String wetEnd = "RH";
    public final String pmEnd = "℃";
    public final String lightEnd = "lx";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherInfo = new WeatherInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather_view, container, false);
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWeatherView.cancelAnimation();

                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

        //WeatherView
        mWeatherView = (WeatherView) rootView.findViewById(R.id.weather_view);
        mTemp = (TextView) rootView.findViewById(R.id.tv_weather_temp);
        mHum = (TextView) rootView.findViewById(R.id.tv_hum_value);
        mWet = (TextView) rootView.findViewById(R.id.tv_wet_value);
        mRain = (TextView) rootView.findViewById(R.id.tv_weather_rain);
        mPM = (TextView) rootView.findViewById(R.id.tv_pm2_value);
        mLight = (TextView) rootView.findViewById(R.id.tv_light_value);

        updateData();
        updateView();
        mWeatherView.startAnimation();
        return rootView;
    }

    private void updateData() {

    }

    private void updateView() {
        if(mWeatherInfo.ismRain()) {
            mWeatherView.setWeather(Constants.weatherStatus.RAIN);
            Log.d("weatherView", "setWeatherView");
            mRain.setText("有雨");
        } else {
            mWeatherView.setWeather(Constants.weatherStatus.SUN);
            Log.d("weatherView", "setWeatherView");
            mRain.setText("无雨");
        }

        mTemp.setText(String.valueOf(mWeatherInfo.getmTemp()) + tempEnd);
        mHum.setText(String.valueOf(mWeatherInfo.getmHum()) + humEnd);
        mWet.setText(String.valueOf(mWeatherInfo.getmWet()) + wetEnd);
        mPM.setText(String.valueOf(mWeatherInfo.getmPM()) + pmEnd);
        mLight.setText(String.valueOf(mWeatherInfo.getmLight()) + lightEnd);
    }

}
