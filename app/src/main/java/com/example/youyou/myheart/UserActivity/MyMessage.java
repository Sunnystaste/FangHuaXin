package com.example.youyou.myheart.UserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.ActivityManagers;

/**
 * Created by youyou on 2017/12/9.
 */

public class MyMessage extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout message1,message2;
    private ImageView back;

    public void onCreate(Bundle savedInstanceState) {
        if (!ActivityManagers.getActivityList().contains(MyMessage.this)) {
            ActivityManagers.addActivity(MyMessage.this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage);
        init();
    }

    private void init() {
        back=findViewById(R.id.back);
        message1=findViewById(R.id.message1);
        message2=findViewById(R.id.message2);
        message2.setOnClickListener(this);
        message1.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ActivityManagers.finishSingleActivityByClass(MyMessage.class);
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                ActivityManagers.finishSingleActivityByClass(MyMessage.class);
                break;
            case R.id.message1:
                Intent intent = new Intent(this, MyMessage_tongzhi.class);
                startActivity(intent);
                break;
            case R.id.message2:
                Intent intent1 = new Intent(this,MyMessage_tixing.class);
                startActivity(intent1);
                break;
        }
    }
}
