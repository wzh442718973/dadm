package com.glaring.colourful.bully.supers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by wzh on 2017/2/9.
 */

public final class RefField extends RefBase<Field> {
    public static RefField Get(Class clszz, String fieldName) {
        Field field = null;
        try {
            field = clszz == null ? null : clszz.getDeclaredField(fieldName);
        } catch (Throwable e) {
            if (DBG_LOG) System.out.print("Not Found Field!< " + clszz + "." + fieldName + " >\n");
        }
        return new RefField(clszz, field);
    }

    public RefField(Class clszz, Field field) {
        super(clszz, field);
    }

    public final Class<?> type() {
        return mValue == null ? null : mValue.getType();
    }

    /**
     * get value
     *
     * @param object (Static->null)
     * @param def
     * @param <V>
     * @return
     */
    public final <V> V get(Object object, V def) {
        Object value = def;
        try {
            final Field field = mValue;
            if (field != null) {
                try {
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers())) {
                        object = null;
                    } else if (object == null) {
                        throw new NullPointerException("object is null!");
                    }

                    if (boolean.class.isInstance(def)) {
                        value = field.getBoolean(object);
                    } else if (byte.class.isInstance(def)) {
                        value = field.getByte(object);
                    } else if (char.class.isInstance(def)) {
                        value = field.getChar(object);
                    } else if (short.class.isInstance(def)) {
                        value = field.getShort(object);
                    } else if (int.class.isInstance(def)) {
                        value = field.getInt(object);
                    } else if (long.class.isInstance(def)) {
                        value = field.getLong(object);
                    } else if (float.class.isInstance(def)) {
                        value = field.getFloat(object);
                    } else if (double.class.isInstance(def)) {
                        value = field.getDouble(object);
                    } else {
                        value = field.get(object);
                    }
                } finally {
                    field.setAccessible(false);
                }
            }
        } catch (Throwable e) {
            System.out.println("field.get:" + e.getMessage() + "\n");
        }
        return (V) value;
    }

    /**
     * set value
     *
     * @param object (Static->null)
     * @param value
     * @param <V>
     */
    public final <V> void set(Object object, V value) {
        try {
            Object _value = value;
            final Field field = mValue;
            if (field != null) {
                try {
                    field.setAccessible(true);
                    if (Modifier.isStatic(field.getModifiers())) {
                        object = null;
                    } else if (object == null) {
                        throw new NullPointerException("object is null!");
                    }
                    if (boolean.class.isInstance(value)) {
                        field.setBoolean(object, (boolean) _value);
                    } else if (byte.class.isInstance(value)) {
                        field.setByte(object, (byte) _value);
                    } else if (char.class.isInstance(value)) {
                        field.setChar(object, (char) _value);
                    } else if (short.class.isInstance(value)) {
                        field.setShort(object, (short) _value);
                    } else if (int.class.isInstance(value)) {
                        field.setInt(object, (int) _value);
                    } else if (long.class.isInstance(value)) {
                        field.setLong(object, (long) _value);
                    } else if (float.class.isInstance(value)) {
                        field.setFloat(object, (float) _value);
                    } else if (double.class.isInstance(value)) {
                        field.setDouble(object, (double) _value);
                    } else {
                        field.set(object, value);
                    }
                } finally {
                    field.setAccessible(false);
                }
            }
        } catch (Throwable e) {
            System.out.println("field.set:" + e.getMessage() + "\n");
        }
    }
}
