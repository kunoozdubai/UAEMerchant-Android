package com.uaemerchant.asynctask;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.uaemerchant.activities.UAEMerchantMainActivity;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;

public class ThumbImageDownloadTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private ImageView imageView = null;
	private String filename;
	private String url;

	public ThumbImageDownloadTask(Context context, String filename,	String url, ImageView imageView) {
		this.context = context;
		this.imageView = imageView;
		this.filename = filename;
		this.url = url;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Utilities.showprogressDialog(context, "Loading images...");

	}

	@Override
	protected Void doInBackground(Void... params) {

		// String filename = institutePicsList.get(i).getFile();
		File file = new File(CommonConstants.MERCHANT_IMAGE_DIR + filename);
		if (!file.exists()) {
			int status = -1;
			status = Utilities.downloadImage(filename, url);
			
			if (status == 200) {
				final Bitmap bitmap;

				bitmap = BitmapFactory.decodeFile(CommonConstants.MERCHANT_IMAGE_DIR
						+ filename, Utilities.getBitmapFactoryoptions(0));
				if (bitmap != null) {
					Utilities.thumbMap.put(filename, new BitmapDrawable(
							bitmap));
				}else{
					file.delete();
				}
				
				((UAEMerchantMainActivity) Utilities.mainActivityContext)
						.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (bitmap != null && imageView != null) {
									imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
								}
							}
						});
			}else{
				file.delete();
			}

		} else {
			final Bitmap bitmap;
			if (Utilities.thumbMap.get(filename) == null) {
				bitmap = BitmapFactory.decodeFile(CommonConstants.MERCHANT_IMAGE_DIR
						+ filename, Utilities.getBitmapFactoryoptions(0));
				
				if (bitmap != null) {
					Utilities.thumbMap.put(filename, new BitmapDrawable(
							bitmap));
				}else{
					file.delete();
				}

				((UAEMerchantMainActivity) Utilities.mainActivityContext)
						.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (bitmap != null && imageView != null) {
									imageView
											.setBackgroundDrawable(new BitmapDrawable(
													bitmap));
								}
							}
						});
			} else {
				((UAEMerchantMainActivity) Utilities.mainActivityContext)
						.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								imageView
										.setBackgroundDrawable(Utilities.thumbMap
												.get(filename));

							}
						});
			}

		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		// Utilities.cancelprogressDialog();
		// progressDialog.hide();
		// progressDialog = null;
	}
};