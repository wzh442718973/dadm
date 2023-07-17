package com.glaring.colourful.bully.ad;

import java.lang.reflect.Method;

/**
 * Created by wzh on 17-12-13.
 */

public final class DEBUG {
    public static String dumpClassLoader(ClassLoader loader) {
        StringBuffer sb = new StringBuffer("dump.ClassLoader\n");
        while (loader != null) {
            sb.append(loader.getClass().getName()).append("\n");
            loader = loader.getParent();
        }
        return sb.toString();
    }

    public static String dumpCall(Method method, Object[] args){
        return dumpCall(method, args, null, false);
    }

    public static String dumpCall(Method method, Object[] args, Object result, boolean detail) {
        StringBuffer sb = new StringBuffer();
        sb.append(method.getDeclaringClass().getName()).append(".").append(method.getName());
        sb.append("(");

        Class[] params = method.getParameterTypes();
        Class reType = method.getReturnType();
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                if (detail) {
                    sb.append(params[i].getSimpleName()).append("=");
                }
                sb.append(args[i] == null ? "null" : args[i]);
            }
        }
        sb.append(")");
        if (detail) {
            sb.append(reType.getSimpleName()).append(":");
        }
        sb.append(result == null ? "null" : result);
        return sb.toString();
    }

    public static String dumpCaller(int depth) {
        return dumpCaller(depth, 1);
    }

    public static String dumpCaller(int depth, int startPos) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        int start = 3 + startPos;
        int end = Math.min(start + depth, elements.length);
        for (; start < end; ++start) {
            StackTraceElement caller = elements[start];
            sb.append(caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getFileName() + "(" + caller.getLineNumber() + ")" + "\n");
        }
        return sb.toString();
    }

    private static final String GAP_SIZE = "  ";
}
