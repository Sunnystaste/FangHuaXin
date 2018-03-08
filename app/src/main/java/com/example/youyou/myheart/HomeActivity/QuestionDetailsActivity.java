package com.example.youyou.myheart.HomeActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youyou.myheart.Adapter.ListViewAdapter3;
import com.example.youyou.myheart.Data.Data;
import com.example.youyou.myheart.Data.PageInfo;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Data.ReplayData;
import com.example.youyou.myheart.Data.Value;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.DownloadUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.CancelVote;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.VoteOne;
import static com.example.youyou.myheart.Tool.AppConstant.addReply;
import static com.example.youyou.myheart.Tool.AppConstant.getQuestionReplies;
import static com.example.youyou.myheart.Tool.AppConstant.voteAnswer;
import static com.example.youyou.myheart.Tool.AppConstant.voteReply;
import static com.example.youyou.myheart.Tool.MediaManager.playSound;
import static com.example.youyou.myheart.Tool.MediaManager.release;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doGet;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;
/**
 * Created by youyou on 2017/12/8.
 */

public class QuestionDetailsActivity extends Activity implements View.OnClickListener{
    private ReplayData Rdata;
    private String lastyuyin_string;
    private QAClientVO qa;
    private AnimationDrawable animationDrawable;
    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
    private ScrollView mScrollView;
    private boolean isLoading;
    private int k;
    private int pageNum;
    private View headview;
    private ImageView back;
    private TextView reply_N;
    private Context context0;
    private ListViewAdapter3 adapter3;
    private Map<Integer,Bitmap> replyIconMap=new HashMap<>();
    private Map<Integer,Bitmap> newIconMap=new HashMap<>();
    private ArrayList<PageInfo> pageInfoArrayList=new ArrayList<>();
    private ArrayList<PageInfo> newpageInfoArrayList=new ArrayList<>();
    private Gson gson=new Gson();
    private ImageView qu_icon,qu_islike,an_icon,an_islike,an_voiceimg,qu_menu;
    private TextView qu_name,an_name,qu_content,an_content,qu_time,an_time,an_x,qu_x,yuyin_tx,X_people_listen;
    private EditText ed;
    private TextView post;
    private RelativeLayout r;
    private ListView l;
    private String url;
    private static final String postUrl=ServerUrl+addReply;
    private static final String Voteurl=ServerUrl+VoteOne;
    private static final String Cancelurl=ServerUrl+CancelVote;
    private String AnswerVoteurl=ServerUrl+voteAnswer;
    private String ReplyVoteurl=ServerUrl+voteReply;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail);
        qu_menu=findViewById(R.id.question_menu);
        qu_menu.setOnClickListener(this);
        ed=findViewById(R.id.reply_ed);
        post=findViewById(R.id.reply_post);
        post.setOnClickListener(this);
        an_voiceimg=findViewById(R.id.yuyin_img0);
        animationDrawable=(AnimationDrawable) ((ImageView) an_voiceimg).getDrawable();;
        mScrollView=findViewById(R.id.scrollview_detail);
        back=findViewById(R.id.back_qa_detail);
        back.setOnClickListener(this);
        headview= getLayoutInflater().inflate(R.layout.reply_head,null);
        reply_N=headview.findViewById(R.id.head_tx);
        l=findViewById(R.id.listview_reply);
        l.setEmptyView(findViewById(android.R.id.empty));
        X_people_listen=findViewById(R.id.reply_X_people_listened);
        an_islike=findViewById(R.id.an_islike);
        r=findViewById(R.id.yuyin0);
        r.setOnClickListener(this);
        qu_islike=findViewById(R.id.qu_islike);
        yuyin_tx=findViewById(R.id.yuyin_tx0);
        qu_icon=findViewById(R.id.qu_icon);
        qu_content=findViewById(R.id.qu_content);
        qu_name=findViewById(R.id.qu_tx);
        qu_time=findViewById(R.id.qu_time);
        qu_x=findViewById(R.id.X_people_tonggan);
        an_content=findViewById(R.id.an_content);
        an_icon=findViewById(R.id.an_icon);
        an_name=findViewById(R.id.answer_name);
        an_time=findViewById(R.id.an_time);
        an_x=findViewById(R.id.X_people_thanks);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        qa= (QAClientVO) bundle.getSerializable("QAClient");
        if(userData.getUserId()==null)
            url=ServerUrl+getQuestionReplies+"?pageSize=10&quId="+qa.quId+"&pageNum=";
        else
            url=ServerUrl+getQuestionReplies+"?pageSize=10&quId="+qa.quId+"&userId="+userData.getUserId()+"&pageNum=";
        init();
    }


    private void init() {
        replyIconMap.clear();
        newIconMap.clear();
        pageInfoArrayList.clear();
        Rdata=new ReplayData();
        isLoading=false;
        k=0;
        replyIconMap.clear();
        pageNum=1;
        context0=this;
        an_islike.setImageResource(R.drawable.like_selector);
        if(qa.answerVoted)
            an_islike.setSelected(true);
        else
            an_islike.setSelected(false);
        an_islike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(an_islike.isSelected()) {
                    an_islike.setSelected(false);
                    an_x.setText(qa.answerVoteNum+"人已感谢");
                }
                   else {
                    an_x.setText(qa.answerVoteNum+1+"人已感谢");
                    an_islike.setSelected(true);
                }
            }
        });

        qu_islike.setImageResource(R.drawable.like_selector);
        if(qa.voted)
            qu_islike.setSelected(true);
        else
            qu_islike.setSelected(false);
        qu_islike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData.getUserId() != null) {
                    if (qu_islike.isSelected()) {
                        qu_islike.setSelected(false);
                        Map<String, String> map = new HashMap<>();
                        map.put("userId", userData.getUserId());
                        map.put("quId", qa.quId);
                        doPost(Cancelurl, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("POST", response.body().toString());
                            }
                        }, token);
                        qa.quVoteNum--;
                        qu_x.setText(qa.quVoteNum+"人有同感");
                        qa.voted = false;
                    } else {
                        qu_islike.setSelected(true);
                        Map<String, String> map = new HashMap<>();
                        map.put("userId", userData.getUserId());
                        map.put("quId", qa.quId);
                        doPost(Voteurl, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("POST",  response.body().toString());
                            }
                        }, token);
                        qa.quVoteNum++;
                        qa.voted = true;
                        qu_x.setText(qa.quVoteNum+"人有同感");
                    }
                }
            }
        });
        qu_content.setText(qa.quContent);
        if(qa.quUserNickname!=null)
            qu_name.setText(qa.quUserNickname);
        qu_time.setText(sdf.format(new Date(qa.quCreatedTime)).toString());
        qu_x.setText(qa.quVoteNum+"人有同感");
        if(qa.anId!=null)
        {
            RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)l.getLayoutParams();
            layout.addRule(RelativeLayout.BELOW,R.id.an_time);
            l.setLayoutParams(layout);
            if(qa.anVoice==null) {
                r.setVisibility(View.GONE);
                X_people_listen.setVisibility(View.GONE);
            }
            else{
                yuyin_tx.setText(qa.anVoiceDuration.intValue()/60+"'"+qa.anVoiceDuration.intValue()%60+"\"");
                X_people_listen.setText(qa.anVoiceListened+"人听过");
            }
            an_x.setText(qa.answerVoteNum+"人已感谢");
            an_content.setText(qa.anContent);
            an_time.setText(sdf.format(new Date(qa.answerDate)).toString());
            an_name.setText(qa.anUserNickname);
        }
        else
        {
            X_people_listen.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layout=(RelativeLayout.LayoutParams)l.getLayoutParams();
            layout.addRule(RelativeLayout.BELOW,R.id.line);
            l.setLayoutParams(layout);
            r.setVisibility(View.GONE);
            an_name.setVisibility(View.GONE);
            an_content.setVisibility(View.GONE);
            an_time.setVisibility(View.GONE);
            an_x.setVisibility(View.GONE);
            an_icon.setVisibility(View.GONE);
            an_islike.setVisibility(View.GONE);
        }
        if(qa.quUserPic!=null)
        doGet(qa.quUserPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                Message msg = new Message();
                msg.obj=bitmap;
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });
        if(qa.anId!=null)
        doGet(qa.anUserPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream in = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                in.close();
                Message msg = new Message();
                msg.obj=bitmap;
                msg.what = 2;
                mHandler.sendMessage(msg);
            }
        });
        onLoad();
        mScrollView.setOnTouchListener(new TouchListenerImpl());
    }
    private void onLoad(){
        doGet(url+pageNum, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                newIconMap.clear();
                String jsonFromServer = response.body().string();
                Data<Value<PageInfo>> data;
                Type jsonType = new TypeToken<Data<Value<PageInfo>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                pageInfoArrayList=data.value.list;
                for (int i =0; i < pageInfoArrayList.size(); i++) {
                    final int j=i;
                    String Userpic = pageInfoArrayList.get(i).userPic;
                    if (Userpic != null)
                        doGet(Userpic, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("GetIcon","failure");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                InputStream in = response.body().byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(in);
                                in.close();
                                replyIconMap.put(j, bitmap);
                                if (replyIconMap.size() == pageInfoArrayList.size()) {
                                    Message msg = new Message();
                                    msg.what = 3;
                                    mHandler.sendMessage(msg);
                                    k++;
                                    pageNum++;
                                }
                            }
                        });
                }
            }
        });
    }

    private void onLoad2(){
        doGet(url+pageNum, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                newIconMap.clear();
                newpageInfoArrayList.clear();
                String jsonFromServer = response.body().string();
                Data<Value<PageInfo>> data;
                Type jsonType = new TypeToken<Data<Value<PageInfo>>>() {
                }.getType();
                data = gson.fromJson(jsonFromServer, jsonType);
                newpageInfoArrayList=data.value.list;
                for (int i =0; i <newpageInfoArrayList.size(); i++) {
                    final int j=i+k*10;
                    String Userpic = newpageInfoArrayList.get(i).userPic;
                    if (Userpic != null)
                        doGet(Userpic, new Callback() {
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
                                Log.e("newpageInfoArrayList", String.valueOf(newpageInfoArrayList.size()));
                                Log.e("newIconMap", String.valueOf(newIconMap.size()));
                                if (newIconMap.size() == newpageInfoArrayList.size()) {
                                    Log.e("更新","SendMessage");
                                    Message msg = new Message();
                                    msg.what = 3;
                                    mHandler.sendMessage(msg);
                                    k++;
                                    pageNum++;
                                }
                            }
                        });
                }
            }
        });
    }



    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
                qu_icon.setImageBitmap((Bitmap) msg.obj);
            if(msg.what==2)
                an_icon.setImageBitmap((Bitmap) msg.obj);
            if(msg.what==3)
            {
                if (adapter3 == null) {
                    isLoading = false;//设置正在刷新标志位false
                    reply_N.setText(" 已显示回复  "+replyIconMap.size()+"人");
                    l.addHeaderView(headview);
                    adapter3=new ListViewAdapter3(pageInfoArrayList,replyIconMap,context0);
                    l.setAdapter(adapter3);
                }else {
                    Log.e("更新","SendMessage");
                    adapter3.add(newpageInfoArrayList,newIconMap);
                    reply_N.setText(" 已显示回复  "+adapter3.getCount()+"人");
                    isLoading = false;//设置正在刷新标志位false
                }
            }
            if(msg.what==4)
            {
                Toast.makeText(context0,"回复成功",Toast.LENGTH_LONG).show();
                init();
                onLoad();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.question_menu:
                PopupMenu popupMenu=new PopupMenu(QuestionDetailsActivity.this,v);//1.实例化PopupMenu
                getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());//2.加载Menu资源

                //3.为弹出菜单设置点击监听
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.option_normal_1:
                                Platform.ShareParams sp = new Platform.ShareParams();
                                sp.setText(qa.quContent);
                                sp.setImageUrl(qa.quUserPic);
                                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                                weibo.setPlatformActionListener(new PlatformActionListener() {
                                    @Override
                                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                                    }

                                    @Override
                                    public void onError(Platform platform, int i, Throwable throwable) {

                                    }

                                    @Override
                                    public void onCancel(Platform platform, int i) {

                                    }
                                });
                                weibo.share(sp);
                                return true;
                            case R.id.option_normal_2:
                                Platform.ShareParams sp0 = new Platform.ShareParams();
                                sp0.setTitle(qa.quContent);
                                sp0.setTitleUrl("http://sharesdk.cn"); // 标题的超链接
                                sp0.setText(qa.anContent);
                                sp0.setImageUrl(qa.quUserPic);
                                sp0.setSite("发布分享的网站名称");
                                sp0.setSiteUrl("发布分享网站的地址");
                                Platform qzone = ShareSDK.getPlatform (QQ.NAME);
                                // 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                                qzone.setPlatformActionListener (new PlatformActionListener() {
                                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                                        //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
                                    }
                                    public void onComplete(Platform arg0, int arg1, HashMap arg2) {
                                        //分享成功的回调
                                    }
                                    public void onCancel(Platform arg0, int arg1) {
                                        //取消分享的回调
                                    }
                                });
                                // 执行图文分享
                                qzone.share(sp0);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();//4.显示弹出菜单
                break;
            case R.id.reply_post:
                Rdata.content=ed.getText().toString();
                Rdata.quId=qa.quId;
                Rdata.userId=userData.getUserId();
                Log.e("POST",gson.toJson(Rdata));
                doPost(postUrl, gson.toJson(Rdata), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("POST",response.body().string());
                        pageNum=1;
                        k=0;
                        Message msg = new Message();
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                        adapter3=null;
                    }
                },token);
                break;
            case R.id.back_qa_detail:
                this.finish();
            case R.id.yuyin0:
                if (animationDrawable.isRunning()) {
                    release();
                    animationDrawable.selectDrawable(0);
                    animationDrawable.stop();
                    yuyin_tx.setText(lastyuyin_string);
                }
                else {
                    lastyuyin_string=yuyin_tx.getText().toString();
                    animationDrawable.start();
                    DownloadUtil.getInstance().download(qa.anVoice, this.getCacheDir().toString(), new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(String path) {
                            playSound(path);
                            yuyin_tx.setText("正在播放中");
                        }

                        @Override
                        public void onDownloading(int progress) {
                            yuyin_tx.setText("正在缓存中");
                        }

                        @Override
                        public void onDownloadFailed() {
                        }
                    });
                }
        }
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
                            Log.e("滑动到了顶端 ", scrollY + "");
                    }
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        if (!isLoading) {
                            Log.e("滑动到了底部 ", scrollY + "");
                            isLoading=true;
                            onLoad2();
                            Log.e("更新","问题回复Listview");
                        }
                        break;
                    }
                    return false;
            }
            return false;
        }
    }



}
