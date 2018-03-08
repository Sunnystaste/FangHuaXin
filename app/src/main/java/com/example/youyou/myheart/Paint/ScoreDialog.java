package com.example.youyou.myheart.Paint;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.youyou.myheart.R;

/**
 * Created by youyou on 2018/1/26.
 */

public class ScoreDialog extends Dialog {
    private ImageView close;
    private CloseOnClickListener closeOnClickListener;
    public ScoreDialog( Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_zhuyi);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    private void initEvent() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeOnClickListener != null) {
                    closeOnClickListener.CloseClick();
                }
            }
        });
    }

    private void initView() {
        close=findViewById(R.id.score_cancel);
    }


    public void setCloseOnclickListener(CloseOnClickListener closeOnClickListener) {
        this.closeOnClickListener = closeOnClickListener;
    }
    public interface CloseOnClickListener {
        public void CloseClick();
    }
}
