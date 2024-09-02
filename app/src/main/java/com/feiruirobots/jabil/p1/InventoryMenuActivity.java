package com.feiruirobots.jabil.p1;

import static com.feiruirobots.jabil.p1.LoginActivity.userName;
import static com.feiruirobots.jabil.p1.MainActivity.gVersion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.feiruirobots.jabil.p1.model.ACTION;
import com.feiruirobots.jabil.p1.model.FUNCTION;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.hutool.core.util.StrUtil;

public class InventoryMenuActivity extends BaseActivity {
    private String action = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_menu);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        action = intent.getStringExtra("ACTION");
        initTitle(ACTION.of(action).msg);

        TextView tv_username = findViewById(R.id.tv_username);
        tv_username.setText(userName);
        TextView tv_version = findViewById(R.id.tv_version);
        tv_version.setText(gVersion);
    }

    @OnClick({R.id.btn_cycle_out, R.id.btn_cycle_in,R.id.btn_consolidation_out, R.id.btn_consolidation_in})
    public void onClick(View view) {
        FUNCTION function = null;
        if (R.id.btn_cycle_out == view.getId()) function = FUNCTION.CYCLE_OUT;
        if (R.id.btn_cycle_in == view.getId()) function = FUNCTION.CYCLE_IN;
        if (R.id.btn_consolidation_out == view.getId()) function = FUNCTION.CONSOLIDATION_OUT;
        if (R.id.btn_consolidation_in == view.getId()) function = FUNCTION.CONSOLIDATION_IN;
        assert function != null;
        if (StrUtil.equals(action, ACTION.INV.value) && (function.equals(FUNCTION.CYCLE_OUT) || function.equals(FUNCTION.CYCLE_IN) )) {
            Intent intent = new Intent(InventoryMenuActivity.this, InventoryActivity.class);
            intent.putExtra("FUNCTION", function.value);
            startActivity(intent);
        }

        if (StrUtil.equals(action, ACTION.INV.value) && (function.equals(FUNCTION.CONSOLIDATION_OUT) || function.equals(FUNCTION.CONSOLIDATION_IN) )) {
            Intent intent = new Intent(InventoryMenuActivity.this, ConsolidationActivity.class);
            intent.putExtra("FUNCTION", function.value);
            startActivity(intent);
        }
    }

}