package com.feiruirobots.jabil.p1;

import static com.feiruirobots.jabil.p1.LoginActivity.userName;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.feiruirobots.jabil.p1.model.ACTION;
import com.feiruirobots.jabil.p1.model.FUNCTION;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

public class MenuActivity extends BaseActivity {
    private String action = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        action = intent.getStringExtra("ACTION");
        initTitle(ACTION.of(action).msg);
        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText(userName);
    }

    @OnClick({R.id.btn_finish_goods, R.id.btn_rtc, R.id.btn_raw_material, R.id.btn_staging, R.id.btn_simi_fg})
    public void onClick(View view) {
        FUNCTION function = null;
        if (R.id.btn_finish_goods == view.getId()) function = FUNCTION.FINISHED_GOODS;
        if (R.id.btn_simi_fg == view.getId()) function = FUNCTION.SEMI_FG;
        if (R.id.btn_rtc == view.getId()) function = FUNCTION.RTV_RTC;
        if (R.id.btn_raw_material == view.getId()) function = FUNCTION.RAW_MATERIAL;
        if (R.id.btn_staging == view.getId()) function = FUNCTION.STAGING;
        assert function != null;
        if (StrUtil.equals(action, ACTION.IN.value)) {
            Intent intent = new Intent(MenuActivity.this, StockInActivity.class);
            intent.putExtra("FUNCTION", function.value);
            startActivity(intent);
        }
        if (StrUtil.equals(action, ACTION.OUT.value)) {
            Intent intent = new Intent(MenuActivity.this, StockOutActivity.class);
            intent.putExtra("FUNCTION", function.value);
            startActivity(intent);
        }
    }
}