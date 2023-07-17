package com.glaring.colourful.bully.mo;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.IBinder;
import android.os.IInterface;

import java.util.HashMap;

class RESET {
    private static final HashMap<String, RESET> mSets = new HashMap<>();

    private static final RefClass ActivityThread;
    private static final RefClass ContextImpl;
    private static final RefClass ApplicationPackageManager;
    private static final RefClass ActivityManagerNative;
    private static final RefClass WindowManagerGlobal;
    private static final RefClass Singleton;

    private static final RefField mInstance;

    static {
        ActivityThread = RefClass.Get("android.app.ActivityThread");
        ContextImpl = RefClass.Get("android.app.ContextImpl");
        ApplicationPackageManager = RefClass.Get("android.app.ApplicationPackageManager");
        ActivityManagerNative = RefClass.Get("android.app.ActivityManagerNative");
        WindowManagerGlobal = RefClass.Get("android.view.WindowManagerGlobal");
        Singleton = RefClass.Get("android.util.Singleton");
        mInstance = Singleton.getField("mInstance");

        mSets.put("package", new PackageManager());
        mSets.put("activity", new ActivityManager());
        mSets.put("window", new WindowManager());
        mSets.put("mount", new MountManager());
        mSets.put("activity_task", new ActivityTaskManager());
    }

    private static final RESET DEFAULT = new RESET();

    public static Context getBaseContext(Context context) {
        while (context != null) {
            if (ContextImpl.isInstance(context)) {
                break;
            }
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                context = null;
            }
        }
        return context;
    }

    private static final RESET getInstance(String name) {
        RESET object = mSets.get(name);
        if (null == object) {
            return DEFAULT;
        } else {
            return object;
        }
    }

    public static void Reset(String service, Context context) {
        getInstance(service).reset(context);
    }

    public static IInterface Update(String service, AIDL aidl) {
        return getInstance(service).update(aidl);
    }

    protected void reset(Context context) {
    }

    protected IInterface update(AIDL aidl) {
        return null;
    }

    static final class MountManager extends RESET {
        @Override
        protected IInterface update(AIDL aidl) {
            aidl.setAidlClass(RefClass.forName("android.os.storage.IMountService"));
            return null;
        }
    }

    static final class PackageManager extends RESET {

        @Override
        protected void reset(Context context) {
            //在之前已经存在已经获取系统接口的服务进行清空重置,这样系统就会自动再去获取
            final RefField sPackageManager = ActivityThread.getField("sPackageManager");
            final RefMethod getPackageManager = ActivityThread.getMethod("getPackageManager");
            final RefField mPackageManager = ContextImpl.getField("mPackageManager");
            final RefField mPM = ApplicationPackageManager.getField("mPM");

            //保存的对象置空，重新获取服务
            sPackageManager.set(null, null);

            Context impl = getBaseContext(context);
            if (impl != null) {
                android.content.pm.PackageManager pm = (android.content.pm.PackageManager) mPackageManager.get(impl, null);
                if (pm != null) {
                    //修改内部的,已经拿到对象就直接修改
                    if (ApplicationPackageManager.isInstance(pm)) {
                        mPM.set(pm, getPackageManager.call(null));
                    }
                }
            }
        }
    }

    static final class ActivityManager extends RESET {
        RefField gDefault = null;
        Object _gDefault = null;
        IInterface IActivityManager = null;

        ActivityManager() {
            gDefault = ActivityManagerNative.getField("gDefault");
            if (gDefault.isNull()) {
                gDefault = RefField.Get(android.app.ActivityManager.class, "IActivityManagerSingleton");
//                    Log.e("proxy", "兼容-" + Build.VERSION.RELEASE);
            }

            _gDefault = gDefault.get(null, null);
            if (_gDefault instanceof IInterface) {
                IActivityManager = (IInterface) _gDefault;
            } else {
                IActivityManager = mInstance.get(_gDefault, null);
            }
        }

        @Override
        protected void reset(Context context) {
            if (_gDefault instanceof IInterface) {
                gDefault.set(null, null);
            } else {
                mInstance.set(_gDefault, null);
            }
        }

        @Override
        protected IInterface update(AIDL aidl) {
            aidl.setAidlObject(IActivityManager);
            aidl.setAsInterface(ActivityManagerNative.getMethod("asInterface", IBinder.class).get());
//            return AIDL.updateStubClass(descriptor, ActivityManagerNative.get());
            return null;
        }
    }

    static final class WindowManager extends RESET {
        final RefField sWindowManagerService = WindowManagerGlobal.getField("sWindowManagerService");
        final RefField sWindowSession = WindowManagerGlobal.getField("sWindowSession");

        @Override
        protected void reset(Context context) {
            sWindowManagerService.set(null, null);
            sWindowSession.set(null, null);
        }
    }

    static final class ActivityTaskManager extends RESET {
        final RefClass ActivityTaskManager = RefClass.Get("android.app.ActivityTaskManager");
        final RefField gDefault = ActivityTaskManager.getField("IActivityTaskManagerSingleton");

        private Object _gDefault;
        private IInterface IActivityTaskManager;

        ActivityTaskManager() {
            _gDefault = gDefault.get(null, null);
            if (_gDefault != null) {
                IActivityTaskManager = Singleton.getMethod("get").call(_gDefault);
            }
        }

        @Override
        protected void reset(Context context) {
            if (_gDefault != null) {
                mInstance.set(_gDefault, null);
            }

        }

        @Override
        protected IInterface update(AIDL aidl) {
            aidl.setAidlObject(IActivityTaskManager);
            return IActivityTaskManager;
//            return AIDL.updateAidlObject(descriptor, instance);
        }
    }
}
