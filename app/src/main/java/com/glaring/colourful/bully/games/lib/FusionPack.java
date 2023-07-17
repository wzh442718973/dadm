package com.glaring.colourful.bully.games.lib;

import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.GET_META_DATA;
import static android.content.pm.PackageManager.GET_PERMISSIONS;
import static android.content.pm.PackageManager.GET_PROVIDERS;
import static android.content.pm.PackageManager.GET_RECEIVERS;
import static android.content.pm.PackageManager.GET_SERVICES;
import static android.content.pm.PackageManager.GET_SIGNATURES;
import static com.glaring.colourful.bully.games.ak.AdKey.DBG_LOG;
import static com.glaring.colourful.bully.games.lib.Android.AppBindData_appInfo;
import static com.glaring.colourful.bully.games.lib.Android.AppBindData_info;
import static com.glaring.colourful.bully.games.lib.Android.AppBindData_providers;
import static com.glaring.colourful.bully.games.lib.Android.ArrayMap;
import static com.glaring.colourful.bully.games.lib.Android.ContentResolver_mPackageName;
import static com.glaring.colourful.bully.games.lib.Android.ContextImpl_mBasePackageName;
import static com.glaring.colourful.bully.games.lib.Android.ContextImpl_mOpPackageName;
import static com.glaring.colourful.bully.games.lib.Android.callApplicationOnCreate;
import static com.glaring.colourful.bully.games.lib.Android.clearSystemProvider;
import static com.glaring.colourful.bully.games.lib.Android.getInstrumentation;
import static com.glaring.colourful.bully.games.lib.Android.installContentProviders;
import static com.glaring.colourful.bully.games.lib.Android.mActivityThread;
import static com.glaring.colourful.bully.games.lib.Android.mBoundApplication;
import static com.glaring.colourful.bully.games.lib.Android.mInitialApplication;
import static com.glaring.colourful.bully.games.lib.Android.mPackages;
import static com.glaring.colourful.bully.games.lib.Android.makeApplication;
import static com.glaring.colourful.bully.games.lib.Android.repairFull;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Parcel;
import android.util.Log;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.glaring.colourful.bully.base.FileLock;
import com.glaring.colourful.bully.base.IO;
import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.supers.RefField;


public enum FusionPack {
    A(null, null, null, 0),  //原有APP
    B(AdKey.PKG_NAME, null, AdKey.PKG_FILE, AdKey.PKG_VER);

    FusionPack(String pkgName, String entryName, String resName, int version) {
        this.mPkgName = pkgName;
        this.mVersion = version;
        this.mResName = resName;
    }

    @Override
    public String toString() {
        return mPkgName + ":<" + mResName + ">";
    }

    public final String mResName;
    public final int mVersion;
    private String mPkgName;

    public final String getPkgName() {
        return mPkgName;
    }

    private File mResFile;
    private File mDataFile;
    PackageInfo mPackageInfo;
    ApplicationInfo mInfo;
    Context mContext;
    Application mApplication;

    public Application getApplication() {
        return mApplication;
    }

    static final ComponentInfo compareInfo(ComponentInfo info, ComponentInfo[] infos) {
        if (infos != null && info != null) {
            for (ComponentInfo _info : infos) {
                if (info.name.equals(_info.name)) {
                    if (_info.processName == null) {
                        _info.processName = _info.packageName;
                    }
                    return _info;
                }
            }
        }
        return null;
    }

    final ActivityInfo getReceiverInfo(ActivityInfo info) {
        return (ActivityInfo) (mPackageInfo == null ? null : compareInfo(info, mPackageInfo.receivers));
    }

    final ServiceInfo getServiceInfo(ServiceInfo info) {
        return (ServiceInfo) (mPackageInfo == null ? null : compareInfo(info, mPackageInfo.services));
    }

    final ProviderInfo getProviderInfo(ProviderInfo info) {
        return (ProviderInfo) (mPackageInfo == null ? null : compareInfo(info, mPackageInfo.providers));
    }

    final ActivityInfo getActivityInfo(ActivityInfo info) {
        ActivityInfo[] infos = mPackageInfo == null ? null : mPackageInfo.activities;
        if (infos != null && info != null) {
            for (ActivityInfo _info : infos) {
                if (_info.name.equals(info.name) || _info.name.equals(info.targetActivity)) {
                    if (_info.processName == null) {
                        _info.processName = _info.packageName;
                    }
                    return _info;
                }
            }
        }
        return null;
    }

