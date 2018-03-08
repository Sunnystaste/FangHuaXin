package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.youyou.myheart.Adapter.ListViewAdapter2;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.HomeActivity.QuestionDetailsActivity;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.getMyAskingQA;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2017/12/9.
 */

public class MyQandA extends Activity implements View.OnClickListener{
    private Context context0;
    private Gson gson=new Gson();
    private ArrayList<QAClientVO> QAClientList=new ArrayList<>();
    private String url;
    private ListView l;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_qanda);
        init();
        onLoad2();
    }
    private void onLoad2(){
        if(userData.getUserId()!=null) {
            url = ServerUrl + getMyAskingQA + "?pageSize=30&pageNum=1&userId=" + userData.getUserId();
            doGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ListView2", "failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Data<Value<QAClientVO>> data;
                    Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    QAClientList.addAll(data.value.list);
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }, token);
        }
    }
    private ImageView back;
    private void init() {
        context0=this;
        l=findViewById(R.id.listview_qanda);
        l.setEmptyView(findViewById(android.R.id.empty));
        back=findViewById(R.id.back_qanda);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_qanda:
                this.finish();
                break;
        }

    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                ListViewAdapter2 adapter2=new ListViewAdapter2(QAClientList, context0);
                l.setAdapter(adapter2);
                l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        QAClientVO qa=adapter2.NoAnsweredClientList.get(position);
                        Intent intent=new Intent(context0, QuestionDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("QAClient",qa);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    };
}
