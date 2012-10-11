package com.uaemerchant.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uaemerchant.pojo.Ad;

public class Parser {

	
	public static ArrayList<Ad> parseAdsList(JSONArray response) {

		ArrayList<Ad> adsList = new ArrayList<Ad>();

//		String trainingId = "";
//		String title = "";
//		String startDate = "";
//		String endDate = "";
//		String startTime = "";
//		String endTime = "";
//		String venue = "";
//		String description = "";
//		String created = "";
//		JSONObject obj;
		// try {
		// obj = (JSONObject) response.get(0);
		// } catch (JSONException e1) {
		// e1.printStackTrace();
		// }
		// JSONObject jsonObject = new JSONObject();
		// JSONArray names = new JSONArray();
		// try {
		// jsonObject = (JSONObject) response.get(0);
		// names = jsonObject.names();
		// } catch (JSONException e1) {
		// e1.printStackTrace();
		// }
		for (int i = 0; i < response.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) response.get(i);

//				if (!jsonObject.isNull((NetworkConstants.TRAINING_ID))) {
//					trainingId = jsonObject
//							.getString(NetworkConstants.TRAINING_ID);
//				}
				
//				Ad ad = new Ad(adId, userId, catId, title, price, city, address, longitude, latitude, description, photo1, photo2, photo3, created, status, email, phone, name);
//				adsList.add(ad);

//				trainingId = "";
//				title = "";
//				startDate = "";
//				endDate = "";
//				startTime = "";
//				endTime = "";
//				venue = "";
//				description = "";
//				created = "";

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return adsList;
	}

}