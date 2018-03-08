package com.example.youyou.myheart.SearchActivity;

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
import com.example.youyou.myheart.FragmentCallBack;
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
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.getAllActivity;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2018/2/19.
 */

public class HuodongFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener,FragmentCallBack {
    private String s;
    private int pageNum;
    private Context context0;
    private Gson gson =new Gson();
    private ArrayList<MyActivityClientVO> list=new ArrayList<>();
    private ArrayList<MyActivityClientVO> Searchlist=new ArrayList<>();
    private ListView l;
    private String Activityurl=ServerUrl+getAllActivity+"?pageSize=20&pageNum=";
    public void ActivityOnload(){
        Log.e("GET",Activityurl+pageNum);
        doGet(Activityurl+pageNum, new Callback() {
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
                list.addAll(data.value.list);
                if (data.value.isLastPage != true) {
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                else{
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }
        },token);
    }
    @Override
    public void onStart() {
        context0=getActivity();
        initViews();
        super.onStart();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.huodongfragment, null);
        return view;
    }


    private void initViews() {
        l=getActivity().findViewById(R.id.huodong_listview);
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
                pageNum++;
                ActivityOnload();
            }
            if (msg.what == 1) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).name.contains(s)) {
                        Searchlist.add(list.get(i));
                        Log.e("SerchList", gson.toJson(list.get(i)));
                    }
                }
                //if (Searchlist.size() != 0)
                    l.setAdapter(new ListViewAdapter_MyActivities(Searchlist, context0));
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


    @Override
    public void callBack(String string) {
        list.clear();
        Searchlist.clear();
        context0=getActivity();
        l.setEmptyView(getActivity().findViewById(android.R.id.empty));
        pageNum=1;
        s=string;
        ActivityOnload();
    }
}