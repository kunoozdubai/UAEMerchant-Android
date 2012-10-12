package com.uaemerchant.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uaemerchant.R;


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
	}
	
		
	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		cancel();

	}  

	@Override
	public void onClick(View v) {
	int id = v.getId();	
	
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
	}


}