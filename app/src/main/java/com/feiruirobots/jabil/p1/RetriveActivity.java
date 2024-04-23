package com.feiruirobots.jabil.p1;

import android.os.Bundle;

import butterknife.ButterKnife;

public class RetriveActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive);
        ButterKnife.bind(this);
    }
}