package com.example.youyou.myheart.DiscoverActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.youyou.myheart.Adapter.ListViewAdapter_activity;
import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.ActivityLoad;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.getAllActivity;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2018/2/9.
 */

public class HuodongFragment extends Fragment  implements AdapterView.OnItemClickListener{
    TestA mtestA;
    Context context;
    public Map<Integer,Bitmap> IconMap=new HashMap<>();
    ListViewAdapter_activity adapter;
    public ArrayList<ActivityClientVO> l=new ArrayList<ActivityClientVO>();
    ListView listView;
    String Url=ServerUrl+getAllActivity+"?pageSize=20&pageNum=1&acTypeFilterFlag=true&acType=";
    @Override
    public void onActivityCreated(Bundle bundle) {
        context=getContext();
        listView=getActivity().findViewById(R.id.listview4);
        init();
        super.onActivityCreated(bundle);
    }

    private void init() {
        IconMap.clear();
        adapter=null;
        l.clear();
        listView.setOnItemClickListener(this);
        onLoad();
    }

    private void onLoad() {
        ActivityLoad.onLoad(Url + 31, l, IconMap);
        ActivityLoad.onLoad(Url + 32, l, IconMap);
        ActivityLoad.onLoad(Url + 33, l, IconMap);
        ActivityLoad.onLoad(Url + 34, l, IconMap);
        ActivityLoad.onLoad(Url + 35, l, IconMap);
        mtestA = new TestA();
        mtestA.run();
        mtestA.setOnTestListening(new TestA.OnTestListening() {

            @Override
            public void TestListening(int i) {
                if(IconMap.size()==l.size()&&l.size()!=0)
                {
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, null);
        return view;
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (adapter == null) {
                    adapter = new ListViewAdapter_activity(l, IconMap, context);
                    listView.setAdapter(adapter);
                    mtestA.destory();
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String qa=l.get(position).uuid;
        Intent intent=new Intent(getContext(), ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("acId",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}