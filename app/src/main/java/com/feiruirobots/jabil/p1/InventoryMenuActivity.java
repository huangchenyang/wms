package com.feiruirobots.jabil.p1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    }

    @OnClick({R.id.btn_cycle_out, R.id.btn_cycle_in})
    public void onClick(View view) {
        FUNCTION function = null;
        if (R.id.btn_cycle_out == view.getId()) function = FUNCTION.CYCLE_OUT;
        if (R.id.btn_cycle_in == view.getId()) function = FUNCTION.CYCLE_IN;
        assert function != null;
        if (StrUtil.equals(action, ACTION.INV.value)) {
            Intent intent = new Intent(InventoryMenuActivity.this, InventoryActivity.class);
            intent.putExtra("FUNCTION", function.value);
            startActivity(intent);
        }
    }
}