package com.feiruirobots.jabil.p1.bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

public class TypeKit {
    private static final String datePattern = "yyyy-MM-dd";
    private static final int dateLen = datePattern.length();

    private static final String dateTimeWithoutSecondPattern = "yyyy-MM-dd HH:mm";
    private static final int dateTimeWithoutSecondLen = dateTimeWithoutSecondPattern.length();

    private static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    public static String toStr(Object s) {
        return s != null ? s.toString() : null;
    }

    public static Integer toInt(Object n) {
        if (n instanceof Integer) {
            return (Integer) n;
        } else if (n instanceof Number) {
            return ((Number) n).intValue();
        }
        // 支持 String 类型转换
        return n != null ? Integer.parseInt(n.toString()) : null;
    }

    public static Long toLong(Object n) {
        if (n instanceof Long) {
            return (Long) n;
        } else if (n instanceof Number) {
            return ((Number) n).longValue();
        }
        // 支持 String 类型转换
        return n != null ? Long.parseLong(n.toString()) : null;
    }

    public static Double toDouble(Object n) {
        if (n instanceof Double) {
            return (Double) n;
        } else if (n instanceof Number) {
            return ((Number) n).doubleValue();
        }
        // 支持 String 类型转换
        return n != null ? Double.parseDouble(n.toString()) : null;
    }

    public static BigDecimal toBigDecimal(Object n) {
        if (n instanceof BigDecimal) {
            return (BigDecimal) n;
        } else if (n != null) {
            return new BigDecimal(n.toString());
        } else {
            return null;
        }
    }

    public static Float toFloat(Object n) {
        if (n instanceof Float) {
            return (Float) n;
        } else if (n instanceof Number) {
            return ((Number) n).floatValue();
        }
        // 支持 String 类型转换
        return n != null ? Float.parseFloat(n.toString()) : null;
    }

    public static Short toShort(Object n) {
        if (n instanceof Short) {
            return (Short) n;
        } else if (n instanceof Number) {
            return ((Number) n).shortValue();
        }
        // 支持 String 类型转换
        return n != null ? Short.parseShort(n.toString()) : null;
    }

    public static Byte toByte(Object n) {
        if (n instanceof Byte) {
            return (Byte) n;
        } else if (n instanceof Number) {
            return ((Number) n).byteValue();
        }
        // 支持 String 类型转换
        return n != null ? Byte.parseByte(n.toString()) : null;
    }

    public static Boolean toBoolean(Object b) {
        if (b instanceof Boolean) {
            return (Boolean) b;
        } else if (b == null) {
            return null;
        }

        // 支持 String 类型转换，并且支持数字 1/0 与字符串 "1"/"0" 转换
        // 此处不能在判断 instanceof String 后强转 String，要支持 int、long 型的 1/0 值
        String s = b.toString();
        if ("true".equalsIgnoreCase(s) || "1".equals(s)) {
            return Boolean.TRUE;
        } else if ("false".equalsIgnoreCase(s) || "0".equals(s)) {
            return Boolean.FALSE;
        }

        return (Boolean) b;
    }

    public static Number toNumber(Object n) {
        if (n instanceof Number) {
            return (Number) n;
        } else if (n == null) {
            return null;
        }

        // 支持 String 类型转换
        String s = n.toString();
        return s.indexOf('.') != -1 ? Double.parseDouble(s) : Long.parseLong(s);
    }
}
