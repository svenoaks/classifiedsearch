package com.smp.findlocal;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchesArrayAdapter extends ArrayAdapter<SearchQuery>
{
	List<SearchQuery> items;

	public SearchesArrayAdapter(Context context, int resource, int textViewResourceId,
			List<SearchQuery> objects)
	{
		super(context, resource, textViewResourceId, objects);
		items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = super.getView(position, null, parent);
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		SearchQuery thisQuery = items.get(position);
		text1.setText(thisQuery.toString());
		text2.setText(thisQuery.getCategoryName());
		
		String minPrice = thisQuery.getMinPrice();
		String maxPrice = thisQuery.getMaxPrice();
		String minAge = thisQuery.getMinAge();
		String maxAge = thisQuery.getMaxAge();
		if (minPrice != null && minPrice.length() > 0)
		{
			TextView mp = (TextView) view.findViewById(R.id.min_price_text);
			mp.setVisibility(View.VISIBLE);
			mp.setText("Min: $" + minPrice);
		}
		else if (minAge != null && minAge.length() > 0)
		{
			TextView mp = (TextView) view.findViewById(R.id.min_age_text);
			mp.setVisibility(View.VISIBLE);
			mp.setText("Min Age: " + minAge);
		}
		if (maxPrice != null && maxPrice.length() > 0)
		{
			TextView mp = (TextView) view.findViewById(R.id.max_price_text);
			mp.setVisibility(View.VISIBLE);
			mp.setText("Max: $" + maxPrice);
		}
		else if (maxAge != null && maxAge.length() > 0)
		{
			TextView mp = (TextView) view.findViewById(R.id.max_age_text);
			mp.setVisibility(View.VISIBLE);
			mp.setText("Max Age: " + maxAge);
		}
		return view;
	}
}
