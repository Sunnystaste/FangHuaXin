package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.youyou.myheart.Data.PostOpinionData;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.createOpinion;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2017/12/9.
 */

public class MyFeedBack extends AppCompatActivity implements View.OnClickListener{
    private Gson gson=new Gson();
    private EditText ed,ed0;
    private Button button;
    private ImageView back;
    private Context context;
    private String url=ServerUrl+createOpinion;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_feedback);
        init();
    }
    private void init() {
        context=this;
        button=findViewById(R.id.tijiao_feedback);
        back=findViewById(R.id.back_feedback);
        back.setOnClickListener(this);
        button.setOnClickListener(this);
        ed=findViewById(R.id.edit_text);
        ed0=findViewById(R.id.ed_tel);
        showSoftInputFromWindow(this,ed);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_feedback:
                this.finish();
                break;
            case R.id.tijiao_feedback:
                if (userData .getUserId()!= null) {
                    PostOpinionData data = new PostOpinionData();
                    data.userId = userData.getUserId();
                    data.contactInfomation = ed0.getText().toString();
                    data.content = ed.getText().toString();
                    doPost(url, gson.toJson(data), new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e("POST", response.body().string());
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        }
                    }, token);
                }
        }

    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                Toast.makeText(context,"提交成功",Toast.LENGTH_LONG).show();
            }
        }
    };
}
