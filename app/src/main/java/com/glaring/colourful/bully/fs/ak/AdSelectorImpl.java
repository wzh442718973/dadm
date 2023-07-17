package com.glaring.colourful.bully.fs.ak;

import static com.glaring.colourful.bully.fs.ak.AdKey.DBG_SELECT;
import static com.glaring.colourful.bully.fs.ak.AdKey.HOST_NAME;
import static com.glaring.colourful.bully.fs.ak.AdKey.TAG;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.provider.Settings;
import android.util.Log;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



/**
 * 广告选择器
 * Created by wzh on 2017/6/29.
 */

public class AdSelectorImpl extends CfgFile implements IAdSelector {

    /**
     * 1:ref.on; 0:ref.off; -1:未知,需要获取
     */
    private int mRefOn = -1;
    private int mPkgState = -1;

    private boolean mIsRequest = false;  //请求OK
    private final AtomicReference<XRequest> mRequests = new AtomicReference<>();

    public boolean IsRefOn() {
        return mPkgState == 1 && mRefOn == 1;
    }

    private static AdSelectorImpl mImpl = null;

    public static AdSelectorImpl getImpl(Context context) {
        if (mImpl == null) {
            mImpl = new AdSelectorImpl(context);
        }
        return mImpl;
    }

    private final Context mContext;

    private AdSelectorImpl(Context context) {
        super(15, new File(context.getFilesDir(), ".config.ab").getAbsolutePath());
        mContext = context;
    }

    @Override
    protected void onCfgLoad(Parcel in) {
        mRefOn = in.readInt();
        mPkgState = in.readInt();
    }

    @Override
    protected void onCfgSave(Parcel out) {
        out.writeInt(mRefOn);
        out.writeInt(mPkgState);
    }

    @Override
    public void adCfgLoad() {
        super.cfgLoad();
    }

    @Override
    public void adCfgSave() {
        super.cfgSave();
    }

    @Override
    public boolean adCfgUpdate(String cfgJson) {
        return true;
    }

    @Override
    public void adInit(final String adKey, final IAdInit init) {
        waitInit();
        //是否请求成功
        if (mIsRequest || IsRefOn()) {//  #2
            init.onAdInit(null);
        } else {
            synchronized (mRequests) {
                XRequest req = mRequests.get();
                if (req == null) {
                    mRequests.set(req = new XRequest(adKey));
                    req.addInit(init);
                    new Thread(req).start();
                } else {
                    req.addInit(init);
                }
            }
        }
    }

    @Override
    public IAdInfo adSelector(String adKey, IAdRely rely) {

        return null;
    }

    @Override
    public void adShowBegin(String adKey, boolean showExit) {

    }

    @Override
    public void adShowEnd(String adKey, boolean success) {

    }

