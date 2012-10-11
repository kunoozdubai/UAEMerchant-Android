package com.uaemerchant.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.common.Utilities;

public class UAEMerchantMainActivity extends Activity implements android.view.View.OnClickListener{

	private Context context;
	
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
		
		switch (id){
		
		case R.id.btnAccount:
			Toast.makeText(context, "btnAccount Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnPostAd:
			Toast.makeText(context, "btnPostAd Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnCarNumberPlate:
			Toast.makeText(context, "btnCarNumberPlate Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnMobilePhoneNumbers:
			Toast.makeText(context, "btnMobilePhoneNumbers Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnElectronics:
			Toast.makeText(context, "btnElectronics Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnCarAndEngines:
			Toast.makeText(context, "btnCarAndEngines Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnRealEstate:
			Toast.makeText(context, "btnRealEstate Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnLadiesOnly:
			Toast.makeText(context, "btnLadiesOnly Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnServices:
			Toast.makeText(context, "btnServices Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnFurniture:
			Toast.makeText(context, "btnFurniture Clicked", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnOthers:
			Toast.makeText(context, "btnOthers Clicked", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
		
		
		
		
		
	}
}