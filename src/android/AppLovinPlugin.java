

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

import android.util.Log;

import java.util.Map;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinAdViewDisplayErrorCode;
import com.applovin.adview.AppLovinAdViewEventListener;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.adview.AppLovinIncentivizedInterstitial;

public class AppLovinPlugin extends CordovaPlugin {
    private static final String LOG_TAG = "AppLovinPlugin";
    private static final String TAG = "AppLovinPlugin";

    private AppLovinIncentivizedInterstitial myIncent = null;
    private AppLovinIncentivizedInterstitial incentivizedInterstitial;
    //    protected TextView adStatusTextView;
    private AppLovinAdView bannerView;

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


        Log.d(LOG_TAG, "SDK Initialized");


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


//        if (Actions.TRACK_EVENT.equals(action)) {
//            String event = inputs.getString(0);
//            JSONArray parameters = inputs.optJSONArray(1);
//            result = trackEvent(event, parameters, callbackContext);
//
//        }else
        if (Actions.setOptions.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = setOptions(callbackContext);

        } else if (Actions.createBanner.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = createBanner(callbackContext);

        } else if (Actions.removeBanner.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = removeBanner(callbackContext);

        } else if (Actions.hideBanner.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = hideBanner(callbackContext);

        } else if (Actions.showBanner.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = showBanner(callbackContext);

        } else if (Actions.showBannerAtXY.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = showBannerAtXY(callbackContext);

        } else if (Actions.prepareInterstitial.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = prepareInterstitial(callbackContext);

        } else if (Actions.isInterstitialReady.equals(action)) {

            result = isInterstitialReady(callbackContext);


        } else if (Actions.showInterstitial.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = showInterstitial(callbackContext);

        } else if (Actions.prepareRewardVideoAd.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = prepareRewardVideoAd(callbackContext);

        } else if (Actions.showRewardVideoAd.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = showRewardVideoAd(callbackContext);

        } else if (Actions.createNativeAd.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = createNativeAd(callbackContext);

        } else if (Actions.removeNativeAd.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = removeNativeAd(callbackContext);

        } else if (Actions.setNativeAdClickArea.equals(action)) {

            String event = inputs.getString(0);
            JSONArray parameters = inputs.optJSONArray(1);
            result = setNativeAdClickArea(callbackContext);


        } else {
            Log.d(LOG_TAG, String.format("Invalid action passed: %s", action));
            result = new PluginResult(Status.INVALID_ACTION);
        }

        if (result != null) {
            callbackContext.sendPluginResult(result);
        }