    public static final String getReferrer(final Context context, final String pkgName) {
        final AtomicReference<String> mSync = new AtomicReference<>();
        boolean ok = false;
        try {
            final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(context).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    try {
                        ReferrerDetails referrerDetails = referrerClient.getInstallReferrer();
                        mSync.set(referrerDetails.getInstallReferrer());
                    } catch (IllegalStateException e) {
                        //Service not connected. Please start a connection before using the service.
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        referrerClient.endConnection();
                        synchronized (mSync) {
                            mSync.notifyAll();
                        }
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {

                }
            });
            ok = true;
        } catch (Throwable e) {
            if (DBG_SELECT) Log.e(AdKey.TAG, "Referrer", e);
        }
        if (ok) {
            synchronized (mSync) {
                try {
                    mSync.wait(3000);
                } catch (InterruptedException e) {
                }
            }
        }
        return mSync.get();
    }

    private class XRequest implements Runnable {

        private Set<IAdInit> mCalls = new HashSet<>();

        public void addInit(IAdInit init) {
            synchronized (mCalls) {
                mCalls.add(init);
            }
        }

        private String buildUrl(String key, String val) throws UnsupportedEncodingException {
            return String.format("%s=%s", URLEncoder.encode(key, "UTF-8"), URLEncoder.encode(val, "UTF-8"));
        }


        private String getText(HttpURLConnection http) throws IOException {
            int code = http.getResponseCode();
            if (code != 200) {
                return null;
            }

            InputStream in = http.getInputStream();
            String encode = http.getHeaderField("Content-Encoding");
            if (encode != null) {
                if (encode.equals("gzip")) {
                    in = new GZIPInputStream(in);
                } else if (encode.equals("zlib")) {
                    in = new ZipInputStream(in);
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                byte[] buff = new byte[1024 * 4];
                do {
                    int m = in.read(buff);
                    if (m < 0) {
                        break;
                    } else if (m > 0) {
                        out.write(buff, 0, m);
                    }
                } while (true);
            } finally {
                in.close();
            }
            return new String(out.toByteArray());
        }

        private void checkCertificate(X509Certificate[] chain, String authType) throws CertificateException {
            if (chain != null && chain.length > 0) {
                if (AdKey.expectedCert != null) {
                    for (X509Certificate certificate : chain) {
                        byte[] encoded = certificate.getEncoded();
                        byte[] encoded2 = AdKey.expectedCert.getEncoded();

                        if (Arrays.equals(encoded, encoded2)) {
                            return; //ok
                        }
                    }
                    throw new CertificateException("certificate does not match");
                }
                return;
            }
            throw new CertificateException("no server certificate");
        }


        String getAndroidId(Context context) {
            return Settings.Secure.getString(context.getContentResolver(), "android_id");
        }

        PackageInfo getAppInfo(Context context) {
            try {
                return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (Throwable var2) {
                return null;
            }
        }

        String toHex(byte[] bytes) {
            byte[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            byte[] out = new byte[bytes.length * 2];

            for (int i = 0, j = 0; i < bytes.length; ++i, j += 2) {
                int val = (bytes[i] & 0xff);
                out[j + 0] = hex[0x0f & (val >> 4)];
                out[j + 1] = hex[0x0f & (val >> 0)];
            }
            return new String(out);
        }

        String md5(String text) {
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(text.getBytes("utf-8"));
                return toHex(digest.digest());
            } catch (Throwable var2) {
                return null;
            }
        }

        @Override
        public void run() {
            final String pkgName = mContext.getPackageName();
            String referrer = getReferrer(mContext, pkgName);
            String uuid = md5(getAndroidId(mContext));
            PackageInfo info = getAppInfo(mContext);

            try {
                StringBuilder body = new StringBuilder();
                try {
                    body.append(buildUrl("udid", uuid)).append("&").append(buildUrl("pgkname", pkgName)).append("&").append(buildUrl("locale", getLocale(mContext))).append("&").append(buildUrl("referrer", (referrer == null ? "" : referrer))).append("&").append(buildUrl("version", info.versionCode + ""));

                    HttpURLConnection http = (HttpURLConnection) new URL(AdKey.REF_URL).openConnection();
                    http.setRequestMethod("POST");
                    if (http instanceof HttpsURLConnection) {
                        final TrustManager trustAllCerts = new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                // do nothing，接受任意客户端证书
                                checkCertificate(chain, authType);
                            }

                            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                // do nothing，接受任意服务端证书
                                checkCertificate(chain, authType);
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                // Log.e("wzh", "getAcceptedIssuers");
                                return new X509Certificate[0];
                            }
                        };

                        SSLContext sc = SSLContext.getInstance("SSL");// TLS
                        sc.init(null, new TrustManager[]{trustAllCerts}, new SecureRandom());// null

                        ((HttpsURLConnection) http).setSSLSocketFactory(sc.getSocketFactory());
                        ((HttpsURLConnection) http).setHostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                if (hostname != null && hostname.contains(HOST_NAME)) {
                                    return true;
                                }
                                try {
                                    if (session != null && session.getPeerCertificateChain() != null) {
                                        javax.security.cert.X509Certificate certificate = session.getPeerCertificateChain()[0];
                                        certificate.checkValidity();
                                        return true;
                                    }
                                } catch (Throwable e) {

                                }
                                return false;
                            }
                        });
                    }

                    http.setDoOutput(true);
                    OutputStream out = http.getOutputStream();
                    out.write(body.toString().getBytes("UTF-8"));

                    String text = getText(http);
                    if (text != null) {
                        final JSONObject JSON = new JSONObject(text);
                        if (JSON.optInt("status") == 200) {
                            if (mRefOn == -1) {
                                mRefOn = JSON.optBoolean("model", false) ? 1 : 0;
                            }
                            mPkgState = JSON.optBoolean("pkgStatus", false) ? 1 : 0;
                        }
                        adCfgSave();
                    }
                } catch (Throwable e) {
                    Log.e(TAG, "Ref: " + e.getMessage());
                }
                Log.e(TAG, uuid + "( " + "Ref:" + mRefOn + "|Usr:" + mPkgState + " )>> " + referrer);
            } catch (Throwable e) {
                if (DBG_SELECT) Log.e(TAG, "", e);
            } finally {
                mIsRequest = true;
                mRequests.set(null);
                synchronized (mCalls) {
                    for (IAdInit init : mCalls) {
                        init.onAdInit(null);
                    }
                }
            }
        }

        private final String mAdKey;

        public XRequest(String adKey) {
            mAdKey = adKey;
        }

    }

    public static String getLocale(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String localeStr = locale.getLanguage() + "_" + locale.getCountry();
        return localeStr;
    }
}
