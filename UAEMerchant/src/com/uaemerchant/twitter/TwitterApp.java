/**
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 * Website: http://www.londatiga.net
 */

package com.uaemerchant.twitter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

public class TwitterApp {
	public static Twitter mTwitter;
	private TwitterSession mSession;
	private AccessToken mAccessToken;
	private CommonsHttpOAuthConsumer mHttpOauthConsumer;
	private OAuthProvider mHttpOauthprovider;
	private String mConsumerKey;
	private String mSecretKey;
	private ProgressDialog mProgressDlg;
	private TwDialogListener mListener;
	private Context context;
	
	private static final String TAG = "TwitterApp";
	
	public TwitterApp(Context context, String consumerKey, String secretKey) {
		this.context	= context;

		mTwitter 		= new TwitterFactory().getInstance();
		
		mSession		= new TwitterSession(context);
		mProgressDlg	= new ProgressDialog(context);
		
		mProgressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mConsumerKey 	= consumerKey;
		mSecretKey	 	= secretKey;
	
		mHttpOauthConsumer = new CommonsHttpOAuthConsumer(mConsumerKey, mSecretKey);
		
		mHttpOauthprovider = new CommonsHttpOAuthProvider("http://twitter.com/oauth/request_token",
													 "http://twitter.com/oauth/access_token",
													 "http://twitter.com/oauth/authorize");
		mAccessToken	= mSession.getAccessToken();
		
		configureToken();

	}
	
	public void setListener(TwDialogListener listener) {
		mListener = listener;
	}
	
	@SuppressWarnings("deprecation")
	private void configureToken() {
		if (mAccessToken != null) {
			mTwitter.setOAuthConsumer(mConsumerKey, mSecretKey);
			
			mTwitter.setOAuthAccessToken(mAccessToken);
		}
	}
	
	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}
	
	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
		
			mAccessToken = null;
		}
	}
	
	public String getUsername() {
		return mSession.getUsername();
	}
	
	public Bitmap importProfilePicture(){
		try {
			
			User user = mTwitter.showUser(mTwitter.getId());
			URL url = user.getProfileImageURL();
			Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
			return bitmap;
		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateStatus(String status) throws Exception {
		try {
			mTwitter.updateStatus(status);
		} catch (TwitterException e) {
			throw e;
		}
	}
	
	public void updateStatusMedia(String status, File file) throws Exception {
		
		StatusUpdate twitterStatus = new StatusUpdate(status);
		twitterStatus.setMedia(file);
		
		try {
			mTwitter.updateStatus(twitterStatus);
		} catch (TwitterException e) {
			throw e;
		}
	}
	
	public void authorize() {
		mProgressDlg.setMessage("Initializing ...");
		mProgressDlg.show();
		
		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;
				
				try {
					authUrl = mHttpOauthprovider.retrieveRequestToken(mHttpOauthConsumer, TwitterGlobals.CALLBACK_URL);	
					
					what = 0;
					
					Log.d(TAG, "Request token url " + authUrl);
				} catch (Exception e) {
					Log.d(TAG, "Failed to get request token");
					
					e.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}
	
	public void processToken(String callbackUrl)  {
		mProgressDlg.setMessage("Finalizing ...");
		mProgressDlg.show();
		
		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;
				
				try {
					mHttpOauthprovider.retrieveAccessToken(mHttpOauthConsumer, verifier);
		
					mAccessToken = new AccessToken(mHttpOauthConsumer.getToken(), mHttpOauthConsumer.getTokenSecret());
				
					configureToken();
				
					User user = mTwitter.verifyCredentials();
				
			        mSession.storeAccessToken(mAccessToken, user.getName());
			        
			        what = 0;
				} catch (Exception e){
					Log.d(TAG, "Error getting access token");
					
					e.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}
	
	private String getVerifier(String callbackUrl) {
		String verifier	 = "";
		
		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");
			
			URL url 		= new URL(callbackUrl);
			String query 	= url.getQuery();
		
			String array[]	= query.split("&");

			for (String parameter : array) {
	             String v[] = parameter.split("=");
	             
	             if (URLDecoder.decode(v[0]).equals(oauth.signpost.OAuth.OAUTH_VERIFIER)) {
	            	 verifier = URLDecoder.decode(v[1]);
	            	 break;
	             }
	        }
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return verifier;
	}
	
	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {
			@Override
			public void onComplete(String value) {
				processToken(value);
			}
			
			@Override
			public void onError(String value) {
				mListener.onError("Failed opening authorization page");
			}
		};
		
		new TwitterDialog(context, url, listener).show();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgressDlg.dismiss();
			
			if (msg.what == 1) {
				if (msg.arg1 == 1)
					mListener.onError("Error getting request token");
				else
					mListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					mListener.onComplete("");
			}
		}
	};
	
	public interface TwDialogListener {
		public void onComplete(String value);		
		
		public void onError(String value);
	}
}