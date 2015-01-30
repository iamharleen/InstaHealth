/**
 * This file layout inflater for product details and downloads image in background through bitmap.
 * 
 * @author Harleen Kaur
 * @version 1.0
 */

package com.example.cmpe285healthproject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	// defining variables for product details
	private Context context;
	private final String[] healthTipsArray;
	private final String[] linkArray;

	// creating constructor to assign values locally sent from main activity
	// file
	public ListAdapter(Context context, String[] healthTips, String[] link) {
		this.context = context;
		this.healthTipsArray = healthTips;
		this.linkArray = link;
	}

	// creating viewholder class for holding values for every element of product
	// array
	private static class ViewHolder {
		TextView textView1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		// creating inflater for the layout on screen
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder healthTipsHolder;
		if (row == null) {
			// adding the product details layout in inflater
			row = inflater.inflate(R.layout.activity_list_adapter, null);

			// declaring viewholder
			healthTipsHolder = new ViewHolder();
				
			healthTipsHolder.textView1 = (TextView) row
					.findViewById(R.id.tip_name);
			//healthTipsHolder.textView2 = (TextView) row
				//	.findViewById(R.id.tip_link);

			// set text of product details according to the array position
			healthTipsHolder.textView1.setText(healthTipsArray[position] + "\n" + linkArray[position]);
			//healthTipsHolder.textView2.setText(linkArray[position]);

			row.setTag(healthTipsHolder);			
		}

		return row;
	}

	@Override
	public int getCount() {
		return healthTipsArray.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}