package com.glaring.colourful.bully.ad;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public final class XObject {

    private static final Set<Class> BASE_CLASS = new HashSet<>();

    static {
        BASE_CLASS.add(Boolean.class);
        BASE_CLASS.add(Number.class);
        BASE_CLASS.add(Character.class);
        BASE_CLASS.add(CharSequence.class);
        BASE_CLASS.add(File.class);
        BASE_CLASS.add(Constructor.class);
        BASE_CLASS.add(Field.class);
        BASE_CLASS.add(Method.class);
        BASE_CLASS.add(Class.class);
        BASE_CLASS.add(boolean.class);
        BASE_CLASS.add(byte.class);
        BASE_CLASS.add(char.class);
        BASE_CLASS.add(short.class);
        BASE_CLASS.add(int.class);
        BASE_CLASS.add(long.class);
        BASE_CLASS.add(float.class);
        BASE_CLASS.add(double.class);
    }


    public static boolean Equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean IsEmpty(CharSequence str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }

    public static final int index(Object[] args, Class clszz, int start) {
        if (args == null || clszz == null) {
            return -1;
        }
        final int count = args.length;
        for (int i = start < 0 ? 0 : start; i < count; ++i) {
            if (args[i] != null && clszz.isInstance(args[i])) {
                return i;
            }
        }
        return -1;
    }

    public static final int lastIndex(Object[] args, Class clszz, int start) {
        if (args == null || clszz == null) {
            return -1;
        }
        final int count = args.length;
        for (int i = start < 0 ? (count - 1) : start; i >= 0; --i) {
            if (args[i] != null && clszz.isInstance(args[i])) {
                return i;
            }
        }
        return -1;
    }


    public static final int index(Object[] args, Class clszz) {
        return index(args, clszz, 0);
    }

    public static final int lastIndex(Object[] args, Class clszz) {
        return lastIndex(args, clszz, -1);
    }

    public static IDump dump(Object obj) {
        return new IDump.MAP(obj, 1, 1);
    }

    public static IDump dump(Object obj, int superDepth, int depth) {
        return new IDump.MAP(obj, superDepth, depth);
    }

    public static boolean baseObject(Object object) {
        if (object == null) {
            return true;
        }
        for (Class clazz : BASE_CLASS) {
            if (clazz.isInstance(object)) {
                return true;
            }
        }
        return false;
    }

    public static boolean baseClass(Class clazz) {
        if (clazz == null) {
            return true;
        }
        if (clazz.isInterface()) {
            return true;
        }
        for (Class cls : BASE_CLASS) {
            if (cls.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
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
}
