package com.glaring.colourful.bully.games.lib;//package com.glaring.colourful.bully.fusion.lib;
//
//import android.content.ComponentName;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.util.Log;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//
//import com.glaring.colourful.bully.fusion.aab.AdKey;
//
//public class GoogleGsmService extends ServiceConnectionProxy {
//
//    protected GoogleGsmService(Object serviceDispatcher, ServiceConnection connection) {
//        super(serviceDispatcher, connection);
//    }
//
//    private String mDescriptor;
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        mDescriptor = getDescriptor(service);
//        super.onServiceConnected(name, service);
//    }
//
//
//    final Object onIGmsServiceBroker(Object source, Method method, Object[] args) throws Throwable {
//        Object object = args[1];
//        Field[] fields = object.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            if (!Modifier.isStatic(field.getModifiers()) && String.class.isAssignableFrom(field.getType())) {
//                field.setAccessible(true);
//                Log.e(AdKey.TAG, "find >>>>>>>>>>>>>>> " + field);
//                String pkg = getPkgName((String) field.get(object));
//                if (pkg != null) {
//                    field.set(object, pkg);
//                    break;
//                }
//
//            }
//        }
//        return method.invoke(source, args);
//    }
//
//    @Override
//    protected Object onInvoke(Object source, String methodName, Method method, Object[] args) throws Throwable {
//        if ("com.google.android.gms.common.internal.IGmsServiceBroker".equals(mDescriptor)) {
//            return onIGmsServiceBroker(source, method, args);
//        }
//        return super.onInvoke(source, methodName, method, args);
//    }
//}
