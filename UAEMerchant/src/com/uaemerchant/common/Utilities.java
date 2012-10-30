package com.uaemerchant.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uaemerchant.activities.UAEMerchantMainActivity;
import com.uaemerchant.facebook.AsyncFacebookRunner;
import com.uaemerchant.facebook.Facebook;

public class Utilities {

	public static AlertDialog alertDialog = null;
	public static ProgressDialog progressDialog = null;

	private static BitmapFactory.Options options = null;

	public static Context mainActivityContext;

	public static HashMap<String, BitmapDrawable> imageMap = new HashMap<String, BitmapDrawable>();
	public static HashMap<String, BitmapDrawable> thumbMap = new HashMap<String, BitmapDrawable>();

	public static Facebook mFacebook;
	public static AsyncFacebookRunner mAsyncRunner;
	public static String userUID = null;
	
	private static boolean isBillingSupported = false;
	private static boolean isInAppTransactionInProcess = false;

	/**
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static String getStringValuesFromPreference(Context context, String name, String defaultValue) {

		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		return myPref.getString(name, defaultValue);

	}

	public static void responseDialog(Context ctx, String title, String errorMessage) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.cancel();
		}
		alertDialog = new AlertDialog.Builder(ctx).create();
		alertDialog.setTitle(title);
		errorMessage = errorMessage.trim();
		alertDialog.setMessage(errorMessage);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				alertDialog.cancel();
			}

		});
		alertDialog.show();
	}

	public static void showprogressDialog(Context ctx, String errorMessage) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
		errorMessage = errorMessage.trim();

		progressDialog = new ProgressDialog(ctx);
		// progressDialog.setCancelable(false);
		progressDialog.setMessage(errorMessage);

		// ((DJIMainActivity) CommonConstants.DJI_MAIN_ACTIVITY_CONTEXT)
		// .runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		progressDialog.show();
		// }
		// });
	}

	public static void cancelprogressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			// ((DJIMainActivity) CommonConstants.DJI_MAIN_ACTIVITY_CONTEXT)
			// .runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			progressDialog.cancel();

			// }
			// });
		}
	}

	/**
	 * @param name
	 * @param value
	 */
	public static void setStringValuesToPreferences(Context context, String name, String value) {
		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPref.edit();

		if (editor != null) {
			editor.putString(name, value);
			editor.commit();
		}
	}

	/**
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBooleanValuesFromPreference(Context context, String name, boolean defaultValue) {

		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		return myPref.getBoolean(name, defaultValue);

	}

	/**
	 * @param name
	 * @param value
	 */
	public static void setBooleanValuesToPreferences(Context context, String name, boolean value) {
		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPref.edit();

		if (editor != null) {
			editor.putBoolean(name, value);
			editor.commit();
		}
	}

	public static int getIntegerValuesFromPreference(Context context, String name, int defaultValue) {

		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		return myPref.getInt(name, defaultValue);

	}

