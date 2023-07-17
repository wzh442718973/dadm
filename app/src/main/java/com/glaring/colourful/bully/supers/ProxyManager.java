package com.glaring.colourful.bully.supers;

import static com.glaring.colourful.bully.supers.IInterfaceObserver.IConnection;
import static com.glaring.colourful.bully.supers.IInterfaceObserver.IProvider;
import static com.glaring.colourful.bully.supers.MatchManager.Match;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.lang.ref.WeakReference;

import com.glaring.colourful.bully.base.XObject;

public final class ProxyManager {

    static final RefClass LoadedApk = RefClass.Get("android.app.LoadedApk");

    static final RefClass ServiceDispatcher = LoadedApk.getSubClass("ServiceDispatcher");
    static final RefField ServiceDispatcher_mConnection = ServiceDispatcher.getField("mConnection");
    static final RefField ServiceDispatcher_mContext = ServiceDispatcher.getField("mContext");
    static final RefField ServiceDispatcher_mActivityThread = ServiceDispatcher.getField("mActivityThread");


    static final RefClass InnerConnection = ServiceDispatcher.getSubClass("InnerConnection");
    static final RefField InnerConnection_mDispatcher = InnerConnection.getField("mDispatcher");

    static final ProxyManager mManager = new ProxyManager();


    private static boolean EqualsIntent(Intent src, Intent tag) {
        if (src == null || tag == null) {
            return false;
        }
        return XObject.Equals(src.getAction(), tag.getAction()) &&
                XObject.Equals(src.getComponent(), tag.getComponent()) &&
                XObject.Equals(src.getData(), tag.getData());
    }

    private static boolean MatchIntent(Intent src, Intent tag) {
        if (src == null || tag == null) {
            return false;
        }
        final int comp = src.getComponent() == null ? 0 : (src.getComponent().equals(tag.getComponent()) ? 1 : -10);
        final int data = src.getData() == null ? 0 : (src.getData().equals(tag.getData()) ? 1 : -10);
        final int action = src.getAction() == null ? 0 : (src.getAction().equals(tag.getAction()) ? 1 : -10);

        return ((comp + data + action) > 0);
    }

    private static final class ConnectionProxy implements ServiceConnection {
        private final Class<? extends IConnection> mClass;
        private final Object mInnerConnection;

        private Context mContext;
        private Intent  mIntent;
        private ServiceConnection mConnection;

        private IConnection mObserver;

        ConnectionProxy(Class<? extends IConnection> clszz, Object inner) {
            this.mClass = clszz;
            this.mInnerConnection = inner;
        }

        void attach(Context context, Intent intent, ServiceConnection connection) {
            this.mContext = context;
            this.mIntent = intent;
            this.mConnection = connection;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //这里做替换,然后重新周流程
            if (mObserver == null) {
                try {
                    mObserver = RefConstructor.Get(this.mClass).newInstanceThrows();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                mObserver.mIntent = new Intent(mIntent);
                AIDL aidl = AIDL.GetAIDL(name.toShortString(), service);
                iLog.e(this.mClass.getSimpleName() + ".onServiceConnected: " + mIntent + " >> " + name + " >> " + aidl);
                if (aidl != null) {
                    mObserver.attach(aidl);
                    if (mContext != null) {
                        aidl.addClassLoader(mContext.getClassLoader());
                    }
                    aidl.addClassLoader(mConnection.getClass().getClassLoader());
                    aidl.addClassLoader(ConnectionProxy.class.getClassLoader());

                    mObserver.onConnectedBind(aidl, name, service);
                }
            }
            mConnection.onServiceConnected(name, mObserver);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mObserver.onConnectedUnbind(name);
            mConnection.onServiceDisconnected(name);

        }
    }

    private static final class ConnectionMatch extends Match<Intent, IConnection, ConnectionProxy> {


        public ConnectionMatch(Intent intent, Class<? extends IConnection> clszz) {
            super(intent, clszz);
        }

        @Override
        public ConnectionProxy newObject(Object inner) {
            return new ConnectionProxy(mClszz, inner);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof ConnectionMatch) {
                return MatchIntent(mKey, ((ConnectionMatch) obj).mKey);
            } else if (obj instanceof Intent) {
                return MatchIntent(mKey, (Intent) obj);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return mKey.hashCode();
        }
    }

    private static final class ProviderMatch extends Match<String, IProvider, IProvider> {

        public ProviderMatch(String s, Class<? extends IProvider> clszz) {
            super(s, clszz);
        }

