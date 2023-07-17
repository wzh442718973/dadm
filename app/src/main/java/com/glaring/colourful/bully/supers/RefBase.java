package com.glaring.colourful.bully.supers;

import java.lang.reflect.Member;

/**
 * Created by wzh on 2017/2/9.
 *
 * @hide
 */
abstract class RefBase<V> {

    static boolean DBG_LOG = false;

    protected Class mClszz;

    protected final V mValue;

    @Override
    public String toString() {
        return mValue == null ? "null" : mValue.toString();
    }

    protected RefBase(Class clszz, V value) {
        this.mClszz = clszz;
        this.mValue = value;
    }

    public final Class Class() {
        return this.mClszz;
    }

    public final V get() {
        return mValue;
    }

    public ClassLoader getClassLoader() {
        return this.mClszz == null ? null : this.mClszz.getClassLoader();
    }

    public final boolean isNull() {
        return mValue == null;
    }

    public final String getName() {
        if (mValue == null) {
            return "null";
        } else if (mValue instanceof Member) {
            return ((Member) mValue).getName();
        } else if (mValue instanceof Class) {
            return ((Class) mValue).getName();
        } else {
            return mValue.getClass().getName();
        }
    }
}
