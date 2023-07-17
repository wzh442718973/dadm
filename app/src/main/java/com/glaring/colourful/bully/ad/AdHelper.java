package com.glaring.colourful.bully.ad;

import android.app.Activity;
import android.content.Context;

public class AdHelper {
    private static boolean DBG_LOG = false;
    private static String TAG = "andy";
//    private static String NATIVE_AD = "3192190914195404_3192196394194856";//"283105476436507_286520742761647";
//    private static String INTERSTITIAL_AD = "3192190914195404_3192200797527749";//"283105476436507_286174982796223";
//
//    public static RefMethod getExtAds;
//
//    private static void addShowAdView(Activity activity, final View adView, View.OnClickListener listener) {
//        FrameLayout layout = new FrameLayout(activity);
//        layout.setBackgroundColor(0xff000000);
//
//        layout.addView(adView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        final DisplayMetrics dm = activity.getResources().getDisplayMetrics();
//        final int viewSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, dm);
//        final AutoButton close = new AutoButton(activity);
//        close.setOnClickListener(listener);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(viewSize, viewSize);
//        params.gravity = Gravity.LEFT | Gravity.TOP;
//        params.topMargin = params.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, dm);
//        layout.addView(close, params);
//        activity.addContentView(layout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        close.startTimer(5);
//    }

    private static class AdDestroy implements Runnable {

        private Runnable run;
        private int count = 0;

        public AdDestroy(Runnable runnable) {
            this.run = runnable;
            this.count = 1;
        }

        @Override
        public void run() {
            if (run != null) {
                try {
                    run.run();
                } finally {
                    if (--count <= 0) {
                        run = null;
                    }
                }
            }
        }
    }


    public static void showNativeAd(final Activity activity, Runnable closeAd) {
        final Runnable close = new AdDestroy(closeAd);
        close.run();
//        String[] ads = getExtAds == null ? new String[2] : getExtAds.call(null);
//        final String adId = ads[0];
//        if (!TextUtils.isEmpty(adId)) {
//            final NativeAd mAd = new NativeAd(activity, adId);
//            mAd.loadAd(mAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
//                @Override
//                public void onMediaDownloaded(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onMediaDownloaded:" + ad);
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    Log.e(TAG, ad + "  Code=" + adError.getErrorCode() + " >> " + adError.getErrorMessage());
//                    if (ad != null) {
//                        ad.destroy();
//                    }
//                    close.run();
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onAdLoaded:" + ad);
//                    if (!AAAHelper.checkStartTimeout(close)) {
//                        View adView = NativeAdView.render(activity, (NativeAd) ad);
//                        addShowAdView(activity, adView, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ad.destroy();
//                                close.run();
//                            }
//                        });
//                    } else {
//                        ad.destroy();
//                    }
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onAdClicked:" + ad);
//                    if (ad != null) {
//                        ad.destroy();
//                    }
//                    close.run();
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onLoggingImpression:" + ad);
//                }
//            }).build());
//            AAAHelper.RegStartTimeout(close);
//        } else {
//            close.run();
//        }
    }

//    private static InterstitialAd mInstance;
//
//    private static final void destroy(Ad ad) {
//        if (ad == mInstance) {
//            mInstance = null;
//        }
//        if (ad != null) {
//            ad.destroy();
//        }
//    }


    public static synchronized void showInterstitialAd(Context context) {
//        String[] ads = getExtAds == null ? new String[2] : getExtAds.call(null);
//        final String adId = ads[1];
//        if (!TextUtils.isEmpty(adId)) {
//            if (mInstance != null) {
//                return;
//            }
//            mInstance = new InterstitialAd(context.getApplicationContext(), adId);
//            mInstance.loadAd(mInstance.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
//                @Override
//                public void onInterstitialDisplayed(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onInterstitialDisplayed: " + ad);
//                }
//
//                @Override
//                public void onInterstitialDismissed(Ad ad) {
//                    destroy(ad);
//                    if (DBG_LOG) Log.e(TAG, "onInterstitialDismissed: " + ad);
//                }
//
//                @Override
//                public void onError(Ad ad, AdError adError) {
//                    destroy(ad);
//                    Log.e(TAG, ad + "  Code=" + adError.getErrorCode() + " >> " + adError.getErrorMessage());
//                }
//
//                @Override
//                public void onAdLoaded(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onAdLoaded: " + ad);
//                    if (ad != null && ad instanceof InterstitialAd) {
//                        ((InterstitialAd) ad).show();
//                    } else {
//                        destroy(ad);
//                    }
//                }
//
//                @Override
//                public void onAdClicked(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onAdClicked: " + ad);
//                    destroy(ad);
//                }
//
//                @Override
//                public void onLoggingImpression(Ad ad) {
//                    if (DBG_LOG) Log.e(TAG, "onLoggingImpression: " + ad);
//                }
//            }).build());
//        }
    }

    public static void showWebAd(final Activity activity, Runnable closeAd) {
        final Runnable close = new AdDestroy(closeAd);
        close.run();
//        final AdWeb mAd = new AdWeb(activity);
//        mAd.loadAd(new AdWeb.OnWebListener() {
//            @Override
//            public void onError(AdWeb ad, String adError) {
//                Log.e(TAG, ad + "  adError=" + adError);
//                if (ad != null) {
//                    ad.destroyAd();
//                }
//                close.run();
//            }
//
//            @Override
//            public void onAdLoaded(final AdWeb ad) {
//                if (DBG_LOG) Log.e(TAG, "onAdLoaded:" + ad);
//                if (!AAAHelper.checkStartTimeout(close)) {
//                    addShowAdView(activity, ad, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ad.destroy();
//                            close.run();
//                        }
//                    });
//                } else {
//                    ad.destroy();
//                }
//            }
//
//            @Override
//            public void onAdClicked(AdWeb ad) {
//                if (DBG_LOG) Log.e(TAG, "onAdClicked:" + ad);
//                if (ad != null) {
//                    ad.destroy();
//                }
//                close.run();
//            }
//
//        });
//        AAAHelper.RegStartTimeout(close);
    }
}
