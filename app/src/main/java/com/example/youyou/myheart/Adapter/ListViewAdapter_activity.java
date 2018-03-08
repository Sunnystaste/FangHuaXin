package com.example.youyou.myheart.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youyou.myheart.Data.ActivityClientVO;
import com.example.youyou.myheart.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by youyou on 2018/2/9.
 */

public class ListViewAdapter_activity  extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("yy年MM月dd日HH:mm");
    private Map<Integer,Bitmap> replyIconMap;
    public ArrayList<ActivityClientVO> ActivityClientList;
    public Context context;

    public ListViewAdapter_activity() {
    }

    public ListViewAdapter_activity(ArrayList<ActivityClientVO> ActivityClientList,Map<Integer,Bitmap> replyIconMap, Context context) {
        this.ActivityClientList = ActivityClientList;
        this.replyIconMap = replyIconMap;
        this.context = context;
    }

    @Override
    public int getCount() {
        return  ActivityClientList.size();
    }

    @Override
    public Object getItem(int position) {
        return ActivityClientList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
            holder = new ViewHolder();
            holder.r=view.findViewById(R.id.activity_relative);
            holder.time=view.findViewById(R.id.activity_time);
            holder.location=view.findViewById(R.id.activity_location);
            holder.score=view.findViewById(R.id.activity_score);
            holder.title=view.findViewById(R.id.activity_title);
            view.setTag(holder);//为了复用holder
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        ActivityClientVO ClientVO = ActivityClientList.get(position);
        BitmapDrawable d=new BitmapDrawable(replyIconMap.get(position));
        d.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        holder.r.setBackground(d);
        holder.title.setText(ClientVO.name);
        holder.time.setText(sdf.format(new Date(ClientVO.startTime)).toString()+"/"+sdf.format(new Date(ClientVO.endTime)).toString());
        holder.location.setText(ClientVO.address);
        holder.score.setText("可得"+ClientVO.integral+"积分");
        return view;
    }


    public void add(List<ActivityClientVO> newActivityClientList, Map<Integer,Bitmap> newreplyIconMap) {
        replyIconMap.putAll(newreplyIconMap);
        ActivityClientList.addAll(newActivityClientList);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        RelativeLayout r;
        TextView title;
        TextView time;
        TextView location;
        TextView score;
    }
}