package com.feiruirobots.jabil.p1.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.feiruirobots.jabil.p1.LoginActivity;
import com.feiruirobots.jabil.p1.common.TTSUtil;
import com.feiruirobots.jabil.p1.common.ToastUtil;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.PrintWriter;
import java.io.StringWriter;

import cn.hutool.core.util.StrUtil;

public abstract class HttpResponse implements OnResponseListener {
    private Context mContext;

    public HttpResponse(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void onStart(int what) {

    }


    public abstract void onOK(JSONObject object);

    public abstract void onFail(JSONObject object);

    public abstract void onError(String errorStr);

    @Override
    public void onSucceed(int what, Response response) {
        try {
            Log.i("=======", response.get().toString());
            JSONObject json = JSONObject.parseObject(response.get().toString());
            String state = json.getString("state");
            String code = json.getString("code");
            if (StrUtil.equals(state, "ok")) {
                onOK(json);
                return;
            }
            if(code!=null && code.equals("403")){  //跳转回登陆界面
                startLogoutActivity();
            }
            onFail(json);
        } catch (Exception ex) {
            onError(getStackTraceAsString(ex));
        }
    }

    public static String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * When there was an error correction.
     *
     * @param what     the credit of the incoming handle is used to distinguish between multiple requests.
     * @param response failure callback.
     */
    @Override
    public void onFailed(int what, Response response) {
        ToastUtil.show(mContext, "network error");
        TTSUtil.speak("network error");
    }

    /**
     * When the handle finish.
     *
     * @param what the credit of the incoming handle is used to distinguish between multiple requests.
     */
    @Override
    public void onFinish(int what) {

    }

    private void startLogoutActivity() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
