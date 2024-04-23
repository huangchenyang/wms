package com.feiruirobots.jabil.p1.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feiruirobots.jabil.p1.R;

/**
 * TODO: document your custom view class.
 */
public class LabelTextView extends LinearLayout {

    private TextView tvTitle;
    private TextView tvLeftText;
    private String m_title, left_text;

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        View.inflate(context, R.layout.ui_label_text_view, this);
        this.tvTitle = findViewById(R.id.tv_title);
        this.tvLeftText = findViewById(R.id.tv_left_text);
        initData();
    }

    private void initData() {
        if (m_title != null) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(m_title);
        }
        if (tvLeftText != null) {
            tvLeftText.setVisibility(VISIBLE);
            tvLeftText.setText(left_text);
        }
    }

    //初始化属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        //标题
        m_title = typedArray.getString(R.styleable.LabelTextView_m_title);
        //左边按钮的文字
        left_text = typedArray.getString(R.styleable.LabelTextView_m_text);
    }

    public void setText(String text) {
        tvLeftText.setText(text);
    }

    public String getText() {
        return tvLeftText.getText().toString();
    }

    public TextView getTvLeftText() {
        return tvLeftText;
    }
}