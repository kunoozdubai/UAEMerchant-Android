//package com.uaemerchant.dialogs;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.view.View;
//
//
//public class ProfileDialog extends Dialog implements View.OnClickListener, OnCancelListener{
//	private View profileView;
//	private Context context = null;
//	private Activity activity;
//	
//	public ProfileDialog(Context ctx) {
//		super(ctx, R.style.preview);
//		this.context = ctx;  
//		activity = (Activity) context;
//		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		profileView = layoutInflater.inflate(R.layout.cafe, (ViewGroup) activity.findViewById(R.layout.main_sky_menu));
//		
//	@Override
//	public void show() {
//		super.show();
//	}
//
//	@Override
//	public void hide() {
//		cancel();
//
//	}  
//
//	@Override
//	public void onClick(View v) {
//	int id = v.getId();	
//	
//	}
//	
//	
//	
//	private OnKeyListener cafeKeyListener = new OnKeyListener() {
//
//		@Override
//		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//			if (keyCode == KeyEvent.KEYCODE_SEARCH
//					|| keyCode == KeyEvent.KEYCODE_MENU) {
//				return true;
//			}
//			if (keyCode == KeyEvent.KEYCODE_BACK
//					&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
//					hide();
//				
//				return true; 
//			}
//			
//			return false;
//		}
//	};
//
//	@Override
//	public void onCancel(DialogInterface dialog) {
//		context = null;
//		activity = null;
//	}
//
//
//}