package com.uaemerchant.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.uaemerchant.R;
import com.uaemerchant.common.Utilities;


public class RestartingApplicationTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private String languageCode;
	
	
	public RestartingApplicationTask(Context ctx, String languageCode){
		context = ctx;
		this.languageCode = languageCode;
		
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utilities.showprogressDialog(context, context.getString(R.string.loading));
	}

	@Override
	protected Void doInBackground(Void... params) {
		Utilities.updateLocale(languageCode);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Utilities.restartMainActivity(context);
		Utilities.cancelprogressDialog();
	}
};