package com.glaring.colourful.bully.supers;

import java.lang.reflect.Constructor;

/**
 * Created by wzh on 2017/2/9.
 */
public final class RefConstructor extends RefBase<Constructor> {

    public static RefConstructor Get(Class clszz, Class... params) {
        Constructor ctor = null;
        try {
            ctor = clszz == null ? null : clszz.getDeclaredConstructor(params);
        } catch (Throwable e) {
            if (DBG_LOG) System.out.print("Not Found Constructor!< " + clszz + ".new" + " >\n");
        }
        return new RefConstructor(clszz, ctor);
    }

    public RefConstructor(Class clszz, Constructor<?> ctor) {
        super(clszz, ctor);
    }

    public final <V> V newInstanceThrows(Object... params) throws Throwable {
        Constructor ctor = mValue;
        if (ctor != null) {
            try {
                ctor.setAccessible(true);
                return (V) ctor.newInstance(params);
            } catch (Throwable e) {
                throw e.getCause();
            } finally {
                ctor.setAccessible(false);
            }
        }
        return null;
    }

    public final <V> V newInstance(Object... params) {
        try {
            return newInstanceThrows(params);
        } catch (Throwable e) {
            if (DBG_LOG) System.out.println("throwable.newInstance:" + e.getMessage() + "\n");
        }
        return null;
    }
}