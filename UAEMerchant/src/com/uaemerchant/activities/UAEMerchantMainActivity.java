package com.uaemerchant.activities;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.dialogs.AccountDialog;
import com.uaemerchant.dialogs.AddPicturesDialog;
import com.uaemerchant.dialogs.PostDialog;
import com.uaemerchant.inapp.BillingService;
import com.uaemerchant.inapp.BillingService.RequestPurchase;
import com.uaemerchant.inapp.BillingService.RestoreTransactions;
import com.uaemerchant.inapp.InAppBillingConstants;
import com.uaemerchant.inapp.InAppBillingConstants.PurchaseState;
import com.uaemerchant.inapp.InAppBillingConstants.ResponseCode;
import com.uaemerchant.inapp.PurchaseObserver;
import com.uaemerchant.inapp.ResponseHandler;
import com.uaemerchant.twitter.TwitterApp;

public class UAEMerchantMainActivity extends Activity implements android.view.View.OnClickListener{

	private Context context;
	private TwitterApp mTwitter = null;
	private static String imagePath = "";
	
	/** Start: inApp */
	public String mPayloadContents = null;
	public InAppPurchaseObserver mInAppPurchaseObserver;
	private Handler mHandler;
	private ResponseHandler responseHandler;
	public BillingService mBillingService;
	/** End: inApp */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		context = this;
		
		Utilities.mainActivityContext = context;
		
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background, Utilities.getBitmapFactoryoptions(1));
//		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//		Utilities.imageMap.put("mainBackground", bitmapDrawable);
//		bitmapDrawable = null;
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_background, Utilities.getBitmapFactoryoptions(1));
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		Utilities.imageMap.put("titleBackground", bitmapDrawable);
		bitmapDrawable = null;
		
