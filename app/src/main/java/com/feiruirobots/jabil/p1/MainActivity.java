package com.feiruirobots.jabil.p1;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.feiruirobots.jabil.p1.model.ACTION;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tv_version.setText("Version 1.29.3");
        tv_version.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://10.121.196.47:11180/jabil/pda/download");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    @OnClick({R.id.btn_in_stock, R.id.btn_retrive, R.id.btn_inventory})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_in_stock) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("ACTION", ACTION.IN.value);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_retrive) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("ACTION", ACTION.OUT.value);
            startActivity(intent);
        }
        if (view.getId() == R.id.btn_inventory) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            intent.putExtra("ACTION", ACTION.INV.value);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Alert");//设置对话框的标题
        dialog.setMessage("Confirm Back!");//设置对话框的内容
        dialog.setCancelable(false);//设置对话框是否可以取消
        //确定按钮的点击事件
        dialog.setPositiveButton("OK", (dialog12, which) -> this.finish());
        //取消按钮的点击事件
        dialog.setNegativeButton("Cancel", (dialog1, which) -> {
        });
        dialog.show();
    }
}