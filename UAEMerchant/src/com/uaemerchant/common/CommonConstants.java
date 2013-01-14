package com.uaemerchant.common;

import android.app.Dialog;

import com.uaemerchant.pojo.Ad;



public class CommonConstants {
	
	public static final String USER_DEFAULT_PREFERENCES = "UAEMerchatSharedPref";
	
	public static final String PREF_NAME = "name"; 
	public static final String PREF_EMAIL = "email";
	public static final String PREF_PHONE = "phone";
	public static final String PREF_CITY = "city";
	public static final String PREF_USER_ID = "userId"; 
	
	
	public static double LATITUDE;
	public static double LONGITUDE;
	
	public static double CURRENT_LATITUDE;
	public static double CURRENT_LONGITUDE;
	
	public static String MERCHANT_IMAGE_DIR = "/mnt/sdcard/UAEMerchant/";
	
	// in app
	public static int DIALOG_CANNOT_CONNECT_ID = 1;
	public static int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
	public static String INAPP_PRODUCT_ID = "android.test.purchased";
	
	public static IResponseListener POST_LISTENER = null;
	public static Ad AD = null;
	public static Dialog POST_DIALOG_CONTEXT;
	

}
