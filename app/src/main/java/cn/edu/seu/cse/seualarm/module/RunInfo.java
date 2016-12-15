package cn.edu.seu.cse.seualarm.module;

import java.util.Date;

/**
 * Created by Coder Geass on 2016/12/9.
 */

public class RunInfo {
    private Date runDate;
    private String runMem;
    private WeatherInfo runWeather;

    public RunInfo() {
        this.runDate = new Date();
        this.runMem = "";
        this.runWeather = new WeatherInfo();
    }

    public RunInfo(Date runDate, String runMem, WeatherInfo runWeather) {
        this.runDate = runDate;
        this.runMem = runMem;
        this.runWeather = runWeather;
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
