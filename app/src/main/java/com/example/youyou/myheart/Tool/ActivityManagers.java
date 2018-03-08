package com.example.youyou.myheart.Tool;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 该类是用于汇总所有Activity信息
 * 能有效的操作List<Activity>中的元素
 * List<Activity>意在List中的元素就为现在存在的Activity
 * Created by youyou on 2017/12/10.
 */

public class ActivityManagers {
    /**
     * @param List<Activity>
     *     单例模式的Activity List;
     */
    private static List<Activity> activityList = new LinkedList<Activity>();
    /**
     * 外部获取单例List<Activity>的方法
     * @return 单例List<Activity>
     */
    public static List<Activity> getActivityList() {
        return activityList;
    }

    /**
     * 添加Activity到Activity容器中
     * @param activity 想要添加的Activity
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 便利所有Activigty并finish
     */
    public static void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 结束指定的Activity
     * @param activity 指定Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     *@param cls 想要删除的Activity对应的类
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }

}