package com.uaemerchant.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uaemerchant.common.Utilities;

public class JSONfunctions {

	/**
	 * return a jason array
	 * 
	 * @param url
	 * @param webServiceName
	 * @return
	 */

	public static JSONArray getArrayfromURLValues(String url,
		String webServiceName, List<NameValuePair> nameValuePairs, String view) {
		InputStream is = null;
		String result = "";
		JSONArray jArray = null;
		url = url + webServiceName;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			// HttpEntity entity = response.getEntity();
			// is = entity.getContent();
			// HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, HTTP.UTF_8);

//			is = entity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// convert response to string
		/*try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			// add element with the service name
			sb.append("{\"" + webServiceName + "\"" + ":");
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			sb.append("}");
			result = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}*/

//		try {
//			if(view.equals("training") && !result.equals("[]")){
//				jArray = new JSONArray("[" + result + "]");
//			}else{
//				jArray = new JSONArray(result);
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		
		try {
			jArray = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jArray;
	}

	/**
	 * return a jason array
	 * 
	 * @param url
	 * @param webServiceName
	 * @return
	 */

	public static JSONObject getJSONObjectWebService(String url,
			String webServiceName, List<NameValuePair> nameValuePairs) {
		InputStream is = null;
		String result = "";
		JSONObject json = null;
		url = url + webServiceName;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			// add element with the service name
			sb.append("{\"" + webServiceName + "\"" + ":");
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			sb.append("}");
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			json = new JSONObject(result);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * This method sends a request to passed URL using POST method.
	 * 
	 * @param 	requestURL URL on which request is to be sent
	 * @param	postData Json data to be posted
	 * @return	InputStream from connection
	 */
	public static JSONObject httpPostRequest( String requestURL, String postData ) {
		//		StringBuilder response = null;
		HttpURLConnection connection = null;
		URL url = null;
		try {
			url = new URL(requestURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setConnectTimeout(15000);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.getOutputStream().write(postData.getBytes());

		} catch (MalformedURLException malFormedExp) {
			malFormedExp.printStackTrace();

		} catch (IOException ioExp) {
			ioExp.printStackTrace();
		}
		JSONObject jsonObject = null;
		String result = "";
		int responseCode;
		try {
			
			responseCode = connection.getResponseCode();
			
			if ( responseCode == HttpURLConnection.HTTP_OK ) {
				result = Utilities.readServerResponse(connection.getInputStream());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			jsonObject =  new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
		
	}

}
