package com.uaemerchant.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uaemerchant.R;
import com.uaemerchant.asynctask.DataDownloadTask;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.inapp.BillingService;
import com.uaemerchant.inapp.BillingService.RequestPurchase;
import com.uaemerchant.inapp.BillingService.RestoreTransactions;
import com.uaemerchant.inapp.Consts;
import com.uaemerchant.inapp.Consts.PurchaseState;
import com.uaemerchant.inapp.Consts.ResponseCode;
import com.uaemerchant.inapp.PurchaseObserver;
import com.uaemerchant.inapp.ResponseHandler;
import com.uaemerchant.pojo.Ad;

public class PostDialog extends Dialog implements View.OnClickListener, OnCancelListener {
	private View postView;
	private Context context = null;
	private Activity activity;
	
	String userId;
	String title;
	String price;
	String category;
	String city;
	String address = "";
	String description;
	boolean location;
	String longitude = "";
	String latitude = "";
	
	private Ad ad = null;
	
	private static HashMap<Integer, String> imagePaths = new HashMap<Integer, String>(3);
	
	/// in app
	
	private static final String TAG = "PostDialog";
	
	private DungeonsPurchaseObserver mDungeonsPurchaseObserver;
    private Handler mHandler;

    private BillingService mBillingService;
    
    /**
     * The developer payload that is sent with subsequent
     * purchase requests.
     */
    private String mPayloadContents = null;

    private static final int DIALOG_CANNOT_CONNECT_ID = 1;
    private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
    
    //////////
	
	
	public PostDialog(Context context) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		postView = layoutInflater.inflate(R.layout.post_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(postView);
	}

