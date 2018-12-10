

package me.mingz.cordova.plugin.applovin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinAdViewDisplayErrorCode;
import com.applovin.adview.AppLovinAdViewEventListener;
import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinEventService;
import com.applovin.sdk.AppLovinEventParameters;
import com.applovin.sdk.AppLovinEventTypes;


public class AppLovinPlugin extends CordovaPlugin {
    private static final String TAG = "AppLovinPlugin";


    // actions
    private static final String ACTION_GET_AD_SETTINGS = "getAdSettings";
    private static final String ACTION_SET_OPTIONS = "setOptions";
    private static final String ACTION_CREATE_BANNER = "createBanner";
    private static final String ACTION_REMOVE_BANNER = "removeBanner";
    private static final String ACTION_HIDE_BANNER = "hideBanner";
    private static final String ACTION_SHOW_BANNER = "showBanner";
    private static final String ACTION_SHOW_BANNER_AT_XY = "showBannerAtXY";
    private static final String ACTION_PREPARE_INTERSTITIAL = "prepareInterstitial";
    private static final String ACTION_SHOW_INTERSTITIAL = "showInterstitial";
    private static final String ACTION_IS_INTERSTITIAL_READY = "isInterstitialReady";
    private static final String ACTION_PREPARE_REWARD_VIDEO_AD = "prepareRewardVideoAd";
    private static final String ACTION_SHOW_REWARD_VIDEO_AD = "showRewardVideoAd";
    private static final String ACTION_CREATE_NATIVE_AD = "createNativeAd";
    private static final String ACTION_REMOVE_NATIVE_AD = "removeNativeAd";
    private static final String ACTION_SET_NATIVE_AD_CLICK_AREA = "setNativeAdClickArea";
    private static final String ACTION_TRACK_EVENT = "trackEvent";

    // adtype
    private static final String ADTYPE_BANNER = "banner";
    private static final String ADTYPE_INTERSTITIAL = "interstitial";
    private static final String ADTYPE_NATIVE = "native";
    private static final String ADTYPE_REWARDVIDEO = "rewardvideo";

    // event
    private static final String EVENT_AD_LOADED = "onAdLoaded";
    private static final String EVENT_AD_FAILLOAD = "onAdFailLoad";
    private static final String EVENT_AD_PRESENT = "onAdPresent";
    private static final String EVENT_AD_LEAVEAPP = "onAdLeaveApp";
    private static final String EVENT_AD_DISMISS = "onAdDismiss";
    private static final String EVENT_AD_WILLPRESENT = "onAdWillPresent";
    private static final String EVENT_AD_WILLDISMISS = "onAdWillDismiss";


    // ads
    private AppLovinInterstitialAdDialog interstitialAds = null;
    private AppLovinIncentivizedInterstitial rewardVideoAds = null;
    //    protected TextView adStatusTextView;
    private AppLovinAdView bannerView = null;


    // options
    protected boolean isTesting = false;


    //
    protected boolean bannerVisible = false;
    protected boolean autoShowBanner = true;
    protected boolean logVerbose = false;
    protected int adWidth = 0;
    protected int adHeight = 0;
    protected boolean overlap = false;
    protected boolean orientationRenew = true;
    protected int adPosition = 8;
    protected int posX = 0;
    protected int posY = 0;
    protected RelativeLayout overlapLayout = null;
    protected LinearLayout splitLayout = null;
    protected ViewGroup parentView = null;
//    protected View adView = null;

    protected void log(final String message) {
//        if ( adStatusTextView != null )
//        {
//            runOnUiThread( new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    adStatusTextView.setText( message );
//                }
//            } );
//        }
//        System.out.println( message );


        Log.d(TAG, message);

    }


