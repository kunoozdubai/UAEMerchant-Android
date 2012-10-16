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
import android.widget.ImageView;

import com.uaemerchant.R;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.pojo.Ad;

public class AddPicturesDialog extends Dialog implements View.OnClickListener, OnCancelListener {
	private View addPhotosView;
	private Context context = null;
	private Activity activity;
	
	private ImageView photo1;
	private ImageView photo2;
	private ImageView photo3;
	private ImageView removePhoto1;
	private ImageView removePhoto2;
	private ImageView removePhoto3;
	

	public AddPicturesDialog(Context context) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addPhotosView = layoutInflater.inflate(R.layout.add_photos_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(addPhotosView);
		
		this.setOnKeyListener(addPicturesKeyListener);
		
		
	}

	@Override
	public void show() {
		super.show();
		initializeViews();

	}

	private void initializeViews() {

		ImageView imageView = (ImageView) addPhotosView.findViewById(R.id.addPhotoBtn);
		imageView.setOnClickListener(this);
		
		photo1 = (ImageView)  findViewById(R.id.photo1);
		photo1.setOnClickListener(this);
		photo2 = (ImageView)  findViewById(R.id.photo2);
		photo2.setOnClickListener(this);
		photo3 = (ImageView)  findViewById(R.id.photo3);
		photo3.setOnClickListener(this);
		
		removePhoto1 = (ImageView)  findViewById(R.id.removePhoto1);
		removePhoto1.setOnClickListener(this);
		removePhoto2 = (ImageView)  findViewById(R.id.removePhoto2);
		removePhoto2.setOnClickListener(this);
		removePhoto3 = (ImageView)  findViewById(R.id.removePhoto3);
		removePhoto3.setOnClickListener(this);

	}

	@Override
	public void hide() {
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.addPhotoBtn) {

		}

	}

	private OnKeyListener addPicturesKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_MENU) {
				return true;
			}
			if (keyCode == KeyEvent.KEYCODE_BACK && (event.getAction() == KeyEvent.ACTION_DOWN)) {
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