package cn.edu.seu.cse.seualarm.controler;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.seu.cse.seualarm.R;
import cn.edu.seu.cse.seualarm.module.WeatherInfo;
import cn.edu.seu.cse.seualarm.util.NetworkUtil;
import cn.edu.seu.cse.seualarm.util.PrefUtil;
import cn.edu.seu.cse.seualarm.util.WeatherInfoClient;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class WeatherViewFragment extends Fragment {
    public static final int REFRESH_DELAY = 1500;
    private PullToRefreshView mPullToRefreshView;
    public WeatherView mWeatherView;
    private TextView mTemp;
    private TextView mRain;
    private TextView mHum;
    private TextView tvHum;
    private TextView mWet;
    private TextView mPM;
    private TextView mLight;
    private TextView tvLight;
    private TextView mRefresh;
    private TextView mRefreshName;
    private ImageView mSunView;
    private WeatherInfo mWeatherInfo;
    private boolean loadOk;
    private boolean isInit = true;
    private boolean isFresh = false;
    private int src = 0;
    private Date refreshTime;
    private String refreshDate;

//    public final String tempEnd = "℃";
//    public final String humEnd = "hPa";
//    public final String wetEnd = "RH";
//    public final String pmEnd = "℃";
//    public final String lightEnd = "lx";


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        src = PrefUtil.getInt(getContext(), cn.edu.seu.cse.seualarm.util.Constants.WEATHER_SRC, 0);

//        View rootView = View.inflate(getActivity(), R.layout.fragment_weather_view, null);
//        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mWeatherView.cancelAnimation();
//                mPullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateData();
//                        mPullToRefreshView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateView();
//                                mWeatherView.startAnimation();
//                            }
//                        }, 1200);
////                        updateView();
////                        mWeatherView.startAnimation();
//                        mPullToRefreshView.setRefreshing(false);
//                    }
//                }, REFRESH_DELAY);
//            }
//        });
//
//        //WeatherView
//        mWeatherView = (WeatherView) rootView.findViewById(R.id.weather_view);
//        mTemp = (TextView) rootView.findViewById(R.id.tv_weather_temp);
//        mHum = (TextView) rootView.findViewById(R.id.tv_hum_value);
//        mWet = (TextView) rootView.findViewById(R.id.tv_wet_value);
//        mRain = (TextView) rootView.findViewById(R.id.tv_weather_rain);
//        mPM = (TextView) rootView.findViewById(R.id.tv_pm2_value);
//        mLight = (TextView) rootView.findViewById(R.id.tv_light_value);

        Log.d("alarm", "weatherviewfragment oncreate loadok:" + loadOk);
        refreshTime = new Date();
        if (!loadOk) {
            updateData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_weather_view, container, false);
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mWeatherInfo.getmRain().contains("无"))
                    mWeatherView.cancelAnimation();
                updateData();
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mPullToRefreshView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
////                                updateView();
//                                mWeatherView.startAnimation();
//                            }
//                        }, 2000);
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
        mRefresh = (TextView) rootView.findViewById(R.id.tv_refresh_value);
        mRefreshName = (TextView) rootView.findViewById(R.id.tv_refresh);
        mSunView = (ImageView) rootView.findViewById(R.id.sun_icon);

        tvHum = (TextView) rootView.findViewById(R.id.tv_hum);
        tvLight = (TextView) rootView.findViewById(R.id.tv_light);
        // mWeatherInfo = new WeatherInfo();

//        if (!loadOk) {
//            updateData();
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////
////                }
////            }, 2000)
//        } else
//            updateView();
        // updateData();

//        mWeatherView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateView();
//                mWeatherView.startAnimation();
//            }
//        }, 1000);

//        mWeatherView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                updateData();
//
//                mWeatherView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateView();
//                        mWeatherView.startAnimation();
//                    }
//                }, 1000);
//            }
//        }, 1000);

        return rootView;
    }


