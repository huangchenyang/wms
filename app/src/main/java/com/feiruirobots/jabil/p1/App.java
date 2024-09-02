package com.feiruirobots.jabil.p1;

import android.app.Application;

import com.feiruirobots.jabil.p1.model.FUNCTION;
import com.yanzhenjie.nohttp.NoHttp;

import java.util.HashMap;
import java.util.List;

import cn.hutool.core.util.StrUtil;

public class App extends Application {
    private static App sInstance;

    private HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

    public static String getMethod(String method) {
//        return StrUtil.format("http://10.121.196.47:11180/jabil/pda{}",method);
//        return StrUtil.format("http://192.168.43.241/jabil/pda{}", method);
//        return StrUtil.format("http://8.134.165.24:11180/jabil/pda{}",method);    //自己的
        return StrUtil.format("http://10.121.196.47:11180/jabil/pda{}",method);   //现场
//        return StrUtil.format("http://192.168.17.172:11180/jabil/pda{}",method);
//        return StrUtil.format("http://8.138.36.33:11180/jabil/pda{}",method);  //新测试环境
    }

    public static String getCodeBarUrl() {
        return StrUtil.format("http://8.134.165.24:11180");
    }

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        CrashHandler crashHandler=CrashHandler.getInstance();
        crashHandler.init(this);
        sInstance = this;
    }

    public void setData(FUNCTION function, String type, List<String> list) {
        String key = function.value + "-" + type;
        if (!hashMap.containsKey(key)) {
            hashMap.put(key, list);
        }
    }

    public List<String> getData(FUNCTION function, String type) {
        String key = function.value + "-" + type;
        return hashMap.get(key);
    }
}
