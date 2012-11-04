package com.uaemerchant.asynctask;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.uaemerchant.activities.UAEMerchantMainActivity;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.pojo.Ad;
import com.uaemerchant.twitter.TwitterApp;
import com.uaemerchant.twitter.TwitterApp.TwDialogListener;
import com.uaemerchant.twitter.TwitterGlobals;

public class TwitterTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private TwDialogListener listener;
	Ad ad;

	public TwitterTask(Context ctx, TwDialogListener listener, Ad ad) {
		context = ctx;
		this.listener = listener;
		this.ad = ad;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utilities.showprogressDialog(context, "Sharing on Twitter");
	}

	@Override
	protected Void doInBackground(Void... params) {
		shareOnTwitter();
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Toast.makeText(context, "Shared on Twitter", Toast.LENGTH_SHORT).show();
		Utilities.cancelprogressDialog();
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

//		Toast.makeText(context, "Sharing on Twitter", Toast.LENGTH_SHORT).show();

		String url = ad.getPhoto1();
		String[] urlTokens = url.split("/");
		String filename = urlTokens[urlTokens.length - 1];

		if (!Utilities.isStringEmptyOrNull(filename)) {

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
		if (!Utilities.isStringEmptyOrNull(filename)) {
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
		if (!Utilities.isStringEmptyOrNull(filename)) {
			File file = new File(CommonConstants.MERCHANT_IMAGE_DIR + filename);
			try {
//				Toast.makeText(context, "Sharing photo3", Toast.LENGTH_SHORT).show();
				((UAEMerchantMainActivity) Utilities.mainActivityContext).getmTwitter().updateStatusMedia(captionStringBuilder.toString() + "...", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

};