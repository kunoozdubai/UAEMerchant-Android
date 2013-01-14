package com.uaemerchant.asynctask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.uaemerchant.R;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.network.JSONfunctions;


public class DataDownloadTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private IResponseListener responseListener;
	private JSONObject jsonObject = null;
	String requestURL;
	String postData;
	
	
	public DataDownloadTask(Context ctx, IResponseListener iResponseListener, String requestURL, String postData){
		context = ctx;
		responseListener = iResponseListener;
		this.requestURL = requestURL;
		this.postData = postData;
		
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utilities.showprogressDialog(context, context.getString(R.string.loading));
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		jsonObject = JSONfunctions.httpPostRequest(requestURL, postData);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(jsonObject != null && jsonObject.length() > 0 ){
			try {
				String error = jsonObject.getString("ERROR");
				if(error.equals("0")){
					responseListener.onSuccess(jsonObject);
				}else{
					responseListener.onError(jsonObject);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}else{
			responseListener.onError(null);
		}
		Utilities.cancelprogressDialog();
	}
};