        @Override
        public IProvider newObject(Object providerHolder) {
            return RefConstructor.Get(this.mClszz, Object.class).newInstance(providerHolder);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj instanceof ProviderMatch) {
                return XObject.Equals(mKey, ((ProviderMatch) obj).mKey);
            } else if (obj instanceof String) {
                return XObject.Equals(mKey, (String) obj);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return mKey.hashCode();
        }
    }

    private static final MatchManager mConnectionMananger = new MatchManager();
    private static final MatchManager mProviderMananger = new MatchManager();
//
//    /**
//     * 代理信息
//     */
//    private static final class Proxy {
//
//        private final Intent mIntent;
//        private final Class<? extends IConnection> clsProxy;
//        private final HashMap<Object, ConnectionProxy> mConnections = new HashMap<>();
//
//        public Proxy(Intent intent, Class<? extends IConnection> proxy) {
//            this.mIntent = intent;
//            this.clsProxy = proxy;
//        }
//
//        public ConnectionProxy getConnection(Object inner) {
//            synchronized (mConnections) {
//                ConnectionProxy conn = mConnections.get(inner);
//                if (conn == null) {
//                    mConnections.put(inner, conn = new ConnectionProxy(this, inner));
//                }
//                return conn;
//            }
//        }
//
//        public void delConnection(Object inner) {
//            synchronized (mConnections) {
//                mConnections.remove(inner);
//            }
//        }
//
//        @Override
//        public String toString() {
//            return mIntent.toString();
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null) return false;
//            if (obj == this) return true;
//            if (obj instanceof Proxy) {
//                if (equalsIntent(mIntent, ((Proxy) obj).mIntent)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        @Override
//        public int hashCode() {
//            return mIntent.hashCode();
//        }
//    }
//
//
//    private static class ProxyManager {
//        private final ArrayList<Proxy> mProxys = new ArrayList<>();
//
//        public final int size() {
//            return mProxys.size();
//        }
//
//        private final Proxy _getProxy(Intent intent) {
//            for (Proxy proxy : mProxys) {
//                if (equalsIntent(proxy.mIntent, intent)) {
//                    return proxy;
//                }
//            }
//            return null;
//        }
//
//
//        private final Proxy findProxy(Intent intent) {
//            if (intent != null) {
//                return _getProxy(intent);
//            }
//            return null;
//        }
//
//        public final void addProxy(Proxy proxy) {
//            if (proxy != null) {
//                if (null == _getProxy(proxy.mIntent)) {
//                    mProxys.add(proxy);
//                } else {
//                    Log.e("andy", "proxy is exist!");
//                }
//            }
//        }
//    }


    public static void registerConnection(Intent intent, Class<? extends IConnection> serviceProxy) {
        mConnectionMananger.add(new ConnectionMatch(intent, serviceProxy));
    }

    public static void registerProvider(String providerName, Class<? extends IProvider> iprovider) {
        mProviderMananger.add(new ProviderMatch(providerName, iprovider));
    }

    public static void matchProviders(String providerName, Object providerHolder) {
        if (providerHolder != null && IProvider.ContentProviderHolder.isInstance(providerHolder)) {
            iLog.e("-------------------" + providerName + "-------------------------------");
            throw new RuntimeException("matchProviders");
//            ProviderMatch match = mProviderMananger.find(providerName);
//            if (match != null) {
//                match.newObject(providerHolder);
//            }
//            new IProvider(providerHolder);
        }
    }

    public static boolean matchConnections(Object[] args) {
        if (mConnectionMananger.size() > 0) {
            int idy = XObject.index(args, Intent.class);
            int idx = XObject.lastIndex(args, InnerConnection.get());
            if (idx >= 0 && idy >= 0) {
                //查找匹配的
                final Match match = mConnectionMananger.find((Intent) args[idy]);
                if (match != null) {
                    WeakReference<Object> _mDispatcher = InnerConnection_mDispatcher.get(args[idx], null);
                    Object mDispatcher = _mDispatcher == null ? null : _mDispatcher.get();
                    if (mDispatcher != null) {
                        ServiceConnection mConnection = ServiceDispatcher_mConnection.get(mDispatcher, null);
                        Context mContext = ServiceDispatcher_mContext.get(mDispatcher, null);
                        if (!(mConnection instanceof ConnectionProxy)) {
                            final ConnectionProxy conn = (ConnectionProxy) match.newObject(args[idx]);
                            ServiceDispatcher_mConnection.set(mDispatcher, conn);
                            conn.attach(mContext, (Intent) args[idy], mConnection);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
