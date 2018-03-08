package com.example.youyou.myheart.DiscoverActivity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by youyou on 2018/2/11.
 */

public class TestA {
    Timer mTimer=new Timer();
    TimerTask task=new TimerTask(){

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            handler.sendMessage(message);

        }

    };
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if(mOnTestListening!=null){
                mOnTestListening.TestListening(0);
            }

            super.handleMessage(msg);
        }

    };
    public void run(){
        mTimer.schedule(task, 50,100);//每五秒执行一次handler
    }
    public void destory(){
        mTimer.cancel();
    }
    public interface OnTestListening{
        void TestListening(int i);
    }

    OnTestListening mOnTestListening=null;
    public void setOnTestListening(OnTestListening e){
        mOnTestListening=e;
    }
}