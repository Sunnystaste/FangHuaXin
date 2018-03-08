package com.example.youyou.myheart.Data;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by youyou on 2018/1/30.
 */

public class QAClientVO implements Serializable {
    public String quId;
    public int quType;
    public String quUserId;
    public String quUserNickname;
    public String quUserPic;
    public String quContent;
    public Integer quVoteNum;
    public long quCreatedTime;
    public String anId;
    public String anVoice;
    public Integer anVoiceDuration;
    public String anVoiceListened;
    public String anUserId;
    public String anUserNickname;
    public String anUserPic;
    public String anUserRemark;
    public String anContent;
    public boolean voted;
    public long answerDate;
    public int answerVoteNum;
    public boolean answerVoted;

}
