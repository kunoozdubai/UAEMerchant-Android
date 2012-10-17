package com.uaemerchant.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.dialogs.AccountDialog;
import com.uaemerchant.dialogs.AddPicturesDialog;
import com.uaemerchant.dialogs.PostDialog;

public class UAEMerchantMainActivity extends Activity implements android.view.View.OnClickListener{

	private Context context;
	private static String imagePath = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		context = this;
		
		Utilities.mainActivityContext = context;
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background, Utilities.getBitmapFactoryoptions(1));
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		Utilities.imageMap.put("mainBackground", bitmapDrawable);
		bitmapDrawable = null;
		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title_background, Utilities.getBitmapFactoryoptions(1));
		bitmapDrawable = new BitmapDrawable(bitmap);
		Utilities.imageMap.put("titleBackground", bitmapDrawable);
		bitmapDrawable = null;
		
		ImageView imageView = (ImageView) findViewById(R.id.background);
		imageView.setBackgroundDrawable(Utilities.imageMap.get("mainBackground"));
		
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.titleParent);
		relativeLayout.setBackgroundDrawable(Utilities.imageMap.get("titleBackground"));
		
		
		Button button = (Button) findViewById(R.id.btnAccount);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnPostAd);
		button.setOnClickListener(this);
		
		button = (Button) findViewById(R.id.btnCarNumberPlate);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnMobilePhoneNumbers);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnElectronics);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnCarAndEngines);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnRealEstate);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnLadiesOnly);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnServices);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnFurniture);
		button.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnOthers);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		
		if(id == R.id.btnAccount){
			new AccountDialog(context).show();
			Toast.makeText(context, "btnAccount Clicked", Toast.LENGTH_SHORT).show();
		}else if(id == R.id.btnPostAd){
			new PostDialog(context).show();
			Toast.makeText(context, "btnPostAd Clicked", Toast.LENGTH_SHORT).show();
		}else if(id == R.id.btnCarNumberPlate || id == R.id.btnMobilePhoneNumbers || id == R.id.btnElectronics
				|| id == R.id.btnCarAndEngines || id == R.id.btnRealEstate || id == R.id.btnLadiesOnly 
				|| id == R.id.btnServices || id == R.id.btnFurniture || id == R.id.btnOthers){
			
			String catId = getCatId(id);
			String catName = getCatName(id);
			
			Intent intent = new Intent(context, CategoryListActivity.class);
			intent.putExtra("catId", catId);
			intent.putExtra("catName", catName);
			startActivity(intent);
		}
	}

	private String getCatName(int id) {

		if (id == R.id.btnCarNumberPlate) {
			return getString(R.string.car_number_plates_txt);
		}
		if (id == R.id.btnMobilePhoneNumbers) {
			return getString(R.string.mobile_phone_numbers_txt);
		}
		if (id == R.id.btnElectronics) {
			return getString(R.string.electronics_txt);
		}
		if (id == R.id.btnCarAndEngines) {
			return getString(R.string.car_and_engines_txt);
		}
		if (id == R.id.btnRealEstate) {
			return getString(R.string.real_estate_txt);
		}
		if (id == R.id.btnLadiesOnly) {
			return getString(R.string.ladies_only_txt);
		}
		if (id == R.id.btnServices) {
			return getString(R.string.services_txt);
		}
		if (id == R.id.btnFurniture) {
			return getString(R.string.furniture_txt);
		}
		if (id == R.id.btnOthers) {
			return getString(R.string.others_txt);
		}
		return "";
	}

	private String getCatId(int id) {
		
		if (id == R.id.btnCarNumberPlate) {
			return "1";
		}
		if (id == R.id.btnMobilePhoneNumbers) {
			return "2";
		}
		if (id == R.id.btnElectronics) {
			return "3";
		}
		if (id == R.id.btnCarAndEngines) {
			return "4";
		}
		if (id == R.id.btnRealEstate) {
			return "5";
		}
		if (id == R.id.btnLadiesOnly) {
			return "6";
		}
		if (id == R.id.btnServices) {
			return "7";
		}
		if (id == R.id.btnFurniture) {
			return "8";
		}
		if (id == R.id.btnOthers) {
			return "9";
		}
		return "";
	}
	
	public static void setImagePath(String path){
		imagePath = path;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {
				AddPicturesDialog.setPhoto(imagePath);	
			} 
		}else if(requestCode == 200){
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();
				AddPicturesDialog.setPhoto(imagePath);
			}
		}
		
	}
	
}