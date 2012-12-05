package com.uaemerchant.dialogs;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.activities.UAEMerchantGoogleMapActivity;
import com.uaemerchant.activities.UAEMerchantMainActivity;
import com.uaemerchant.asynctask.ThumbImageDownloadTask;
import com.uaemerchant.asynctask.TwitterTask;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.facebook.FacebookHandler;
import com.uaemerchant.facebook.SessionEvents.AuthListener;
import com.uaemerchant.pojo.Ad;
import com.uaemerchant.twitter.TwitterApp;
import com.uaemerchant.twitter.TwitterApp.TwDialogListener;
import com.uaemerchant.twitter.TwitterGlobals;

public class ItemDetailDialog extends Dialog implements View.OnClickListener, OnCancelListener {
	private View itemDetailView;
	private Context context = null;
	private Activity activity;
	private Ad ad;

	private ImageView photo1;
	private String filename1;
	private ImageView photo2;
	private String filename2;
	private ImageView photo3;
	private String filename3;

	public ItemDetailDialog(Context context, Ad ad) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemDetailView = layoutInflater.inflate(R.layout.item_details_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(itemDetailView);
		this.ad = ad;

	}

	@Override
	public void show() {
		super.show();
		initializeViews();

		ImageView imageView = null;

		String[] photoArray = new String[3];
		photoArray[0] = ad.getPhoto1();
		photoArray[1] = ad.getPhoto2();
		photoArray[2] = ad.getPhoto3();

		for (int i = 0; i < photoArray.length; i++) {
			String url = photoArray[i];
			String[] urlTokens = url.split("/");
			if (i == 0) {
				imageView = photo1;
				filename1 = urlTokens[urlTokens.length - 1]; 
			} else if (i == 1) {
				imageView = photo2;
				filename2 = urlTokens[urlTokens.length - 1];
			} else if (i == 2) {
				imageView = photo3;
				filename3 = urlTokens[urlTokens.length - 1];
			}
			String filename = urlTokens[urlTokens.length - 1];
			if (!Utilities.isStringEmptyOrNull(url) && Utilities.thumbMap.get(url) == null) {
				new ThumbImageDownloadTask(context, filename, url, imageView).execute();
			} else {
				imageView.setOnClickListener(null);
			}
		}

	}

	private void initializeViews() {

		photo1 = (ImageView) itemDetailView.findViewById(R.id.photo1);
		photo1.setOnClickListener(this);
		photo1.setBackgroundDrawable(Utilities.imageMap.get("placeHolder"));
		photo2 = (ImageView) itemDetailView.findViewById(R.id.photo2);
		photo2.setOnClickListener(this);
		photo2.setBackgroundDrawable(Utilities.imageMap.get("placeHolder"));
		photo3 = (ImageView) itemDetailView.findViewById(R.id.photo3);
		photo3.setOnClickListener(this);
		photo3.setBackgroundDrawable(Utilities.imageMap.get("placeHolder"));

		TextView textview = (TextView) itemDetailView.findViewById(R.id.itemTitle);
		textview.setText(ad.getTitle());

		textview = (TextView) itemDetailView.findViewById(R.id.byTxt);
		textview.setText(ad.getName());

		textview = (TextView) itemDetailView.findViewById(R.id.cityTxt);
		textview.setText(ad.getCity());

		textview = (TextView) itemDetailView.findViewById(R.id.addressTxt);
		textview.setText(ad.getAddress());

		textview = (TextView) itemDetailView.findViewById(R.id.dateTxt);
		textview.setText(ad.getCreated());

		textview = (TextView) itemDetailView.findViewById(R.id.descriptionTxt);
		textview.setText(ad.getDescription());

		textview = (TextView) itemDetailView.findViewById(R.id.priceTxt);
		textview.setText(ad.getPrice());

		ImageView imageView = (ImageView) findViewById(R.id.messageBtn);
		imageView.setOnClickListener(this);

		imageView = (ImageView) findViewById(R.id.callBtn);
		imageView.setOnClickListener(this);

		imageView = (ImageView) findViewById(R.id.emailBtn);
		imageView.setOnClickListener(this);
		
		imageView = (ImageView) findViewById(R.id.facebookBtn);
		imageView.setOnClickListener(this);
		
		imageView = (ImageView) findViewById(R.id.twitterBtn);
		imageView.setOnClickListener(this);

		if (!Utilities.isStringEmptyOrNull(ad.getLongitude()) && !Utilities.isStringEmptyOrNull(ad.getLatitude())
				&& !ad.getLongitude().equals("-1.0000") && !ad.getLatitude().equals("-1.0000") ) {

			imageView = (ImageView) findViewById(R.id.locationIcon);
			imageView.setVisibility(View.VISIBLE);
			imageView.setOnClickListener(this);
		}

	}

