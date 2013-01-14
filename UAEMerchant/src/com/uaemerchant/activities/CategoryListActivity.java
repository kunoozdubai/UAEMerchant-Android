package com.uaemerchant.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.adapters.CategoryListViewAdapter;
import com.uaemerchant.asynctask.DataDownloadTask;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.NetworkConstants;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.dialogs.ItemDetailDialog;
import com.uaemerchant.network.Parser;
import com.uaemerchant.pojo.Ad;

public class CategoryListActivity extends Activity implements View.OnClickListener, OnItemClickListener, OnScrollListener {

	private Context context;
	private ListView myList = null;
	CategoryListViewAdapter adapter = null;
	
	private static int totalRows;

	private EditText searchBar;
	private String key = "";

	public int page = 1;

	ArrayList<Ad> categoryArrayList = null;

	private String latest = "";
	private String catName;
	private String catId;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_list_activity);
		context = this; 
		
		ImageView imageView = (ImageView) findViewById(R.id.background);
		imageView.setBackgroundDrawable(Utilities.imageMap.get("mainBackground"));
		
		Bundle extras = getIntent().getExtras();
		catName = extras.getString("catName");
		catId = extras.getString("catId");
		catName = catName.trim();
		
//		TextView titleTxt = (TextView) findViewById(R.id.titleTxt);
//		titleTxt.setText(catName);
		
		Button button = (Button) findViewById(R.id.btnCategories);
		button.setOnClickListener(this);

		categoryArrayList = new ArrayList<Ad>();

		String postData = createRequestJSON(catId, String.valueOf(page), "10", key);

		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair(NetworkConstants.VIEW,
		// "lawarticles"));
		// nameValuePairs.add(new BasicNameValuePair(NetworkConstants.PAGE,
		// String
		// .valueOf(page)));
		// nameValuePairs.add(new BasicNameValuePair("json", "1"));
		// if (!Utilities.isStringEmptyOrNull(latest)) {
		// nameValuePairs.add(new BasicNameValuePair(NetworkConstants.LATEST,
		// latest));
		// titleTxt.setText(getString(R.string.al_mastajrat_al_nasharyah_btn));
		// } else {
		// titleTxt.setText(getString(R.string.al_qawaneen_btn));
		// }
		Utilities.showprogressDialog(context, "Loading...");
		new DataDownloadTask(context, new AdsResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_AD_LIST, postData).execute();

		searchBar = (EditText) findViewById(R.id.searchBar);
		searchBar.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_ENTER:
						// Toast.makeText(context, "Searching: " +
						// searchBar.getText().toString(),
						// Toast.LENGTH_SHORT).show();
						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						updateList();
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

	}

	private void updateList() {
		Toast.makeText(context, "Searching: " + searchBar.getText().toString(), Toast.LENGTH_SHORT).show();
		page = 1;
		key = searchBar.getText().toString();

		categoryArrayList = null;
		categoryArrayList = new ArrayList<Ad>();

		// Button button = (Button) findViewById(R.id.loadMoreBtn);
		// button.setVisibility(View.VISIBLE);
		String postJSON = createRequestJSON(catId, String.valueOf(page), "10", key);
		new DataDownloadTask(context, new AdsResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_AD_LIST, postJSON).execute();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onScroll(AbsListView lw, final int firstVisibleItem,
	                 final int visibleItemCount, final int totalItemCount) {

//	    switch(lw.getId()) {
//	        case R.id.list:     
//  	            final int lastItem = firstVisibleItem + visibleItemCount;
//	            if(lastItem == totalItemCount) {
//	    			page = page + 1;
//	    			key = searchBar.getText().toString();
//	    			String postJSON = createRequestJSON(catId, String.valueOf(page), "10", key);
//	    			new DataDownloadTask(context, new AdsResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_AD_LIST, postJSON).execute();
//	            	
//	            	Toast.makeText(context, "Reached last item", Toast.LENGTH_SHORT).show();
//	            	lw = null;
//	            }
//	            break;
//	        default:
//	        	break;
//	    }
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Ad adObject = (Ad) adapter.getItem(position);
		new ItemDetailDialog(context, adObject).show();
//		Toast.makeText(context, "Item Clicked: " + adObject.getTitle(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnCategories) {
			finish();
		}
	}

	private class AdsResponse implements IResponseListener {

		@Override
		public void onSuccess(JSONObject response) {
//			 Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
			if (categoryArrayList.size() == 0) {
				categoryArrayList = Parser.parseAdsList(response);
				adapter = new CategoryListViewAdapter(context, categoryArrayList);
				myList = (ListView) findViewById(R.id.list);
				myList.setAdapter(adapter);
				myList.setOnItemClickListener(CategoryListActivity.this);
				adapter.setListener(CategoryListActivity.this);
				myList.setOnScrollListener(CategoryListActivity.this);
			} else {
				ArrayList<Ad> newArrayList = Parser.parseAdsList(response);
				categoryArrayList = mergeAdsList(categoryArrayList, newArrayList);
				adapter.notifyDataSetChanged();
				myList.invalidate();
			}
		}

		@Override
		public void onError(JSONObject response) {
//			Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show();
			// Button button = (Button) findViewById(R.id.loadMoreBtn);
			// button.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		Utilities.unbindDrawables(findViewById(R.id.ad_list_activity));
		Utilities.clearThumbMap();
		System.gc();
		
		super.onDestroy();
	}

	private ArrayList<Ad> mergeAdsList(ArrayList<Ad> AdsArrayList, ArrayList<Ad> newArrayList) {
		for (int i = 0; i < newArrayList.size(); i++) {
			AdsArrayList.add(newArrayList.get(i));
		}
		return AdsArrayList;
	}

	private static String createRequestJSON(String catId, String page, String limit, String key) {
		StringBuilder requestData = new StringBuilder();
		requestData.append(NetworkConstants.CAT_ID);
		requestData.append("=");
		requestData.append(catId);
		requestData.append("&");
		requestData.append(NetworkConstants.PAGE);
		requestData.append("=");
		requestData.append(page);
		requestData.append("&");
		requestData.append(NetworkConstants.LIMIT);
		requestData.append("=");
		requestData.append(limit);
		if (!Utilities.isStringEmptyOrNull(key)) {
			requestData.append("&");
			requestData.append(NetworkConstants.KEY);
			requestData.append("=");
			requestData.append(key);
			
		}
		return requestData.toString();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(totalRows > myList.getAdapter().getCount()){
			if (myList.getLastVisiblePosition() + 1 >= myList.getAdapter().getCount()) {
				myList.smoothScrollToPosition(myList.getAdapter().getCount() - 3);
				page = page + 1;
				key = searchBar.getText().toString();
				String postJSON = createRequestJSON(catId, String.valueOf(page), "10", key);
				new DataDownloadTask(context, new AdsResponse(), NetworkConstants.UAE_MERCHANT_URL + NetworkConstants.WS_AD_LIST, postJSON).execute();
//				Toast.makeText(context, "Reached last item", Toast.LENGTH_SHORT).show();
	
			}
		}

	}

	public static int getTotalRows() {
		return totalRows;
	}

	public static void setTotalRows(int totalRows) {
		CategoryListActivity.totalRows = totalRows;
	}

}