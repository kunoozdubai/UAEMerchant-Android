/*
 * Copyright 2004 - Present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uaemerchant.facebook;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.uaemerchant.common.Utilities;
import com.uaemerchant.facebook.Facebook.DialogListener;
import com.uaemerchant.facebook.SessionEvents.AuthListener;
import com.uaemerchant.facebook.SessionEvents.LogoutListener;

public class FacebookHandler{

	public static final String APP_ID = "415714138477887"; // Uaemerchant facebook app id
	private Handler mHandler;
	private Context context;
	private Activity activity;
	private static FacebookHandler facebookHandlerInstance;
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	final static int PICK_EXISTING_PHOTO_RESULT_CODE = 1;
	String[] main_items = { "Update Status", "App Requests", "Get Friends", "Upload Photo",
			"Place Check-in", "Run FQL Query", "Graph API Explorer", "Token Refresh" };
	String[] permissions = { "offline_access", "publish_stream", "user_photos", "user_videos", "publish_checkins",
	"photo_upload" };
	private String userProfilePicPath = null;
	private AuthListener authListener = null;

	/** Called when the activity is first created. */
	private FacebookHandler(Context ctx) {
		context = ctx;
		activity = (Activity) context;
		if (APP_ID == null) {
			Util.showAlert(context, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running this example: see FbAPIs.java");
			return;
		}
		mHandler = new Handler(context.getMainLooper());
		Utilities.mFacebook = new Facebook(APP_ID);
		SessionStore.restore(Utilities.mFacebook, context);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());

		Utilities.mAsyncRunner = new AsyncFacebookRunner(Utilities.mFacebook);
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
	}

	public void Login(AuthListener listener){
		this.authListener = listener;
			if (Utilities.mFacebook.isSessionValid()) {
				return;
//				SessionEvents.onLogoutBegin();
//				Utilities.mAsyncRunner.logout(context, new LogoutRequestListener());
			} else {
				Utilities.mFacebook.authorize(activity, permissions, AUTHORIZE_ACTIVITY_RESULT_CODE, new LoginDialogListener());
			}
	}
	public static FacebookHandler getInstance(Context context) {
		if (facebookHandlerInstance == null) {
			facebookHandlerInstance = new FacebookHandler(context);
		}
		return facebookHandlerInstance;
	}

	private class LogoutRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SessionEvents.onLogoutFinish();
				}
			});
		}
	}

	private final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(FacebookError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(DialogError error) {
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			SessionEvents.onLoginError("Action Canceled");
		}
	}

	public void sharePhoto(String path, String caption){
		if (Utilities.mFacebook.isSessionValid()) {
			Uri photoUri = Uri.parse(path);
			photoUri = Uri.fromFile(new File(path));
			photoUri = Uri.parse("file:///sdcard/Pictures/nobody.jpg");
			photoUri = Uri.parse("content://media/external/images/media/nobody");
			if (photoUri != null) {
				Bundle params = new Bundle();
				try {
					params.putByteArray("photo",
							Utilities.scaleTempImage(context, path));
				} catch (IOException e) {
					e.printStackTrace();
				}
				params.putString("caption", caption);
				Utilities.mAsyncRunner.request("me/photos", params, "POST",
						new PhotoUploadListener(), null);
			} else {
				Toast.makeText(context,"Error selecting image from the gallery.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	/*
	 * callback for the photo upload
	 */
	public class PhotoUploadListener extends BaseRequestListener {

		@Override 
		public void onComplete(final String response, final Object state) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, "photoupload success", Toast.LENGTH_SHORT).show();
				}
			});
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(context, "photoupload Error: " + error.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 */

	public class FbAPIsAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {
//			Configuration.isFaceBookLogin = true;
//			if(Configuration.mediaType != -1)
//			new ShareOptionsDialog(context).showSourceDialog(Configuration.mediaPath, Configuration.mediaType);
			authListener.onAuthSucceed();
			requestUserData();
		}

		@Override
		public void onAuthFail(String error) {
			authListener.onAuthFail(error);
			requestUserData();
		}
	}

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
		}
	}

	/*
	 * Request user name, and picture to show on the main screen.
	 */
	public void requestUserData() {
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		Utilities.mAsyncRunner.request("me", params, new UserRequestListener());
	}   

	/*
	 * Callback for fetching current user's name, picture, uid.
	 */
	public class UserRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(response);

				userProfilePicPath = jsonObject.getString("picture");
				final String name = jsonObject.getString("name");
				Utilities.userUID = jsonObject.getString("id");
				
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						//                      mText.setText("Welcome " + name + "!");
//						mUserPic.setVisibility(View.VISIBLE);
//						mUserPic.setImageBitmap(Utilities.getBitmap(picURL));
//						mUserPic.bringToFront();
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
}
