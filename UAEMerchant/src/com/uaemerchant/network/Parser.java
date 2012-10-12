package com.uaemerchant.network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uaemerchant.activities.CategoryListActivity;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.pojo.Ad;

public class Parser {

	public static ArrayList<Ad> parseAdsList(JSONObject object) {

		ArrayList<Ad> adsList = new ArrayList<Ad>();

		String adId = "";
		String userId = "";
		String catId = "";
		String title = "";
		String price = "";
		String city = "";
		String address = "";
		String longitude = "";
		String latitude = "";
		String description = "";
		String photo1 = "";
		String photo2 = "";
		String photo3 = "";
		String created = "";
		String status = "";
		String email = "";
		String phone = "";
		String name = "";

		JSONArray response = new JSONArray();
		if (!object.isNull("response")) {
			try {
				response = (JSONArray) object.getJSONArray("response");
				int totalRows = object.getInt("totalrows");
				CategoryListActivity.setTotalRows(totalRows);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < response.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) response.get(i);

				if (!jsonObject.isNull((NetworkConstants.AD_ID))) {
					adId = jsonObject.getString(NetworkConstants.AD_ID);
				}
				if (!jsonObject.isNull((NetworkConstants.USER_ID))) {
					userId = jsonObject.getString(NetworkConstants.USER_ID);
				}
				if (!jsonObject.isNull((NetworkConstants.CAT_ID))) {
					catId = jsonObject.getString(NetworkConstants.CAT_ID);
				}
				if (!jsonObject.isNull((NetworkConstants.TITLE))) {
					title = jsonObject.getString(NetworkConstants.TITLE);
				}
				if (!jsonObject.isNull((NetworkConstants.PRICE))) {
					price = jsonObject.getString(NetworkConstants.PRICE);
				}
				if (!jsonObject.isNull((NetworkConstants.CITY))) {
					city = jsonObject.getString(NetworkConstants.CITY);
				}
				if (!jsonObject.isNull((NetworkConstants.ADDRESS))) {
					address = jsonObject.getString(NetworkConstants.ADDRESS);
				}
				if (!jsonObject.isNull((NetworkConstants.LONGITUDE))) {
					longitude = jsonObject.getString(NetworkConstants.LONGITUDE);
				}
				if (!jsonObject.isNull((NetworkConstants.LATITUDE))) {
					latitude = jsonObject.getString(NetworkConstants.LATITUDE);
				}
				if (!jsonObject.isNull((NetworkConstants.DESCRIPTION))) {
					description = jsonObject.getString(NetworkConstants.DESCRIPTION);
				}
				if (!jsonObject.isNull((NetworkConstants.PHOTO_1))) {
					photo1 = jsonObject.getString(NetworkConstants.PHOTO_1);
				}
				if (!jsonObject.isNull((NetworkConstants.PHOTO_2))) {
					photo2 = jsonObject.getString(NetworkConstants.PHOTO_2);
				}
				if (!jsonObject.isNull((NetworkConstants.PHOTO_3))) {
					photo3 = jsonObject.getString(NetworkConstants.PHOTO_3);
				}
				if (!jsonObject.isNull((NetworkConstants.CREATED))) {
					created = jsonObject.getString(NetworkConstants.CREATED);
				}
				if (!jsonObject.isNull((NetworkConstants.STATUS))) {
					status = jsonObject.getString(NetworkConstants.STATUS);
				}
				if (!jsonObject.isNull((NetworkConstants.EMAIL))) {
					email = jsonObject.getString(NetworkConstants.EMAIL);
				}
				if (!jsonObject.isNull((NetworkConstants.PHONE))) {
					phone = jsonObject.getString(NetworkConstants.PHONE);
				}
				if (!jsonObject.isNull((NetworkConstants.NAME))) {
					name = jsonObject.getString(NetworkConstants.NAME);
				}
				Ad ad = new Ad(adId, userId, catId, title, price, city, address, longitude, latitude, description, photo1, photo2, photo3, created, status,
						email, phone, name);
				adsList.add(ad);

				adId = "";
				userId = "";
				catId = "";
				title = "";
				price = "";
				city = "";
				address = "";
				longitude = "";
				latitude = "";
				description = "";
				photo1 = "";
				photo2 = "";
				photo3 = "";
				created = "";
				status = "";
				email = "";
				phone = "";
				name = "";
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return adsList;
	}

	public static String parseRegisterationResponse(JSONObject response) {
		String userId = "";
		try {
			if (!response.isNull("response")) {
				userId = response.getString("response");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userId;
	}

}