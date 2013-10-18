/* This file is part of C-Feed for Android <http://github.com/jpkrause>.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License version 3
* as published by the Free Software Foundation.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
* for more details.
*
* Copyright (C) 2013 John Krause
*/
package com.jpkrause.c_feed;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private ArrayList<ResultsDetails> _data;
	Context _c;

	CustomAdapter(ArrayList<ResultsDetails> data, Context c) {
		_data = data;
		_c = c;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) _c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item_message, null);
		}

		ImageView image = (ImageView) v.findViewById(R.id.icon);
		TextView titleView = (TextView) v.findViewById(R.id.title);
		TextView descView = (TextView) v.findViewById(R.id.description);
		TextView dateView = (TextView) v.findViewById(R.id.date);

		ResultsDetails msg = _data.get(position);
		image.setImageResource(msg.getIcon());
		titleView.setText(msg.getTitle());
		descView.setText(msg.getDesc());
		dateView.setText(msg.getDate());

		return v;
	}
}
