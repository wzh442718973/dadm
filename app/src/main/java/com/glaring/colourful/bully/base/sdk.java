package com.glaring.colourful.bully.base;

public class sdk {
    static {
        System.loadLibrary("nonSdk");
    }

    public static native Object fun(int cmd, Object... params);

    public static void get(String a){

    }
}