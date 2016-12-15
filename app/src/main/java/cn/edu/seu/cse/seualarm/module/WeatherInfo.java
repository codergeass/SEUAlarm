package cn.edu.seu.cse.seualarm.module;

import java.io.Serializable;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class WeatherInfo implements Serializable{
    private double mTemp;
    private boolean mRain;
    private double mHum;
    private double mWet;
    private double mPM;
    private double mLight;

    public WeatherInfo() {
        this.mTemp = 20.0;
        this.mRain = true;
        this.mHum = 1030.0;
        this.mWet = 80.0;
        this.mPM = 100.0;
        this.mLight = 100.0;
    }

    public WeatherInfo(double mTemp, boolean mRain, double mHum, double mWet, double mPM, double mLight) {
        this.mTemp = mTemp;
        this.mRain = mRain;
        this.mHum = mHum;
        this.mWet = mWet;
        this.mPM = mPM;
        this.mLight = mLight;
    }

    public double getmWet() {
        return mWet;
    }

    public void setmWet(double mWet) {
        this.mWet = mWet;
    }

    public double getmLight() {
        return mLight;
    }

    public void setmLight(double mLight) {
        this.mLight = mLight;
    }

    public double getmTemp() {
        return mTemp;
    }

    public void setmTemp(double mTemp) {
        this.mTemp = mTemp;
    }

    public boolean ismRain() {
        return mRain;
    }

    public void setmRain(boolean mRain) {
        this.mRain = mRain;
    }

    public double getmHum() {
        return mHum;
    }

    public void setmHum(double mHum) {
        this.mHum = mHum;
    }

    public double getmPM() {
        return mPM;
    }

    public void setmPM(double mPM) {
        this.mPM = mPM;
    }
}
