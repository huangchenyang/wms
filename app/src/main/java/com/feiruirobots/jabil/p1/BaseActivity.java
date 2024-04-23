package com.feiruirobots.jabil.p1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.feiruirobots.jabil.p1.ui.MyTopBar;

import java.util.Timer;

public class BaseActivity extends Activity {
    protected boolean isVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    protected void initTitle(String title) {
        TextView tv_header_title = findViewById(R.id.tv_header_title);
        if (tv_header_title != null) tv_header_title.setText(title);
        LinearLayout ll_header_back = findViewById(R.id.ll_header_back);
        if (ll_header_back != null) ll_header_back.setOnClickListener(v -> this.onBackPressed());
    }

    @Override
    protected void onStart() {
        isVisible = true;
        super.onStart();
    }

    @Override
    protected void onStop() {
        isVisible = false;
        super.onStop();
    }
}
