package com.example.youyou.myheart.Data;

import android.text.Editable;

/**
 * Created by youyou on 2018/2/17.
 */

public class AuthenticateData {
    private String userId;
    private Integer collegeType;
    private String studentId;
    private String userName;
    private String phoneNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCollegeType() {
        return collegeType;
    }

    public void setCollegeType(Integer collegeType) {
        this.collegeType = collegeType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
