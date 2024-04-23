package com.feiruirobots.jabil.p1.bean;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;

public class Ret extends HashMap {

    private static final long serialVersionUID = -2150729333382285526L;

    /**
     * 状态
     */
    static String STATE = "state";
    static Object STATE_OK = "ok";
    static Object STATE_FAIL = "fail";

    /**
     * 数据
     */
    static String DATA = "data";
    static boolean dataWithOkState = false;            // data(Object) 方法伴随 ok 状态

    /**
     * 消息
     */
    static String MSG = "msg";

    public Ret() {
    }

    public static Ret of(Object key, Object value) {
        return new Ret().set(key, value);
    }

    public static Ret by(Object key, Object value) {
        return new Ret().set(key, value);
    }

    public static Ret create() {
        return new Ret();
    }

    public static Ret ok() {
        return new Ret().setOk();
    }

    public static Ret ok(String msg) {
        return new Ret().setOk()._setMsg(msg);
    }

    public static Ret ok(Object key, Object value) {
        return new Ret().setOk().set(key, value);
    }

    public static Ret fail() {
        return new Ret().setFail();
    }

    public static Ret fail(String msg) {
        return new Ret().setFail()._setMsg(msg);
    }

    @Deprecated
    public static Ret fail(Object key, Object value) {
        return new Ret().setFail().set(key, value);
    }

    public static Ret state(Object value) {
        return new Ret()._setState(value);
    }

    public static Ret data(Object data) {
        return new Ret()._setData(data);
    }

    public static Ret msg(String msg) {
        return new Ret()._setMsg(msg);
    }

    /**
     * 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
     * <p>
     * 如果未来开放为 public，当 stateWatcher 不为 null 且 dataWithOkState 为 true
     * 与 _setData 可以形成死循环调用
     */
    protected Ret _setState(Object value) {
        super.put(STATE, value);
        return this;
    }

    /**
     * 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
     * <p>
     * 如果未来开放为 public，当 stateWatcher 不为 null 且 dataWithOkState 为 true
     * 与 _setState 可以形成死循环调用
     */
    protected Ret _setData(Object data) {
        super.put(DATA, data);
        if (dataWithOkState) {
            _setState(STATE_OK);
        }
        return this;
    }

    // 避免产生 setter/getter 方法，以免影响第三方 json 工具的行为
    protected Ret _setMsg(String msg) {
        super.put(MSG, msg);
        return this;
    }

    public String getMsg() {
        String s = getStr(MSG);
        return s != null ? s.toString() : null;
    }

    public Ret setOk() {
        return _setState(STATE_OK);
    }

    public Ret setFail() {
        return _setState(STATE_FAIL);
    }

    public boolean isOk() {
        Object state = get(STATE);
        if (STATE_OK.equals(state)) {
            return true;
        }
        if (STATE_FAIL.equals(state)) {
            return false;
        }
        throw new IllegalStateException("调用 isOk() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
    }

    public boolean isFail() {
        Object state = get(STATE);
        if (STATE_FAIL.equals(state)) {
            return true;
        }
        if (STATE_OK.equals(state)) {
            return false;
        }


        throw new IllegalStateException("调用 isFail() 之前，必须先调用 ok()、fail() 或者 setOk()、setFail() 方法");
    }

    public Ret set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public Ret setIfNotBlank(Object key, String value) {
        if (StrUtil.isNotBlank(value)) {
            set(key, value);
        }
        return this;
    }

    public Ret setIfNotNull(Object key, Object value) {
        if (value != null) {
            set(key, value);
        }
        return this;
    }

    public Ret set(Map map) {
        super.putAll(map);
        return this;
    }

    public Ret set(Ret ret) {
        super.putAll(ret);
        return this;
    }

    public Ret delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    public String getStr(Object key) {
        Object s = get(key);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.intValue() : null;
    }

    public Long getLong(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.longValue() : null;
    }

    public Double getDouble(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.doubleValue() : null;
    }

    public Float getFloat(Object key) {
        Number n = (Number) get(key);
        return n != null ? n.floatValue() : null;
    }

    public Number getNumber(Object key) {
        return (Number) get(key);
    }

    public Boolean getBoolean(Object key) {
        return (Boolean) get(key);
    }

    /**
     * key 存在，并且 value 不为 null
     */
    public boolean notNull(Object key) {
        return get(key) != null;
    }

    /**
     * key 不存在，或者 key 存在但 value 为null
     */
    public boolean isNull(Object key) {
        return get(key) == null;
    }

    /**
     * key 存在，并且 value 为 true，则返回 true
     */
    public boolean isTrue(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == true));
    }

    /**
     * key 存在，并且 value 为 false，则返回 true
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return (value instanceof Boolean && ((Boolean) value == false));
    }

    public boolean equals(Object ret) {
        return ret instanceof Ret && super.equals(ret);
    }
}