	@Override
	public void show() {
		super.show();
		userId = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_USER_ID, "");
		if(!Utilities.isStringEmptyOrNull(userId)){
			initializeViews();
		}else{
			showRegisterAlertDialog();
		}
	}

	private void initializeViews() {
		
		Button button = (Button) postView.findViewById(R.id.postBtn);
		button.setOnClickListener(this); 
		
		button = (Button) postView.findViewById(R.id.btnCategories);
		button.setOnClickListener(this);
		
		Spinner city = (Spinner) findViewById(R.id.cityInput);
		ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(context, R.array.cities, R.layout.simple_spinner_selected_layout);
//		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCity.setDropDownViewResource(R.layout.simple_spinner_layout);
		city.setAdapter(adapterCity);
		
		Spinner category = (Spinner) findViewById(R.id.categoryInput);
		ArrayAdapter<CharSequence> adaptergCategory = ArrayAdapter.createFromResource(context, R.array.category, R.layout.simple_spinner_selected_layout);
//		adaptergCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adaptergCategory.setDropDownViewResource(R.layout.simple_spinner_layout);
		category.setAdapter(adaptergCategory);
		
		
	}

	@Override
	public void hide() {
		if(mBillingService != null){
			ResponseHandler.unregister(mDungeonsPurchaseObserver);
			mBillingService.unbind();
		}
		
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.postBtn){

			prepareData();
			if(ad != null){
				if(location){
					if("".equals(ad.getLongitude()) || "".equals(ad.getLatitude())){
						showLocationAlertDialog();
						return;
					}	
				}
				showAddPicturesDialog();
				
			}else{
				Toast.makeText(context, "Please fill in the form!", Toast.LENGTH_SHORT).show();
			}
		}
		if(id == R.id.btnCategories){
			hide();
		}

	}

	
	private void showRegisterAlertDialog() {
		Builder alertBuilder = new Builder(context);
        alertBuilder.setCancelable(false);
		alertBuilder.setTitle(context.getString(R.string.register_title));
        alertBuilder.setMessage(context.getString(R.string.register_msg));
        
        alertBuilder.setNegativeButton(context.getString(R.string.no_thanks), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				hide();
			}
		});

        alertBuilder.setPositiveButton(context.getString(R.string.register_title), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open registration dialog
				new RegisterDialog(context).show();
				hide();
			}
		});

        alertBuilder.create().show(); 
	}
	
	private void showLocationAlertDialog() {
		Builder alertBuilder = new Builder(context);
        alertBuilder.setCancelable(true);
		alertBuilder.setTitle(context.getString(R.string.location_txt));
        alertBuilder.setMessage(context.getString(R.string.location_msg));
        
        alertBuilder.setNegativeButton(context.getString(R.string.try_again), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PostDialog.this.onClick(postView.findViewById(R.id.postBtn));
			}
		});

        alertBuilder.setPositiveButton(context.getString(R.string.post_anyway), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open registration dialog
				showAddPicturesDialog();
				
			}
		});

        alertBuilder.create().show(); 
	}
	
	public void showAddPicturesDialog() {
		Builder alertBuilder = new Builder(context);
        
		alertBuilder.setTitle(context.getString(R.string.add_pictures));
        alertBuilder.setMessage(context.getString(R.string.add_pictures_msg));
        
        alertBuilder.setNegativeButton(context.getString(R.string.no_post_ad), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				postData();
				//hide();
			}
		});

        alertBuilder.setPositiveButton(context.getString(R.string.add_pictures), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open dialog to add pictures
///////////uncomment this part to enable in app purchase
//				Utilities.showprogressDialog(context, "Making transaction, please wait!");
//				mHandler = new Handler();
//		        mDungeonsPurchaseObserver = new DungeonsPurchaseObserver(mHandler);
//		        mBillingService = new BillingService();
//		        mBillingService.setContext(context);
//////////////////////////////////////////////////////////
				
//		        mPurchaseDatabase = new PurchaseDatabase(this);
		
/////////////////uncomment this part as well to for in-aap purchase				
		        // Check if billing is supported.
//		        ResponseHandler.register(mDungeonsPurchaseObserver);
//		        if (!mBillingService.checkBillingSupported()) {
//		            createBillingDialog(DIALOG_CANNOT_CONNECT_ID);
//		        }
//		        ResponseHandler.register(mDungeonsPurchaseObserver);
				
//				CommonConstants.AD = ad;
//				CommonConstants.POST_LISTENER = new PostResponse();
//				CommonConstants.POST_DIALOG_CONTEXT = PostDialog.this;
//				startInApp();
////////////////////////////////////////////
				new AddPicturesDialog(context, ad, new PostResponse()).show();
			}
		});

        alertBuilder.create().show(); 
	}
	
	private OnKeyListener postKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			}
			if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_DOWN)) {
				hide();

				return true;
			}

			return false;
		}
	};
	
	private class PostResponse implements IResponseListener {

		@Override
		public void onSuccess(JSONObject response) {
//			Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();

			hide();				
			
		}

		@Override
		public void onError(JSONObject response) {
//			Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		context = null;
		activity = null;
		Utilities.unbindDrawables(findViewById(R.id.post_dialog));
	}
	
	
	private void prepareData(){
		
		title  = ((EditText) findViewById(R.id.titleInput)).getText().toString();
		if(Utilities.isStringEmptyOrNull(title)){
			Toast.makeText(context, "Please provide a title!", Toast.LENGTH_SHORT);
			return;
		}
		category = ((Spinner) findViewById(R.id.categoryInput)).getSelectedItem().toString();
		if(Utilities.isStringEmptyOrNull(category)){
			Toast.makeText(context, "Please provide a Category!", Toast.LENGTH_SHORT);
			return;
		}
		price  = ((EditText) findViewById(R.id.priceInput)).getText().toString();
		if(Utilities.isStringEmptyOrNull(price)){
			Toast.makeText(context, "Please provide a price!", Toast.LENGTH_SHORT);
			return;
		}
		city = ((Spinner) findViewById(R.id.cityInput)).getSelectedItem().toString();
		//address = ((EditText) findViewById(R.id.addressInput)).getText().toString();
		description  = ((EditText) findViewById(R.id.descriptionInput)).getText().toString();
		location  = ((ToggleButton) findViewById(R.id.locationInput)).isChecked();
		if (location) {
			// get current GPS location of the device and put in longitude and
			// latitude variables;
			Geocoder geocoder;
			String bestProvider;
			List<Address> user = null;
			double lat;
			double lng;

			LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

			Criteria criteria = new Criteria();
			bestProvider = lm.getBestProvider(criteria, false);
			Location location = lm.getLastKnownLocation(bestProvider);

			if (location == null) {
//				Toast.makeText(activity, "Location Not found", Toast.LENGTH_LONG).show();
			} else {
				geocoder = new Geocoder(activity);
				try {
					user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
					lat = (double) user.get(0).getLatitude();
					lng = (double) user.get(0).getLongitude();
					longitude = String.valueOf(lng);
					latitude = String.valueOf(lat);
					
					if("null".equals(longitude)){
						longitude = "";
					}
					if("null".equals(latitude)){
						latitude = "";
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			longitude = "";
			latitude = "";
		}
		category = Utilities.getCatId(category);
		ad = new Ad("", userId, category, title, price, city, address, longitude, latitude, description, "", "", "", "", "", "", "", "");
		
		
	}
	
	private void postData(){
		
		String postData = createRequestJSON(userId, title, city, address, description, price, category, longitude, latitude);
		
		new DataDownloadTask(context, new PostResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_POST, postData).execute();
	}
	
	private static String createRequestJSON(String userId, String title, String city,String address, String description, String price, String category, String longitude, String latitude) {
		StringBuilder requestData = new StringBuilder();
		requestData.append(NetworkConstants.TITLE);
		requestData.append("=");
		requestData.append(title);
		requestData.append("&");
		requestData.append(NetworkConstants.PRICE);
		requestData.append("=");
		requestData.append(price);
		requestData.append("&");
		requestData.append(NetworkConstants.USER_ID);
		requestData.append("=");
		requestData.append(userId);
		requestData.append("&");
		requestData.append(NetworkConstants.CAT_ID);
		requestData.append("=");
		requestData.append(category);
		requestData.append("&");
		requestData.append(NetworkConstants.ADDRESS);
		requestData.append("=");
		requestData.append(address);
		requestData.append("&");
		requestData.append(NetworkConstants.CITY);
		requestData.append("=");
		requestData.append(city);
		requestData.append("&");
		requestData.append(NetworkConstants.LONGITUDE);
		requestData.append("=");
		requestData.append(longitude);
		requestData.append("&");
		requestData.append(NetworkConstants.LATITUDE);
		requestData.append("=");
		requestData.append(latitude);
		requestData.append("&");
		requestData.append(NetworkConstants.DESCRIPTION);
		requestData.append("=");
		requestData.append(description);
			
		return requestData.toString();
	}
	
	private void startInApp() {
		if (Consts.DEBUG) {
            Log.d(TAG, "buying: " + CommonConstants.INAPP_PRODUCT_ID);
        }
        if (!mBillingService.requestPurchase(CommonConstants.INAPP_PRODUCT_ID, mPayloadContents)) {
            createBillingDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
        }
		
		
//			if (!Utilities.isBillingSupported()) {
//				((UAEMerchantMainActivity)Utilities.mainActivityContext).createBillingDialog(1);
//			}else {
//				Utilities.setIsInAppTransactionInProcess(true);
//				Log.i("InApp", "BEFORE MAKING PURCHASE REQUEST .....  AT ATM ITEM ................ON CLICK");
////				for(int i =0; i<5000; i++){
////					// do Nothing
////				}
//				if(!((UAEMerchantMainActivity)Utilities.mainActivityContext).mBillingService.requestPurchase(CommonConstants.INAPP_PRODUCT_ID, null)){
//					Log.i("InApp", "PURCHASE REQUEST SENT .....  AT ATM ITEM ................ON CLICK...................AFTER");
//					Utilities.setIsInAppTransactionInProcess(false);
//					((UAEMerchantMainActivity)Utilities.mainActivityContext).createBillingDialog(2);
//				}
//			}
	}

	
	/**
     * Each product in the catalog is either MANAGED or UNMANAGED.  MANAGED
     * means that the product can be purchased only once per user (such as a new
     * level in a game). The purchase is remembered by Android Market and
     * can be restored if this application is uninstalled and then
     * re-installed. UNMANAGED is used for products that can be used up and
     * purchased multiple times (such as poker chips). It is up to the
     * application to keep track of UNMANAGED products for the user.
     */
    private enum Managed { MANAGED, UNMANAGED }

    /**
     * A {@link PurchaseObserver} is used to get callbacks when Android Market sends
     * messages to this application so that we can update the UI.
     */
    private class DungeonsPurchaseObserver extends PurchaseObserver {
        public DungeonsPurchaseObserver(Handler handler) {
            super((Activity)context, handler);
        }

        @Override
        public void onBillingSupported(boolean supported) {
            if (Consts.DEBUG) {
                Log.i(TAG, "supported: " + supported);
            }
            if (supported) {
//                restoreDatabase();
//                mBuyButton.setEnabled(true);
//                mEditPayloadButton.setEnabled(true);
            } else {
                createBillingDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
            }
        }

        @Override
        public void onPurchaseStateChange(PurchaseState purchaseState, String itemId,
                int quantity, long purchaseTime, String developerPayload) {
            if (Consts.DEBUG) {
                Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
            }

//            if (developerPayload == null) {
//                logProductActivity(itemId, purchaseState.toString());
//            } else {
//                logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
//            }
//
//            if (purchaseState == PurchaseState.PURCHASED) {
//                mOwnedItems.add(itemId);
//            }
//            mCatalogAdapter.setOwnedItems(mOwnedItems);
//            mOwnedItemsCursor.requery();
        }

        @Override
        public void onRequestPurchaseResponse(RequestPurchase request,
                ResponseCode responseCode) {
            if (Consts.DEBUG) {
                Log.d(TAG, request.mProductId + ": " + responseCode);
            }
            if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase was successfully sent to server");
                }
//                logProductActivity(request.mProductId, "sending purchase request");
            } else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
                if (Consts.DEBUG) {
                    Log.i(TAG, "user canceled purchase");
                }
//                logProductActivity(request.mProductId, "dismissed purchase dialog");
            } else {
                if (Consts.DEBUG) {
                    Log.i(TAG, "purchase failed");
                }
//                logProductActivity(request.mProductId, "request purchase returned " + responseCode);
            }
        }

        @Override
        public void onRestoreTransactionsResponse(RestoreTransactions request,
                ResponseCode responseCode) {
            if (responseCode == ResponseCode.RESULT_OK) {
                if (Consts.DEBUG) {
                    Log.d(TAG, "completed RestoreTransactions request");
                }
                // Update the shared preferences so that we don't perform
                // a RestoreTransactions again.
//                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
//                SharedPreferences.Editor edit = prefs.edit();
//                edit.putBoolean(DB_INITIALIZED, true);
//                edit.commit();
            } else {
                if (Consts.DEBUG) {
                    Log.d(TAG, "RestoreTransactions error: " + responseCode);
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
		String helpUrl = replaceLanguageAndRegion(context.getString(R.string.help_url));
		final Uri helpUri = Uri.parse(helpUrl);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setIcon(android.R.drawable.stat_sys_warning);
		builder.setMessage(messageId);
		builder.setCancelable(false);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setNegativeButton(R.string.learn_more, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_VIEW, helpUri);
				context.startActivity(intent);
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