    protected String getReasonByCode(int code) {
//        SDK_DISABLED
//                FETCH_AD_TIMEOUT
//        NO_NETWORK
//                NO_FILL
//        UNABLE_TO_RENDER_AD
//                INVALID_ZONE
//        INVALID_AD_TOKEN
//                UNSPECIFIED_ERROR
//        INCENTIVIZED_NO_AD_PRELOADED
//                INCENTIVIZED_UNKNOWN_SERVER_ERROR
//        INCENTIVIZED_SERVER_TIMEOUT
//                INCENTIVIZED_USER_CLOSED_VIDEO
//        INVALID_RESPONSE
//                INVALID_URL
//        UNABLE_TO_PREPARE_NATIVE_AD
//                NATIVE_AD_IMPRESSION_ALREADY_TRACKED
//        UNABLE_TO_PRECACHE_RESOURCES
//                UNABLE_TO_PRECACHE_IMAGE_RESOURCES
//        UNABLE_TO_PRECACHE_VIDEO_RESOURCES

        switch (code) {
            case AppLovinErrorCodes.SDK_DISABLED:
                return "SDK DISABLED";
            case AppLovinErrorCodes.FETCH_AD_TIMEOUT:
                return "FETCH_AD_TIMEOUT";
            case AppLovinErrorCodes.NO_NETWORK:
                return "NO_NETWORK";
            case AppLovinErrorCodes.NO_FILL:
                return "NO_FILL";
            case AppLovinErrorCodes.UNABLE_TO_RENDER_AD:
                return "UNABLE_TO_RENDER_AD";
            case AppLovinErrorCodes.INVALID_AD_TOKEN:
                return "INVALID_AD_TOKEN";
            case AppLovinErrorCodes.UNSPECIFIED_ERROR:
                return "UNSPECIFIED_ERROR";
            case AppLovinErrorCodes.INCENTIVIZED_NO_AD_PRELOADED:
                return "INCENTIVIZED_NO_AD_PRELOADED";
            case AppLovinErrorCodes.INCENTIVIZED_UNKNOWN_SERVER_ERROR:
                return "INCENTIVIZED_UNKNOWN_SERVER_ERROR";
            case AppLovinErrorCodes.INCENTIVIZED_SERVER_TIMEOUT:
                return "INCENTIVIZED_SERVER_TIMEOUT";
            case AppLovinErrorCodes.INCENTIVIZED_USER_CLOSED_VIDEO:
                return "INCENTIVIZED_USER_CLOSED_VIDEO";
            case AppLovinErrorCodes.INVALID_RESPONSE:
                return "INVALID_RESPONSE";
            case AppLovinErrorCodes.INVALID_URL:
                return "INVALID_URL";
            case AppLovinErrorCodes.UNABLE_TO_PREPARE_NATIVE_AD:
                return "UNABLE_TO_PREPARE_NATIVE_AD";
            case AppLovinErrorCodes.NATIVE_AD_IMPRESSION_ALREADY_TRACKED:
                return "NATIVE_AD_IMPRESSION_ALREADY_TRACKED";
            case AppLovinErrorCodes.UNABLE_TO_PRECACHE_RESOURCES:
                return "UNABLE_TO_PRECACHE_RESOURCES";
            case AppLovinErrorCodes.UNABLE_TO_PRECACHE_IMAGE_RESOURCES:
                return "UNABLE_TO_PRECACHE_IMAGE_RESOURCES";
            case AppLovinErrorCodes.UNABLE_TO_PRECACHE_VIDEO_RESOURCES:
                return "UNABLE_TO_PRECACHE_VIDEO_RESOURCES";
            default:
                return "unexcept";
        }

    }


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        AppLovinSdk.initializeSdk(cordova.getActivity().getApplicationContext());

        AppLovinSdk.getInstance(cordova.getActivity().getApplicationContext()).getSettings().setTestAdsEnabled(true);

        // Warn user if SDK key is invalid
//        boolean isLegitSdkKey = checkSdkKey();

        // Prompt to add config flags if SDK key is legit
//        if ( isLegitSdkKey )
//        {
//            maybePromptConfigFlags();
//        }


        log("SDK Initialized");


    }


