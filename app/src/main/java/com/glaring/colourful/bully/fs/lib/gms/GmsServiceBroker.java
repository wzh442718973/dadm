package com.glaring.colourful.bully.fs.lib.gms;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.fs.lib.FusionPack;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.RefClass;
import com.glaring.colourful.bully.mo.RefField;
import com.glaring.colourful.bully.mo.RefMethod;

public class GmsServiceBroker extends IInterfaceObserver.IConnection {
    public static final String ACTION = "com.google.android.gms.measurement.START";
    public static final String DESC = "com.google.android.gms.common.internal.IGmsServiceBroker";

//    @Override
//    public IInterface queryLocalInterface(String descriptor) {
//        return super.queryLocalInterface(descriptor);
//    }
//
//    @Override
//    public void onConnectedBind(AIDL aidl, ComponentName name, IBinder service) {
//        if (null == aidl.setProxyClass("bz").getProxyClass()) {//Lcom/google/android/gms/common/internal/zzac;
//            aidl.setProxyClass("com.google.android.gms.common.internal.zzac");
//        }
//        super.onConnectedBind(aidl, name, service);
//    }

    private static RefClass GetServiceRequest;
    private static RefField pkgName;
    private static RefField CREATOR;

    private static Parcelable.Creator creator;
    private static RefMethod writeParcelable;

    private void init() {
        if (GetServiceRequest == null) {
            GetServiceRequest = new RefClass(getAidl().forName("com.google.android.gms.common.internal.GetServiceRequest"));
        }
        if (pkgName == null) {
            pkgName = GetServiceRequest.getField("zzd");
        }

        RefField[] CREATORs = GetServiceRequest.getFields("CREATOR", Parcelable.Creator.class);
        if (CREATORs.length > 0) {
            creator = CREATORs[0].get(null, null);

            RefMethod[] methods = new RefClass(creator.getClass()).getMethods(null, void.class, GetServiceRequest.get(), Parcel.class, int.class);
            if (methods.length > 0) {
                writeParcelable = methods[0];
            }
        }

    }


    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        boolean ok = true;
        if(AdKey.DBG_LOG)Log.e(AdKey.TAG, "GmsServiceBroker.transact: " + code);
        if (code == 46) {
            init();
            data.setDataPosition(0);
            data.enforceInterface(getAidl().mDescriptor);
            IBinder IGmsCallbacks = data.readStrongBinder();
            Parcelable request = null;

            if (!pkgName.isNull() && data.readInt() != 0) {
                request = (Parcelable) creator.createFromParcel(data);
//                android.os.Bundle.CREATOR.createFromParcel(data);

                pkgName.set(request, FusionPack.A.getPkgName());

                data = Parcel.obtain();
                data.writeInterfaceToken(getAidl().mDescriptor);
                data.writeStrongBinder(IGmsCallbacks);
                data.writeInt(1);

                writeParcelable.call(null, request, data, 0);
//                data.writeParcelable(request, 0);
                ok = super.transact(code, data, reply, flags);

                data.recycle();
            }
        } else {
            ok = super.transact(code, data, reply, flags);
        }
        return ok;
    }


    static class GmsCallbacks extends IInterfaceObserver.IStub {

        public GmsCallbacks(IInterfaceObserver observer, IInterface stub) {
            super(observer, stub);
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    }

}
