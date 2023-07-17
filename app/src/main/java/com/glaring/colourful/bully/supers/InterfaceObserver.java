package com.glaring.colourful.bully.supers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;

/***
 * 用于普通的接口代理
 * @param <T>
 */
public abstract class InterfaceObserver<T> implements InvocationHandler {
    private final HashMap<String, Method> mMethods = new HashMap<>();
    private static final HashSet<String> globalFilters = new HashSet<>();

    static {
        globalFilters.add("toString");
        globalFilters.add("hashCode");
        globalFilters.add("equals");
    }

    //
    private String get_name(String name, Class clszz) {
        return name;
    }

    protected InterfaceObserver() {
        setName(this.getClass().getSimpleName());

        Class clszz = getClass();
        while (clszz != null) {
            //有的在父类注解Hook函数,子类重载父类一些处理方法
            Method[] methods = clszz.getDeclaredMethods();
            for (Method method : methods) {
                ServiceInterface serviceInterface = method.getAnnotation(ServiceInterface.class);
                if (serviceInterface != null) {
                    if (!Modifier.isPublic(method.getModifiers())) {
                        method.setAccessible(true);
                    }
                    //子类可以覆盖父类的接口
                    final String name = get_name(serviceInterface.value(), clszz);
                    if (!mMethods.containsKey(name)) {
                        mMethods.put(name, method);
                    }
                }
            }
            clszz = clszz.getSuperclass();
        }
    }

    public void attach(AIDL aidl) {
        this.mAidl = aidl;
    }

    public AIDL getAidl() {
        return this.mAidl;
    }

    private AIDL mAidl;

    /**
     * Observer.name
     */
    private String mName;
    /**
     * 原始调用的对象
     */
    private T mSource;
    /***
     * 通过接口生成的代理对象
     */
    private T mProxy;

    public final Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        try {
            final String methodName = method.getName();
            final Method hookMethod = mMethods.get(methodName);
            if (hookMethod != null) {
                return hookMethod.invoke(this, mSource, method, objects);
            } else if (globalFilters.contains(methodName)) {
                return method.invoke(mSource, objects);
            } else {
                return onInvoke(mSource, method, objects);
            }
        } catch (InvocationTargetException e) {
            iLog.e(mName + ".invoke", e);
            throw e.getCause(); //调用的异常进行的封装，直接抛出
        } catch (Throwable e) {
            iLog.e(mName + ".invoke", e);
            throw e.getCause();
        }
    }

    public final String getName() {
        return mName;
    }

    /**
     * @param name
     * @hide
     */
    protected final InterfaceObserver<T> setName(String name) {
        this.mName = name;
        return this;
    }

    public final <V extends T> V getSource() {
        return (V) mSource;
    }

    public final <V extends T> V getProxy() {
        return (V) mProxy;
    }

    public final Object callSource(Method method, Object... objects) throws Throwable {
        try {
            return method.invoke(mSource, objects);
        } catch (InvocationTargetException e) {
            iLog.e(mName + ".invoke", e);
            throw e.getCause(); //调用的异常进行的封装，直接抛出
        } catch (Throwable e) {
            iLog.e(mName + ".invoke", e);
            throw e.getCause();
        }
    }

    public final Method findMethod(String methodName) {
        if (mSource != null) {
            Method[] methods = mSource.getClass().getMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    return method;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (mName != null) {
            return mName;
        } else if (mAidl != null) {
            return mAidl.toString();
        } else {
            return super.toString();
        }
    }

    public InterfaceObserver<T> setSource(T source) {
        if (source == null) {
            throw new NullPointerException("source is null!");
        }
        this.mSource = source;
        return this;
    }

    public T makeProxy(T proxyClassObject) {
        return makeProxy(proxyClassObject.getClass());
    }

    public T makeProxy(Class proxyClass) {
        if (mProxy == null) {
            if (proxyClass == null) {
                throw new NullPointerException("interfaces Class is null!");
            }
            final RefClass mClass = new RefClass(proxyClass);
            mProxy = (T) Proxy.newProxyInstance(proxyClass.getClassLoader(), mClass.getInterfaces(true), this);
        }
        return mProxy;
    }


    /**
     * 拦截之后的处理,可以在此打印所有函数是否安装规则过滤
     *
     * @param source
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        return method.invoke(source, args);
    }
}
