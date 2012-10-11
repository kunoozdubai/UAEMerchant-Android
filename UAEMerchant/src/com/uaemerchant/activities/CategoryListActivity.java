package com.uaemerchant.activities;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.uaemerchant.R;
import com.uaemerchant.adapters.CategoryListViewAdapter;
import com.uaemerchant.common.IResponseListener;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.network.Parser;
import com.uaemerchant.pojo.Ad;

public class CategoryListActivity extends Activity implements
		OnClickListener, OnItemClickListener {

	private Context context;
	private static ListView myList;
	CategoryListViewAdapter adapter = null;

	private EditText searchBar;
	private String key = "";

	public int page = 1;

	ArrayList<Ad> categoryArrayList = null;

	private String latest = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_list_activity);
		context = this;

//		TextView titleTxt = (TextView) findViewById(R.id.titelTxt);

		Bundle extras = getIntent().getExtras();

		categoryArrayList = new ArrayList<Ad>();

//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.VIEW,
//				"lawarticles"));
//		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.PAGE, String
//				.valueOf(page)));
//		nameValuePairs.add(new BasicNameValuePair("json", "1"));
//		if (!Utilities.isStringEmptyOrNull(latest)) {
//			nameValuePairs.add(new BasicNameValuePair(NetworkConstants.LATEST,
//					latest));
//			titleTxt.setText(getString(R.string.al_mastajrat_al_nasharyah_btn));
//		} else {
//			titleTxt.setText(getString(R.string.al_qawaneen_btn));
//		}
//		new DataDownloadTask(context, new LawArticleResponse(), nameValuePairs,"lawarticles")
//				.execute();
		
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
						inputManager.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
//						updateList();
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});

	}

//	private void updateList() {
////		Toast.makeText(context, "Searching: " + searchBar.getText().toString(),
////				Toast.LENGTH_SHORT).show();
//		page = 1;
//		key = searchBar.getText().toString();
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.VIEW,
//				"category"));
//		nameValuePairs.add(new BasicNameValuePair(NetworkConstants.PAGE, String
//				.valueOf(page)));
//		nameValuePairs.add(new BasicNameValuePair("json", "1"));
//		if (!Utilities.isStringEmptyOrNull(key)) {
//			nameValuePairs
//					.add(new BasicNameValuePair(NetworkConstants.KEY, key));
//		}
//		if (!Utilities.isStringEmptyOrNull(latest)) {
//			nameValuePairs.add(new BasicNameValuePair(NetworkConstants.LATEST,
//					latest));
//		}
//		categoryArrayList = null;
//		categoryArrayList = new ArrayList<LawArticles>();
//
//		Button button = (Button) findViewById(R.id.loadMoreBtn);
//		button.setVisibility(View.VISIBLE);
//
//		new DataDownloadTask(context, new LawArticleResponse(), nameValuePairs,"category")
//				.execute();
//	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Ad adObject = (Ad) adapter.getItem(position);
		
			
		Toast.makeText(context,
				"Item Clicked: " + adObject.getTitle(),
				Toast.LENGTH_SHORT).show();
		}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		
//		if (id == R.id.loadMoreBtn) {
//			page = page + 1;
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair(NetworkConstants.VIEW,
//					"category"));
//			nameValuePairs.add(new BasicNameValuePair(NetworkConstants.PAGE,
//					String.valueOf(page)));
//			nameValuePairs.add(new BasicNameValuePair("json", "1"));
//			if (!Utilities.isStringEmptyOrNull(key)) {
//				nameValuePairs.add(new BasicNameValuePair(NetworkConstants.KEY,
//						key));
//			}
//			
//			new DataDownloadTask(context, new LawArticleResponse(),
//					nameValuePairs,"category").execute();
//			// } else if (id == R.id.downloadBtn) {
//			// ViewHolder viewHolder = (ViewHolder) v.getTag();
//			// Toast.makeText(context,
//			// "downloadBtn clicked of: " + viewHolder.titleTxt.getText(),
//			// Toast.LENGTH_SHORT).show();
//		}
	}

	private class AdsResponse implements IResponseListener {

		@Override
		public void onSuccess(JSONArray response) {
//			Toast.makeText(context, "onSuccess", Toast.LENGTH_SHORT).show();
			if (categoryArrayList.size() == 0) {
				categoryArrayList = Parser.parseAdsList(response);
				adapter = new CategoryListViewAdapter(context,
						categoryArrayList);
				myList = (ListView) findViewById(R.id.list);
				myList.setAdapter(adapter);
				myList.setOnItemClickListener(CategoryListActivity.this);
				adapter.setListener(CategoryListActivity.this);
			} else {
				ArrayList<Ad> newArrayList = Parser.parseAdsList(response);
				categoryArrayList = mergeAdsList(
						categoryArrayList, newArrayList);
				adapter.notifyDataSetChanged();
				myList.invalidate();
			}
		}

		@Override
		public void onError(JSONArray response) {
			Toast.makeText(context, "onError", Toast.LENGTH_SHORT).show();
//			Button button = (Button) findViewById(R.id.loadMoreBtn);
//			button.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		Utilities.unbindDrawables(findViewById(R.id.ad_list_activity));
		System.gc();
		super.onDestroy();
	}

	private ArrayList<Ad> mergeAdsList(
			ArrayList<Ad> AdsArrayList,
			ArrayList<Ad> newArrayList) {
		for (int i = 0; i < newArrayList.size(); i++) {
			AdsArrayList.add(newArrayList.get(i));
		}
		return AdsArrayList;
	}
}