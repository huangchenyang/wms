package com.feiruirobots.jabil.p1.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feiruirobots.jabil.p1.R;

public class MyTopBar extends RelativeLayout {
    private Button btnLeft;
    private Button btnRight;
    private TextView tvTitle;
    private String title;
    private String leftText;
    private String rightText;

    public MyTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化属性
        initAttrs(context, attrs);
        //填充视图
        View.inflate(context, R.layout.ui_my_topbar, this);
        btnLeft = findViewById(R.id.btn_left_top_bar);
        tvTitle = findViewById(R.id.tv_title_top_bar);
        btnRight = findViewById(R.id.btn_right_top_bar);
        initData();
    }

    //初始化属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyTopBar);
        //标题
        title = typedArray.getString(R.styleable.MyTopBar_m_title);
        //左边按钮的文字
        leftText = typedArray.getString(R.styleable.MyTopBar_m_left_text);
        //左边按钮的图片
        rightText = typedArray.getString(R.styleable.MyTopBar_m_right_text);
    }

    // 初始化数据,因为每个按钮，我都在布局文件中将其显示状态设置为GONE 了
    // 所以在填充数据的时候，要显示一下
    private void initData() {
        if (title != null) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
        if (leftText != null) {
            btnLeft.setVisibility(VISIBLE);
            btnLeft.setText(leftText);
        }
        if (rightText != null) {
            btnRight.setVisibility(VISIBLE);
            btnRight.setText(rightText);
        }
    }

    //获取左边文字按钮
    public void setLeftBtnText(String text) {
        btnLeft.setText(text);
    }

    //获取右边文字按钮
    public void setRightBtnText(String text) {
        btnRight.setText(text);
    }

    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    //获取左边文字按钮
    public Button getLeftBtn() {
        return btnLeft;
    }

    //获取右边文字按钮
    public Button getRightBtn() {
        return btnRight;
    }

    public TextView getTitle() {
        return tvTitle;
    }
}