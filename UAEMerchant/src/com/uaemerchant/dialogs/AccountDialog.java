package com.uaemerchant.dialogs;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uaemerchant.R;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.Utilities;


public class AccountDialog extends Dialog implements View.OnClickListener, OnCancelListener{
	private View accountView;
	private Context context = null;
	private Activity activity;
	
	public AccountDialog(Context context) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		accountView = layoutInflater.inflate(R.layout.account_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(accountView);
	}
	
		
	@Override
	public void show() {
		super.show();
		String userId = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_USER_ID, "");
		if(!Utilities.isStringEmptyOrNull(userId)){
			initializeViews();
		}else{
			
			showRegisterAlertDialog();
		}
	}
	
	
	private void showRegisterAlertDialog() {
		Builder alertBuilder = new Builder(context);
        alertBuilder.setCancelable(false);
		alertBuilder.setTitle(context.getString(R.string.register_title));
        alertBuilder.setMessage(context.getString(R.string.register_msg));
        
        alertBuilder.setNegativeButton(context.getString(R.string.no_thanks), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Go back to main category activity
				hide();
			}
		});

        alertBuilder.setPositiveButton(context.getString(R.string.register_title), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// open registration dialog
				new RegisterDialog(context).show();
				hide();
			}
		});

        alertBuilder.create().show(); 
	}


	private void initializeViews() {
		
		
		
		Button button = (Button) accountView.findViewById(R.id.btnCategories);
		button.setOnClickListener(this);
		button = (Button) accountView.findViewById(R.id.btnEdit);
		button.setOnClickListener(this);
		
		TextView textview = (TextView) accountView.findViewById(R.id.nameTxt);
		String value = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_NAME, "");
		textview.setText(value);
		
		textview = (TextView) accountView.findViewById(R.id.emailTxt);
		value = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_EMAIL, "");
		textview.setText(value);
		
		textview = (TextView) accountView.findViewById(R.id.phoneTxt);
		value = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_PHONE, "");
		textview.setText(value);
		
		textview = (TextView) accountView.findViewById(R.id.cityTxt);
		value = Utilities.getStringValuesFromPreference(context, CommonConstants.PREF_CITY, "");
		textview.setText(value);

	}

	@Override
	public void hide() {
		cancel();
	}  

	@Override
	public void onClick(View v) {
		int id = v.getId();	
		if(id == R.id.btnCategories){
			hide();
		}
		if(id == R.id.btnEdit){
			new RegisterDialog(context).show();
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(!hasFocus){
			hide();
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	private OnKeyListener accountKeyListener = new OnKeyListener() {

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
	@Override
	protected void onStop() {
		super.onStop();
		hide();
	}


}