//		ImageView imageView = (ImageView) findViewById(R.id.background);
//		imageView.setBackgroundDrawable(Utilities.imageMap.get("mainBackground"));
		
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.titleParent);
		relativeLayout.setBackgroundDrawable(Utilities.imageMap.get("titleBackground"));
		
		
		Button button = (Button) findViewById(R.id.btnAccount);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnPostAd);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.btnCarNumberPlate);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnMobilePhoneNumbers);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnElectronics);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnCarAndEngines);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnRealEstate);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnLadiesOnly);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnServices);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnFurniture);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnOthers);
		button.setOnClickListener(this);
		
		responseHandler = new ResponseHandler(this);
		mHandler = new Handler();
		mInAppPurchaseObserver = new InAppPurchaseObserver(mHandler);
		mBillingService = new BillingService();
		Log.i("InApp", "start BillingService() ");
		mBillingService.setContext(this);
		Utilities.setBillingSupported(mBillingService.checkBillingSupported());
		
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		if(id == R.id.btnAccount){
			new AccountDialog(context).show();
			Toast.makeText(context, "btnAccount Clicked", Toast.LENGTH_SHORT).show();
		}else if(id == R.id.btnPostAd){
			new PostDialog(context).show();
			Toast.makeText(context, "btnPostAd Clicked", Toast.LENGTH_SHORT).show();
		}else if(id == R.id.btnCarNumberPlate || id == R.id.btnMobilePhoneNumbers || id == R.id.btnElectronics
				|| id == R.id.btnCarAndEngines || id == R.id.btnRealEstate || id == R.id.btnLadiesOnly 
				|| id == R.id.btnServices || id == R.id.btnFurniture || id == R.id.btnOthers){
			
			String catId = getCatId(id);
			String catName = getCatName(id);
			
			Intent intent = new Intent(context, CategoryListActivity.class);
			intent.putExtra("catId", catId);
			intent.putExtra("catName", catName);
			startActivity(intent);
		}
	}

	private String getCatName(int id) {

		if (id == R.id.btnCarNumberPlate) {
			return getString(R.string.car_number_plates_txt);
		}
		if (id == R.id.btnMobilePhoneNumbers) {
			return getString(R.string.mobile_phone_numbers_txt);
		}
		if (id == R.id.btnElectronics) {
			return getString(R.string.electronics_txt);
		}
		if (id == R.id.btnCarAndEngines) {
			return getString(R.string.car_and_engines_txt);
		}
		if (id == R.id.btnRealEstate) {
			return getString(R.string.real_estate_txt);
		}
		if (id == R.id.btnLadiesOnly) {
			return getString(R.string.ladies_only_txt);
		}
		if (id == R.id.btnServices) {
			return getString(R.string.services_txt);
		}
		if (id == R.id.btnFurniture) {
			return getString(R.string.furniture_txt);
		}
		if (id == R.id.btnOthers) {
			return getString(R.string.others_txt);
		}
		return "";
	}

	private String getCatId(int id) {
		
		if (id == R.id.btnCarNumberPlate) {
			return "1";
		}
		if (id == R.id.btnMobilePhoneNumbers) {
			return "2";
		}
		if (id == R.id.btnElectronics) {
			return "3";
		}
		if (id == R.id.btnCarAndEngines) {
			return "4";
		}
		if (id == R.id.btnRealEstate) {
			return "5";
		}
		if (id == R.id.btnLadiesOnly) {
			return "6";
		}
		if (id == R.id.btnServices) {
			return "7";
		}
		if (id == R.id.btnFurniture) {
			return "8";
		}
		if (id == R.id.btnOthers) {
			return "9";
		}
		return "";
	}
	
	public static void setImagePath(String path){
		imagePath = path;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				AddPicturesDialog.setPhoto(imagePath);	
			} 
		}else if(requestCode == 200){
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();
				AddPicturesDialog.setPhoto(imagePath);
			}
		}
		
	}
	
	public TwitterApp getmTwitter() {
		return mTwitter ;
	}

	public void setmTwitter(TwitterApp mTwitter) {
		this.mTwitter = mTwitter;
	}
	
	
	private class InAppPurchaseObserver extends PurchaseObserver {
		public InAppPurchaseObserver(Handler handler) {
			super(UAEMerchantMainActivity.this, handler);
		}
		
		@Override
		public void onBillingSupported(boolean supported) {
			if (InAppBillingConstants.DEBUG) {
//				Log.d(TapResortConstants.TAG, "supported: " + supported);
			}
			Utilities.setBillingSupported(supported);
			if (supported) {

			} 
		}

		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState,
				String itemId, /*int quantity, */int boughtShellsQuantity,long purchaseTime,
				String developerPayload) {
			// TODO Auto-generated method stub
			if (InAppBillingConstants.DEBUG) {
//				Log.d(Consts.TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
			}

			if (developerPayload == null) {
				//                logProductActivity(itemId, purchaseState.toString());
			} else {
				//                logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
			}
		}


		@Override
		public void onRequestPurchaseResponse(RequestPurchase request,
				ResponseCode responseCode) {
			// TODO Auto-generated method stub
			if (InAppBillingConstants.DEBUG) {
//				Log.d(TapResortConstants.TAG, request.mProductId + ": " + responseCode);
			}
			if (responseCode == ResponseCode.RESULT_OK) {
				if (InAppBillingConstants.DEBUG) {
//					Log.d(TapResortConstants.TAG, "purchase was successfully sent to server");
				}
				//                logProductActivity(request.mProductId, "sending purchase request");
			} else if (responseCode == InAppBillingConstants.ResponseCode.RESULT_USER_CANCELED) {
				if (InAppBillingConstants.DEBUG) {
//					Log.d(TapResortConstants.TAG, "user canceled purchase");
				}
				//                logProductActivity(request.mProductId, "dismissed purchase dialog");
			} else {
				if (InAppBillingConstants.DEBUG) {
//					Log.d(TapResortConstants.TAG, "purchase failed");
//					Toast.makeText(TapResortActivity.this, " Transaction Failed !", Toast.LENGTH_SHORT);
				}
				//                logProductActivity(request.mProductId, "request purchase returned " + responseCode);
			}
		}

		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request,
				ResponseCode responseCode) {
			// TODO Auto-generated method stub
			if (responseCode == ResponseCode.RESULT_OK) {
				if (InAppBillingConstants.DEBUG) {
//					Log.d(TapResortConstants.TAG, "completed RestoreTransactions request");
				}

			} else {
				if (InAppBillingConstants.DEBUG) {
//					Log.d(TapResortConstants.TAG, "RestoreTransactions error: " + responseCode);
				}
			}
		}
	}
	
	public void createBillingDialog(int id) {
		switch(id) {
		case 1: //CommonConstants.DIALOG_CANNOT_CONNECT_ID:
			createBillingResponseDialog(R.string.cannot_connect_title,
					R.string.cannot_connect_message);
			break;
		case 2: //CommonConstants.DIALOG_BILLING_NOT_SUPPORTED_ID:
			createBillingResponseDialog(R.string.billing_not_supported_title,
					R.string.billing_not_supported_message);
			break;
		}
	}
	private void createBillingResponseDialog(int titleId, int messageId) {
		String helpUrl = replaceLanguageAndRegion(getString(R.string.help_url));
		final Uri helpUri = Uri.parse(helpUrl);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(titleId);
		builder.setIcon(android.R.drawable.stat_sys_warning);
		builder.setMessage(messageId);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setNegativeButton(R.string.learn_more, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW, helpUri);
				startActivity(intent);
			}
		});
		builder.create();
		builder.show();
	}
	private String replaceLanguageAndRegion(String str) {
		// Substitute language and or region if present in string
		if (str.contains("%lang%") || str.contains("%region%")) {
			Locale locale = Locale.getDefault();
			str = str.replace("%lang%", locale.getLanguage().toLowerCase());
			str = str.replace("%region%", locale.getCountry().toLowerCase());
		}
		return str;
	}

	
}