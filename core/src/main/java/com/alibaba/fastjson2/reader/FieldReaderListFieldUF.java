package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.UnsafeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Locale;

import static com.alibaba.fastjson2.util.UnsafeUtils.UNSAFE;

final class FieldReaderListFieldUF<T>
        extends FieldReaderList<T, Object> {
    final long fieldOffset;

    FieldReaderListFieldUF(
            String fieldName,
            Type fieldType,
            Class fieldClass,
            Type itemType,
            Class itemClass,
            int ordinal,
            long features,
            String format,
            Locale locale,
            Collection defaultValue,
            Field field) {
        super(
                fieldName,
                fieldType,
                fieldClass,
                itemType,
                itemClass,
                ordinal,
                features,
                format,
                locale,
                defaultValue,
                null,
                field,
                null
        );
        fieldOffset = UnsafeUtils.objectFieldOffset(field);
    }

    @Override
    public void accept(Object object, Object value) {
        UNSAFE.putObject(object, fieldOffset, value);
    }
}
