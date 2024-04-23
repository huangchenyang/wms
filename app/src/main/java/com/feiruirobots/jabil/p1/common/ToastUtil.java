package com.feiruirobots.jabil.p1.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

/**
 * desc  Toast工具类
 */
public class ToastUtil {
    @SuppressLint("HandlerLeak")
    private static Handler handler = new Handler() {
        public void handleMessage(@NonNull Message msg) {
            mToast.show();
        }
    };

    private ToastUtil() {
        throw new AssertionError();
    }

    private static Toast mToast;

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void showWithSound(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void showWithSound(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void showWithSound(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    @SuppressLint("ShowToast")
    public static void show(Context context, CharSequence text, int duration) {
        if (mToast == null)
            mToast = Toast.makeText(context, text, duration);
        else {
            mToast.setText(text);
        }
        handler.sendEmptyMessage(0);
    }
}