package com.uaemerchant.adapters;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uaemerchant.R;
import com.uaemerchant.asynctask.ThumbImageDownloadTask;
import com.uaemerchant.common.Utilities;
import com.uaemerchant.pojo.Ad;

public class CategoryListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private ArrayList<Ad> categoryList;

	private OnClickListener listener;
	
	

	public CategoryListViewAdapter(Context context, ArrayList<Ad> list) {
		this.context = context;
		this.categoryList = list;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (categoryList != null) {
			return categoryList.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (categoryList != null && position >= 0 && position < getCount()) {
			return categoryList.get(position);
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		if (categoryList != null && position >= 0 && position < getCount()) {
			return 0;
		}

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final View view;
		final ViewHolder viewHolder;
		final Ad a = categoryList.get(position);

		if (convertView == null) {
			viewHolder = new ViewHolder();
			view = mInflater.inflate(R.layout.ad_item, parent, false);

			viewHolder.thumbnailImg = (ImageView) view.findViewById(R.id.thumbnailImg);
			viewHolder.itemName = (TextView) view.findViewById(R.id.itemName);
			viewHolder.priceTxt = (TextView) view.findViewById(R.id.itemPrice);
			viewHolder.byTxt = (TextView) view.findViewById(R.id.byTxt);
			viewHolder.dateTxt = (TextView) view.findViewById(R.id.dateTxt);
			viewHolder.descriptionTxt = (TextView) view.findViewById(R.id.itemDescription);

//			viewHolder.itemName.setText(categoryList.get(position).getTitle());
//			viewHolder.priceTxt.setText(categoryList.get(position).getPrice());
//			viewHolder.byTxt.setText(categoryList.get(position).getName());
//			viewHolder.dateTxt.setText(categoryList.get(position).getCreated());
			viewHolder.itemName.setText(a.getTitle());
			viewHolder.priceTxt.setText(a.getPrice());
			viewHolder.byTxt.setText(a.getName());
//			Date date = new Date());
//			DateFormat df = DateFormat.getDateTimeInstance();
//			String dateStr = df.format(date);
			viewHolder.dateTxt.setText(a.getCreated().split(" ")[0]);
			viewHolder.descriptionTxt.setText(a.getDescription().trim());
			
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
			viewHolder.itemName.setText(a.getTitle());
			viewHolder.priceTxt.setText(a.getPrice());
			viewHolder.byTxt.setText(a.getName());
//			Date date = new Date(a.getCreated());
//			DateFormat df = DateFormat.getDateTimeInstance();
//			String dateStr = df.format(date);
			viewHolder.dateTxt.setText(a.getCreated().split(" ")[0]);
			viewHolder.descriptionTxt.setText(a.getDescription().trim());
		}
		
		String url = a.getPhoto1();
		String[] urlTokens = url.split("/");
		String filename = urlTokens[urlTokens.length - 1];
		ImageView imageView  = (ImageView)view.findViewById(R.id.thumbnailImg);
		if(!Utilities.isStringEmptyOrNull(url) && Utilities.thumbMap.get(url) == null){
				new ThumbImageDownloadTask(context, filename, url, imageView).execute();
		}
		if(Utilities.thumbMap.get(filename) != null){
			imageView.setBackgroundDrawable(Utilities.thumbMap.get(filename));
//			viewHolder.thumbnailImg.setBackgroundDrawable(Utilities.thumbMap.get(filename));
		}else{
			imageView.setBackgroundDrawable(Utilities.imageMap.get("placeHolder"));
		}

		return view;
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	public class ViewHolder{
		ImageView thumbnailImg;
		TextView itemName;
		TextView priceTxt;
		TextView byTxt;
		TextView dateTxt;
		TextView descriptionTxt;
	}

}
