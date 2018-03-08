package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youyou.myheart.Adapter.ListViewAdapter_point;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.MyFinishedActivityVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.Paint.ScoreDialog;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.getMyFinishedActivities;
import static com.example.youyou.myheart.Tool.AppConstant.getUserIntegral;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2017/12/9.
 */

public class MyPoints extends Activity implements View.OnClickListener{
    private TextView point;
    private Context context0;
    private Gson gson=new Gson();
    private ScoreDialog scoredialog;
    private ImageView back,zhuyi;
    private ListView l;
    private ArrayList<MyFinishedActivityVO> list;
    private String url;
    private String pointUrl;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_point);
        init();
    }
    private void init() {
        point=findViewById(R.id.MyPoint);
        context0=this;
        l=findViewById(R.id.listview_point);
        l.setEmptyView(findViewById(android.R.id.empty));
        scoredialog=new ScoreDialog(this);
        zhuyi=findViewById(R.id.mypoint_zhuyi);
        zhuyi.setOnClickListener(this);
        back=findViewById(R.id.back_point);
        back.setOnClickListener(this);
        if(userData.getUserId()!=null) {
            pointUrl = ServerUrl + getUserIntegral + "?userId=" + userData.getUserId();
            url = ServerUrl + getMyFinishedActivities + "?pageSize=20&pageNum=1&userId=" + userData.getUserId();
            doGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Data<Value<MyFinishedActivityVO>> data;
                    Type jsonType = new TypeToken<Data<Value<MyFinishedActivityVO>>>() {
                    }.getType();
                    Log.e("GET", jsonFromServer);
                    data = gson.fromJson(jsonFromServer, jsonType);
                    list = data.value.list;
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }, token);
            doGet(pointUrl, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    Log.e("POST", s);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        Integer value = jsonObject.getInt("value");
                        Message msg = new Message();
                        msg.obj = value;
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, token);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_point:
                this.finish();
                break;
            case R.id.mypoint_zhuyi:
                scoredialog.setCloseOnclickListener(new ScoreDialog.CloseOnClickListener() {
                    @Override
                    public void CloseClick() {
                        scoredialog.dismiss();
                    }
                });
                scoredialog.show();
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                l.setAdapter(new ListViewAdapter_point(list,context0));
            }
            if(msg.what==1){
                point.setText(msg.obj.toString());
            }
        }
    };
}
