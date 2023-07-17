package com.glaring.colourful.bully.supers;

import static java.lang.reflect.Proxy.isProxyClass;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.glaring.colourful.bully.base.XObject;

/**
 * 得到aidl的信息
 * 通过获取aidl描述获取Stub.asInterface方法或者Stub$Proxy类
 */
public final class AIDL {
    public AIDL getAIDL(String descriptor) {
        AIDL aidl = GetAIDL(descriptor);
        if (aidl != null) {
            aidl.mLoaders.addAll(this.mLoaders);
        }
        return aidl;
    }

    public AIDL getAIDL(String serviceName, IBinder binder) {
        AIDL aidl = GetAIDL(serviceName, binder);
        if (aidl != null) {
            aidl.mLoaders.addAll(this.mLoaders);
        }
        return aidl;
    }

    /**
     * aidl的描述字符串,aidl类名称
     */
    public final String mDescriptor;

    @Override
    public String toString() {
        return mDescriptor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof AIDL)) {
            return false;
        }
        return XObject.Equals(mDescriptor, ((AIDL) obj).mDescriptor);
    }


    @Override
    public int hashCode() {
        return mDescriptor.hashCode();
    }

    //一般为服务名称或描述
    private String mName;

    //描述当前aidl接口的加载器
    private ArrayList<ClassLoader> mLoaders = new ArrayList<>();

    /**
     * aidl描述文件的class
     */
    private Class mAidlClass;

    private Class mStubClass;

    private Class mProxyClass;