    public boolean isLoad(Context context) {
        if (mResName != null) {
            return mResFile != null;
        } else {
            return true;
        }
    }

    public boolean unloadResFile(Context context) {
        if (mResName != null) {
            return false;
        } else {
            return true;
        }
    }

    private static void RemoveFile(File file, boolean self) {
        ArrayList<File> childs = new ArrayList<>();
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) {
                childs.addAll(Arrays.asList(list));
            }
        }
        while (childs.size() > 0) {
            File f = childs.remove(childs.size() - 1);
            if (!f.exists()) {

            } else if (f.isDirectory()) {
                File[] list = f.listFiles();
                if (list != null && list.length > 0) {
                    childs.add(f);
                    childs.addAll(Arrays.asList(list));
                } else {
                    f.delete();
                }
            } else {
                f.delete();
            }
        }

        if (self) {
            file.delete();
        }

        if (self || file.isFile()) {
            file.delete();
        }
    }

    private boolean loadResFile(Context context) {
        if (mResName != null) {
            mDataFile = new File(context.getFilesDir().getParentFile(), "." + mPkgName);
            mResFile = new File(mDataFile, "base.apk");

            if (!mDataFile.exists()) {
                if (!mDataFile.mkdirs()) {
                    if (DBG_LOG) Log.e(AdKey.TAG, "路径创建失败: " + mDataFile);
                    return false;
                }
            }

            if (DBG_LOG) Log.e(AdKey.TAG, "loadResFile." + mResName);
            FileLock mLock = null;
            try {
                mLock = new FileLock(new File(mDataFile, "base.lock"));
                mLock.lock();

                byte[] md5 = new byte[16];
                long lockLastModified = Long.MAX_VALUE;
                final long lastModified = mResFile.exists() ? mResFile.lastModified() : -1l;
                try {
                    Parcel parcel = Parcel.obtain();
                    try {
                        byte[] data = IO.Read(mLock.getLockFile());
                        if (data != null) {
                            parcel.unmarshall(data, 0, data.length);
                            parcel.setDataPosition(0);
                            parcel.setDataCapacity(data.length);
                        }
                        lockLastModified = parcel.readLong();
                        parcel.readByteArray(md5);
                        if (lastModified != lockLastModified) {
                            Arrays.fill(md5, (byte) 0);
                        }
                    } finally {
                        parcel.recycle();
                    }
                } catch (Throwable e) {
                    if (DBG_LOG) {
                        Log.e(AdKey.TAG, "lock", e);
                    }
                }
                if (DBG_LOG) Log.e(AdKey.TAG, "Lock: " + lastModified + " * " + lockLastModified);
                long s = System.currentTimeMillis();
                if (encryption.decrypt(context, mResName, mResFile, md5)) {

                    Parcel parcel = Parcel.obtain();
                    try {
                        parcel.writeLong(mResFile.lastModified());
                        parcel.writeByteArray(md5);
                        IO.Write(mLock.getLockFile(), parcel.marshall());
                    } finally {
                        parcel.recycle();
                    }
                }
                if (true) {
                    Log.e(AdKey.TAG, "load:" + (System.currentTimeMillis() - s) + "ms");
                }
            } catch (Throwable e) {
                Log.e(AdKey.TAG, "res", e);
                return false;
            } finally {
                if (mLock != null) {
                    mLock.unlock();
                }
            }
        }
        return true;
    }

    static final String[] mergeStrings(String[]... sources) {
        ArrayList<String> list = new ArrayList<>(16);
        for (String[] ary : sources) {
            if (ary != null) {
                list.addAll(Arrays.asList(ary));
            }
        }
        if (list.isEmpty()) {
            return null;
        } else {
            return list.toArray(new String[list.size()]);
        }
    }

    static final int[] mergeInts(int[]... sources) {
        IntBuffer buffer = IntBuffer.allocate(32);
        for (int[] ary : sources) {
            if (ary != null) {
                buffer.put(ary);
            }
        }
        return Arrays.copyOf(buffer.array(), buffer.position());
    }

    /**
     * zip file "/data/user/0/com.sabangrooster.funny.gamepro/.com.hello324tp.india/base.apk",
     * zip file "/data/app/com.sabangrooster.funny.gamepro-76n83Dhk5b7JjTkdg0gzAQ==/split_config.arm64_v8a.apk",
     * zip file "/data/app/com.sabangrooster.funny.gamepro-76n83Dhk5b7JjTkdg0gzAQ==/split_config.xxhdpi.apk",
     * zip file "/data/app/com.sabangrooster.funny.gamepro-76n83Dhk5b7JjTkdg0gzAQ==/base.apk"
     * dex代码文件必须插入在签名，不然查找dex会出现以下异常
     * No original dex files found for dex location /data/app/com.sabangrooster.funny.gamepro-76n83Dhk5b7JjTkdg0gzAQ==/split_config.arm64_v8a.apk
     * 1.第一个为dataDir的数据
     *
     * @param clazz
     * @param fieldName
     * @param target
     * @param source
     * @param insert
     * @param append
     */
    static final void copyField(Class clazz, String fieldName, Object target, Object source, Object append, boolean insert) {

        //insert dex文件必须插入到
        RefField field = RefField.Get(clazz, fieldName);
        if (field.isNull()) {
            return;
        }
        if (source != null) {
            Object value = field.get(source, null);
            if (value == null) {
                return;
            }
            if (append == null) {
                field.set(target, value);
            } else if (append instanceof String[]) {
                if (insert) {
                    field.set(target, mergeStrings((String[]) append, (String[]) value));
                } else {
                    field.set(target, mergeStrings((String[]) value, (String[]) append));
                }
            } else if (append instanceof int[]) {
                if (insert) {
                    field.set(target, mergeInts((int[]) append, (int[]) value));
                } else {
                    field.set(target, mergeInts((int[]) value, (int[]) append));
                }
            }
        } else if (append != null) {
            field.set(target, append);
        }
    }

    static final void copyField(Class clazz, String fieldName, Object target, Object source, Object append) {
        copyField(clazz, fieldName, target, source, append, false);
    }

    /**
     * 状态DVD数据到系统
     *
     * @param context
     */
    private void LoadDVD(Context context, boolean startLoad) {
        if (mResName == null) {
            return;
        }
        if (mContext == null) {
            final PackageManager pm = context.getPackageManager();
            PackageInfo newPackageInfo = pm.getPackageArchiveInfo(mResFile.getAbsolutePath(), GET_ACTIVITIES | GET_PROVIDERS | GET_SERVICES | GET_RECEIVERS | GET_PERMISSIONS | GET_META_DATA);
            if (!newPackageInfo.packageName.equals(mPkgName)) {
                throw new RuntimeException("配置包名不一致");
            }
            mPackageInfo = newPackageInfo;
            mInfo = newPackageInfo.applicationInfo;
            final ApplicationInfo realInfo = A.mInfo;

            final String dataDir = mDataFile.getPath(); //realInfo.dataDir;
            final String[] sourceDirs = new String[]{mResFile.getPath()};

            mInfo.processName = realInfo.processName;
            mInfo.uid = realInfo.uid;
            mInfo.dataDir = dataDir;
            mInfo.sourceDir = sourceDirs[0];
            mInfo.publicSourceDir = sourceDirs[0];
            mInfo.nativeLibraryDir = realInfo.nativeLibraryDir;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if ("androidx.core.app.CoreComponentFactory".equals(mInfo.appComponentFactory)) {
                    mInfo.appComponentFactory = null;
                }
            }
            copyField(ApplicationInfo.class, "credentialEncryptedDataDir", mInfo, null, dataDir);
            copyField(ApplicationInfo.class, "credentialProtectedDataDir", mInfo, null, dataDir);
            copyField(ApplicationInfo.class, "deviceEncryptedDataDir", mInfo, null, dataDir);
            copyField(ApplicationInfo.class, "deviceProtectedDataDir", mInfo, null, dataDir);
            copyField(ApplicationInfo.class, "scanPublicSourceDir", mInfo, null, dataDir);
            copyField(ApplicationInfo.class, "scanSourceDir", mInfo, null, dataDir);

            copyField(ApplicationInfo.class, "splitPublicSourceDirs", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "splitSourceDirs", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "splitNames", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "splitClassLoaderNames", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "compileSdkVersionCodename", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "compileSdkVersion", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "nativeLibraryRootRequiresIsa", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "resourceDirs", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "seInfo", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "seInfoUser", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "nativeLibraryRootDir", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "primaryCpuAbi", mInfo, realInfo, null);
            copyField(ApplicationInfo.class, "sharedLibraryFiles", mInfo, realInfo, null);

            copyField(PackageInfo.class, "splitNames", mPackageInfo, A.mPackageInfo, null);
            copyField(PackageInfo.class, "splitRevisionCodes", mPackageInfo, A.mPackageInfo, null);

            copyField(PackageInfo.class, "signingInfo", newPackageInfo, A.mPackageInfo, null);
            copyField(PackageInfo.class, "signatures", newPackageInfo, A.mPackageInfo, null);


            repairFull(newPackageInfo);
            clearSystemProvider();

            if (newPackageInfo.packageName.equals(A.mPkgName)) {
                if (ArrayMap.isInstance(mPackages)) {
                    ArrayMap.getMethod("clear").call(mPackages);
                }
            }
            Context newContext = null;
            try {
                newContext = context.createPackageContext(newPackageInfo.packageName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
                if (newContext == null) {
                    Log.e(AdKey.TAG, "context is null");
                    System.exit(0);
                }
                mContext = newContext;

                if (DBG_LOG) Log.e(AdKey.TAG, "createPackageContext: " + newContext);
                Object pi = Android.getLoadedApk(newContext);
                if (DBG_LOG) Log.e(AdKey.TAG, "LoadedApk: " + pi);

                ContextImpl_mBasePackageName.set(newContext, mPkgName);
                ContextImpl_mOpPackageName.set(newContext, mPkgName);
                ContentResolver_mPackageName.set(context.getContentResolver(), mPkgName);

                final boolean isInroad = false;
                if (isInroad) {
                    AppBindData_info.set(mBoundApplication, pi);
                    AppBindData_appInfo.set(mBoundApplication, newPackageInfo.applicationInfo);
                }
                List<ProviderInfo> providers = null;
                if (newPackageInfo.providers != null) {
                    ArrayList<ProviderInfo> newProviders = new ArrayList<>();
                    providers = AppBindData_providers.get(mBoundApplication, null);

                    AppBindData_providers.set(mBoundApplication, null);
                    for (ProviderInfo info : providers) {
                        if (null == getProviderInfo(info)) {
                            newProviders.add(info);
                        }
                    }

                    providers = newProviders;
                }
                Application application = null;
                if (isInroad) {
                    mInitialApplication.set(mActivityThread, null);
                    application = makeApplication.call(pi, false, null);
                    mInitialApplication.set(mActivityThread, application);
                } else {
                    application = makeApplication.callThrow(pi, false, null);
                }
                if (application == null) {
                    Log.e(AdKey.TAG, "application is null");
                    System.exit(0);
                }

                mApplication = application;
                if (providers != null) {
                    AppBindData_providers.set(mBoundApplication, providers);
                }

                if (DBG_LOG) Log.e(AdKey.TAG, "Application: " + application);
                    if (newPackageInfo.providers != null) {
                        installContentProviders.callThrow(mActivityThread, application, Arrays.asList(newPackageInfo.providers));
                    }

                Object mInstrumentation = getInstrumentation.call(mActivityThread);
                callApplicationOnCreate.callThrow(mInstrumentation, application);
            } catch (Throwable e) {
                Log.e(AdKey.TAG, "load.play:", e);
                if (DBG_LOG) e.printStackTrace();
            }
        }
    }

    static void InitPackage(Context base, Application application) {
        A.mPkgName = base.getPackageName();
        A.mContext = base;
        A.mApplication = application;
        try {
            A.mPackageInfo = base.getPackageManager().getPackageInfo(A.mPkgName, GET_SIGNATURES);
            A.mInfo = A.mPackageInfo.applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            A.mInfo = base.getApplicationInfo();
            e.printStackTrace();
        }
    }

    static void LoadPackage(Context context) {
        FusionPack[] packs = values();
        for (int i = 1; i < packs.length; ++i) {
            if (packs[i].loadResFile(context)) {
                packs[i].LoadDVD(context, true);
            } else {
                Log.e(AdKey.TAG, "资源加载失败!");
                Runtime.getRuntime().exit(0);
            }
        }
    }
}