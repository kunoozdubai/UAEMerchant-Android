package com.uaemerchant.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.asynctask.DataUploadTask;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.pojo.Ad;

public class AddPicturesDialog extends Dialog implements View.OnClickListener, OnCancelListener {
	private View addPhotosView;
	private static Context context = null;
	private Activity activity;
	
	private static ImageView photo1;
	private static ImageView photo2;
	private static ImageView photo3;
	private static ImageView removePhoto1;
	private static ImageView removePhoto2;
	private static ImageView removePhoto3;
	
	private static int count = 1;
	
	private static Ad ad;
	private IResponseListener postListener;
	
	public AddPicturesDialog(Context context, Ad ad, IResponseListener postListener) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addPhotosView = layoutInflater.inflate(R.layout.add_photos_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(addPhotosView);
		this.ad = ad;
		this.postListener = postListener;
		
		this.setOnKeyListener(addPicturesKeyListener);
		
		
	}

	@Override
	public void show() {
		super.show();
		count = 1;
		Utilities.setIntegerValuesToPreferences(context, "imageCount", 0);
		initializeViews();

	}

	private void initializeViews() {

		ImageView imageView = (ImageView) addPhotosView.findViewById(R.id.addPhotoBtn);
		imageView.setOnClickListener(this);
		
		Button button = (Button) addPhotosView.findViewById(R.id.postBtn);
		button.setOnClickListener(this);
		
		photo1 = (ImageView)  findViewById(R.id.photo1);
		photo1.setOnClickListener(this);
		photo2 = (ImageView)  findViewById(R.id.photo2);
		photo2.setOnClickListener(this);
		photo3 = (ImageView)  findViewById(R.id.photo3);
		photo3.setOnClickListener(this);
		
		removePhoto1 = (ImageView)  findViewById(R.id.removePhoto1);
		removePhoto1.setOnClickListener(this);
		removePhoto2 = (ImageView)  findViewById(R.id.removePhoto2);
		removePhoto2.setOnClickListener(this);
		removePhoto3 = (ImageView)  findViewById(R.id.removePhoto3);
		removePhoto3.setOnClickListener(this);

	}

