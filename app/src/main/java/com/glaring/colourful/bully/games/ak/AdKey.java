package com.glaring.colourful.bully.games.ak;

import java.security.cert.X509Certificate;

public abstract class AdKey {

    static final String HOST_NAME = "api.reptbc.xyz";
    static final String REF_URL = String.format("https://%s/v1/check/referrer/ANDROID", HOST_NAME);

    public static String TAG = AdKey.class.getSimpleName();
    static X509Certificate expectedCert;
    public static final boolean DBG_SELECT = false;
    public static final boolean DBG_LOG = false;

    public static String PKG_NAME = "com.un.px.it";
    public static String PKG_FILE = "assets/xxx.dat";
    public static int PKG_VER = 1;
}
