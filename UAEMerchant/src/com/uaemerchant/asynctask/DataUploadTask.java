package com.uaemerchant.asynctask;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.network.JSONfunctions;


public class DataUploadTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private IResponseListener responseListener;
	private JSONObject jsonObject = null;
	String requestURL;
//	String postData;
	List<NameValuePair> nameValuePairs;
	String[] imagePaths;
	
	
	public DataUploadTask(Context ctx, IResponseListener iResponseListener, String requestURL, List<NameValuePair> nameValuePairs, String[] imagePaths){
		context = ctx;
		responseListener = iResponseListener;
		this.requestURL = requestURL;
//		this.postData = postData;
		this.nameValuePairs = nameValuePairs;
		this.imagePaths = imagePaths;
		
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utilities.showprogressDialog(context, "Loading...");
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		jsonObject = JSONfunctions.HttpMediaPostReq(requestURL, nameValuePairs, imagePaths);
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