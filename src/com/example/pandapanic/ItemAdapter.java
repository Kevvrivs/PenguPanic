package com.example.pandapanic;

import model.ChecklistItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ItemAdapter extends ArrayAdapter<ChecklistItem>{
	/**
	 * Adapter context
	 */
	Context mContext;

	/**
	 * Adapter View layout
	 */
	int mLayoutResourceId;

	public ItemAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);

		mContext = context;
		mLayoutResourceId = layoutResourceId;
	}

	/**
	 * Returns the view for a specific item on the list
	 */
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		final ChecklistItem currentItem = getItem(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);
		}

		row.setTag(currentItem);
		final CheckBox checkBox = (CheckBox) row.findViewById(R.id.checkIsCompleted);
		final TextView textName = (TextView)row.findViewById(R.id.textItem);
		final TextView textQuantity = (TextView) row.findViewById(R.id.textQuantity);
		checkBox.setChecked(false);
		checkBox.setEnabled(true);
		textName.setText(currentItem.getItemName());
		textQuantity.setText(Float.toString(currentItem.getQuantity()));
		

		return row;
	}
	

}
