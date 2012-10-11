package com.uaemerchant.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import android.content.Context;
import android.os.AsyncTask;

import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.Utilities;


public class DataDownloadTask extends AsyncTask<Void, Void, Void> {

	private Context context;
	private IResponseListener responseListener;
	private JSONArray jsonArray = null;
	private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public DataDownloadTask(Context ctx, IResponseListener iResponseListener, List<NameValuePair> nameValuePairs){
		context = ctx;
		responseListener = iResponseListener;
		this.nameValuePairs = nameValuePairs;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Utilities.showprogressDialog(context, "Loading...");
	}

	@Override
	protected Void doInBackground(Void... params) {
		
//		jsonArray = JSONfunctions.getArrayfromURLValues(
//				NetworkConstants.DJI_URL, NetworkConstants.WS_INDEX,
//				nameValuePairs);
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(jsonArray != null && jsonArray.length() > 0 ){
			responseListener.onSuccess(jsonArray);
		}else{
			responseListener.onError(null);
		}
		Utilities.cancelprogressDialog();
	}
};