package com.example.youyou.myheart.Adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.youyou.myheart.Data.PageInfo;
import com.example.youyou.myheart.Data.QAClientVO;
import com.example.youyou.myheart.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by youyou on 2018/2/5.
 */

public class ListViewAdapter3 extends BaseAdapter {
    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
    private List<PageInfo> PageInfoList;
    private Map<Integer,Bitmap> IconMap;
    private Context context;
    public ListViewAdapter3() {
    }

    public ListViewAdapter3(List<PageInfo>PageInfoList,Map<Integer,Bitmap> IconMap, Context context) {
        this.PageInfoList = PageInfoList;
        this.context=context;
        this.IconMap=IconMap;
    }

    @Override
    public int getCount() {
        return PageInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return PageInfoList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.reply_item,parent,false);
            holder = new ViewHolder();
            holder.answer_name=view.findViewById(R.id.reply_tx);
            holder.answer_content=view.findViewById(R.id.reply_content);
            holder.Icon=view.findViewById(R.id.reply_icon);
            holder.like=view.findViewById(R.id.reply_islike);
            holder.X_people_like=view.findViewById(R.id.reply_X_people_like);
            holder.time=view.findViewById(R.id.reply_time);
            view.setTag(holder);//为了复用holder
        }else
        {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        PageInfo pageInfo=PageInfoList.get(position);
        holder.answer_content.setText(pageInfo.content);
        holder.X_people_like.setText(pageInfo.voteNum.toString());
        holder.time.setText(sdf.format(new Date(pageInfo.createdTime)).toString());
        holder.answer_name.setText(pageInfo.userNickName);
        holder.Icon.setImageBitmap(IconMap.get(position));
        holder.like.setImageResource(R.drawable.like_selector);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like.isSelected()) {
                    holder.like.setSelected(false);
                    pageInfo.voteNum--;
                }else {
                    holder.like.setSelected(true);
                    pageInfo.voteNum++;
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    public void add(List<PageInfo> newPageInfoList, Map<Integer,Bitmap>  newBitmapMap) {
        IconMap.putAll(newBitmapMap);
        PageInfoList.addAll(newPageInfoList);
        notifyDataSetChanged();
    }

    static class ViewHolder
    {
        TextView time;
        TextView answer_name;
        TextView answer_content;
        ImageView Icon;
        ImageView like;
        TextView X_people_like;
    }
}