	@Override
	public void hide() {
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.addPhotoBtn) {
			
			//show photo picker dialog
			int c = Utilities.getIntegerValuesFromPreference(context, "imageCount", 0);
			if(c < 3){
				new PhotoOptionsDialog(context).show();
				
			}else{
				Toast.makeText(context, "You can only add 3 Photos", Toast.LENGTH_SHORT).show();
			}
		}else if(id == R.id.removePhoto1){
			photo1.setImageBitmap(null);
			photo1.setVisibility(View.GONE);
			removePhoto1.setVisibility(View.GONE);
			count = 1;
			ad.setPhoto1("");
			int c = Utilities.getIntegerValuesFromPreference(context, "imageCount", 0);
			if(c != 0){
				c--;
				Utilities.setIntegerValuesToPreferences(context, "imageCount", c);
			}else{
				count = 1;
			}
		}else if(id == R.id.removePhoto2){
			photo2.setImageBitmap(null);
			photo2.setVisibility(View.GONE);
			removePhoto2.setVisibility(View.GONE);
			count = 2;
			ad.setPhoto2("");
			int c = Utilities.getIntegerValuesFromPreference(context, "imageCount", 0);
			if(c != 0){
				c--;
				Utilities.setIntegerValuesToPreferences(context, "imageCount", c);
			}
			else{
				count = 1;
			}
		}else if(id == R.id.removePhoto3){
			photo3.setImageBitmap(null);
			photo3.setVisibility(View.GONE);
			removePhoto3.setVisibility(View.GONE);
			count = 3;
			ad.setPhoto3("");
			int c = Utilities.getIntegerValuesFromPreference(context, "imageCount", 0);
			if(c != 0){
				c--;
				Utilities.setIntegerValuesToPreferences(context, "imageCount", c);
			}else{
				count = 1;
			}
		}else if(id ==  R.id.postBtn){
			postData();
		}
	}
	
	
	private void postData(){
		
//		String postData = createRequestJSON(ad.getUserId(), ad.getTitle(), ad.getCity(), ad.getAddress(), 
//				ad.getDescription(), ad.getPrice(), ad.getCatId(), ad.getLongitude(), ad.getLatitude());
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.TITLE,ad.getTitle()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.CITY,ad.getCity()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.USER_ID,ad.getUserId()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.ADDRESS,ad.getAddress()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.DESCRIPTION,ad.getDescription()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.PRICE,ad.getPrice()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.CAT_ID,ad.getCatId()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.LONGITUDE,ad.getLongitude()));
		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.LATITUDE,ad.getLatitude()));
		
		String[] imagePaths = new String[3];
		imagePaths[0] = ad.getPhoto1();
		imagePaths[1] = ad.getPhoto2();
		imagePaths[2] = ad.getPhoto3();
		
		new DataUploadTask(context, postListener, NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_POST, nameValuePairs, imagePaths).execute();
		hide();
	}
	
	private static String createRequestJSON(String userId, String title, String city, String address, String description, String price, String category,
			String longitude, String latitude) {
		
		return "";
//		StringBuilder requestData = new StringBuilder();
//		requestData.append(NetworkConstants.TITLE);
//		requestData.append("=");
//		requestData.append(title);
//		requestData.append("&");
//		requestData.append(NetworkConstants.PRICE);
//		requestData.append("=");
//		requestData.append(price);
//		requestData.append("&");
//		requestData.append(NetworkConstants.USER_ID);
//		requestData.append("=");
//		requestData.append(userId);
//		requestData.append("&");
//		requestData.append(NetworkConstants.CAT_ID);
//		requestData.append("=");
//		requestData.append(category);
//		requestData.append("&");
//		requestData.append(NetworkConstants.ADDRESS);
//		requestData.append("=");
//		requestData.append(address);
//		requestData.append("&");
//		requestData.append(NetworkConstants.CITY);
//		requestData.append("=");
//		requestData.append(city);
//		requestData.append("&");
//		requestData.append(NetworkConstants.LONGITUDE);
//		requestData.append("=");
//		requestData.append(longitude);
//		requestData.append("&");
//		requestData.append(NetworkConstants.LATITUDE);
//		requestData.append("=");
//		requestData.append(latitude);
//		requestData.append("&");
//		requestData.append(NetworkConstants.DESCRIPTION);
//		requestData.append("=");
//		requestData.append(description);
//		requestData.append("&");

//		String photo1 = "";
//		String photo2 = "";
//		String photo3 = "";
//		String name = "photo1";
//		FileInputStream fis = null;
//		try {
//			if (!Utilities.isStringEmptyOrNull(ad.getPhoto1())) {
//				fis = new FileInputStream(ad.getPhoto1());
//				photo1 = Utilities.copyFileInBuffer(fis);
//
//				requestData.append(name);
//				requestData.append("=");
//				requestData.append(photo1);
//				requestData.append("&");
//				name = "photo2";
//
//			}
//			if (!Utilities.isStringEmptyOrNull(ad.getPhoto2())) {
//				fis = new FileInputStream(ad.getPhoto2());
//				photo2 = Utilities.copyFileInBuffer(fis);
//
//				requestData.append(name);
//				requestData.append("=");
//				requestData.append(photo2);
//				requestData.append("&");
//				if (name.equals("photo1")) {
//					name = "photo2";
//				} else {
//					name = "photo3";
//
//				}
//
//			}
//			if (!Utilities.isStringEmptyOrNull(ad.getPhoto3())) {
//				fis = new FileInputStream(ad.getPhoto3());
//				photo3 = Utilities.copyFileInBuffer(fis);
//
//				requestData.append(name);
//				requestData.append("=");
//				requestData.append(photo3);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		return requestData.toString();
	}
	
	private OnKeyListener addPicturesKeyListener = new OnKeyListener() {

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

	@Override
	public void onCancel(DialogInterface dialog) {
		context = null;
		activity = null;
		if(photo1 != null){
			photo1.setBackgroundDrawable(null);
		}
		if(photo2 != null){
			photo2.setBackgroundDrawable(null);
		}
		if(photo3 != null){
			photo3.setBackgroundDrawable(null);
		}
		
		photo1 = null;
		photo2 = null;
		photo3 = null;
		
		
		Utilities.unbindDrawables(findViewById(R.id.account_dialog));
	}
	
	public static void setPhoto(String imagePath){
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, Utilities.getBitmapFactoryoptions(4));
		Bitmap bmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
		BitmapDrawable drawable = new BitmapDrawable(bmp);
		
		if(bitmap != null){
			bitmap.recycle();
		}
		
		if(count == 1){
			photo1.setVisibility(View.VISIBLE);
			removePhoto1.setVisibility(View.VISIBLE);
			photo1.setBackgroundDrawable(drawable);
			ad.setPhoto1(imagePath);
			
		}else if(count == 2){
			photo2.setVisibility(View.VISIBLE);
			removePhoto2.setVisibility(View.VISIBLE);
			photo2.setBackgroundDrawable(drawable);
			ad.setPhoto2(imagePath);
			
		}else if(count == 3){
			photo3.setVisibility(View.VISIBLE);
			removePhoto3.setVisibility(View.VISIBLE);
			photo3.setBackgroundDrawable(drawable);
			ad.setPhoto3(imagePath);
		}
		int c = Utilities.getIntegerValuesFromPreference(context, "imageCount", 0);
		c++;
		Utilities.setIntegerValuesToPreferences(context, "imageCount", c);
		count++;
	}

}