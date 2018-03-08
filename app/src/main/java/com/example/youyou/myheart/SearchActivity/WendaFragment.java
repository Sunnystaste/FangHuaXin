package com.example.youyou.myheart.SearchActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.youyou.myheart.Adapter.ListViewAdapter;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.FragmentCallBack;
import com.example.youyou.myheart.HomeActivity.QuestionDetailsActivity;
import com.example.youyou.myheart.R;
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

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.getAllQA;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2018/2/19.
 */

public class WendaFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener,FragmentCallBack {
    private ListViewAdapter listViewAdapter;
    private String s;
    private int pageNum;
    private Context context0;
    private Gson gson =new Gson();
    private ArrayList<QAClientVO> list=new ArrayList<>();
    private ArrayList<QAClientVO> Searchlist=new ArrayList<>();
    private ListView l;
    private int k=0;//IconMap的大小
    private String Questionurl=ServerUrl+getAllQA+"?pageSize=10&pageNum=";
    private Map<Integer,Bitmap> IconMap=new HashMap<>();
    public void QuestionOnload(){
        doGet(Questionurl+pageNum, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonFromServer = response.body().string();
                Data<Value<QAClientVO>> data;
                Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                Log.e("QA清单",jsonFromServer);
                list.addAll(data.value.list);
                if(pageNum<=5){
                //if (data.value.isLastPage != true) {
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
        super.onStart();
    }
    private void IconMapOnload(){
        for (int i = 0; i < Searchlist.size(); i++) {
            String anUserpic = Searchlist.get(i).anUserPic;
            if (anUserpic != null);
                k++;
        }
        for (int i = 0; i < Searchlist.size(); i++) {
            final int j = i;
            String anUserpic = Searchlist.get(i).anUserPic;
            if (anUserpic != null)
                doGet(anUserpic, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("GetIcon", "failure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream in = response.body().byteStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                        in.close();
                        IconMap.put(j, bitmap);
                        if (IconMap.size() == Searchlist.size() ) {
                            Message msg = new Message();
                            msg.what = 2;
                            mHandler.sendMessage(msg);
                        }
                    }
                });
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        context0 = getContext();
        initViews();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_two, null);
        return view;
    }


    private void initViews() {
        pageNum=1;
        l=getActivity().findViewById(R.id.listview_two);
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
                QuestionOnload();
            }
            if (msg.what == 1) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).quContent.contains(s)) {
                        Searchlist.add(list.get(i));
                    }
                }
                if (Searchlist.size() != 0)
                    IconMapOnload();
            }
            if (msg.what == 2) {
                //if (Searchlist.size() != 0) {
                    listViewAdapter = new ListViewAdapter(Searchlist, IconMap, context0);
                    l.setAdapter(listViewAdapter);
                //}
                //else
                    //listViewAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QAClientVO qa=listViewAdapter.QAClientList.get(position);
        Intent intent=new Intent(getContext(), QuestionDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("QAClient",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void callBack(String string) {
        Searchlist.clear();
        list.clear();
        IconMap.clear();
        l.setEmptyView(getActivity().findViewById(android.R.id.empty));
        context0=getActivity();
        pageNum=1;
        s=string;
        QuestionOnload();
    }
}