package com.uaemerchant.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.uaemerchant.R;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;

public class UAEMerchantGoogleMapActivity extends FragmentActivity implements OnMarkerClickListener {

	private Context context;
	// private MapView mapView;

	private GoogleMap mMap;
	private Marker adMarker;
	private String adAdress = "";
	private static final LatLng adLocation = new LatLng(CommonConstants.LATITUDE, CommonConstants.LONGITUDE);

	private Marker currentMarker;
	private String currentAdress = "";
	private static final LatLng currentLocation = new LatLng(CommonConstants.CURRENT_LATITUDE, CommonConstants.CURRENT_LONGITUDE);

	// private static final double latitudeE6 = 25; // 25269700
	// private static final double longitudeE6 = 55; // 55309500

	private static final double latitudeE6 = CommonConstants.LATITUDE;
	private static final double longitudeE6 = CommonConstants.LONGITUDE;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_activity);

		context = this;

		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1)).getMap();

		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(latitudeE6, longitudeE6, 1);
			Address returnedAddress = new Address(new Locale("en"));
			if (addresses != null && addresses.size() > 0) {
				returnedAddress = addresses.get(0);
			}
			StringBuilder strReturnedAddress = new StringBuilder("");
			for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
				strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
			}
			adAdress = strReturnedAddress.toString();

			geocoder = new Geocoder(this, Locale.ENGLISH);
			addresses = geocoder.getFromLocation(CommonConstants.CURRENT_LATITUDE, CommonConstants.CURRENT_LONGITUDE, 1);
			if (addresses != null && addresses.size() > 0) {
				returnedAddress = addresses.get(0);
			}
			strReturnedAddress = new StringBuilder("");
			for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
				strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
			}
			currentAdress = strReturnedAddress.toString();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setUpMap();

	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Add lots of markers to the map.
		adMarker = mMap.addMarker(new MarkerOptions().position(adLocation).title("Location").snippet(adAdress)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

		currentMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").snippet(currentAdress)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

		// Creates a marker rainbow demonstrating how to create default marker
		// icons of different
		// hues (colors).

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		// mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		// mMap.setOnInfoWindowClickListener(this);
		// mMap.setOnMarkerDragListener(this);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map1).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressLint("NewApi")
				// We check which build version we are using.
				@Override
				public void onGlobalLayout() {
					LatLngBounds bounds = new LatLngBounds.Builder().include(adLocation).include(currentLocation).build();
					// if (Build.VERSION.SDK_INT <
					// Build.VERSION_CODES.JELLY_BEAN) {
					// mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					// } else {
					// mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					// }
					mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
				}
			});
		}
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

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

}