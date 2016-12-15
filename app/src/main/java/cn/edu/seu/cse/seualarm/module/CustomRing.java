package cn.edu.seu.cse.seualarm.module;

import java.io.Serializable;

/**
 * Created by Joe on 2016/1/30.
 */
public class CustomRing implements Serializable {
    private String ringName;
    private String ringPath;

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingPath() {
        return ringPath;
    }

    public void setRingPath(String ringPath) {
        this.ringPath = ringPath;
    }
}