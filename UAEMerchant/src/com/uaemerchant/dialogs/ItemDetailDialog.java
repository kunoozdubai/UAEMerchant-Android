package com.uaemerchant.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uaemerchant.R;
import com.uaemerchant.activities.UAEMerchantGoogleMapActivity;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.pojo.Ad;


public class ItemDetailDialog extends Dialog implements View.OnClickListener, OnCancelListener{
	private View itemDetailView;
	private Context context = null;
	private Activity activity;
	private Ad ad;
	
	public ItemDetailDialog(Context context, Ad ad) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemDetailView = layoutInflater.inflate(R.layout.item_details_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(itemDetailView);
		this.ad  = ad;
	}
	
		
	@Override
	public void show() {
		super.show();
		initializeViews();
	}

	private void initializeViews() {
		TextView textview = (TextView) itemDetailView.findViewById(R.id.itemTitle);
		textview.setText(ad.getTitle());
		
		textview = (TextView) itemDetailView.findViewById(R.id.byTxt);
		textview.setText(ad.getName());
		
		textview = (TextView) itemDetailView.findViewById(R.id.cityTxt);
		textview.setText(ad.getCity());
		
		textview = (TextView) itemDetailView.findViewById(R.id.addressTxt);
		textview.setText(ad.getAddress());
		
		textview = (TextView) itemDetailView.findViewById(R.id.dateTxt);
		textview.setText(ad.getCreated());
		
		textview = (TextView) itemDetailView.findViewById(R.id.descriptionTxt);
		textview.setText(ad.getDescription());
		
		
		ImageView imageView = (ImageView) findViewById(R.id.messageBtn);
		imageView.setOnClickListener(this);
		
		imageView = (ImageView) findViewById(R.id.callBtn);
		imageView.setOnClickListener(this);
		
		imageView = (ImageView) findViewById(R.id.emailBtn);
		imageView.setOnClickListener(this);
		
		if(!Utilities.isStringEmptyOrNull(ad.getLongitude()) || !Utilities.isStringEmptyOrNull(ad.getLatitude())){

			imageView = (ImageView) findViewById(R.id.locationIcon);
			imageView.setVisibility(View.VISIBLE);
			imageView.setOnClickListener(this);
			
		}
		
		
		
		

	}


	@Override
	public void hide() {
		cancel();

	}  
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.locationIcon){
			Intent intent = new Intent(context, UAEMerchantGoogleMapActivity.class);
			context.startActivity(intent);
		}else if(id == R.id.messageBtn){
			
		}
		else if(id == R.id.callBtn){
			
		}
		else if(id == R.id.emailBtn){
	
}

	}
	
	private OnKeyListener itemDetailKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_SEARCH
					|| keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			}
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
					hide();
				
				return true; 
			}
			
			return false;
		}
	};

	@Override
	public void onCancel(DialogInterface dialog) {
		context = null;
		activity = null;
		Utilities.unbindDrawables(findViewById(R.id.account_dialog));
	}


}