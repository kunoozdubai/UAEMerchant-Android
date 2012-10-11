package com.uaemerchant.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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

		View view = convertView;
//		ViewHolder viewHolder;

		if (view == null) {
//			viewHolder = new ViewHolder();
//			view = mInflater.inflate(R.layout.law_articles_item, parent, false);
//			viewHolder.mainParent = (RelativeLayout) view.findViewById(R.id.mainParent);
//
//			viewHolder.thumbnailImg = (ImageView) view
//					.findViewById(R.id.thumbnailImg);
//			viewHolder.titleTxt = (TextView) view.findViewById(R.id.titleTxt);
//
//			viewHolder.titleTxt.setText(categoryList.get(position)
//					.getTitle());
//			view.setTag(viewHolder);
		} else {
//			viewHolder = (ViewHolder) view.getTag();
//			viewHolder.titleTxt.setText(categoryList.get(position)
//					.getTitle());
		}
		
//		String filename = categoryList.get(position).getThumb();
//		if(!Utilities.isStringEmptyOrNull(filename) && CommonConstants.thumbMap.get(filename) == null){
//			if(filename.contains(".jpg") || filename.contains(".png")){
//				new ThumbImageDownloadTask(context, NetworkConstants.THUMB_URL, filename, viewHolder.thumbnailImg).execute();
//			}
//		}
//		if(CommonConstants.thumbMap.get(filename) != null){
//			viewHolder.thumbnailImg.setBackgroundDrawable(CommonConstants.thumbMap.get(filename));
//		}else{
//			viewHolder.thumbnailImg.setBackgroundResource(R.drawable.new_image);
//		}
//		viewHolder.downloadBtn.setOnClickListener(listener);
//		viewHolder.downloadBtn.setTag(viewHolder);

		return view;
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

}
