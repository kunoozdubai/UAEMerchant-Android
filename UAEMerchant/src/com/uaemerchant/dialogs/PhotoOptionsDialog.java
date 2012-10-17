package com.uaemerchant.dialogs;



import java.io.File;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.uaemerchant.R;
import com.uaemerchant.activities.UAEMerchantMainActivity;
import com.uaemerchant.common.Utilities;

public class PhotoOptionsDialog {
	
	private Context context;
	private Options[] options;

	public PhotoOptionsDialog(Context ctx) {
		context = ctx;
	}

	public void show( ) {
		loadOptions();
		// define the list adapter with the choices
		ListAdapter adapter = new OptionsAdapter( context, options );

		final AlertDialog.Builder ad = new AlertDialog.Builder(context);
		// define the alert dialog with the choices and the action to take
		// when one of the choices is selected
		ad.setCancelable(false);
		ad.setTitle("Upload a Picture");
		ad.setSingleChoiceItems( adapter, -1, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// a choice has been made!
				handleDialogSelection(options[which].getId());
				Log.d("", "chosen " + options[which].getName() );
				dialog.dismiss();
				
			}
		});
		ad.setNeutralButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		ad.show();
		
	}
	/**
	 * Create all the choices for the list
	 */
	private void loadOptions() {
		options = new Options[2];
		
		options[0]  = new Options( "Take Picture from Camera", 0);
		options[1]  = new Options( "Choose from Library", 1);
		
	}
	/**
	 * Definition of the list adapter...uses the View Holder pattern to
	 * optimize performance.
	 */
	static class OptionsAdapter extends ArrayAdapter<Options> {

		private static final int RESOURCE = R.layout.source_options;
		private LayoutInflater inflater;

		static class ViewHolder {
			TextView nameTxVw;
		}

		public OptionsAdapter(Context context, Options[] objects)
		{
			super(context, RESOURCE, objects);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;

			if ( convertView == null ) {
				// inflate a new view and setup the view holder for future use
				convertView = inflater.inflate( RESOURCE, null );

				holder = new ViewHolder();
				holder.nameTxVw =
					(TextView) convertView.findViewById(R.id.name);
				convertView.setTag( holder );
			}  else {
				// view already defined, retrieve view holder
				holder = (ViewHolder) convertView.getTag();
			}

			Options options = (Options) getItem( position );
			if ( options == null ) {
				Log.e( "dialog", "Invalid category for position: " + position );
			}
            if (options != null) {
                holder.nameTxVw.setText( options.getName() );
            }
            //			holder.nameTxVw.setCompoundDrawables(null, null, options.getImg(), null);
			return convertView;
		}
	}

	/**
	 * POJO for holding each list choice
	 *
	 */
	class Options {
		private String name;
		private int id;

		public Options( String name, int id) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
	
	public void handleDialogSelection(int id){
		Uri fileUri;
		switch(id){
		case 0:
			// take Picture
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(); 
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			((UAEMerchantMainActivity)Utilities.mainActivityContext).startActivityForResult(intent, 100);
			break;
		case 1:
			// chose from library
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/jpg");
				photoPickerIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/Pictures"));
				((UAEMerchantMainActivity)Utilities.mainActivityContext).startActivityForResult(photoPickerIntent, 200);
			}
			break;
		}
	}
	
	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(){
		return Uri.fromFile(getOutputMediaFile());
	}
	
	/** Create a File for saving an image or video */
	private File getOutputMediaFile(){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "UAEMerchant");
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				return null;
			}
		}
		String imagePath = mediaStorageDir.getPath() + File.separator + new Date().getTime() + ".jpg";
		UAEMerchantMainActivity.setImagePath(imagePath);
		return new File(imagePath);
	}
	
}