//    @Override
//    public void onStart() {
////        if (!loadOk) {
////            updateData();
//////            new Handler().postDelayed(new Runnable() {
//////                @Override
//////                public void run() {
//////
//////                }
//////            }, 2000)
////        } else
////            updateView();
//        super.onStart();
//    }

    @Override
    public void onStart() {
        if (!loadOk) {
            updateData();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }, 2000)
        } else
            updateView();

        super.onStart();
    }

    private void updateRefresh() {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        if (loadOk) {
            mRefreshName.setVisibility(View.VISIBLE);
            mRefresh.setText(mWeatherInfo.getmRefresh());
        }
        else {
            mRefreshName.setVisibility(View.GONE);
            mRefresh.setText("数据未更新");
        }
    }

    @Override
    public void onPause() {
        mWeatherView.cancelAnimation();
        isFresh = false;
        super.onPause();
    }

    private void updateData() {
        if (NetworkUtil.isNetworkAvailable(getContext())) {
            src = PrefUtil.getInt(getContext(), cn.edu.seu.cse.seualarm.util.Constants.WEATHER_SRC, 0);
            WeatherInfoClient.getWeathInfo(src);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!WeatherInfoClient.resCode) {
                        if (isInit && !loadOk)
                            mWeatherInfo = new WeatherInfo();
                        final Snackbar snackbar = Snackbar.make(getView(), "查询天气信息失败", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
                        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                                .setTextColor(Color.parseColor("#423752"));
                        snackbar.show();
                        loadOk = false;
                        updateView();
//                        mWeatherView.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                final Snackbar snackbar = Snackbar.make(getView(), "查询天气信息失败", Snackbar.LENGTH_SHORT);
//                                snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
//                                ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
//                                        .setTextColor(Color.parseColor("#423752"));
//                                snackbar.show();
//                            }
//                        }, 500);
                    } else {
                        mWeatherInfo = WeatherInfoClient.weatherInfo;
                        loadOk = true;
                        isFresh = true;
                        refreshTime = new Date();
                        updateView();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                final Snackbar snackbar = Snackbar.make(getView(), "天气信息已获取", Snackbar.LENGTH_SHORT);
//                                snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
//                                ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
//                                        .setTextColor(Color.parseColor("#423752"));
//                                snackbar.show();
//                            }
//                        }, 500);
                    }
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isInit && !loadOk)
                        mWeatherInfo = new WeatherInfo();
                    final Snackbar snackbar = Snackbar.make(getView(), "网络连接不可用", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
                    ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                            .setTextColor(Color.parseColor("#423752"));
                    snackbar.show();
                    loadOk = false;
//                    updateView();
                }
            }, 1000);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    final Snackbar snackbar = Snackbar.make(getView(), "网络连接不可用", Snackbar.LENGTH_SHORT);
//                    snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
//                    ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
//                            .setTextColor(Color.parseColor("#423752"));
//                    snackbar.show();
//                }
//            }, 1000);
        }
    }

    public void updateView() {
        if(!mWeatherInfo.getmRain().contains("无") && mWeatherInfo.getmRain().contains("雨")) {
            mSunView.setVisibility(View.INVISIBLE);
            mWeatherView.setWeather(Constants.weatherStatus.RAIN);
            mWeatherView.startAnimation();
            Log.d("weatherView", "setWeatherView");
            //mRain.setText("有雨");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
            int now = Integer.valueOf(simpleDateFormat.format(new Date()));
            if (now >= 6 && now <= 18)
                mSunView.setImageResource(R.drawable.sun_cloud_icon);
            else
                mSunView.setImageResource(R.drawable.full_moon_512);

            mSunView.setVisibility(View.VISIBLE);
            mWeatherView.setWeather(Constants.weatherStatus.SUN);
            Log.d("weatherView", "setWeatherView");
            //mRain.setText("无雨");
        }

        if (src == 2) {
            tvHum.setText("风向");
            tvLight.setText("风力");
        } else {
            tvHum.setText("气压");
            tvLight.setText("照度");
        }

//        mTemp.setText(String.valueOf(mWeatherInfo.getmTemp()) + tempEnd);
//        mHum.setText(String.valueOf(mWeatherInfo.getmHum()) + humEnd);
//        mWet.setText(String.valueOf(mWeatherInfo.getmWet()) + wetEnd);
//        mPM.setText(String.valueOf(mWeatherInfo.getmPM()) + pmEnd);
//        mLight.setText(String.valueOf(mWeatherInfo.getmLight()) + lightEnd);

        mRain.setText(mWeatherInfo.getmRain());
        mTemp.setText(String.valueOf(mWeatherInfo.getmTemp()));
        mHum.setText(String.valueOf(mWeatherInfo.getmHum()));
        mWet.setText(String.valueOf(mWeatherInfo.getmWet()));
        mPM.setText(String.valueOf(mWeatherInfo.getmPM()));
        mLight.setText(String.valueOf(mWeatherInfo.getmLight()));

        if ((isInit || isFresh) && loadOk) {
            if (isInit)
                isInit = false;
            final Snackbar snackbar = Snackbar.make(getView(), "天气信息已获取", Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(Color.parseColor("#9b92b3"));
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text))
                    .setTextColor(Color.parseColor("#423752"));
            snackbar.show();
        }

        updateRefresh();
    }

    public void setLoadOk(boolean ok) {
        loadOk = ok;
    }

    public void setmWeatherInfo(WeatherInfo weatherInfo) {
        mWeatherInfo = weatherInfo;
    }

    public WeatherInfo getmWeatherInfo() {
        return mWeatherInfo;
    }
}
