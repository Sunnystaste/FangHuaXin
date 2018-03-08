package com.example.youyou.myheart.DiscoverActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.youyou.myheart.Adapter.ListViewAdapter_activity;
import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

public class ZuixinFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ProgressDialog mProgressDialog;
    boolean isLoading=false;
    Context context;
    private Map<Integer,Bitmap> replyIconMap=new HashMap<>();
    private Map<Integer,Bitmap> newIconMap=new HashMap<>();
    ListViewAdapter_activity adapter;
    private ArrayList<ActivityClientVO> l=new ArrayList<ActivityClientVO>();
    Gson gson=new Gson();
    private int pageNum=1;
    ListView listView;
    String Url=ServerUrl+getAllActivity+"?pageSize=10&&pageNum=";
    private int k;//标识第几次加载
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onActivityCreated(Bundle bundle) {
        context=getContext();
        listView=getActivity().findViewById(R.id.zuixin_listview);
        init();
        super.onActivityCreated(bundle);
    }
    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        k=0;
        replyIconMap.clear();
        adapter=null;
        pageNum=1;
        l.clear();
        listView.setOnItemClickListener(this);
        onLoad();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = listView.getChildAt(listView.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == listView.getHeight()&&!isLoading) {
                            isLoading=true;
                            onLoad();
                            Log.e("更新","活动Listview");
                        }
                }
            }
        });
        // 设置进度条的形式为圆形转动的进度条
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置是否可以通过点击Back键取消
        mProgressDialog.setCancelable(false);
        // 设置在点击Dialog外是否取消Dialog进度条
        mProgressDialog.setCanceledOnTouchOutside(false);
        // 设置提示的title的图标，默认是没有的，如果没有设置title的话只设置Icon是不会显示图标的
        mProgressDialog.setIcon(R.drawable.pic_logo);
        mProgressDialog.setTitle("请耐心等待");
        mProgressDialog.setMessage("活动list正在加载");
        mProgressDialog.show();
    }

    private void onLoad() {
        if(pageNum!= -1 )//
            doGet(Url+pageNum,new Callback(){

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Data<Value<ActivityClientVO>> data;
                    Type jsonType = new TypeToken<Data<Value<ActivityClientVO>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    l=data.value.list;
                    newIconMap.clear();
                    for (int i = 0; i < l.size(); i++) {
                        final int j=i+k*10;
                        String anUserpic = l.get(i).titleGraph;
                        if (anUserpic != null)
                            doGet(anUserpic, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("GetIcon","failure");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    InputStream in = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                                    in.close();
                                    if(adapter!=null) {//之后加载
                                        newIconMap.put(j, bitmap);
                                        if (newIconMap.size() == l.size()) {
                                            Message msg = new Message();
                                            msg.what = 1;
                                            mHandler.sendMessage(msg);
                                            k++;
                                            pageNum++;
                                        }
                                    }
                                    else{//第一次加载
                                        replyIconMap.put(j, bitmap);
                                        if (replyIconMap.size() == l.size()) {
                                            Message msg = new Message();
                                            msg.what = 1;
                                            mHandler.sendMessage(msg);
                                            k++;
                                            pageNum++;
                                        }
                                    }
                                }
                            });
                    }
                }
            });

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        return view;
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (adapter == null) {
                    adapter = new ListViewAdapter_activity(l, replyIconMap, context);
                    listView.setAdapter(adapter);
                    mProgressDialog.dismiss();
                } else {
                    adapter.add(l,newIconMap);
                    isLoading = false;//设置正在刷新标志位false
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String qa=adapter.ActivityClientList.get(position).uuid;
        Intent intent=new Intent(getContext(), ActivityDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("acId",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