//    /**
//     * AIDL$Stub.asInterface方法生成的代理对象或以前生成好的
//     */ 同一个服务可能多个绑定，所以这个aidl不能保存
//    private IInterface aidlObject;

    /**
     * AIDL$Stub.asInterface()
     * 查找到类似的函数就不找proxyClass
     */
    private Method asInterface;

    /**
     * asInterface没有找到就继续查找代理的类, 直接new Class(IBinder)
     */
    private Constructor newPorxy;

    /**
     * 1. aidlObject
     * 2. asInterface
     * 3. Stub$Porxy
     * 通过这个优先级获取
     *
     * @param descriptor
     */
    public AIDL(String descriptor) {
        this.mDescriptor = descriptor;
    }

    public AIDL setName(String name) {
        this.mName = name;
        return this;
    }

    public AIDL addClassLoader(ClassLoader loader) {
        if (loader != null) {
            for (ClassLoader load : mLoaders) {
                if (load == loader) {
                    return this;
                }
            }
            this.mLoaders.add(loader);
        }
        return this;
    }

    public ClassLoader[] getClassLoader() {
        return this.mLoaders.toArray(new ClassLoader[0]);
    }

    public Class forName(String clsName) {
        for (ClassLoader loader : mLoaders) {
            Class clszz = RefClass.forName(clsName, loader);
            if (clszz != null) {
                return clszz;
            }
        }
        return null;
    }

    /**
     * 接口的Proxy代理对象
     *
     * @param aidlObject
     * @return
     */
    public AIDL setAidlObject(IInterface aidlObject) {
        if (aidlObject != null && this.mAidlClass == null) {
            Class[] interfacess = aidlObject.getClass().getInterfaces();
            for (Class clazz : interfacess) {
                if (mDescriptor.equals(clazz.getName())) {
                    this.mAidlClass = clazz;
                    break;
                }
            }
        }
        return this;
    }

    /**
     * aidl接口类
     * 理论上mDescriptor就是aidl的类名，但不一定全是
     *
     * @param aidlClass
     * @return
     */
    public AIDL setAidlClass(Class aidlClass) {
        this.mAidlClass = aidlClass;
        return this;
    }

    public AIDL setAidlClass(String clsName) {
        this.mAidlClass = forName(clsName);
        return this;
    }

    public AIDL setStubClass(Class stubClass) {
        this.mStubClass = stubClass;
        return this;
    }

    public AIDL setStubClass(String clsName) {
        this.mStubClass = forName(clsName);
        return this;
    }

    public Class getStubClass() {
        return this.mStubClass;
    }

    public AIDL setProxyClass(Class proxyClass) {
        this.mProxyClass = proxyClass;
        return this;
    }

    public AIDL setProxyClass(String clsName) {
        this.mProxyClass = forName(clsName);
        return this;
    }

    public Class getProxyClass() {
        return this.mProxyClass;
    }


    /**
     * asInterface的接口
     *
     * @param asInterface
     * @return
     */
    public AIDL setAsInterface(Method asInterface) {
        this.asInterface = asInterface;
        return this;
    }

    private final void initInterface() {
        if (mAidlClass == null) {
            return;
        }
        if (asInterface == null && newPorxy == null) {
            if (mStubClass != null) {
                if (null != (asInterface = findAsInterface(mStubClass, mAidlClass))) {
                    return;
                }
            }
            if (mProxyClass != null) {
                try {
                    if (null != (newPorxy = mProxyClass.getDeclaredConstructor(IBinder.class))) {
                        return;
                    }
                } catch (Throwable e) {
                    mProxyClass = null; //没有所以无用，置空
                }
            }

            //1. 标准aidl文件生成的获取方式
            RefClass Stub = new RefClass(this.mAidlClass).getSubClass("Stub");
            if (null != (asInterface = findAsInterface(Stub.get(), this.mAidlClass))) {
                return;
            }

            //针对stub也被混淆了，查找子类
            final Class[] subClasss = this.mAidlClass.getDeclaredClasses();
            for (Class subClass : subClasss) {
                if (!(subClass.isAssignableFrom(this.mAidlClass))) {
                    continue;
                }
                try {
                    this.newPorxy = subClass.getDeclaredConstructor(IBinder.class);
                    this.mProxyClass = subClass;
                    return;
                } catch (Throwable e) {
                    if (null != (asInterface = findAsInterface(subClass, this.mAidlClass))) {
                        this.mStubClass = subClass;
                        return;
                    }
                }
            }

            //针对stub及proxy类都被优化到同级中，目前没有接口支持
        }
    }

    private final AtomicBoolean mFirst = new AtomicBoolean(true);

    public Class getAidlClass() {
        if (mAidlClass == null) {
            setAidlClass(mDescriptor);
        }
        return mAidlClass;
    }

    public <V extends IInterface> V newStub$Proxy(IBinder binder, V srcProxy) {
        try {
            getAidlClass();
            initInterface();
            if (srcProxy != null) {
                return srcProxy;
            }

            if (this.mAidlClass == null || binder == null) {
                return null;
            }
            if (isProxyClass(binder.getClass())) {
                return (V) binder;
            }

            if (this.mAidlClass.isInstance(binder)) {
                return (V) binder;
            }

            if (asInterface != null) {
                try {
                    asInterface.setAccessible(true);
                    return (V) asInterface.invoke(null, binder);
                } finally {
                    asInterface.setAccessible(false);
                }
            }

            if (newPorxy != null) {
                try {
                    newPorxy.setAccessible(true);
                    return (V) newPorxy.newInstance(binder);
                } finally {
                    newPorxy.setAccessible(false);
                }
            }
        } catch (Throwable e) {
            iLog.e(mDescriptor + ".newStub$Proxy", e);
        }
        return null;
    }


    private static final Method findAsInterface(Class Stub, Class... aidlClass) {
        if (Stub != null) {
            try {
                Method as = Stub.getMethod("asInterface", IBinder.class);
                return as; //存在不会报异常
            } catch (Throwable e) {

            }
            final Method[] methods = Stub.getDeclaredMethods();
            for (Method method : methods) {
                if (Modifier.isStatic(method.getModifiers())) {
                    Class[] params = method.getParameterTypes();
                    Class resType = method.getReturnType();
                    if (params.length == 1 && IBinder.class == params[0]) {
                        for (Class clszz : aidlClass) {
                            if (clszz.isAssignableFrom(resType)) {
                                return method;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static final HashMap<String, AIDL> mAIDLs = new HashMap<>();

    public static AIDL GetAIDL(String descriptor) {
        if (descriptor != null && descriptor.length() > 0) {
            synchronized (mAIDLs) {
                AIDL info = mAIDLs.get(descriptor);
                if (info == null) {
                    mAIDLs.put(descriptor, info = new AIDL(descriptor));
                }
                return info;
            }
        }
        return null;
    }

    public static AIDL GetAIDL(String serviceName, IBinder binder) {
        try {
            AIDL info = AIDL.GetAIDL(binder.getInterfaceDescriptor());
            if (serviceName == null) {
                serviceName = info.mDescriptor;
            }
            return info.setName(serviceName);
        } catch (Throwable e) {

        }
        return null;
    }
}