//    private boolean checkSdkKey()
//    {
//        final String sdkKey = AppLovinSdk.getInstance( getApplicationContext() ).getSdkKey();
//        if ( "YOUR_SDK_KEY".equalsIgnoreCase( sdkKey ) )
//        {
//            new AlertDialog.Builder( this )
//                    .setTitle( "ERROR" )
//                    .setMessage( "Please update your sdk key in the manifest file." )
//                    .setCancelable( false )
//                    .setNeutralButton( "OK", null )
//                    .show();
//
//            return false;
//        }
//
//        return true;
//    }


    @Override
    public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {
        PluginResult result = null;
        JSONObject options;

        if (ACTION_SET_OPTIONS.equals(action)) {
            options = inputs.optJSONObject(0);

            result = setOptions(options, callbackContext);

        } else if (ACTION_CREATE_BANNER.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = createBanner(callbackContext);

        } else if (ACTION_REMOVE_BANNER.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = removeBanner(callbackContext);

        } else if (ACTION_HIDE_BANNER.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = hideBanner();

        } else if (ACTION_SHOW_BANNER.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            int nPos = inputs.optInt(0);
            result = showBanner(nPos, 0, 0);

        } else if (ACTION_SHOW_BANNER_AT_XY.equals(action)) {

            options = inputs.optJSONObject(0);
            int x = options.optInt("x");
            int y = options.optInt("y");
            result = showBannerAtXY(10, x, y, callbackContext);

        } else if (ACTION_PREPARE_INTERSTITIAL.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = prepareInterstitial(callbackContext);

//        } else if (ACTION_IS_INTERSTITIAL_READY.equals(action)) {
//
//            result = isInterstitialReady(callbackContext);


        } else if (ACTION_SHOW_INTERSTITIAL.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = showInterstitial(callbackContext);

        } else if (ACTION_PREPARE_REWARD_VIDEO_AD.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = prepareRewardVideoAd(callbackContext);

        } else if (ACTION_SHOW_REWARD_VIDEO_AD.equals(action)) {

//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
            result = showRewardVideoAd(callbackContext);

//        } else if (ACTION_CREATE_NATIVE_AD.equals(action)) {
//
////            String event = inputs.getString(0);
////            JSONArray parameters = inputs.optJSONArray(1);
//            result = createNativeAd(callbackContext);
//
//        } else if (ACTION_REMOVE_NATIVE_AD.equals(action)) {
//
////            String event = inputs.getString(0);
////            JSONArray parameters = inputs.optJSONArray(1);
//            result = removeNativeAd(callbackContext);
//
//        } else if (ACTION_SET_NATIVE_AD_CLICK_AREA.equals(action)) {
//
////            String event = inputs.getString(0);
////            JSONArray parameters = inputs.optJSONArray(1);
//            result = setNativeAdClickArea(callbackContext);

        } else if (ACTION_TRACK_EVENT.equals(action)) {
            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = trackEvent(event, parameters, callbackContext);


        } else {
            log(String.format("Invalid action passed: %s", action));
            result = new PluginResult(Status.INVALID_ACTION);
        }

        if (result != null) {
            callbackContext.sendPluginResult(result);
        }

        return true;
    }


    private PluginResult setOptions(JSONObject options, final CallbackContext callbackContext) {
        if (options != null) {
//            if (options.has("license")) {
//                this.validateLicense(options.optString("license"));
//            }

            if (options.has("isTesting")) {
                this.isTesting = options.optBoolean("isTesting");
            }

//            if (options.has("logVerbose")) {
//                this.logVerbose = options.optBoolean("logVerbose");
//            }
//
            if (options.has("width")) {
                this.adWidth = options.optInt("width");
            }
//
            if (options.has("height")) {
                this.adHeight = options.optInt("height");
            }
//
            if (options.has("overlap")) {
                this.overlap = options.optBoolean("overlap");
            }
//
//            if (options.has("orientationRenew")) {
//                this.orientationRenew = options.optBoolean("orientationRenew");
//            }
//
            if (options.has("position")) {
                this.adPosition = options.optInt("position");
            }
//
            if (options.has("x")) {
                this.posX = options.optInt("x");
            }
//
            if (options.has("y")) {
                this.posY = options.optInt("y");
            }
//
//            if (options.has("bannerId")) {
//                this.bannerId = options.optString("bannerId");
//            }
//
//            if (options.has("interstitialId")) {
//                this.interstialId = options.optString("interstitialId");
//            }
        }
        return null;
    }

    private void createBannerView() {
        bannerView = new AppLovinAdView(AppLovinAdSize.BANNER, cordova.getActivity());

        //
        // Optional: Set listeners
        //
        bannerView.setAdLoadListener(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(final AppLovinAd ad) {
                log("adReceived: Banner loaded");
                fireAdEvent(EVENT_AD_LOADED, ADTYPE_BANNER);
//                        bannerView.loadNextAd();
            }

            @Override
            public void failedToReceiveAd(final int errorCode) {
                // Look at AppLovinErrorCodes.java for list of error codes
                log("failedToReceiveAd: Banner failed to load with error code " + errorCode + getReasonByCode(errorCode));
                fireAdErrorEvent(EVENT_AD_FAILLOAD, ADTYPE_BANNER, errorCode, getReasonByCode(errorCode));
            }
        });

        bannerView.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(final AppLovinAd ad) {
                log("adDisplayed: Banner Displayed");
                fireAdEvent(EVENT_AD_PRESENT, ADTYPE_BANNER);
            }

            @Override
            public void adHidden(final AppLovinAd ad) {

                log("adHidden: Banner Hidden");
                fireAdEvent(EVENT_AD_DISMISS, ADTYPE_BANNER);
            }
        });

        bannerView.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(final AppLovinAd ad) {
                log("adClicked: Banner Clicked");
                bannerView.loadNextAd();
            }
        });

        bannerView.setAdViewEventListener(new AppLovinAdViewEventListener() {
            @Override
            public void adOpenedFullscreen(final AppLovinAd ad, final AppLovinAdView adView) {
                log("adOpenedFullscreen: Banner opened fullscreen");
            }

            @Override
            public void adClosedFullscreen(final AppLovinAd ad, final AppLovinAdView adView) {
                log("adClosedFullscreen: Banner closed fullscreen");
            }

            @Override
            public void adLeftApplication(final AppLovinAd ad, final AppLovinAdView adView) {
                log("adLeftApplication: Banner left application");
                fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_BANNER);
            }

            @Override
            public void adFailedToDisplay(final AppLovinAd ad, final AppLovinAdView adView, final AppLovinAdViewDisplayErrorCode code) {
                log("adFailedToDisplay: Banner failed to display with error code " + code);
                fireAdEvent(EVENT_AD_FAILLOAD, ADTYPE_BANNER);
            }
        });
    }

    private void detachBanner() {
        if (bannerView != null) {
            bannerView.setVisibility(View.INVISIBLE);
            this.bannerVisible = false;
            ViewGroup parentView = (ViewGroup) this.bannerView.getParent();
            if (parentView != null) {
                parentView.removeView(this.bannerView);
            }

        }
    }

    private PluginResult createBanner(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {

                if (bannerView == null) {
                    createBannerView();
                    bannerVisible = false;

                } else {
                    detachBanner();
                }

                bannerView.loadNextAd();

            }
        });

        return null;
    }


    private void destroyBannerView(AppLovinAdView bannerView) {
        bannerView.destroy();
    }

    private PluginResult removeBanner(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (AppLovinPlugin.this.bannerView != null) {
                    AppLovinPlugin.this.hideBanner();
                    AppLovinPlugin.this.destroyBannerView(AppLovinPlugin.this.bannerView);
                    AppLovinPlugin.this.bannerView = null;
                }

                AppLovinPlugin.this.bannerVisible = false;
            }
        });

        return null;
    }


    private void pauseAdView(AppLovinAdView bannerView) {
        bannerView.pause();
    }


    public Activity getActivity() {
        return cordova.getActivity();
    }

    private PluginResult hideBanner() {

        if (bannerView != null) {
            this.autoShowBanner = false;
            Activity activity = this.getActivity();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    AppLovinPlugin.this.detachBanner();
                    AppLovinPlugin.this.pauseAdView(AppLovinPlugin.this.bannerView);
                }
            });
        }


        return null;
    }


    public View getView() {
//        if(adapter != null) return adapter.getView();
//        else {
        // Cordova 3.x, class CordovaWebView extends WebView, -> AbsoluteLayout -> ViewGroup -> View -> Object
        if (View.class.isAssignableFrom(CordovaWebView.class)) {
            return (View) webView;
        }

        // Cordova 4.0.0-dev, interface CordovaWebView { View getView(); }
        try {
            Method getViewMethod = CordovaWebView.class.getMethod("getView", (Class<?>[]) null);
            if (getViewMethod != null) {
                Object[] args = {};
                return (View) getViewMethod.invoke(webView, args);
            }
        } catch (Exception e) {
        }

        // or else we return the root view, but this should not happen
        return getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//        }
    }


    protected int __getAdViewWidth(AppLovinAdView view) {
        return view.getWidth();

    }


    protected int __getAdViewHeight(AppLovinAdView view) {
        return view.getHeight();

    }


    private PluginResult showBanner(final int argPos, final int argX, final int argY) {

        // TODO add view to mainView, and show
        if (this.bannerView == null) {
            Log.e("GenericAdPlugin", "banner is null, call createBanner() first.");
        } else {
            boolean bannerAlreadyVisible = this.bannerVisible;
            final Activity activity = this.getActivity();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    View mainView = AppLovinPlugin.this.getView();
                    if (mainView == null) {
                        Log.e("AppLovinPlugin", "Error: could not get main view");
                    } else {
                        Log.d("AppLovinPlugin", "webview class: " + mainView.getClass());
                        if (AppLovinPlugin.this.bannerVisible) {
                            AppLovinPlugin.this.detachBanner();
                        }

                        int bw = AppLovinPlugin.this.__getAdViewWidth(AppLovinPlugin.this.bannerView);
                        int bh = AppLovinPlugin.this.__getAdViewHeight(AppLovinPlugin.this.bannerView);
                        Log.d("GenericAdPlugin", String.format("show banner: (%d x %d)", bw, bh));
                        ViewGroup rootView = (ViewGroup) mainView.getRootView();
                        int rw = rootView.getWidth();
                        int rh = rootView.getHeight();
                        Log.w("GenericAdPlugin", "show banner, overlap:" + AppLovinPlugin.this.overlap + ", position: " + argPos);
                        if (AppLovinPlugin.this.overlap) {
                            int x = AppLovinPlugin.this.posX;
                            int y = AppLovinPlugin.this.posY;
                            int ww = mainView.getWidth();
                            int wh = mainView.getHeight();
                            if (argPos >= 1 && argPos <= 9) {
                                switch ((argPos - 1) % 3) {
                                    case 0:
                                        x = 0;
                                        break;
                                    case 1:
                                        x = (ww - bw) / 2;
                                        break;
                                    case 2:
                                        x = ww - bw;
                                }

                                switch ((argPos - 1) / 3) {
                                    case 0:
                                        y = 0;
                                        break;
                                    case 1:
                                        y = (wh - bh) / 2;
                                        break;
                                    case 2:
                                        y = wh - bh;
                                }
                            } else if (argPos == 10) {
                                x = argX;
                                y = argY;
                            }

                            int[] offsetRootView = new int[]{0, 0};
                            int[] offsetWebView = new int[]{0, 0};
                            rootView.getLocationOnScreen(offsetRootView);
                            mainView.getLocationOnScreen(offsetWebView);
                            x += offsetWebView[0] - offsetRootView[0];
                            y += offsetWebView[1] - offsetRootView[1];
                            if (AppLovinPlugin.this.overlapLayout == null) {
                                AppLovinPlugin.this.overlapLayout = new RelativeLayout(activity);
                                rootView.addView(AppLovinPlugin.this.overlapLayout, new RelativeLayout.LayoutParams(-1, -1));
                                AppLovinPlugin.this.overlapLayout.bringToFront();
                            }

                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bw, bh);
                            params.leftMargin = x;
                            params.topMargin = y;
                            AppLovinPlugin.this.overlapLayout.addView(AppLovinPlugin.this.bannerView, params);
                            AppLovinPlugin.this.parentView = AppLovinPlugin.this.overlapLayout;
                        } else {
                            AppLovinPlugin.this.parentView = (ViewGroup) mainView.getParent();
                            if (!(AppLovinPlugin.this.parentView instanceof LinearLayout)) {
                                AppLovinPlugin.this.parentView.removeView(mainView);
                                AppLovinPlugin.this.splitLayout = new LinearLayout(AppLovinPlugin.this.getActivity());
                                AppLovinPlugin.this.splitLayout.setOrientation(LinearLayout.VERTICAL);
                                AppLovinPlugin.this.splitLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1, 0.0F));
                                mainView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1, 1.0F));
                                AppLovinPlugin.this.splitLayout.addView(mainView);
                                AppLovinPlugin.this.getActivity().setContentView(AppLovinPlugin.this.splitLayout);
                                AppLovinPlugin.this.parentView = AppLovinPlugin.this.splitLayout;
                            }

                            if (argPos <= 3) {
                                AppLovinPlugin.this.parentView.addView(AppLovinPlugin.this.bannerView, 0);
                            } else {
                                AppLovinPlugin.this.parentView.addView(AppLovinPlugin.this.bannerView);
                            }
                        }

                        AppLovinPlugin.this.parentView.bringToFront();
                        AppLovinPlugin.this.parentView.requestLayout();
                        AppLovinPlugin.this.bannerView.setVisibility(View.INVISIBLE);
                        AppLovinPlugin.this.bannerVisible = true;
                        AppLovinPlugin.this.resumeBannerView(AppLovinPlugin.this.bannerView);
                        mainView.requestFocus();
                    }
                }
            });
        }
        return null;
    }

    private void resumeBannerView(AppLovinAdView view) {
        view.resume();
    }


    private PluginResult showBannerAtXY(final int argPos, final int argX, final int argY, final CallbackContext callbackContext) {
        // TODO update options
        showBanner(argPos, argX, argY);
        return null;
    }

    private PluginResult prepareInterstitial(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _prepareInterstitial();
            }
        });
        callbackContext.success();
        return null;
    }

    private void _prepareInterstitial() {
        log("_prepareInterstitial");
        interstitialAds = AppLovinInterstitialAd.create(
                AppLovinSdk.getInstance(cordova.getActivity().getApplicationContext()),
                cordova.getActivity().getApplicationContext());
        log("AppLovinInterstitialAd.create");

        interstitialAds.setAdLoadListener(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd appLovinAd) {
                log("Interstitial loaded");
                fireAdEvent(EVENT_AD_LOADED, ADTYPE_INTERSTITIAL);
            }

            @Override
            public void failedToReceiveAd(int i) {
                String reason = getReasonByCode(i);
                log("Interstitial failed to load with error code " + i + reason);

                fireAdErrorEvent(EVENT_AD_FAILLOAD, ADTYPE_INTERSTITIAL, i, reason);
            }
        });

        interstitialAds.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                log("Interstitial Displayed");
                fireAdEvent(EVENT_AD_PRESENT, ADTYPE_INTERSTITIAL);
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                log("Interstitial Hidden");
                fireAdEvent(EVENT_AD_DISMISS, ADTYPE_INTERSTITIAL);
            }
        });

        interstitialAds.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                log("Interstitial Clicked");
                fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_INTERSTITIAL);
            }
        });


        interstitialAds.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
                log("Video Started");
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {
                log("Video Ended");
            }
        });


    }

    private PluginResult showInterstitial(final CallbackContext callbackContext) {

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                interstitialAds.show();
            }
        });
        return null;
    }

    private PluginResult prepareRewardVideoAd(final CallbackContext callbackContext) {
        log("prepareRewardVideoAd");
        rewardVideoAds = AppLovinIncentivizedInterstitial.create(cordova.getActivity().getApplicationContext());
        rewardVideoAds.setUserIdentifier("DEMO_USER_IDENTIFIER"); // Set an optional user identifier used for S2S callbacks
        rewardVideoAds.preload(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd appLovinAd) {
                log("adReceived: Rewarded video loaded.");
//                showButton.setEnabled( true );
                fireAdEvent(EVENT_AD_LOADED, ADTYPE_REWARDVIDEO);
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                log("failedToReceiveAd: Rewarded video failed to load with error code " + errorCode + getReasonByCode(errorCode));
                fireAdErrorEvent(EVENT_AD_FAILLOAD, ADTYPE_REWARDVIDEO, errorCode, getReasonByCode(errorCode));
            }
        });
        return null;
    }

    private PluginResult showRewardVideoAd(final CallbackContext callbackContext) {
        //
        // OPTIONAL: Create listeners
        //

        // Reward Listener
        AppLovinAdRewardListener adRewardListener = new AppLovinAdRewardListener() {
            @Override
            public void userRewardVerified(AppLovinAd appLovinAd, Map map) {
                // AppLovin servers validated the reward. Refresh user balance from your server.  We will also pass the number of coins
                // awarded and the name of the currency.  However, ideally, you should verify this with your server before granting it.

                // i.e. - "Coins", "Gold", whatever you set in the dashboard.
                String currencyName = (String) map.get("currency");

                // For example, "5" or "5.00" if you've specified an amount in the UI.
                String amountGivenString = (String) map.get("amount");

                log("userRewardVerified: Rewarded " + amountGivenString + " " + currencyName);

                // By default we'll show a alert informing your user of the currency & amount earned.
                // If you don't want this, you can turn it off in the Manage Apps UI.
            }

            @Override
            public void userOverQuota(AppLovinAd appLovinAd, Map map) {
                // Your user has already earned the max amount you allowed for the day at this point, so
                // don't give them any more money. By default we'll show them a alert explaining this,
                // though you can change that from the AppLovin dashboard.

                log("userOverQuota: Reward validation request exceeded quota with response: " + map);
            }

            @Override
            public void userRewardRejected(AppLovinAd appLovinAd, Map map) {
                // Your user couldn't be granted a reward for this view. This could happen if you've blacklisted
                // them, for example. Don't grant them any currency. By default we'll show them an alert explaining this,
                // though you can change that from the AppLovin dashboard.

                log("userRewardRejected: Reward validation request was rejected with response: " + map);
            }

            @Override
            public void validationRequestFailed(AppLovinAd appLovinAd, int responseCode) {
                if (responseCode == AppLovinErrorCodes.INCENTIVIZED_USER_CLOSED_VIDEO) {
                    // Your user exited the video prematurely. It's up to you if you'd still like to grant
                    // a reward in this case. Most developers choose not to. Note that this case can occur
                    // after a reward was initially granted (since reward validation happens as soon as a
                    // video is launched).
                } else if (responseCode == AppLovinErrorCodes.INCENTIVIZED_SERVER_TIMEOUT || responseCode == AppLovinErrorCodes.INCENTIVIZED_UNKNOWN_SERVER_ERROR) {
                    // Some server issue happened here. Don't grant a reward. By default we'll show the user
                    // a alert telling them to try again later, but you can change this in the
                    // AppLovin dashboard.
                } else if (responseCode == AppLovinErrorCodes.INCENTIVIZED_NO_AD_PRELOADED) {
                    // Indicates that the developer called for a rewarded video before one was available.
                    // Note: This code is only possible when working with rewarded videos.
                }

                log("validationRequestFailed: Reward validation request failed with error code: " + responseCode + getReasonByCode(responseCode));
                fireAdErrorEvent(EVENT_AD_FAILLOAD, ADTYPE_REWARDVIDEO, responseCode, getReasonByCode(responseCode));
            }

            @Override
            public void userDeclinedToViewAd(AppLovinAd appLovinAd) {
                // This method will be invoked if the user selected "no" when asked if they want to view an ad.
                // If you've disabled the pre-video prompt in the "Manage Apps" UI on our website, then this method won't be called.

                log("userDeclinedToViewAd: User declined to view ad");
            }
        };

        // Video Playback Listener
        AppLovinAdVideoPlaybackListener adVideoPlaybackListener = new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
                log("videoPlaybackBegan: Video Started");
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean fullyWatched) {
                log("videoPlaybackEnded: Video Ended");
            }
        };

        // Ad Dispaly Listener
        AppLovinAdDisplayListener adDisplayListener = new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {

                log("adDisplayed: Ad Displayed");
                fireAdEvent(EVENT_AD_PRESENT, ADTYPE_REWARDVIDEO);
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                log("adHidden: Ad Dismissed");
                fireAdEvent(EVENT_AD_DISMISS, ADTYPE_REWARDVIDEO);

            }
        };

        // Ad Click Listener
        AppLovinAdClickListener adClickListener = new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                log("Ad Click");
                fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_REWARDVIDEO);
            }
        };

        rewardVideoAds.show(cordova.getActivity(), adRewardListener, adVideoPlaybackListener, adDisplayListener, adClickListener);
        return null;
    }

