package com.example.youyou.myheart.UserActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.ActivityManagers;

/**
 * Created by youyou on 2018/1/25.
 */

public class MyMessage_tixing extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_tixing);
        init();
    }

    private void init() {
        if (!ActivityManagers.getActivityList().contains(MyMessage_tixing.this)) {
            ActivityManagers.addActivity(MyMessage_tixing.this);
        }
        back=findViewById(R.id.back_tixing);
        back.setOnClickListener(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityManagers.finishSingleActivityByClass(MyMessage_tixing.class);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_tixing:
                ActivityManagers.finishSingleActivityByClass(MyMessage_tixing.class);
        }
    }
}