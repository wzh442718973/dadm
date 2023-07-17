package com.glaring.colourful.bully.mo;

import android.util.Log;

final class iLog {
    static final boolean DBG_LOG = false;
    static final String DBG_TAG = "monitor";

    public static final void l(String msg) {
        Log.e(DBG_TAG, msg);
    }

    public static final void l(String msg, Throwable e) {
        Log.e(DBG_TAG, msg);
    }

    public static final void i(String msg) {
        if (DBG_LOG) Log.i(DBG_TAG, msg);
    }

    public static final void i(String msg, Throwable e) {
        if (DBG_LOG) Log.i(DBG_TAG, msg, e);
    }

    public static final void e(String msg) {
        if (DBG_LOG) Log.e(DBG_TAG, msg);
    }

    public static final void e(String msg, Throwable e) {
        if (DBG_LOG) Log.e(DBG_TAG, msg, e);
    }
}
