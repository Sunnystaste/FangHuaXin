package com.example.youyou.myheart.HomeActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.youyou.myheart.Adapter.ListViewAdapter;
import com.example.youyou.myheart.Adapter.ListViewAdapter2;
import com.example.youyou.myheart.Adapter.MyHeaderPagerAdapter;
import com.example.youyou.myheart.Data.Banner;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.CallBack;
import com.example.youyou.myheart.LoginCallBack;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.activity.CaptureActivity;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.*;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;

/**
 * Created by youyou on 2017/12/10.
 */
public class HomeFragment extends Fragment implements
        CallBack,LoginCallBack,View.OnClickListener,AdapterView.OnItemClickListener{
    private ProgressDialog mProgressDialog;
    //打开扫描界面请求码
    private int in =1;
    private int k;//标识第几次加载
    private LinearLayout l1,l2,l3;
    public boolean answered=true;//当前页面的编号,true:已回答问题;false:未回答问题
    private boolean LoadFinished=false;//数据加载完成
    private boolean LoadFinished2=false;//数据加载完成
    public boolean isLoading = false;//表示是否正处于加载状态
    public boolean head_isLoading=false;//上拉更新标识
    private int pageNum=1;
    private int newPageNum=1;
    public static final String headUrl = ServerUrl + getAllBanners;
    public String noanswerlistviewUrl = ServerUrl + getAllQA+"?pageSize=10&order=new&pageNum=";
    public String listviewUrl = ServerUrl + getAllQA+"?pageSize=10&pageNum=";
    private Gson gson = new Gson();
    private static ViewPager head;
    private static HomeFragment context;
    private static Context context0;
    private ListView listview,listview2;
    public View Load_MoreView,Load_Finished;
    public ImageView img_cursor;
    public TextView Answered_Question,New_Question;
    private ScrollView mScrollView;
    public ListViewAdapter adapter;
    public ListViewAdapter2 adapter2;
    public LayoutInflater inflater;
    public View[] lv=new View[4];
    private Map<Integer,Bitmap> IconMap=new HashMap<>();
    private Map<Integer,Bitmap> newIconMap=new HashMap<>();
    public ArrayList<String> linkUrlList=new ArrayList<String>();
    public ArrayList<QAClientVO> AnsweredQAClientList=new ArrayList<QAClientVO>();
    public ArrayList<QAClientVO> NotAnsweredQAClientList=new ArrayList<QAClientVO>();
    public ArrayList<QAClientVO> newAnsweredQAClientList=new ArrayList<QAClientVO>();
    public ArrayList<QAClientVO> newNotAnsweredQAClientList=new ArrayList<QAClientVO>();
    @Override
    public void onActivityCreated(Bundle bundle) {
        l1=getActivity().findViewById(R.id.tiwen_l);
        l2=getActivity().findViewById(R.id.qiandao_l);
        l3=getActivity().findViewById(R.id.yuyue_l);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
        l3.setOnClickListener(this);
        Answered_Question=getActivity().findViewById(R.id.answered_question);
        New_Question=getActivity().findViewById(R.id.zuixin_question);
        New_Question.setOnClickListener(this);
        Answered_Question.setOnClickListener(this);
        img_cursor=getActivity().findViewById(R.id.cursor);
        inflater = LayoutInflater.from(getContext());
        Load_MoreView=getActivity().findViewById(R.id.load_more);
        Load_Finished=getActivity().findViewById(R.id.load_finished);
        Load_Finished.setVisibility(View.GONE);
        Load_MoreView.setVisibility(View.GONE);
        listview=getActivity().findViewById(R.id.listview);
        listview2=getActivity().findViewById(R.id.listview2);
        mScrollView=(ScrollView)getActivity().findViewById(R.id.mScrollView);
        mScrollView.setOnTouchListener(new TouchListenerImpl());
        context0=getContext();
        context=this;
        head= getActivity().findViewById(R.id.head_viewpager);
        GetViewPager();
        mProgressDialog=new ProgressDialog(getActivity());
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
        init();
        super.onActivityCreated(bundle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.homefragment, container, false);
        return view;
    }
    //初始化控件以及获得一些服务器数据
    private void init() {
        adapter=null;
        adapter2=null;
        k=0;
        if(answered)
            listview2.setVisibility(View.GONE);
        else
            listview.setVisibility(View.GONE);
        LoadFinished=false;
        LoadFinished2=false;
        isLoading = false;
        adapter=null;
        adapter2=null;
        pageNum=1;
        newPageNum=1;
        IconMap.clear();
        AnsweredQAClientList.clear();
        NotAnsweredQAClientList.clear();
    }

    //加载HeadViewPager
    public void GetViewPager(){
            doGet(headUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ViewPager","failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Log.e("ViewPager",jsonFromServer);
                    Data<List<Banner>> data;
                    Type jsonType = new TypeToken<Data<List<Banner>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    List<Banner> l = data.value;
                    for (int i = 0; i < l.size(); i++) {
                        String picurl = l.get(i).picUrl;
                        String linkUrl = l.get(i).linkUrl;
                        linkUrlList.add(linkUrl);
                        final int j=i;
                        doGet(picurl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("GetViewPager","Failure");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream in = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(in);
                                in.close();
                                ImageView im=new ImageView(getContext());
                                im.setImageBitmap(bitmap);
                                lv[j]=im;
                                if (lv[0]!=null && lv[1]!=null
                                        && lv[2]!=null &&lv[3]!=null) {
                                    Message msg=new Message();
                                    msg.what=0;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        });
                    }
                }
            });
    }

    //已回答问题刷新加载
    public void onLoad() {
        doGet(listviewUrl + pageNum, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ListView","failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonFromServer = response.body().string();
                Log.e("ListView1",jsonFromServer);
                Data<Value<QAClientVO>> data;
                Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                AnsweredQAClientList=data.value.list;
                //将已回答问题的回答者pic加入answerIconList
                for (int i = 0; i < AnsweredQAClientList.size(); i++) {
                    final int j=i;
                    String anUserpic = AnsweredQAClientList.get(i).anUserPic;
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
                                IconMap.put(j, bitmap);
                                if (IconMap.size() == AnsweredQAClientList.size()) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                    k++;
                                }
                            }
                        });
                }
                //如果加载数据到低最后一页
                //if (data.value.isLastPage==true)
                if (pageNum==6) {
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                    isLoading = false;//设置正在刷新标志位false}
                }
            }
        });
    }
    //更新
    public void onLoad1() {
        doGet(listviewUrl + pageNum, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ListView","failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                newAnsweredQAClientList.clear();
                newIconMap.clear();
                String jsonFromServer = response.body().string();
                Log.e("ListView1",jsonFromServer);
                Data<Value<QAClientVO>> data;
                Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                newAnsweredQAClientList=data.value.list;
                //将已回答问题的回答者pic加入answerIconList
                for (int i = 0; i < newAnsweredQAClientList.size(); i++) {
                    final int j=i+k*10;
                    String anUserpic = newAnsweredQAClientList.get(i).anUserPic;
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
                                newIconMap.put(j, bitmap);
                                if (newIconMap.size() == newAnsweredQAClientList.size()) {
                                    Message msg = new Message();
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);
                                    k++;
                                }
                            }
                        });
                }
                //如果加载数据到低最后一页
                //if (data.value.isLastPage==true)
                if (pageNum==6) {
                    Message msg = new Message();
                    msg.what = 2;
                    mHandler.sendMessage(msg);
                    isLoading = false;//设置正在刷新标志位false}
                }
            }
        });
    }
    //未回答问题刷新
    private void onLoad2(){
        if(newPageNum<=20)
            doGet(noanswerlistviewUrl + newPageNum, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ListView2","failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonFromServer = response.body().string();
                    Log.e("ListView2",jsonFromServer);
                    Data<Value<QAClientVO>> data;
                    Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    NotAnsweredQAClientList=data.value.list;
                    Message msg=new Message();
                    msg.what=3;
                    mHandler.sendMessage(msg);
                }
            });
    }
    //更新
    private void onLoad21(){
        if(newPageNum<=20)
            doGet(noanswerlistviewUrl + newPageNum, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ListView2","failure");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    newNotAnsweredQAClientList.clear();
                    String jsonFromServer = response.body().string();
                    Log.e("ListView2",jsonFromServer);
                    Data<Value<QAClientVO>> data;
                    Type jsonType = new TypeToken<Data<Value<QAClientVO>>>() {
                    }.getType();
                    data = gson.fromJson(jsonFromServer, jsonType);
                    newNotAnsweredQAClientList=data.value.list;
                    //如果列表是最后一页
                    if (data.value.isLastPage) {
                        LoadFinished2=true;
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                        newPageNum++;
                        isLoading = false;//设置正在刷新标志位false}
                    }
                    Message msg=new Message();
                    msg.what=3;
                    mHandler.sendMessage(msg);
                }
            });
    }

    //加载完成
    public void loadComplete()
    {
        if(answered)
            pageNum++;
        else
            newPageNum++;
        isLoading = false;//设置正在刷新标志位false
        Load_MoreView.setVisibility(View.GONE);//设置刷新界面不可见
    }


    //游标Cursor的动画设置
    public void answeredselect(int index){
        Animation animation = null;
        int bmpWidth =img_cursor.getWidth();
        switch (index) {
            case 0:
                if (answered&&in==2)
                {
                    in=1;
                    animation = new TranslateAnimation(bmpWidth, 0, 0, 0);
                }
                break;
            case 1:
                if (!answered&&in==1) {
                    animation = new TranslateAnimation(0, bmpWidth, 0, 0);
                    in=2;
                }break;
        }
        if(animation!=null) {
            animation.setFillAfter(true);// true表示图片停在动画结束位置
            animation.setDuration(300); //设置动画时间为300
            img_cursor.startAnimation(animation);//开始动画
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tiwen_l:
                startActivity(new Intent(getActivity(), QuestionActivity.class));
                break;
            case R.id.qiandao_l:
                startQrCode();
                break;
            case R.id.yuyue_l:
                startActivity(new Intent(getActivity(), YuyueActivity.class));
                break;
            case R.id.zuixin_question:
                Load_Finished.setVisibility(View.GONE);
                answered=false;
                listview.setVisibility(View.GONE);
                listview2.setVisibility(View.VISIBLE);
                answeredselect(1);
                Log.e("message", String.valueOf(answered));
                break;


            case R.id.answered_question:
                answered=true;
                answeredselect(0);
                listview.setVisibility(View.VISIBLE);
                listview2.setVisibility(View.GONE);
                Log.e("message", String.valueOf(answered));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QAClientVO qa=adapter.QAClientList.get(position);
        Intent intent=new Intent(getContext(), QuestionDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("QAClient",qa);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void LogincallBack(String s) {
        init();
        if(s!="null") {
            noanswerlistviewUrl = ServerUrl + getAllQA + "?pageSize=10&order=new&userId=" + userData.getUserId() + "&pageNum=";
            listviewUrl = ServerUrl + getAllQA + "?pageSize=10&userId=" + userData.getUserId() + "&pageNum=";
        }
        if (s=="null")
        {
            noanswerlistviewUrl = ServerUrl + getAllQA + "?pageSize=10&order=new&pageNum=";
            listviewUrl = ServerUrl + getAllQA + "?pageSize=10&pageNum=";
        }
        onLoad2();
        onLoad();
    }


    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();
                    if (scrollY == 0) {
                        if (!head_isLoading) {
                            head_isLoading = true;
                            Log.e("滑动到了顶端 ", scrollY + "");
                            //init();
                        }
                    }

                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        if (!isLoading) {
                            if (answered && !LoadFinished) {
                                Log.e("底部", "更新");
                                isLoading = true;
                                Load_MoreView.setVisibility(View.VISIBLE);
                                Log.e("message", String.valueOf(answered));
                                onLoad1();
                            }
                            if (!answered && !LoadFinished2) {
                                Log.e("底部", "更新");
                                isLoading = true;
                                Load_MoreView.setVisibility(View.VISIBLE);
                                Log.e("message", String.valueOf(answered));
                                onLoad21();
                            }
                        }
                        break;
                    }
                    return false;
            }
            return false;
        }
    }


    //回调CallBack用于绑定ViewPager点击事件
    @Override
    public void callBack(int position) {
        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
        intent.putExtra("url", linkUrlList.get(position));
        startActivity(intent);
    }

    //异步处理Handler
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //HeadViewPager对应
            if (msg.what == 0) {
                ArrayList<View> list=new ArrayList<View>(Arrays.asList(lv));;
                head.setAdapter(new MyHeaderPagerAdapter(list,context));
                head.setCurrentItem(0);
                mProgressDialog.dismiss();
            }
            //已回答问题ListView
            if(msg.what==1) {
                if (adapter == null) {
                    pageNum++;
                    adapter = new ListViewAdapter(AnsweredQAClientList,IconMap, context0);
                    listview.setAdapter(adapter);
                    head_isLoading=false;
                    listview.setOnItemClickListener(context);
                } else if(isLoading){
                    adapter.add(newAnsweredQAClientList,newIconMap);
                    loadComplete();
                }
            }

            //已回答问题ListView所有数据加载完成
            if(msg.what==2)
            {
                if(LoadFinished==false) {
                    pageNum++;
                    Load_MoreView.setVisibility(View.GONE);
                    Load_Finished.setVisibility(View.VISIBLE);
                    LoadFinished=true;
                }
            }

            //未回答问题ListView
            if(msg.what==3) {
                if (adapter2 == null) {
                    newPageNum++;
                    adapter2 = new ListViewAdapter2(NotAnsweredQAClientList, context0);
                    listview2.setAdapter(adapter2);
                    head_isLoading=false;
                    listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            QAClientVO qa=adapter2.NoAnsweredClientList.get(position);
                            Intent intent=new Intent(getContext(), QuestionDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("QAClient",qa);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else if(isLoading){
                    adapter2.add(NotAnsweredQAClientList);
                    loadComplete();
                }
            }
        }
    };

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}

