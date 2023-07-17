package com.glaring.colourful.bully.mo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by wzh on 2017/2/9.
 */
public final class RefMethod extends RefBase<Method> {
    public static RefMethod Get(Class clszz, String methodName, Class... params) {
        Method method = null;
        try {
            method = clszz == null ? null : clszz.getDeclaredMethod(methodName, params);
        } catch (Throwable e) {
            if (DBG_LOG)
                System.out.print("Not Found Method!< " + clszz + "." + methodName + " >\n");
        }
        return new RefMethod(clszz, method);
    }

    public RefMethod(Class clszz, Method method) {
        super(clszz, method);
    }

    public Class<?>[] paramList() {
        return mValue == null ? null : mValue.getParameterTypes();
    }


    /**
     * invoke method
     *
     * @param object (Static->null)
     * @param args
     * @param <V>
     * @return
     */
    public final <V> V callThrow(Object object, Object... args) throws Throwable {
        final Method method = mValue;
        if (method != null) {
            try {
                method.setAccessible(true);
                if (Modifier.isStatic(method.getModifiers())) {
                    object = null;
                } else if (object == null) {
                    throw new NullPointerException("Method call object is null!");
                }
                return (V) method.invoke(object, args);
            } catch (Throwable e) {
                throw e.getCause();
            } finally {
                method.setAccessible(false);
            }
        }
        return null;
    }


    /**
     * invoke method
     *
     * @param object (Static->null)
     * @param args
     * @param <V>
     * @return
     */
    public final <V> V call(Object object, Object... args) {
        try {
            return callThrow(object, args);
        } catch (Throwable e) {
            if (DBG_LOG) System.out.println("method.invoke:" + e.getMessage() + "\n");
        }
        return null;
    }
}