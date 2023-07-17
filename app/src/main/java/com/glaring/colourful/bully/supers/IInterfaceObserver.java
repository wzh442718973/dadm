package com.glaring.colourful.bully.supers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.FileDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 用于aidl接口或远程接口调用
 * public interface IMyAidlInterface extends IInterface {
 * public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException;
 * <p>
 * public com.glaring.colourful.bully.fusion.IMy getMy(java.lang.String name) throws android.os.RemoteException;
 * <p>
 * public static abstract class Stub extends Binder implements IMyAidlInterface {
 * private static final java.lang.String DESCRIPTOR = "com.glaring.colourful.bully.fusion.IMyAidlInterface";
 * public Stub() {
 * this.attachInterface(this, DESCRIPTOR);
 * }
 * <p>
 * public static IMyAidlInterface asInterface(IBinder obj) {
 * if ((obj == null)) {
 * return null;
 * }
 * android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
 * if (((iin != null) && (iin instanceof com.glaring.colourful.bully.fusion.IMyAidlInterface))) {
 * return ((com.glaring.colourful.bully.fusion.IMyAidlInterface) iin);
 * }
 * return new com.glaring.colourful.bully.fusion.IMyAidlInterface.Stub.Proxy(obj);
 * }
 *
 * @Override public android.os.IBinder asBinder() {
 * return this;
 * }
 * @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
 * java.lang.String descriptor = DESCRIPTOR;
 * switch (code) {
 * case INTERFACE_TRANSACTION: {
 * reply.writeString(descriptor);
 * return true;
 * }
 * case TRANSACTION_basicTypes: {
 * data.enforceInterface(descriptor);
 * int _arg0;
 * _arg0 = data.readInt();
 * long _arg1;
 * _arg1 = data.readLong();
 * boolean _arg2;
 * _arg2 = (0 != data.readInt());
 * float _arg3;
 * _arg3 = data.readFloat();
 * double _arg4;
 * _arg4 = data.readDouble();
 * java.lang.String _arg5;
 * _arg5 = data.readString();
 * this.basicTypes(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
 * reply.writeNoException();
 * return true;
 * }
 * case TRANSACTION_getMy: {
 * data.enforceInterface(descriptor);
 * java.lang.String _arg0;
 * _arg0 = data.readString();
 * com.glaring.colourful.bully.fusion.IMy _result = this.getMy(_arg0);
 * reply.writeNoException();
 * reply.writeStrongBinder((((_result != null)) ? (_result.asBinder()) : (null)));
 * return true;
 * }
 * default: {
 * return super.onTransact(code, data, reply, flags);
 * }
 * }
 * }
 * <p>
 * private static class Proxy implements com.glaring.colourful.bully.fusion.IMyAidlInterface {
 * private android.os.IBinder mRemote;
 * <p>
 * Proxy(android.os.IBinder remote) {
 * mRemote = remote;
 * }
 * @Override public android.os.IBinder asBinder() {
 * return mRemote;
 * }
 * <p>
 * public java.lang.String getInterfaceDescriptor() {
 * return DESCRIPTOR;
 * }
 * <p>
 * public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, java.lang.String aString) throws android.os.RemoteException {
 * android.os.Parcel _data = android.os.Parcel.obtain();
 * android.os.Parcel _reply = android.os.Parcel.obtain();
 * try {
 * _data.writeInterfaceToken(DESCRIPTOR);
 * _data.writeInt(anInt);
 * _data.writeLong(aLong);
 * _data.writeInt(((aBoolean) ? (1) : (0)));
 * _data.writeFloat(aFloat);
 * _data.writeDouble(aDouble);
 * _data.writeString(aString);
 * mRemote.transact(Stub.TRANSACTION_basicTypes, _data, _reply, 0);
 * _reply.readException();
 * } finally {
 * _reply.recycle();
 * _data.recycle();
 * }
 * }
 * @Override public com.glaring.colourful.bully.fusion.IMy getMy(java.lang.String name) throws android.os.RemoteException {
 * android.os.Parcel _data = android.os.Parcel.obtain();
 * android.os.Parcel _reply = android.os.Parcel.obtain();
 * com.glaring.colourful.bully.fusion.IMy _result;
 * try {
 * _data.writeInterfaceToken(DESCRIPTOR);
 * _data.writeString(name);
 * mRemote.transact(Stub.TRANSACTION_getMy, _data, _reply, 0);
 * _reply.readException();
 * _result = com.glaring.colourful.bully.fusion.IMy.Stub.asInterface(_reply.readStrongBinder());
 * } finally {
 * _reply.recycle();
 * _data.recycle();
 * }
 * return _result;
 * }
 * }
 * }
 * <p>
 * IInterfaceObserver， IConnect 返回主要以IBinder为主，接口主要用于代理处理
 * IProxy，IProvier返回主要及接口为主
 * IStub 主要以Binder为主，接口无用，主要 transact 接口处理
 */
