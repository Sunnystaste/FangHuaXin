package com.example.youyou.myheart.Data;

/**
 * Created by youyou on 2018/1/30.
 */

public class ActivityClientVO {
    public String uuid;
    public String name;
    public String acType;
    public String integral;
    public long startTime;
    public long endTime;
    public String address;
    public String titleGraph;
    @Override
    public String toString() {
        return "DataBean [uuid=" + uuid + ", name=" + name + ", acType=" + acType + ", integral=" + integral
                + ", startTime=" + startTime + ", endTime=" + endTime + ", address=" + address + ", titleGraph="
                + titleGraph + "]";
    }
}
