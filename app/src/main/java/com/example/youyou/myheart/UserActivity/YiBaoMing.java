package com.example.youyou.myheart.UserActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.youyou.myheart.Adapter.ListViewAdapter_MyActivities;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.MyActivityClientVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.DiscoverActivity.ActivityDetail;
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
import static com.example.youyou.myheart.Tool.AppConstant.getMyActivities;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2018/2/18.
 */

public class YiBaoMing extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener{
    private Context context0;
    private Gson gson =new Gson();
    private ArrayList<MyActivityClientVO> list=new ArrayList<>();
    private ListView l;
    private String url;
    @Override
    public void onActivityCreated(Bundle bundle) {
        context0 = getContext();
        initViews();
        if (userData.getUserId() != null) {
            url = ServerUrl + getMyActivities + "?pageSize=20&pageNum=1&userId=" + userData.getUserId()
                    + "&isCheckin=0&homeworkStatus=0";
            doGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Data<Value<MyActivityClientVO>> data;
                    Type jsonType = new TypeToken<Data<Value<MyActivityClientVO>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    Log.e("已报名活动清单", jsonFromServer);
                    list = data.value.list;
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
            }, token);
        }
        super.onActivityCreated(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_one, null);
        return view;
    }


    private void initViews() {
        l=getActivity().findViewById(R.id.listview_one);
        l.setEmptyView(getActivity().findViewById(android.R.id.empty));
        l.setOnItemClickListener(this);
    }
    //异步处理Handler
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                l.setAdapter(new ListViewAdapter_MyActivities(list,context0));
                }
            }
        };
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String qa=list.get(position).uuid;
        Intent intent=new Intent(getContext(), ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("acId",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
