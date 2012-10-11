package com.uaemerchant.common;

import org.json.JSONArray;

public interface IResponseListener {

	public void onSuccess(JSONArray response);
	public void onError( JSONArray response );
}
