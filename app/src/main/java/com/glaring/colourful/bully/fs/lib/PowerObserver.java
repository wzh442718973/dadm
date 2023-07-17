package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.os.WorkSource;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;
import com.glaring.colourful.bully.mo.RefField;

class PowerObserver extends IInterfaceObserver {

    private static final RefField mNames = RefField.Get(WorkSource.class, "mNames");

    @ServiceInterface("acquireWakeLock")
    public Object _acquireWakeLock(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class);
        while (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
                break;
            } else {
                idx = XObject.index(args, String.class, idx + 1);
            }
        }

        if (0 <= (idx = XObject.index(args, WorkSource.class))) {
            WorkSource work = (WorkSource) args[idx];
            String[] names = mNames.get(work, null);
            final int N = names == null ? 0 : names.length;
            for (int i = 0; i < N; ++i) {
                if (null != names[i] && null != AAAHelper.findPack(names[i], false)) {
                    names[i] = A.getPkgName();
                }
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("releaseWakeLock")
    public Object _releaseWakeLock(Object source, Method method, Object[] args) throws Throwable {
        try {
            return super.onInvoke(source, method, args);
        } catch (Throwable e) {
            return null;
        }
    }

    @ServiceInterface("updateWakeLockWorkSource")
    public Object _updateWakeLockWorkSource(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, WorkSource.class);
        if (0 <= idx) {
            WorkSource work = (WorkSource) args[idx];

            String[] names = mNames.get(work, null);
            final int N = names == null ? 0 : names.length;
            for (int i = 0; i < N; ++i) {
                if (null != names[i] && null != AAAHelper.findPack(names[i], false)) {
                    names[i] = A.getPkgName();
                }
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("wakeUp")
    public Object _wakeUp(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            FusionPack dvd = AAAHelper.findPack((String) args[idx], false);
            if (dvd != null) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }
}
