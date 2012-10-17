package com.uaemerchant.dialogs;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.asynctask.DataDownloadTask;
import com.uaemerchant.common.CommonConstants;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.network.Parser;

public class RegisterDialog extends Dialog implements View.OnClickListener, OnCancelListener {
	private View registerView;
	private Context context = null;
	private Activity activity;
	
	String name;
	String email;
	String phone;
	String city;
	
	public RegisterDialog(Context context) {
		super(context, R.style.preview);
		this.context = context;
		activity = (Activity) this.context;
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		registerView = layoutInflater.inflate(R.layout.register_dialog, (ViewGroup) activity.findViewById(R.layout.main_activity));
		setContentView(registerView);
	}

	@Override
	public void show() {
		super.show();
		initializeViews();

	}

	private void initializeViews() {

		Button button = (Button) registerView.findViewById(R.id.saveBtn);
		button.setOnClickListener(this);
		button = (Button) registerView.findViewById(R.id.registerBtn);
		button.setOnClickListener(this);
		
		Spinner s1 = (Spinner) findViewById(R.id.cityInput);
		ArrayAdapter<CharSequence> adaptergender = ArrayAdapter.createFromResource(context, R.array.cities, android.R.layout.simple_spinner_item);
		adaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s1.setAdapter(adaptergender);
	}

	@Override
	public void hide() {
		cancel();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.registerBtn){
			
			email = ((EditText)registerView.findViewById(R.id.emailInput)).getText().toString();
			if(!Utilities.isValidEmail(email)){
				Toast.makeText(context, "Please provide a valid email!", Toast.LENGTH_SHORT).show();
				return;
			}
			name = ((EditText)registerView.findViewById(R.id.nameInput)).getText().toString();
			phone = ((EditText)registerView.findViewById(R.id.phoneInput)).getText().toString();
			city = ((Spinner) findViewById(R.id.cityInput)).getSelectedItem().toString();
			
			String postData = createRequestJSON(name, email, phone, city);
			
			new DataDownloadTask(context, new RegisterResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_REGISTER, postData).execute();
			
			
		}

	}

	private OnKeyListener registerKeyListener = new OnKeyListener() {

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
	
	private class RegisterResponse implements IResponseListener {

		@Override
		public void onSuccess(JSONObject response) {
			Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
			String userId = Parser.parseRegisterationResponse(response);
			
			if(!Utilities.isStringEmptyOrNull(userId)){
				
				Utilities.setStringValuesToPreferences(context, CommonConstants.PREF_USER_ID, userId);
				Utilities.setStringValuesToPreferences(context, CommonConstants.PREF_NAME, name);
				Utilities.setStringValuesToPreferences(context, CommonConstants.PREF_EMAIL, email);
				Utilities.setStringValuesToPreferences(context, CommonConstants.PREF_PHONE, phone);
				Utilities.setStringValuesToPreferences(context, CommonConstants.PREF_CITY, city);
				Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show();
			}
			hide();				
			
		}

		@Override
		public void onError(JSONObject response) {
			Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		context = null;
		activity = null;
		Utilities.unbindDrawables(findViewById(R.id.register_dialog));
	}
	
	private static String createRequestJSON(String name, String email, String phone, String city) {
		StringBuilder requestData = new StringBuilder();
		requestData.append(NetworkConstants.NAME);
		requestData.append("=");
		requestData.append(name);
		requestData.append("&");
		requestData.append(NetworkConstants.EMAIL);
		requestData.append("=");
		requestData.append(email);
		requestData.append("&");
		requestData.append(NetworkConstants.PHONE);
		requestData.append("=");
		requestData.append(phone);
		requestData.append("&");
		requestData.append(NetworkConstants.CITY);
		requestData.append("=");
		requestData.append(city);
			
		return requestData.toString();
	}

}