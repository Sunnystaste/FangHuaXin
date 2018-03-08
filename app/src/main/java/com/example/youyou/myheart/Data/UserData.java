package com.example.youyou.myheart.Data;

/**
 * Created by youyou on 2018/2/15.
 */

public class UserData {
    private String userId;
    private String nickname;
    private String userPic;
    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserData() {
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", userPic='" + userPic + '\'' +
                '}';
    }

    public UserData(String userId, String nickname, String userPic) {
        this.userId = userId;
        this.nickname = nickname;
        this.userPic = userPic;
    }
}
