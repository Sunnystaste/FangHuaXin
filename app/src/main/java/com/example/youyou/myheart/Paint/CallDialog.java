package com.example.youyou.myheart.Paint;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.example.youyou.myheart.R;

/**
 * Created by youyou on 2018/2/8.
 */

public class CallDialog extends Dialog {
    public LinearLayout l1,l2,l3,l4;
    private LlOnClickListener l1OnClickListener;
    private L2OnClickListener l2OnClickListener;
    private L3OnClickListener l3OnClickListener;
    private L4OnClickListener l4OnClickListener;
    public CallDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();

    }

    private void initEvent() {
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l1OnClickListener != null) {
                    l1OnClickListener.L1Click();
                }
            }
        });


        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l2OnClickListener != null) {
                    l2OnClickListener.L2Click();
                }
            }
        });


        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l3OnClickListener != null) {
                    l3OnClickListener.L3Click();
                }
            }
        });


        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l4OnClickListener != null) {
                    l4OnClickListener.L4Click();
                }
            }
        });
    }

    private void initView() {
        l1=findViewById(R.id.shahe_call);
        l2=findViewById(R.id.qingshuihe_call);
        l3=findViewById(R.id.quanguo_call);
        l4=findViewById(R.id.quxiao_call);
    }

    public void setL1OnclickListener( LlOnClickListener l1OnClickListener) {
        this.l1OnClickListener = l1OnClickListener;
    }
    public interface  LlOnClickListener {
        public void L1Click();
    }



    public void setL2OnclickListener( L2OnClickListener l2OnClickListener) {
        this.l2OnClickListener = l2OnClickListener;
    }
    public interface  L2OnClickListener {
        public void L2Click();
    }


    public void setL3OnclickListener( L3OnClickListener l3OnClickListener) {
        this.l3OnClickListener = l3OnClickListener;
    }
    public interface  L3OnClickListener {
        public void L3Click();
    }


    public void setL4OnclickListener( L4OnClickListener l4OnClickListener) {
        this.l4OnClickListener = l4OnClickListener;
    }
    public interface  L4OnClickListener {
        public void L4Click();
    }





}
