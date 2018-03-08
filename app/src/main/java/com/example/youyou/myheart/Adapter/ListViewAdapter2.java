package com.example.youyou.myheart.Adapter;

import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.R;

import java.io.IOException;
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
import static com.example.youyou.myheart.Tool.AppConstant.VoteOne;
import static com.example.youyou.myheart.Tool.AppConstant.sortMap;
import static com.example.youyou.myheart.Tool.OkHttp3Util.doPost;

/**
 * Created by youyou on 2018/1/31.
 */
public class ListViewAdapter2 extends BaseAdapter {
    private static final String Voteurl=ServerUrl+VoteOne;
    private static final String Cancelurl=ServerUrl+CancelVote;
    public List<QAClientVO> NoAnsweredClientList;
    private Context context;
    public ListViewAdapter2() {
    }

    public ListViewAdapter2(List<QAClientVO> NoAnsweredClientList, Context context) {
        this.NoAnsweredClientList = NoAnsweredClientList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return NoAnsweredClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return NoAnsweredClientList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item2,parent,false);
            holder = new ViewHolder();
            holder.sort = (TextView) view.findViewById(R.id.sort2);
            holder.question_content = (TextView)view.findViewById(R.id.question_content2);
            TextPaint tp=holder.question_content.getPaint();
            tp.setFakeBoldText(true);
            holder.like=view.findViewById(R.id.like2);
            holder.X_people_like=view.findViewById(R.id.X_people_like2);
            view.setTag(holder);//为了复用holder
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        QAClientVO qaClientVO=NoAnsweredClientList.get(position);
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
        return view;
    }


    public void add(List<QAClientVO> newQAClientList) {
        NoAnsweredClientList.addAll(newQAClientList);
        notifyDataSetChanged();
    }

    static class ViewHolder
    {
        TextView sort;
        TextView question_content;
        ImageView like;
        TextView X_people_like;
    }
}
