package com.example.youyou.myheart.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.youyou.myheart.Data.MyFinishedActivityVO;
import com.example.youyou.myheart.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.youyou.myheart.Tool.AppConstant.activityMap;

/**
 * Created by youyou on 2018/2/17.
 */

public class ListViewAdapter_point  extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日");
    public ArrayList<MyFinishedActivityVO> MyFinishedActivityVOList;
    public Context context;

    public ListViewAdapter_point() {
    }

    public ListViewAdapter_point(ArrayList<MyFinishedActivityVO> MyFinishedActivityVOList, Context context) {
        this.MyFinishedActivityVOList = MyFinishedActivityVOList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return MyFinishedActivityVOList.size();
    }

    @Override
    public Object getItem(int position) {
        return MyFinishedActivityVOList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.points_item, parent, false);
            holder = new ViewHolder();
            holder.mingcheng=view.findViewById(R.id.mingcheng_item);
            holder.mokuai=view.findViewById(R.id.mokuai_item);
            holder.jifen=view.findViewById(R.id.jifen_item);
            holder.riqi=view.findViewById(R.id.riqi_item);
            view.setTag(holder);//为了复用holder
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        MyFinishedActivityVO VO = MyFinishedActivityVOList.get(position);
        holder.mingcheng.setText(VO.acName);
        holder.riqi.setText(sdf.format(new Date(VO.finishedTime)).toString());
        holder.jifen.setText(VO.acIntegral.toString());
        holder.mokuai.setText(activityMap.get(VO.acType));
        return view;
    }


    static class ViewHolder {
        TextView mingcheng;
        TextView mokuai;
        TextView riqi;
        TextView jifen;
    }
}