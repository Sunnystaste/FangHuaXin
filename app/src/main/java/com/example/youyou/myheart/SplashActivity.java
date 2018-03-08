package com.example.youyou.myheart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.example.youyou.myheart.Tool.ActivityManagers;
import com.google.gson.Gson;

/**
 * 闪屏页
 * 延时两秒之后跳转至主Activity
 **/
public class SplashActivity extends Activity {
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash);
        if (!ActivityManagers.getActivityList().contains(SplashActivity.this)) {
            ActivityManagers.addActivity(SplashActivity.this);
        }
        new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(2000);
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    ActivityManagers.finishSingleActivityByClass(SplashActivity.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