        return true;
    }


    private PluginResult setOptions(final CallbackContext callbackContext) {
        return null;
    }

    private PluginResult createBanner(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                bannerView = new AppLovinAdView(AppLovinAdSize.BANNER, cordova.getActivity());

                //
                // Optional: Set listeners
                //
                bannerView.setAdLoadListener(new AppLovinAdLoadListener() {
                    @Override
                    public void adReceived(final AppLovinAd ad) {
                        Log.d(TAG, "adReceived: Banner loaded");
                    }

                    @Override
                    public void failedToReceiveAd(final int errorCode) {
                        // Look at AppLovinErrorCodes.java for list of error codes
                        Log.d(TAG, "failedToReceiveAd: Banner failed to load with error code " + errorCode);
                    }
                });

                bannerView.setAdDisplayListener(new AppLovinAdDisplayListener() {
                    @Override
                    public void adDisplayed(final AppLovinAd ad) {
                        Log.d(TAG, "adDisplayed: Banner Displayed");
                    }

                    @Override
                    public void adHidden(final AppLovinAd ad) {
                        Log.d(TAG, "adHidden: Banner Hidden");
                    }
                });

                bannerView.setAdClickListener(new AppLovinAdClickListener() {
                    @Override
                    public void adClicked(final AppLovinAd ad) {
                        Log.d(TAG, "adClicked: Banner Clicked");
                    }
                });

                bannerView.setAdViewEventListener(new AppLovinAdViewEventListener() {
                    @Override
                    public void adOpenedFullscreen(final AppLovinAd ad, final AppLovinAdView adView) {
                        Log.d(TAG, "adOpenedFullscreen: Banner opened fullscreen");
                    }

                    @Override
                    public void adClosedFullscreen(final AppLovinAd ad, final AppLovinAdView adView) {
                        Log.d(TAG, "adClosedFullscreen: Banner closed fullscreen");
                    }

                    @Override
                    public void adLeftApplication(final AppLovinAd ad, final AppLovinAdView adView) {
                        Log.d(TAG, "adLeftApplication: Banner left application");
                    }

                    @Override
                    public void adFailedToDisplay(final AppLovinAd ad, final AppLovinAdView adView, final AppLovinAdViewDisplayErrorCode code) {
                        Log.d(TAG, "adFailedToDisplay: Banner failed to display with error code " + code);
                    }
                });
                bannerView.loadNextAd();
            }
        });

        return null;
    }

    private PluginResult removeBanner(final CallbackContext callbackContext) {
        bannerView.pause();
        return null;
    }

    private PluginResult hideBanner(final CallbackContext callbackContext) {
        bannerView.pause();
        return null;
    }

    private PluginResult showBanner(final CallbackContext callbackContext) {
        bannerView.loadNextAd();
        return null;
    }

    private PluginResult showBannerAtXY(final CallbackContext callbackContext) {
        bannerView.loadNextAd();
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

    private PluginResult showInterstitial(final CallbackContext callbackContext) {
        final CallbackContext delayCallback = callbackContext;
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean result = _showInterstitial();
                if (result) {
                    delayCallback.success();
                } else {
                    delayCallback.error("Ad is not ready!");
                }
            }
        });
        return null;
    }

    private PluginResult prepareRewardVideoAd(final CallbackContext callbackContext) {
        Log.d(LOG_TAG, "prepareRewardVideoAd");
        incentivizedInterstitial = AppLovinIncentivizedInterstitial.create(cordova.getActivity().getApplicationContext());
        incentivizedInterstitial.setUserIdentifier("DEMO_USER_IDENTIFIER"); // Set an optional user identifier used for S2S callbacks
        incentivizedInterstitial.preload(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd appLovinAd) {
                Log.d(TAG, "adReceived: Rewarded video loaded.");
//                showButton.setEnabled( true );
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                Log.d(TAG, "failedToReceiveAd: Rewarded video failed to load with error code " + errorCode);
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

                Log.d(TAG, "userRewardVerified: Rewarded " + amountGivenString + " " + currencyName);

                // By default we'll show a alert informing your user of the currency & amount earned.
                // If you don't want this, you can turn it off in the Manage Apps UI.
            }

            @Override
            public void userOverQuota(AppLovinAd appLovinAd, Map map) {
                // Your user has already earned the max amount you allowed for the day at this point, so
                // don't give them any more money. By default we'll show them a alert explaining this,
                // though you can change that from the AppLovin dashboard.

                Log.d(TAG, "userOverQuota: Reward validation request exceeded quota with response: " + map);
            }

            @Override
            public void userRewardRejected(AppLovinAd appLovinAd, Map map) {
                // Your user couldn't be granted a reward for this view. This could happen if you've blacklisted
                // them, for example. Don't grant them any currency. By default we'll show them an alert explaining this,
                // though you can change that from the AppLovin dashboard.

                Log.d(TAG, "userRewardRejected: Reward validation request was rejected with response: " + map);
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

                Log.d(TAG, "validationRequestFailed: Reward validation request failed with error code: " + responseCode);
            }

            @Override
            public void userDeclinedToViewAd(AppLovinAd appLovinAd) {
                // This method will be invoked if the user selected "no" when asked if they want to view an ad.
                // If you've disabled the pre-video prompt in the "Manage Apps" UI on our website, then this method won't be called.

                Log.d(TAG, "userDeclinedToViewAd: User declined to view ad");
            }
        };

        // Video Playback Listener
        AppLovinAdVideoPlaybackListener adVideoPlaybackListener = new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
                Log.d(TAG, "videoPlaybackBegan: Video Started");
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean fullyWatched) {
                Log.d(TAG, "videoPlaybackEnded: Video Ended");
            }
        };

        // Ad Dispaly Listener
        AppLovinAdDisplayListener adDisplayListener = new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                Log.d(TAG, "adDisplayed: Ad Displayed");
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                Log.d(TAG, "adHidden: Ad Dismissed");
            }
        };

        // Ad Click Listener
        AppLovinAdClickListener adClickListener = new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                log("Ad Click");
            }
        };

        incentivizedInterstitial.show(cordova.getActivity(), adRewardListener, adVideoPlaybackListener, adDisplayListener, adClickListener);
        return null;
    }

    private PluginResult createNativeAd(final CallbackContext callbackContext) {
        return null;
    }

    private PluginResult removeNativeAd(final CallbackContext callbackContext) {
        return null;
    }

    private PluginResult setNativeAdClickArea(final CallbackContext callbackContext) {
        return null;
    }

    private PluginResult isInterstitialReady(final CallbackContext callbackContext) {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean result = myIncent != null && myIncent.isAdReadyToDisplay();
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, result));
            }
        });

        return null;
    }


    private void _prepareInterstitial() {
        Log.d(LOG_TAG, "_prepareInterstitial");
        myIncent = AppLovinIncentivizedInterstitial.create(cordova.getActivity().getApplicationContext());
        Log.d(LOG_TAG, "AppLovinIncentivizedInterstitial.create");
        myIncent.preload(new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd ad) {
                Log.d(LOG_TAG, "adReceived");

                JSONObject data = new JSONObject();
                try {
                    data.put("id", ad.getAdIdNumber());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fireEvent("applovin.load", data);
            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                Log.d(LOG_TAG, String.format("failedToReceiveAd: %d", errorCode));

                JSONObject data = new JSONObject();
                try {
                    data.put("error", errorCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                fireEvent("applovin.load_error", data);
            }
        });
    }

    private boolean _showInterstitial() {
        if (myIncent == null || !myIncent.isAdReadyToDisplay()) {
            return false;
        }

        myIncent.show(
                cordova.getActivity(),
                new AppLovinAdRewardListener() {
                    @Override
                    public void userDeclinedToViewAd(final AppLovinAd appLovinAd) {
                        Log.d(LOG_TAG, "User declined to view rewarded video");
                        fireEvent("applovin.close", null);
                    }

                    @Override
                    public void userRewardVerified(final AppLovinAd ad, final Map map) {
                        final String currency = (String) map.get("currency");
                        final String amountStr = (String) map.get("amount");
                        final int amount = (int) Double.parseDouble(amountStr); // AppLovin returns amount as double

                        Log.d(LOG_TAG, "Verified " + amount + " " + currency);

                        JSONObject data = new JSONObject();
                        try {
                            data.put("amount", amount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fireEvent("applovin.reward", data);
                    }

                    @Override
                    public void userOverQuota(final AppLovinAd appLovinAd, final Map map) {
                        Log.d(LOG_TAG, "Rewarded video validation request for ad did exceed quota with response: " + map);
                    }

                    @Override
                    public void validationRequestFailed(final AppLovinAd appLovinAd, final int errorCode) {
                        Log.d(LOG_TAG, "Rewarded video validation request for ad failed with error code: " + errorCode);
                    }

                    @Override
                    public void userRewardRejected(final AppLovinAd appLovinAd, final Map map) {
                        Log.d(LOG_TAG, "Rewarded video validation request was rejected with response: " + map);
                    }
                },
                new AppLovinAdVideoPlaybackListener() {
                    @Override
                    public void videoPlaybackBegan(AppLovinAd ad) {
                        Log.d(LOG_TAG, "Rewarded video playback began");
                    }

                    @Override
                    public void videoPlaybackEnded(AppLovinAd ad, double percentViewed, boolean fullyWatched) {
                        if (fullyWatched) {
                            fireEvent("applovin.completed", null);
                        }
                    }
                },
                new AppLovinAdDisplayListener() {
                    @Override
                    public void adDisplayed(AppLovinAd appLovinAd) {
                        fireEvent("applovin.start", null);
                    }

                    @Override
                    public void adHidden(AppLovinAd appLovinAd) {
                        fireEvent("applovin.close", null);
                    }
                }
        );

        return true;
    }

//    private PluginResult trackEvent(final String event, final JSONArray data, CallbackContext callbackContext) {
//        final AppLovinEventService eventService = AppLovinSdk.getInstance(cordova.getActivity()).getEventService();
//
//        Map<String, String> parameters = new HashMap<String, String>();
//        String eventName;
//        if (event.equalsIgnoreCase("USER_COMPLETED_LEVEL")) {
//            eventName = AppLovinEventTypes.USER_COMPLETED_LEVEL;
//            parameters.put(AppLovinEventParameters.COMPLETED_LEVEL_IDENTIFIER, data.optString(0, "0"));
//
//        } else if (event.equalsIgnoreCase("USER_COMPLETED_IN_APP_PURCHASE")) {
//            eventName = AppLovinEventTypes.USER_COMPLETED_IN_APP_PURCHASE;
//            parameters.put(AppLovinEventParameters.REVENUE_AMOUNT, data.optString(0, "0"));
//            parameters.put(AppLovinEventParameters.REVENUE_CURRENCY, data.optString(1, "USD"));
//            parameters.put(AppLovinEventParameters.IN_APP_PURCHASE_TRANSACTION_IDENTIFIER, data.optString(2, ""));
//
//        } else if (event.equalsIgnoreCase("USER_SPENT_VIRTUAL_CURRENCY")) {
//            eventName = AppLovinEventTypes.USER_SPENT_VIRTUAL_CURRENCY;
//            parameters.put(AppLovinEventParameters.VIRTUAL_CURRENCY_AMOUNT, data.optString(0, "0"));
//            parameters.put(AppLovinEventParameters.VIRTUAL_CURRENCY_NAME, data.optString(1, ""));
//
//        } else if (event.equalsIgnoreCase("USER_COMPLETED_TUTORIAL")) {
//            eventName = AppLovinEventTypes.USER_COMPLETED_TUTORIAL;
//
//        } else if (event.equalsIgnoreCase("USER_VIEWED_PRODUCT")) {
//            eventName = AppLovinEventTypes.USER_VIEWED_PRODUCT;
//            parameters.put(AppLovinEventParameters.PRODUCT_IDENTIFIER, data.optString(0, ""));
//
//        } else if (event.equalsIgnoreCase("USER_LOGGED_IN")) {
//            eventName = AppLovinEventTypes.USER_LOGGED_IN;
//
//        } else {
//            return null;
//        }
//
//        eventService.trackEvent(eventName, parameters);
//
//        return null;
//    }

    private void fireEvent(String eventName, JSONObject jsonObj) {
        String data = "";
        if (jsonObj != null) {
            data = jsonObj.toString();
        }
//
        StringBuilder js = new StringBuilder();
        js.append("javascript:cordova.fireDocumentEvent('");
        js.append(eventName);
        js.append("'");
        if (data != null && !"".equals(data)) {
            js.append(",");
            js.append(data);
        }
        js.append(");");
//
        final String code = js.toString();

        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(code);
            }
        });


    }
}
