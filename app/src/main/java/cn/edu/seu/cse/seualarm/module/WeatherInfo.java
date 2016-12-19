package cn.edu.seu.cse.seualarm.module;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class WeatherInfo implements Serializable {
//    private double mTemp;
//    private boolean mRain;
//    private double mHum;
//    private double mWet;
//    private double mPM;
//    private double mLight;

    private String mTemp;
    private String mRain;
    private String mHum;
    private String mWet;
    private String mPM;
    private String mLight;
    private String mRefresh;

    private SimpleDateFormat YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WeatherInfo() {
        this.mTemp = "20℃";
        this.mRain = "无雨";
        this.mHum = "1034hPa";
        this.mWet = "60%RH";
        this.mPM = "200";
        this.mLight = "1000lx";
        this.mRefresh = YMDHM.format(new Date());
    }

    public WeatherInfo(String mTemp, String mRain, String mHum, String mWet, String mPM, String mLight,
                       String mRefresh) {
        this.mTemp = mTemp;
        this.mRain = mRain;
        this.mHum = mHum;
        this.mWet = mWet;
        this.mPM = mPM;
        this.mLight = mLight;
        this.mRefresh = mRefresh;
    }

    public String getmRefresh() {
        return mRefresh;
    }

    public void setmRefresh(String mRefresh) {
        this.mRefresh = mRefresh;
    }

    public String getmTemp() {
        return mTemp;
    }

    public void setmTemp(String mTemp) {
        this.mTemp = mTemp;
    }

    public String getmRain() {
        return mRain;
    }

    public void setmRain(String mRain) {
        this.mRain = mRain;
    }

    public String getmHum() {
        return mHum;
    }

    public void setmHum(String mHum) {
        this.mHum = mHum;
    }

    public String getmWet() {
        return mWet;
    }

    public void setmWet(String mWet) {
        this.mWet = mWet;
    }

    public String getmPM() {
        return mPM;
    }

    public void setmPM(String mPM) {
        this.mPM = mPM;
    }

    public String getmLight() {
        return mLight;
    }

    public void setmLight(String mLight) {
        this.mLight = mLight;
    }
}

//    public WeatherInfo() {
//        this.mTemp = 20.0;
//        this.mRain = true;
//        this.mHum = 1030.0;
//        this.mWet = 80.0;
//        this.mPM = 100.0;
//        this.mLight = 100.0;
//    }
//
//    public WeatherInfo(double mTemp, boolean mRain, double mHum, double mWet, double mPM, double mLight) {
//        this.mTemp = mTemp;
//        this.mRain = mRain;
//        this.mHum = mHum;
//        this.mWet = mWet;
//        this.mPM = mPM;
//        this.mLight = mLight;
//    }
//
//    public double getmWet() {
//        return mWet;
//    }
//
//    public void setmWet(double mWet) {
//        this.mWet = mWet;
//    }
//
//    public double getmLight() {
//        return mLight;
//    }
//
//    public void setmLight(double mLight) {
//        this.mLight = mLight;
//    }
//
//    public double getmTemp() {
//        return mTemp;
//    }
//
//    public void setmTemp(double mTemp) {
//        this.mTemp = mTemp;
//    }
//
//    public boolean ismRain() {
//        return mRain;
//    }
//
//    public void setmRain(boolean mRain) {
//        this.mRain = mRain;
//    }
//
//    public double getmHum() {
//        return mHum;
//    }
//
//    public void setmHum(double mHum) {
//        this.mHum = mHum;
//    }
//
//    public double getmPM() {
//        return mPM;
//    }
//
//    public void setmPM(double mPM) {
//        this.mPM = mPM;
//    }
//}
