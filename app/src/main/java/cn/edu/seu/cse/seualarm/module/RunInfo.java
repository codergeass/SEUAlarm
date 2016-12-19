package cn.edu.seu.cse.seualarm.module;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class RunInfo implements Serializable, Comparable{
    private Date runDate;
    private String runMem;
    private WeatherInfo runWeather;
    private String runID;
    private int runTimes;
    private int isRun;

    public RunInfo() {
        this.runDate = new Date();
        this.runMem = "";
        this.runWeather = new WeatherInfo();
        this.runTimes = 0;
        this.isRun = 0;
        this.runID = new Date().toString() + new Random().nextLong();
    }

    public RunInfo(Date runDate, String runMem, WeatherInfo runWeather, int runTimes, int isRun) {
        this.runDate = runDate;
        this.runMem = runMem;
        this.runWeather = runWeather;
        this.runTimes = runTimes;
        this.isRun = isRun;
        this.runID = new Date().toString() + new Random().nextLong();
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof RunInfo) {
            RunInfo r = (RunInfo)o;
            return this.runDate.compareTo(r.getRunDate());
        }
        return 0;
    }

    public int getIsRun() {
        return isRun;
    }

    public void setIsRun(int isRun) {
        this.isRun = isRun;
    }

    public String getRunID() {
        return runID;
    }

    public void setRunID(String runID) {
        this.runID = runID;
    }

    public int getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(int runTimes) {
        this.runTimes = runTimes;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public String getRunMem() {
        return runMem;
    }

    public void setRunMem(String runMem) {
        this.runMem = runMem;
    }

    public WeatherInfo getRunWeather() {
        return runWeather;
    }

    public void setRunWeather(WeatherInfo runWeather) {
        this.runWeather = runWeather;
    }
}