	@Override
	public void hide() {
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.locationIcon) {
			CommonConstants.LATITUDE = Double.parseDouble(ad.getLatitude());
			CommonConstants.LONGITUDE = Double.parseDouble(ad.getLongitude());
			Intent intent = new Intent(context, UAEMerchantGoogleMapActivity.class);
			context.startActivity(intent);
		} else if (id == R.id.messageBtn) {
			Utilities.sms(ad.getPhone());
		} else if (id == R.id.callBtn) {
			Utilities.call(ad.getPhone());
		} else if (id == R.id.emailBtn) {
			Utilities.email(ad.getEmail());
		} else if (id == R.id.facebookBtn) {
			FacebookHandler.getInstance(context);
			if(!Utilities.mFacebook.isSessionValid()){
//				(context, "Facebook Login", Toast.LENGTH_SHORT).show();
				FacebookHandler.getInstance(context).Login(new LoginFacebookListener());
			} else {
				Toast.makeText(context, "Sharing on facebook!", Toast.LENGTH_SHORT).show();
				shareOnFacebook();
			}
			
		} else if (id == R.id.twitterBtn) {
//			Utilities.showprogressDialog(context, "Sharing on twitter, please wait!");
//			Toast.makeText(Utilities.mainActivityContext, "Twitter button clicked", Toast.LENGTH_SHORT).show();
			if(((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter() == null){
				((UAEMerchantMainActivity) Utilities.mainActivityContext).setmTwitter(new TwitterApp(context, TwitterGlobals.TWITTER_CONSUMER_KEY, TwitterGlobals.TWITTER_CONSUMER_SECRET));
			}
//			
			if(((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().hasAccessToken()){
				new TwitterTask(context, mTwLoginDialogListener, ad).execute();
				//shareOnTwitter();
			}else{
//				((UAEMerchantMainActivity) Utilities.mainActivityContext).setmTwitter(new TwitterApp(context, TwitterGlobals.TWITTER_CONSUMER_KEY, TwitterGlobals.TWITTER_CONSUMER_SECRET));
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().setListener(mTwLoginDialogListener);
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().authorize();
			}
			
		} else if(id == R.id.photo1){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + CommonConstants.MERCHANT_IMAGE_DIR+filename1), "image/*");
			((UAEMerchantMainActivity)Utilities.mainActivityContext).startActivity(intent);
		} else if(id == R.id.photo2){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + CommonConstants.MERCHANT_IMAGE_DIR+filename2), "image/*");
			((UAEMerchantMainActivity)Utilities.mainActivityContext).startActivity(intent);
		} else if(id == R.id.photo3){
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + CommonConstants.MERCHANT_IMAGE_DIR+filename3), "image/*");
			((UAEMerchantMainActivity)Utilities.mainActivityContext).startActivity(intent);
		}

	}

	private void shareOnFacebook() {
		String date = ad.getCreated();
		String[] tokens = date.split(" ");
		date = tokens[0];
		
		StringBuilder captionStringBuilder = new StringBuilder();
		captionStringBuilder.append("Title: " + ad.getTitle());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("Price: " + ad.getPrice());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("Name: " + ad.getName());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("Description: " + ad.getDescription());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("City: " + ad.getCity());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("Address: " + ad.getAddress());
		captionStringBuilder.append("\n");
		captionStringBuilder.append("Date: " + date);
		
		String url = ad.getPhoto1();
		String[] urlTokens = url.split("/");
		String filename = urlTokens[urlTokens.length - 1];
		if(!Utilities.isStringEmptyOrNull(filename)){
			FacebookHandler.getInstance(context).sharePhoto(CommonConstants.MERCHANT_IMAGE_DIR + filename, captionStringBuilder.toString());
		}
		url = ad.getPhoto2();
		urlTokens = url.split("/");
		filename = urlTokens[urlTokens.length - 1];
		if(!Utilities.isStringEmptyOrNull(filename)){
			FacebookHandler.getInstance(context).sharePhoto(CommonConstants.MERCHANT_IMAGE_DIR + filename, captionStringBuilder.toString());
		}
		url = ad.getPhoto3();
		urlTokens = url.split("/");
		filename = urlTokens[urlTokens.length - 1];
		if(!Utilities.isStringEmptyOrNull(filename)){
			FacebookHandler.getInstance(context).sharePhoto(CommonConstants.MERCHANT_IMAGE_DIR + filename, captionStringBuilder.toString());
		}		
	}
	
	private void shareOnTwitter() {
//		Toast.makeText(Utilities.mainActivityContext, "in sharing", Toast.LENGTH_SHORT).show();
		String date = ad.getCreated();
		String[] tokens = date.split(" ");
		date = tokens[0];
		
		StringBuilder captionStringBuilder = new StringBuilder();
		captionStringBuilder.append("Title: " + ad.getTitle());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("Price: " + ad.getPrice());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("Name: " + ad.getName());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("Description: " + ad.getDescription());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("City: " + ad.getCity());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("Address: " + ad.getAddress());
		captionStringBuilder.append(",\n");
		captionStringBuilder.append("Date: " + date);
		
		Toast.makeText(context, "Sharing on Twitter", Toast.LENGTH_SHORT).show();
		
		String url = ad.getPhoto1();
		String[] urlTokens = url.split("/");
		String filename = urlTokens[urlTokens.length - 1];
		
		if(!Utilities.isStringEmptyOrNull(filename)){
			
			File file = new File(CommonConstants.MERCHANT_IMAGE_DIR + filename);
			try {
//				Toast.makeText(context, "Sharing photo1", Toast.LENGTH_SHORT).show();
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().updateStatusMedia(captionStringBuilder.toString() + ".", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		url = ad.getPhoto2();
		urlTokens = url.split("/");
		filename = urlTokens[urlTokens.length - 1];
		if(!Utilities.isStringEmptyOrNull(filename)){
			File file = new File(CommonConstants.MERCHANT_IMAGE_DIR + filename);
			try {
//				Toast.makeText(context, "Sharing photo2", Toast.LENGTH_SHORT).show();
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().updateStatusMedia(captionStringBuilder.toString() + "..", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		url = ad.getPhoto3();
		urlTokens = url.split("/");
		filename = urlTokens[urlTokens.length - 1];
		if(!Utilities.isStringEmptyOrNull(filename)){
			File file = new File(CommonConstants.MERCHANT_IMAGE_DIR + filename);
			try {
//				Toast.makeText(context, "Sharing photo3", Toast.LENGTH_SHORT).show();
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().updateStatusMedia(captionStringBuilder.toString() + "...", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//		Utilities.cancelprogressDialog();

		
	}


	private OnKeyListener itemDetailKeyListener = new OnKeyListener() {

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
	
	private class LoginFacebookListener implements AuthListener{

		@Override
		public void onAuthSucceed() {
//			Toast.makeText(context, "Facebook login scucessfull", Toast.LENGTH_SHORT).show();
			shareOnFacebook();
		}

		@Override
		public void onAuthFail(String error) {
//			Toast.makeText(context, "Facebook login failed", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * Listener for Twitter - Calls after authorize is finalized.
	 */

	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		@Override
		public void onComplete(String value) {
			String username = ((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().getUsername();
			username = (username.equals("")) ? "No Name" : username;
//			Toast.makeText(context, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
			new TwitterTask(context, mTwLoginDialogListener, ad).execute();
			//shareOnTwitter();

		}

		@Override
		public void onError(String value) {
			String username = ((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().getUsername();
			username = (username.equals("")) ? "No Name" : username;
//			Toast.makeText(context, "Twitter connection failed", Toast.LENGTH_LONG).show();
//			Configuration.isTwitterLogin = true;
//			new ShareOptionsDialog(context).showSourceDialog(com.sg.common.Configuration.mediaPath, mediaType);

//			shareOnTwitter();
		}
	};
	

	@Override
	public void onCancel(DialogInterface dialog) {
		context = null;
		activity = null;
		Utilities.unbindDrawables(findViewById(R.id.account_dialog));
	}
	
	

}