package com.example.youyou.myheart.Data;

/**
 * Created by youyou on 2018/1/30.
 */

public class ActivityDetailClientVO {
    public String uuid;
    public String name;
    public int type;
    public int integral;
    public String limitationCount;
    public int homeworkType;
    public long startTime;
    public long endTime;
    public String address;
    public String acContent;
    public String titleGraph;
    public boolean needAppointment;
    public boolean isCheckin;
    public int status;

    @Override
    public String toString() {
        return "ActivityDetailClientVO{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", integral=" + integral +
                ", limitationCount=" + limitationCount +
                ", homeworkType=" + homeworkType +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", address='" + address + '\'' +
                ", acContent='" + acContent + '\'' +
                ", titleGraph='" + titleGraph + '\'' +
                ", needAppointment=" + needAppointment +
                ", isCheckin=" + isCheckin +
                ", status=" + status +
                '}';
    }
}
