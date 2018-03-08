package com.example.youyou.myheart.Tool;


import com.example.youyou.myheart.Data.SortData;
import com.example.youyou.myheart.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youyou on 2017/12/14.
 */

public class AppConstant {
    public static final String ServerUrl="https://www.huaxinapp.xyz/test/";
    public static final String getAllActivity="/activity/getAllActivityClientVOs.do";
    public static final String getActivityDetail=" /activity/getActivityDetailClientVO.do";
    public static final String getQA="/qa/getQAClientVO.do";
    public static final String getAllQA="/qa/getAllQAClientVO.do";
    public static final String getMyActivities="/activity/getMyActivities.do";
    public static final String getMyFinishedActivities="/user/getMyFinishedActivities.do";
    public static final String getUserIntegral="/user/getUserIntegral.do";
    public static final String getQuestionReplies="/reply/getQuestionReplies.do";
    public static final String getMyAskingQA="/qa/getMyAskingQAClientVO.do";
    public static final String getMyAnsweringQA="/qa/getMyAnsweringQAClientVO.do";
    public static final String getUserCreditStatus="/user/getUserCreditStatus.do";
    public static final String getAllBanners ="/banner/getAllBanners.do";

    public static final String CancelVote="/qa/cancelVote.do" ;
    public static final String VoteOne="/qa/voteOne.do";
    public static final String SighUp="/activityRecord/activitySignUp.do";
    public static final String addReply="/reply/addReply.do";
    public static final String createUser ="/user/createUser.do";
    public static final String userLogin ="/user/userLogin.do";
    public static final String addQuestion="/qa/addQuestion.do";
    public static final String Authenticate="/user/userAuthenticate.do";
    public static final String GetAuthenticate="/user/getUserAuthInfo.do";
    public static final String createOpinion="/opinion/createOpinion.do";
    public static final String VoicListened="/qa/VoiceListenedOne.do";
    public static final String ActivityCheckin="/activityRecord/activityCheckin.do";
    public static final String voteAnswer = "/qa/voteAnswer.do";
    public static final String voteReply  = "/reply/voteReply.do";
    public static final String getIsVoted="/qa/getIsVoted.do";
    public static final Map<Integer,String> sortMap=new HashMap<Integer,String>() {
        {
            put(1,"人际交往");
            put(2,"个人成长");
            put(3,"恋爱情感");
            put(4,"家庭关系");
            put(5,"情绪压力");
            put(6,"职业规划");
            put(7,"性心理");
            put(8,"其他");
        }
    };
    public static final Map<Integer,String> activityMap=new HashMap<Integer,String>() {
        {
            put(1,"沙盘疗法体验");
            put(2,"园艺心理体验");
            put(3,"艺术心理体验");
            put(4,"中心回访体验");
            put(6,"催眠体验");
            put(7,"脑波音乐");
            put(8,"减压绘画体验");
            put(9,"放松训练项目");
            put(21,"心微课");
            put(22,"心理学讲座");
            put(23,"心理学课程");
            put(24,"团体心理辅导");
            put(31,"心理志愿服务");
            put(32,"大学生心理健康节");
            put(33,"新生心理健康节");
            put(34,"心理辅导站活动");
            put(35,"心理社团活动");
            put(41,"沙河心韵");
            put(42,"一心一语");
            put(43,"成长吧");
            put(44,"XY老师的心理沙龙");
            put(45,"成长导师");
            put(46,"柠檬沙语");
            put(51,"心理测评");
            put(52,"其他");
        }
    };
    public static final List<SortData> SortList= Arrays.asList(
            new SortData(R.drawable.icon_experience,"体验"),
            new SortData(R.drawable.icon_course,"课程"),
            new SortData(R.drawable.icon_activity,"活动"),
            new SortData(R.drawable.icon_salon,"沙龙"),
            new SortData(R.drawable.icon_recommend,"其他"));



    public static final List<String> TiyanList= Arrays.asList("沙盘疗法体验", "沙盘疗法体验","园艺心理体验","艺术心理体验","中心回访体验","催眠体验","脑波音乐","减压绘画体验","放松训练项目");
    public static final List<String> KechengList= Arrays.asList("心微课","心理学讲座","心理学课程","团体心理辅导");
    public static final List<String> HuodongList= Arrays.asList("大学生心理健康节","新生心理健康节","心理辅导站活动","心理社团活动","心理志愿服务");
    public static final List<String> ShalongList= Arrays.asList("XY老师的心理沙龙","沙河心韵","一心一语","成长吧","成长导师面对面","柠檬沙语");
    public static final List<String> QitaList= Arrays.asList("心理测评","其他");

    public static List createArrays() {
        {
            ArrayList<String> list = new ArrayList<String>();
            list.add("通信与信息工程学院");
            list.add("电子工程学院");
            list.add("微电子与固体电子学院");
            list.add("物理电子学院");
            list.add("光电信息学院");
            list.add("计算机科学与工程学院");
            list.add("信息与软件工程学院");
            list.add("自动化工程学院");
            list.add("机械电子工程学院");
            list.add("生命科学与技术学院");
            list.add("数学科学学院");
            list.add("英才实验学院");
            list.add("经济与管理学院");
            list.add("政治与公共管理学院");
            list.add("外国语学院");
            list.add("马克思主义教育学院");
            list.add("资源与环境学院");
            list.add("航空航天学院");
            list.add("通信抗干扰重点实验室");
            list.add("医学院");
            list.add("创新创业学院");
            list.add("基础与前沿研究院");
            list.add("能源科学与工程学院");
            list.add("心理中心");
            return list;
        }
    }
}
