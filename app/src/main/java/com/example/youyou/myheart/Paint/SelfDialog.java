package com.example.youyou.myheart.Paint;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.youyou.myheart.UserActivity.UserFragment;
import com.example.youyou.myheart.R;

/**
 * 创建自定义的dialog，主要学习其实现原理
 */
public class SelfDialog extends Dialog {
    public SmoothCheckBox xieyi;
    private RelativeLayout sina;//新浪按钮
    private RelativeLayout qq;//QQ按钮
    private TextView zhuxiao;//注销按钮
    private ImageView cancel;//右上角XX按钮
    public TextView last;//最近登陆
    private SinaOnClickListener sinaOnClickListener;//取消按钮被点击了的监听器
    private QqOnClickListener qqOnClickListener;//确定按钮被点击了的监听器
    private XieyiCheckedChangeLisener xieyiCheckedChangeLisener;
    private CancelOnClickListener cancelOnClickListener;
    private ZhuxiaoOnClickListener zhuxiaoOnClickListener;
    /**
     * 设置Cancel的监听
     * @param cancelOnClickListener
     */
    public void setCancelOnClickListener(CancelOnClickListener cancelOnClickListener) {
        this.cancelOnClickListener = cancelOnClickListener;
    }


    /**
     * 设置XieyiChekeBox的监听
     * @param xieyiOnCheckedChangeListener
     */
    public void setXieyiOnCheckedChangeListener(XieyiCheckedChangeLisener xieyiOnCheckedChangeListener) {
        this.xieyiCheckedChangeLisener = xieyiOnCheckedChangeListener;
    }

    /**
     * 设置sinaLinearlayout的监听
     * @param sinaOnClickListener
     */
    public void setSinaOnClickListener(SinaOnClickListener sinaOnClickListener) {
        this.sinaOnClickListener = sinaOnClickListener;
    }

    /**
     * 设置QQLinearlayout的监听
     * @param qqOnClickListener
     */
    public void setQqOnclickListener(QqOnClickListener qqOnClickListener) {
        this.qqOnClickListener = qqOnClickListener;
    }
    public void setZhuxiaoOnclickListener(ZhuxiaoOnClickListener zhuxiaoOnClickListener) {
        this.zhuxiaoOnClickListener = zhuxiaoOnClickListener;
    }

    public SelfDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelOnClickListener != null) {
                    cancelOnClickListener.CancelClick();
                }
            }
        });
        xieyi.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (xieyiCheckedChangeLisener != null) {
                    xieyiCheckedChangeLisener.XieyiChecked(isChecked);
                }
            }
        });
        //设置确定按钮被点击后，向外界提供监听
        sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sinaOnClickListener != null) {
                    sinaOnClickListener.SinaClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qqOnClickListener != null) {
                    qqOnClickListener.QqClick();
                }
            }
        });

        zhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zhuxiaoOnClickListener != null) {
                    zhuxiaoOnClickListener.ZhuxiaoClick();
                }
            }
        });
    }


    /**
     * 初始化界面控件
     */
    private void initView() {
        zhuxiao=(TextView) findViewById(R.id.zhuxiao);
        cancel=(ImageView) findViewById(R.id.cancel);
        qq = (RelativeLayout) findViewById(R.id.qq);
        xieyi=(SmoothCheckBox) findViewById(R.id.xie_cb);
        sina = (RelativeLayout) findViewById(R.id.sina);
        last = (TextView) findViewById(R.id.textView2);
        last.setText(UserFragment.last);
    }



    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface QqOnClickListener {
        public void QqClick();
    }
    public interface ZhuxiaoOnClickListener {
        public void ZhuxiaoClick();
    }
    public interface SinaOnClickListener {
        public void SinaClick();
    }

    public interface XieyiCheckedChangeLisener{
        public void XieyiChecked(boolean isChecked);
    }

    public interface CancelOnClickListener {
        public void CancelClick();
    }
}
