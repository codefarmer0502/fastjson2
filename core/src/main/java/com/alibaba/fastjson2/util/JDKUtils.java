package com.alibaba.fastjson2.util;

import javax.sql.DataSource;
import javax.sql.RowSet;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteOrder;

public class JDKUtils {
    public static final long FIELD_DECIMAL_INT_COMPACT_OFFSET;
    public static final boolean BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    public static final boolean UNSAFE_SUPPORT = true;

    static {
        long fieldOffset = -1;
        try {
            Field field = BigDecimal.class.getDeclaredField("intCompact");
            fieldOffset = UnsafeUtils.objectFieldOffset(field);
        } catch (Throwable ignored) {
            // ignored
        }
        FIELD_DECIMAL_INT_COMPACT_OFFSET = fieldOffset;
    }

    public static boolean isSQLDataSourceOrRowSet(Class<?> type) {
        return DataSource.class.isAssignableFrom(type)
                || RowSet.class.isAssignableFrom(type);
    }

    public static char[] getCharArray(String str) {
        return str.toCharArray();
    }
}
