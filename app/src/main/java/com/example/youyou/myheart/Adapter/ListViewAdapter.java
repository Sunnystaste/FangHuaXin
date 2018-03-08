package com.example.youyou.myheart.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.Paint.CircleImagView;
import com.example.youyou.myheart.R;
import com.example.youyou.myheart.Tool.DownloadUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.youyou.myheart.UserActivity.UserFragment.token;
import static com.example.youyou.myheart.UserActivity.UserFragment.userData;
import static com.example.youyou.myheart.Tool.AppConstant.CancelVote;
import static com.example.youyou.myheart.Tool.AppConstant.ServerUrl;
import static com.example.youyou.myheart.Tool.AppConstant.VoicListened;
import static com.example.youyou.myheart.Tool.AppConstant.VoteOne;
import static com.example.youyou.myheart.Tool.AppConstant.sortMap;
import static com.example.youyou.myheart.Tool.MediaManager.playSound;
import static com.example.youyou.myheart.Tool.MediaManager.release;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2018/1/31.
 */

public class ListViewAdapter extends BaseAdapter {
    private static final String url=ServerUrl+VoicListened;
    private static final String Voteurl=ServerUrl+VoteOne;
    private static final String Cancelurl=ServerUrl+CancelVote;
    private String lastyuyin_string;
    private TextView lastyuyin_tx;
    private boolean isanim;
    private AnimationDrawable lastanim;
    private Map<Integer,Bitmap> IconMap;
    public ArrayList<QAClientVO> QAClientList;
    public Context context;
    public ListViewAdapter() {
    }

    @SuppressLint("NewApi")
    public ListViewAdapter(ArrayList<QAClientVO> QAClientList, Map<Integer,Bitmap> IconMap, Context context) {
        this.QAClientList = QAClientList;
        this.IconMap=IconMap;
        this.context=context;
        this.isanim=false;
    }

    @Override
    public int getCount() {
        return QAClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return QAClientList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.r=view.findViewById(R.id.yuyin);
            holder.answer_content=view.findViewById(R.id.answer_content);
            holder.sort = (TextView) view.findViewById(R.id.sort);
            holder.question_content = (TextView)view.findViewById(R.id.question_content);
            TextPaint tp=holder.question_content.getPaint();
            tp.setFakeBoldText(true);
            holder.like=view.findViewById(R.id.like);
            holder.X_people_like=view.findViewById(R.id.X_people_like);
            holder.answer_icon=view.findViewById(R.id.answer_icon);
            holder.answer_name=view.findViewById(R.id.answer_name);
            holder.yuyin_img=view.findViewById(R.id.yuyin_img);
            holder.yuyin_tx=view.findViewById(R.id.yuyin_tx);
            holder.X_people_listen=view.findViewById(R.id.X_people_listen);
            view.setTag(holder);//为了复用holder
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        QAClientVO qaClientVO=QAClientList.get(position);
        holder.answer_content.setText(qaClientVO.anContent);
        holder.answer_icon.setImageBitmap(IconMap.get(position));
        holder.sort.setText(sortMap.get(qaClientVO.quType));
        holder.question_content.setText(qaClientVO.quContent);
        holder.like.setImageResource(R.drawable.like_selector);
        if(qaClientVO.voted) {
            holder.like.setSelected(true);
            Log.e("qaClientVO"+position,"Vote");
        }else
            holder.like.setSelected(false);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData.getUserId() != null) {
                    if (holder.like.isSelected()) {
                        holder.like.setSelected(false);
                        Map<String, String> map = new HashMap<>();
                        map.put("userId", userData.getUserId());
                        map.put("quId", qaClientVO.quId);
                        doPost(Cancelurl, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("POST", "取消点赞成功" + response.body().toString());
                            }
                        }, token);
                        qaClientVO.quVoteNum--;
                        qaClientVO.voted = false;
                        notifyDataSetChanged();
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("userId", userData.getUserId());
                        map.put("quId", qaClientVO.quId);
                        doPost(Voteurl, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("POST", "点赞成功" + response.body().toString());
                            }
                        }, token);
                        qaClientVO.quVoteNum++;
                        qaClientVO.voted = true;
                        notifyDataSetChanged();
                    }
                }
            }
        });
        holder.X_people_like.setText(qaClientVO.quVoteNum.toString()+"人有同感");
        holder.answer_name.setText(qaClientVO.anUserNickname);
        holder.yuyin_img.setImageResource(R.drawable.button_play_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) ((ImageView) holder.yuyin_img).getDrawable();
        if(qaClientVO.anId==null)
            holder.answer_content.setVisibility(View.GONE);
        if(qaClientVO.anVoice==null)
        {
            holder.answer_content.setVisibility(View.VISIBLE);
            holder.r.setVisibility(View.GONE);
            holder.X_people_listen.setVisibility(View.GONE);
        }
        else
        {
            holder.X_people_listen.setText(qaClientVO.anVoiceListened+"人听过");
            holder.yuyin_tx.setText(qaClientVO.anVoiceDuration.intValue()/60+"'"+qaClientVO.anVoiceDuration.intValue()%60+"\"");
            holder.answer_content.setVisibility(View.GONE);
            holder.r.setVisibility(View.VISIBLE);
            holder.X_people_listen.setVisibility(View.VISIBLE);
            holder.r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (animationDrawable.isRunning()) {
                        release();
                        isanim=false;
                        animationDrawable.selectDrawable(0);
                        animationDrawable.stop();
                        lastyuyin_tx.setText(lastyuyin_string);
                    }
                    else {
                        if(isanim==true)
                        {
                            lastyuyin_tx.setText(lastyuyin_string);
                            lastanim.stop();
                            lastanim.selectDrawable(0);
                        }
                        isanim = true;
                        lastyuyin_tx=v.findViewById(R.id.yuyin_tx);
                        lastyuyin_string=lastyuyin_tx.getText().toString();
                        lastanim=animationDrawable;
                        animationDrawable.start();
                        DownloadUtil.getInstance().download(qaClientVO.anVoice, context.getCacheDir().toString(), new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(String path) {
                                playSound(path);
                                Map<String,String> map=new HashMap<>();
                                map.put("userId",userData.getUserId());
                                map.put("anId",qaClientVO.anId);
                                doPost(url, map, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.e("POST",response.body().toString());
                                    }
                                },token);
                                holder.yuyin_tx.setText("正在播放中");
                            }

                            @Override
                            public void onDownloading(int progress) {
                                holder.yuyin_tx.setText("正在缓存中");
                            }

                            @Override
                            public void onDownloadFailed() {
                            }
                        });
                    }
                }
            });
        }
        return view;
    }



    public void add(List<QAClientVO> newQAClientList,Map<Integer,Bitmap>  newBitmapMap) {
        IconMap.putAll(newBitmapMap);
        QAClientList.addAll(newQAClientList);
        notifyDataSetChanged();
    }

    static class ViewHolder
    {
        RelativeLayout r;
        TextView sort;
        TextView question_content;
        ImageView like;
        TextView X_people_like;
        CircleImagView answer_icon;
        TextView answer_name;
        ImageView yuyin_img;
        TextView yuyin_tx;
        TextView X_people_listen;
        TextView answer_content;
    }
}
