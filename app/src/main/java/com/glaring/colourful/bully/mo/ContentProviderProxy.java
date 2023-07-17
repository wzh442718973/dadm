package com.glaring.colourful.bully.mo;//package com.glaring.colourful.bully.monitor;
//
//import android.content.pm.ProviderInfo;
//import android.os.IInterface;
//import android.util.Log;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//
//import com.glaring.colourful.bully.reflection.RefClass;
//import com.glaring.colourful.bully.reflection.RefField;
//
//public class ContentProviderProxy extends IInterfaceObserver {
//    /***
//     可以直接继承InterfaceObserver，但防止通过asBinder再通过ContentProviderProxy重新生成，所以继承此。这样保证无论怎么操作都使用自己的
//
//     public class ContentProviderHolder implements Parcelable {
//     public final ProviderInfo info;
//     public IContentProvider provider;
//     public IBinder connection;
//     public boolean noReleaseNeeded;
//     }
//     */
//
//
//    static final RefClass ContentProviderHolder;
//    static final RefField provider;
//    static final RefField info;
//
//    static {
//        RefClass Holder = RefClass.Get("android.app.IActivityManager$ContentProviderHolder");
//        if (Holder.isNull()) {
//            Holder = RefClass.Get("android.app.ContentProviderHolder");
//        }
//        ContentProviderHolder = Holder;
//        provider = ContentProviderHolder.getField("provider");
//        info = ContentProviderHolder.getField("info");
//    }
//
//    private final Object mProviderHolder;
//
//    public static ContentProviderProxy buildProxy(Object providerHolder) {
//        if (providerHolder != null) {
//            IInterface mProvider = provider.get(providerHolder, null);
//            if (mProvider != null) {
//                return new ContentProviderProxy(providerHolder);
//            } else if (provider.isNull()) {
//                //先兼容测试
//                Log.e("andy", "provider is null");
//            } else {
//                ProviderInfo _info = info.get(providerHolder, null);
//                if (_info != null) {
//                    Log.e("andy", info.get(providerHolder, null) + "provider is null");
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * @param providerHolder: ContentProviderHolder
//     */
//    private ContentProviderProxy(Object providerHolder) {
//        super();
//        this.mProviderHolder = providerHolder;
//        IInterface mProvider = provider.get(providerHolder, null);
//        provider.set(providerHolder, setSource(mProvider).makeProxy(mProvider));
//    }
//
//    public Object build() {
//        return mProviderHolder;
//    }
//
//    private final HashMap<String, String> mPkgs = new HashMap<>();
//
//    public ContentProviderProxy setPkgNames(String hostPkg, String... childPkgs) {
//        mPkgs.clear();
//        if (childPkgs != null) {
//            for (String child : childPkgs) {
//                mPkgs.put(child, hostPkg);
//            }
//        }
//        return this;
//    }
//
//    @Override
//    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
//        if (args != null && args.length > 0) {
//            if (args[0] instanceof String) {
//                //判断是否为包名
//                String pkg = mPkgs.get(args[0]);
//                if (pkg != null) {
//                    args[0] = pkg;
//                }
//            }
//        }
//        return super.onInvoke(source, method, args);
//    }
//
//}
