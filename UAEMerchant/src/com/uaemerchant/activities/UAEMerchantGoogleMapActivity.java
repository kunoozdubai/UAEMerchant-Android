package com.uaemerchant.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.uaemerchant.R;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;

public class UAEMerchantGoogleMapActivity extends MapActivity {

	private Context context;
	private MapView mapView;

//	private static final double latitudeE6 = 25; // 25269700
//	private static final double longitudeE6 = 55; // 55309500
	
	private static final double latitudeE6 = CommonConstants.LATITUDE;
	private static final double longitudeE6 = CommonConstants.LONGITUDE;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_activity);

		context = this;
		
		mapView = (MapView) findViewById(R.id.map_view);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
		CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(
				drawable, this);

		// GeoPoint point = new GeoPoint(latitudeE6, longitudeE6);

		GeoPoint point = new LatLonPoint(latitudeE6, longitudeE6);
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(latitudeE6, longitudeE6, 1);
			Address returnedAddress = new Address(new Locale("en"));
			if(addresses != null && addresses.size() > 0){
				returnedAddress = addresses.get(0);
			}
			StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
			for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
				strReturnedAddress.append(returnedAddress.getAddressLine(i))
						.append("\n");
			}
			OverlayItem overlayitem = new OverlayItem(point, strReturnedAddress.toString(),
					"");
			itemizedOverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedOverlay);
			MapController mapController = mapView.getController();
			mapController.animateTo(point);
			mapController.setZoom(6);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// OverlayItem overlayitem = new OverlayItem(point, "Dubai", "");
		//
		// itemizedOverlay.addOverlay(overlayitem);
		// mapOverlays.add(itemizedOverlay);
		//
		// MapController mapController = mapView.getController();
		//
		// mapController.animateTo(point);
		// mapController.setZoom(6);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		
			default:
				break;
			}
		}
	};
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private static final class LatLonPoint extends GeoPoint {
		public LatLonPoint(double latitude, double longitude) {
			super((int) (latitude * 1E6), (int) (longitude * 1E6));
		}
	}
	
	@Override
	protected void onDestroy() {
		Utilities.unbindDrawables(findViewById(R.id.location_activity));
		System.gc();
		super.onDestroy();
	}

}