//    private PluginResult createNativeAd(final CallbackContext callbackContext) {
//        return null;
//    }
//
//    private PluginResult removeNativeAd(final CallbackContext callbackContext) {
//        return null;
//    }
//
//    private PluginResult setNativeAdClickArea(final CallbackContext callbackContext) {
//        return null;
//    }
//
//    private PluginResult isInterstitialReady(final CallbackContext callbackContext) {
//        cordova.getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                boolean result = interstitialAds != null && interstitialAds.isAdReadyToDisplay();
//                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
//            }
//        });
//
//        return null;
//    }


    private PluginResult trackEvent(final String event, final JSONArray data, CallbackContext callbackContext) {
        final AppLovinEventService eventService = AppLovinSdk.getInstance(cordova.getActivity()).getEventService();

        Map<String, String> parameters = new HashMap<String, String>();
        String eventName;
        if (event.equalsIgnoreCase("USER_COMPLETED_LEVEL")) {
            eventName = AppLovinEventTypes.USER_COMPLETED_LEVEL;
            parameters.put(AppLovinEventParameters.COMPLETED_LEVEL_IDENTIFIER, data.optString(0, "0"));

        } else if (event.equalsIgnoreCase("USER_COMPLETED_IN_APP_PURCHASE")) {
            eventName = AppLovinEventTypes.USER_COMPLETED_IN_APP_PURCHASE;
            parameters.put(AppLovinEventParameters.REVENUE_AMOUNT, data.optString(0, "0"));
            parameters.put(AppLovinEventParameters.REVENUE_CURRENCY, data.optString(1, "USD"));
            parameters.put(AppLovinEventParameters.IN_APP_PURCHASE_TRANSACTION_IDENTIFIER, data.optString(2, ""));

        } else if (event.equalsIgnoreCase("USER_SPENT_VIRTUAL_CURRENCY")) {
            eventName = AppLovinEventTypes.USER_SPENT_VIRTUAL_CURRENCY;
            parameters.put(AppLovinEventParameters.VIRTUAL_CURRENCY_AMOUNT, data.optString(0, "0"));
            parameters.put(AppLovinEventParameters.VIRTUAL_CURRENCY_NAME, data.optString(1, ""));

        } else if (event.equalsIgnoreCase("USER_COMPLETED_TUTORIAL")) {
            eventName = AppLovinEventTypes.USER_COMPLETED_TUTORIAL;

        } else if (event.equalsIgnoreCase("USER_VIEWED_PRODUCT")) {
            eventName = AppLovinEventTypes.USER_VIEWED_PRODUCT;
            parameters.put(AppLovinEventParameters.PRODUCT_IDENTIFIER, data.optString(0, ""));

        } else if (event.equalsIgnoreCase("USER_LOGGED_IN")) {
            eventName = AppLovinEventTypes.USER_LOGGED_IN;

        } else {
            return null;
        }

        eventService.trackEvent(eventName, parameters);

        return null;
    }


    protected String __getProductShortName() {
        return "AppLovinAds";
    }

    protected void fireAdEvent(String event, String adType) {
        String obj = this.__getProductShortName();
        String json = String.format("{'adNetwork':'%s','adType':'%s','adEvent':'%s'}", obj, adType, event);
        this.fireEvent(event, json);
    }

    protected void fireAdErrorEvent(String event, String adType, int errCode, String errMsg) {
        String obj = this.__getProductShortName();
        String json = String.format("{'adNetwork':'%s','adType':'%s','adEvent':'%s','error':%d,'reason':'%s'}", obj, adType, event, errCode, errMsg);
        this.fireEvent(event, json);
    }

    private void fireEvent(String eventName, String json) {
//        String data = "";
//        if (jsonObj != null) {
//            data = jsonObj.toString();
//        }

        StringBuilder js = new StringBuilder();
        js.append("javascript:cordova.fireDocumentEvent('");
        js.append(eventName);
        js.append("'");
        if (json != null && !"".equals(json)) {
            js.append(",");
            js.append(json);
        }
        js.append(");");

        final String code = js.toString();

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(code);
            }
        });


    }
}
