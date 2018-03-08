package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.Data.AuthenticateData;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.userAuthInfo;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wx.wheelview.widget.WheelViewDialog;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.Authenticate;
import static com.example.youyou.myheart.Tool.AppConstant.GetAuthenticate;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.createArrays;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2017/12/9.
 */

public class MyAccreditation extends Activity implements View.OnClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_accreditation);
        init();
    }
    private Gson gson=new Gson();
    private AuthenticateData data;
    private Button button;
    private EditText xuehao,xingming,dianhua;
    private TextView textView;
    private ImageView back, xueyuan;
    private static final String url=ServerUrl+Authenticate;
    private String urlget;
    private void init() {
        data = new AuthenticateData();
        button = findViewById(R.id.post_data);
        xuehao = findViewById(R.id.ed_xuehao);
        xingming = findViewById(R.id.ed_xingming);
        dianhua = findViewById(R.id.ed_dianhua);
        textView = findViewById(R.id.tx_xueyuan);
        xueyuan = findViewById(R.id.imageView_xueyuan);
        xueyuan.setOnClickListener(this);
        back = findViewById(R.id.back_accreditation);
        back.setOnClickListener(this);
        button.setOnClickListener(this);
        if (userData .getUserId()!= null) {
            urlget=ServerUrl+GetAuthenticate+"?userId="+userData.getUserId();
            Map<String, String> map = new HashMap<>();
            map.put("userId", userData.getUserId());
            doGet(urlget, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Message msg = new Message();
                    msg.obj = s;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }, token);
        }
    }
    public void showDialog() {
        WheelViewDialog dialog = new WheelViewDialog(this);
        final List<String> xueyuan=createArrays();
        dialog.setTitle("学院名单").setItems(xueyuan).setButtonText("确定").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(7).show();
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int i, String s) {
                data.setCollegeType(i+1);
                textView.setText(xueyuan.get(i));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_accreditation:
                this.finish();
                break;
            case R.id.imageView_xueyuan:
                showDialog();
                break;
            case R.id.post_data:
                if (userData .getUserId()!= null) {
                    data.setPhoneNumber(dianhua.getText().toString());
                    data.setStudentId(xuehao.getText().toString());
                    data.setUserName(xingming.getText().toString());
                    data.setUserId(userData.getUserId());
                    Log.e("POST", gson.toJson(data));
                    doPost(url, gson.toJson(data), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e("POST", response.body().string());
                        }
                    }, token);
                }
        }
    }
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String s= (String) msg.obj;
                Log.e("POST",s);
                userAuthInfo info;
                Data<userAuthInfo> data;
                List<String> l=createArrays();
                Type jsonType = new TypeToken<Data<userAuthInfo>>() {
                }.getType();
                data = gson.fromJson(s, jsonType);
                info=data.value;
                dianhua.setText(info.phoneNumber);
                xingming.setText(info.userName);
                xuehao.setText(info.studentId);
                textView.setText(l.get(info.collegeType-1));
            }
        }
    };
}