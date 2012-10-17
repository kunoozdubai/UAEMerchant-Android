package com.uaemerchant.dialogs;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uaemerchant.R;
import com.uaemerchant.asynctask.DataDownloadTask;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.common.Utilities;
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
	String address;
	String description;
	boolean location;
	String longitude;
	String latitude;
	
	private Ad ad = null;
	
	private static HashMap<Integer, String> imagePaths = new HashMap<Integer, String>(3); 
	
	
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
		
		Spinner city = (Spinner) findViewById(R.id.cityInput);
		ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(context, R.array.cities, android.R.layout.simple_spinner_item);
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		city.setAdapter(adapterCity);
		
		Spinner category = (Spinner) findViewById(R.id.categoryInput); 
		ArrayAdapter<CharSequence> adaptergCategory = ArrayAdapter.createFromResource(context, R.array.category, android.R.layout.simple_spinner_item);
		adaptergCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category.setAdapter(adaptergCategory);
		
		
	}

	@Override
	public void hide() {
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.postBtn){

			prepareData();
			if(ad != null){
				showAddPicturesDialog();
			}else{
				Toast.makeText(context, "Please fill in the form!", Toast.LENGTH_SHORT).show();
			}
		}

	}

	
	private void showRegisterAlertDialog() {
		Builder alertBuilder = new Builder(context);
        
		alertBuilder.setTitle("Register!");
        alertBuilder.setMessage("You need to register to post your ads");
        
        alertBuilder.setNegativeButton("No Thanks", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				hide();
			}
		});

        alertBuilder.setPositiveButton("Register", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open registration dialog
				new RegisterDialog(context).show();
				hide();
			}
		});

        alertBuilder.create().show(); 
	}
	
	private void showAddPicturesDialog() {
		Builder alertBuilder = new Builder(context);
        
		alertBuilder.setTitle("Add Pictures");
        alertBuilder.setMessage("Would you like to add pictures to the Ad for $1");
        
        alertBuilder.setNegativeButton("No, Post Ad", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				postData();
				hide();
			}
		});

        alertBuilder.setPositiveButton("Add Pictures", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open dialog to add pictures
				
			
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
			Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();

			hide();				
			
		}

		@Override
		public void onError(JSONObject response) {
			Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show();
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
		address = ((EditText) findViewById(R.id.addressInput)).getText().toString();
		description  = ((EditText) findViewById(R.id.descriptionInput)).getText().toString();
		location  = ((ToggleButton) findViewById(R.id.locationInput)).isChecked();
		if(location){
			//get current GPS location of the device and put in longitude and latitude variables;
			
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
//		requestData.append("&");
//		requestData.append(NetworkConstants.PHOTO_1);
//		requestData.append("=");
//		requestData.append("");
//		requestData.append("&");
//		requestData.append(NetworkConstants.PHOTO_2);
//		requestData.append("=");
//		requestData.append("");
//		requestData.append("&");
//		requestData.append(NetworkConstants.PHOTO_3);
//		requestData.append("=");
//		requestData.append("");
		
		
		
			
		return requestData.toString();
	}

}