package com.glaring.colourful.bully.games.lib;

import android.content.ComponentName;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;
import com.glaring.colourful.bully.supers.RefClass;
import com.glaring.colourful.bully.supers.RefField;

public class JobObserver extends IInterfaceObserver {

    private static RefClass JobInfo = RefClass.Get("android.app.job.JobInfo");
    private static RefField service = JobInfo.getField("service");

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if(!JobInfo.isNull()){
            int index = XObject.index(args, JobInfo.get());
            if(index >= 0){
                ComponentName mm = service.get(args[index], null);
                FusionPack dvd = mm == null ? null : AAAHelper.findPack(mm.getPackageName(), false);
                if(dvd != null){
                    service.set(args[index], new ComponentName(FusionPack.A.getPkgName(), mm.getClassName()));
                }
            }
        }

        return super.onInvoke(source, method, args);
    }
}