	public static void setIntegerValuesToPreferences(Context context, String name, int value) {
		SharedPreferences myPref = context.getSharedPreferences(CommonConstants.USER_DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPref.edit();

		if (editor != null) {
			editor.putInt(name, value);
			editor.commit();
		}
	}

	/**
	 * 
	 * Check whether network is connected or not
	 * 
	 * @param context
	 *            Context of application
	 * @return true if connected false otherwise
	 */
	public static boolean isNetworkAvailable(Context context) {

		boolean connected = false;
		ConnectivityManager connectivityManager = null;
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (netInfo != null) {
			connected = netInfo.isConnected();
		}
		return connected;
	}

	public static void createSuccessAlertDialog(Context context, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(message);
		alertDialog.setButton("Okay", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				alertDialog.cancel();
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.show();
	}

	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	public static String copyFileInBuffer(InputStream is) throws IOException {
		// StringBuilder data = new StringBuilder();
		// byte[] buffer = new byte[1024];
		// int read;
		// while ((read = is.read(buffer)) != -1) {
		// //out.write(buffer, 0, read);
		// data.append(buffer.toString());
		// buffer = new byte[1024];
		// }

		byte[] data = {};

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int bytesRead;
		try {
			while ((bytesRead = is.read(b)) != -1) {
				bos.write(b, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = bos.toByteArray();

		return Base64.encodeToString(data, Base64.DEFAULT);

	}

	public static boolean isArrayValuesEmptyOrNull(String[] values) {
		for (int i = 0; i < values.length; i++) {
			if (null == values[i] || "".equals(values[i])) {
				return false;
			}
		}
		return true;
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	public final static boolean isValidNumber(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.PHONE.matcher(target).matches();
		}
	}

	public static int downloadImage(String fileName, String url) {
		File folder = new File(CommonConstants.MERCHANT_IMAGE_DIR);
		File f = null;
		if (!folder.exists()) {
			folder.mkdirs();
			f = new File(CommonConstants.MERCHANT_IMAGE_DIR + fileName);
		} else {
			f = new File(CommonConstants.MERCHANT_IMAGE_DIR + fileName);

		}
		HttpURLConnection conn = null;
		try {
			URL imageUrl = new URL(url);
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			copyFile(is, os);
			os.close();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		int statusCode = -1;
		try {
			statusCode = conn.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statusCode;
	}

	public static boolean isStringEmptyOrNull(String checkString) {
		// return !(checkString != null && !"".equals(checkString) &&
		// checkString
		// .length() > 0);
		if ("".equals(checkString) || "null".equals(checkString) || null == checkString) {
			return true;
		}

		return false;
	}

	public static void startMainActivity(Context context) {
		Intent intent = new Intent(context, UAEMerchantMainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void unbindDrawables(View view) {
		if (view != null) {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
				view.getResources().flushLayoutCache();
				view.destroyDrawingCache();
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
			} else {
				view = null;
			}
		}
	}

	public static BitmapFactory.Options getBitmapFactoryoptions(int inSampleSize) {
		if (options == null) {
			options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			options.inDither = true;
			options.inPurgeable = true;
			options.inSampleSize = inSampleSize;
		} else {
			options.inSampleSize = inSampleSize;
		}
		return options;
	}

	public static String readServerResponse(InputStream inputStream) {
		StringBuilder response = new StringBuilder("");
		try {
			char[] buffer = new char[1024];

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));
				while (reader.read(buffer) != -1) {
					response.append(buffer);
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} catch (MalformedURLException malFormedExp) {
			malFormedExp.printStackTrace();

		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}

		if (response == null || response.length() <= 0) {
			response = new StringBuilder("");
		}
		return response.toString();
	}

	public static String getCatId(String name) {

		if (name.equals("Car Number Plates")) {
			return "1";
		}
		if (name.equals("Mobile Phone Numbers")) {
			return "2";
		}
		if (name.equals("Electronics")) {
			return "3";
		}
		if (name.equals("Car and Engines")) {
			return "4";
		}
		if (name.equals("Real Estate")) {
			return "5";
		}
		if (name.equals("Ladies Only")) {
			return "6";
		}
		if (name.equals("Services")) {
			return "7";
		}
		if (name.equals("Furniture")) {
			return "8";
		}
		if (name.equals("Others")) {
			return "9";
		}

		return "";
	}

	public static void call(String number) {
		number = number.trim();
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + number));
			((UAEMerchantMainActivity) mainActivityContext).startActivity(callIntent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(mainActivityContext, "Call Failed", Toast.LENGTH_SHORT).show();
		}
	}

	public static void sms(String number) {

		number = number.trim();
		Uri uri = Uri.parse("smsto:" + number);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		// intent.putExtra("sms_body", message);
		((UAEMerchantMainActivity) mainActivityContext).startActivity(intent);
	}

	public static void email(String email) {
		email = email.trim();
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		String aEmailList[] = { email };
		// String aEmailCCList[] = { "user3@fakehost.com","user4@fakehost.com"};
		// String aEmailBCCList[] = { "user5@fakehost.com" };
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
		// emailIntent.putExtra(android.content.Intent.EXTRA_CC, aEmailCCList);
		// emailIntent.putExtra(android.content.Intent.EXTRA_BCC,
		// aEmailBCCList);
		// emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		// "My subject");
		emailIntent.setType("plain/text");
		// emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
		// "My message body.");
		((UAEMerchantMainActivity) mainActivityContext).startActivity(emailIntent);

	}

	public static void clearThumbMap() {
		Iterator thumbMapIterator = thumbMap.keySet().iterator();

		while (thumbMapIterator.hasNext()) {
			String key = (String) thumbMapIterator.next();
			BitmapDrawable bitmapDrawable = (BitmapDrawable) thumbMap.get(key);
			if (bitmapDrawable != null) {
				bitmapDrawable.getBitmap().recycle();
			}
			thumbMap.put(key, null);

		}

	}

	public static byte[] scaleTempImage(Context context, String photoUri) throws IOException {
		Bitmap srcBitmap;
		srcBitmap = BitmapFactory.decodeFile(photoUri, getBitmapFactoryoptions(2));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		if (srcBitmap != null) {
			srcBitmap.recycle();
			srcBitmap = null;
		}
		return bMapArray;
	}

	public static void setBillingSupported(boolean isBillingSupported) {
		Utilities.isBillingSupported = isBillingSupported;
		Log.i("InApp", " isBillingSupported Flag true");
	}
	public static boolean isBillingSupported() {
		return isBillingSupported;
	}
	
	public static boolean isIsInAppTransactionInProcess() {
		return Utilities.isInAppTransactionInProcess;
	}
	public static void setIsInAppTransactionInProcess(boolean isInAppTransactionInProcess) {
		Utilities.isInAppTransactionInProcess = isInAppTransactionInProcess;
	}
}