public class IInterfaceObserver extends InterfaceObserver<IInterface> implements IBinder {

    @Override
    public IInterface queryLocalInterface(String descriptor) {
        return getProxy();
    }

    @Override
    public String getInterfaceDescriptor() throws RemoteException {
        return mIBinder.getInterfaceDescriptor();
    }

    @Override
    public boolean pingBinder() {
        return mIBinder.pingBinder();
    }

    @Override
    public boolean isBinderAlive() {
        return mIBinder.isBinderAlive();
    }

    @Override
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {
        mIBinder.dump(fd, args);
    }

    @Override
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
        mIBinder.dumpAsync(fd, args);
    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return mIBinder.transact(code, data, reply, flags);
    }

    @Override
    public void linkToDeath(DeathRecipient recipient, int flags) throws RemoteException {
        mIBinder.linkToDeath(recipient, flags);
    }

    @Override
    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return mIBinder.unlinkToDeath(recipient, flags);
    }

    /***
     * 原始的服务对象IBinder
     */
    private IBinder mIBinder;

    protected final IBinder asBinder() {
        return mIBinder;
    }

    //对于服务AIDL：asBinder获取到的值与注册到服务端列表的值应该是一样的(不一样将会被检测出来)
    //对于非服务AIDL：可以返回原始也可以返回自身,防止通过此值调用asInterface得到新的代理对象,绕过监管
    //默认返回的Binder中的BinderProxy类，其中mNativeData用来与底层类关联
    @ServiceInterface("asBinder")
    public Object _asBinder(Object source, Method method, Object[] args) throws Throwable {
        return this;
    }

    @Override
    public InterfaceObserver<IInterface> setSource(IInterface source) {
        this.mIBinder = source.asBinder();
        return super.setSource(source);
    }

    public InterfaceObserver<IInterface> setSource(IBinder source) {
        this.mIBinder = source;
        return this;
    }

    /**
     * 返回getProxy()，主要是接口匹配
     * 通过asBinder()返回self，再通过onTransact拦截处理
     */
    public static class IStub extends Binder implements InvocationHandler {

        protected Binder mBinder;

        public IStub() {
            super();
        }

        public void attach(AIDL aidl) {
            this.mAidl = aidl;
        }

        public AIDL getAidl() {
            return this.mAidl;
        }

        private AIDL mAidl;

        private Object mProxy;
        private Object mSource;

        public IStub(IInterfaceObserver observer, IInterface stub) {
            super();
            AIDL aidl = observer.getAidl().getAIDL(null, stub.asBinder());
            attach(aidl);
            mSource = stub;
            final RefClass mClass = new RefClass(stub.getClass());
            mProxy = Proxy.newProxyInstance(mClass.getClassLoader(), mClass.getInterfaces(true), this);
            if (stub instanceof Binder) {
                mBinder = (Binder) stub;
            }
        }

        public final <V> V getSource() {
            return (V) mSource;
        }

        public final <V> V getProxy() {
            return (V) mProxy;
        }


        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            /**
             * 注意：
             * 1. 处理返回的数据要在onTransact之后处理
             * 2. 处理参数需要再onTransact之前处理
             * 3. data只能读,写可能导致远端异常
             * 4. reply为当前返回的数据可读可写
             * 5. 写入之前记得 setDataPosition(0)
             * 6. 参数读取：enforceInterface(descriptor)， 读取参数;
             * 7。参数修改：writeInterfaceToken(mDescriptor)，写入参数;
             * 8. 返回值读取：readException(), 读取返回值
             * 9. 返回值修改：writeNoException(), 写入返回值(void不用调用)
             */
            return mBinder.transact(code, data, reply, flags);
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final String name = method.getName();
            if ("asBinder".equals(name)) {
                return this;
            } else {
                return null;
            }
        }
    }

    /**
     * 返回 getProxy()
     * 通过Proxy转发到映射函数上，或onInvoke
     * 因为传入的proxy已经是Proxy代理类，所有代码不会再执行queryLocalInterface()操作
     */
    public static class IProxy extends IInterfaceObserver {

        public IProxy() {
            super();
        }

        public IProxy(IInterfaceObserver observer, IInterface proxy) {
            super();
            AIDL aidl = observer.getAidl().getAIDL(null, proxy.asBinder());
            attach(aidl);
            setSource(proxy);
            makeProxy(proxy);
        }
    }

    /**
     * 返回 self，
     * 通过queryLocalInterface(), 返回getProxy()
     */
    public static class IConnection extends IInterfaceObserver {

        Intent mIntent;

        protected Intent getIntent(){
            return mIntent;
        }

        public void onConnectedBind(AIDL aidl, ComponentName name, IBinder service) {
            if (getProxy() == null) {
                IInterface source = aidl.newStub$Proxy(service, null);
                if (source != null) {
                    this.setSource(source);
                    this.makeProxy(aidl.getAidlClass());
                } else {
                    this.setSource(service);
                }
//                this.newProxy(aidl.newStub$Proxy(service), aidl.getAidlClass());
            }
        }

        public void onConnectedUnbind(ComponentName name) {

        }
    }

    /**
     * 不用返回，new IProvider()，时传入的是ContentProviderHolder类型对象，内部直接替换
     */
    public static class IProvider extends IInterfaceObserver {
        /***
         可以直接继承InterfaceObserver，但防止通过asBinder再通过ContentProviderProxy重新生成，所以继承此。这样保证无论怎么操作都使用自己的

         public class ContentProviderHolder implements Parcelable {
         public final ProviderInfo info;
         public IContentProvider provider;
         public IBinder connection;
         public boolean noReleaseNeeded;
         }
         */


        static final RefClass ContentProviderHolder;
        static final RefField provider;
        static final RefField info;

        static {
            RefClass Holder = RefClass.Get("android.app.IActivityManager$ContentProviderHolder");
            if (Holder.isNull()) {
                Holder = RefClass.Get("android.app.ContentProviderHolder");
            }
            ContentProviderHolder = Holder;
            provider = ContentProviderHolder.getField("provider");
            info = ContentProviderHolder.getField("info");
        }

        private final Object mProviderHolder;

        /**
         * @param providerHolder: ContentProviderHolder
         */
        public IProvider(Object providerHolder) {
            super();
            setName("provider");
            this.mProviderHolder = providerHolder;
            IInterface mProvider = provider.get(providerHolder, null);
            if (mProvider != null) {
                provider.set(providerHolder, setSource(mProvider).makeProxy(mProvider));
            } else if (provider.isNull()) {
                //先兼容测试
                iLog.e(providerHolder.getClass() + ":  provider is null");
            } else {
                ProviderInfo _info = info.get(providerHolder, null);
                if (_info != null) {
                    iLog.e(providerHolder.getClass() + "： info is null");
                }
            }
        }

        public Object getProviderHolder() {
            return mProviderHolder;
        }
    }

}
