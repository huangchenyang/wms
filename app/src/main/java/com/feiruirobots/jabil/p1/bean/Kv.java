package com.feiruirobots.jabil.p1.bean;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

public class Kv extends HashMap {

    private static final long serialVersionUID = -808251639784763326L;

    public Kv() {
    }

    public static Kv of(Object key, Object value) {
        return new Kv().set(key, value);
    }

    public static Kv by(Object key, Object value) {
        return new Kv().set(key, value);
    }

    public static Kv create() {
        return new Kv();
    }

    public Kv set(Object key, Object value) {
        super.put(key, value);
        return this;
    }

    public Kv setIfNotBlank(Object key, String value) {
        if (ObjUtil.isNotEmpty(value)) {
            set(key, value);
        }
        return this;
    }

    public Kv setIfNotNull(Object key, Object value) {
        if (value != null) {
            set(key, value);
        }
        return this;
    }

    public Kv set(Map map) {
        super.putAll(map);
        return this;
    }

    public Kv set(Kv kv) {
        super.putAll(kv);
        return this;
    }

    public Kv delete(Object key) {
        super.remove(key);
        return this;
    }

    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    public <T> T getAs(Object key, Object defaultValue) {
        Object ret = get(key);
        return (T) (ret != null ? ret : defaultValue);
    }

    public String getStr(Object key) {
        Object s = get(key);
        return s != null ? s.toString() : null;
    }

    public Integer getInt(Object key) {
        return TypeKit.toInt(get(key));
    }

    public Long getLong(Object key) {
        return TypeKit.toLong(get(key));
    }

    public BigDecimal getBigDecimal(Object key) {
        return TypeKit.toBigDecimal(get(key));
    }

    public Double getDouble(Object key) {
        return TypeKit.toDouble(get(key));
    }

    public Float getFloat(Object key) {
        return TypeKit.toFloat(get(key));
    }

    public Number getNumber(Object key) {
        return TypeKit.toNumber(get(key));
    }

    public Boolean getBoolean(Object key) {
        return TypeKit.toBoolean(get(key));
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
        return value != null && TypeKit.toBoolean(value);
    }

    /**
     * key 存在，并且 value 为 false，则返回 true
     */
    public boolean isFalse(Object key) {
        Object value = get(key);
        return value != null && !TypeKit.toBoolean(value);
    }

    public boolean equals(Object kv) {
        return kv instanceof Kv && super.equals(kv);
    }
}
