package com.feiruirobots.jabil.p1;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.view.textclassifier.TextLinks;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
import com.feiruirobots.jabil.p1.http.CallServer;
import com.feiruirobots.jabil.p1.http.HttpResponse;
import com.feiruirobots.jabil.p1.model.Carton;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.StringRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.hutool.core.thread.ThreadUtil;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        dumpExceptionToNet(ex);
        ex.printStackTrace();
        // 如果系统提供了默认的异常处理器，则交给系统去结束我们的程序，否则就由我们自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            ThreadUtil.sleep(5000);
            Process.killProcess(Process.myPid());
        }
    }

    private void dumpExceptionToNet(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        ToastUtil.show(mContext, result);
        StringRequest request = new StringRequest(App.getMethod("/errorInfo"), RequestMethod.POST);
        request.add("msg", result);
        CallServer.getInstance().add(0, request, new HttpResponse(mContext) {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onOK(JSONObject object) {
                ToastUtil.show(mContext, "错误已经上传");
            }

            @Override
            public void onFail(JSONObject object) {
                TTSUtil.speak("Fail");
            }

            @Override
            public void onError() {
                TTSUtil.speak("Error");
            }
        });
    }
}
