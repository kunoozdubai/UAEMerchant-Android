package com.uaemerchant.activities;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
	
	private Context context;
	
	public CustomItemizedOverlay(Drawable defaultMarker) {
		  super(boundCenterBottom(defaultMarker));
	}
	
	public CustomItemizedOverlay(Drawable defaultMarker, Context context) {
		  this(defaultMarker);
		  this.context = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mapOverlays.get(index);
		Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
		return true;
	}
	
	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
	    this.populate();
	}

}