package com.smp.findlocal;

import static com.smp.findlocal.Constants.*;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class SearchTextDialog extends DialogFragment implements OnClickListener
{
	SearchQuery query;

	public interface SearchTextDialogListener
	{
		public void onSearchEntered(SearchQuery query);
	}

	SearchTextDialogListener mListener;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			mListener = (SearchTextDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement SearchDialogListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Enter Search Text");
		View view = inflater.inflate(R.layout.search_text_layout, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		query = getArguments().getParcelable(CATEGORY_RESULTS);
		//getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getDialog().findViewById(R.id.ok_button).setOnClickListener(this);

		String cg = query.getCategoryGroupName();
		if (cg.equals(FOR_SALE) || cg.equals(ANIMALS) || cg.equals(REAL_ESTATE) || cg.equals(SERVICES) || cg.equals(VEHICLES))
		{
			getView().findViewById(R.id.price_layout).setVisibility(View.VISIBLE);
		}
		if (cg.equals(PERSONALS))
		{
			getView().findViewById(R.id.age_layout).setVisibility(View.VISIBLE);
		}
	}

	public void onClick(View view)
	{
		String searchText = ((TextView) getView().findViewById(R.id.search_text)).getText().toString();
		boolean hasImage = ((CheckBox) getView().findViewById(R.id.thumbs_check)).isChecked();
		boolean titleOnly = ((RadioButton) getView().findViewById(R.id.title_button)).isChecked();
		String minAgeText = ((TextView) getView().findViewById(R.id.min_age_text)).getText().toString();
		String maxAgeText = ((TextView) getView().findViewById(R.id.max_age_text)).getText().toString();
		String minPriceText = ((TextView) getView().findViewById(R.id.min_price_text)).getText().toString();
		String maxPriceText = ((TextView) getView().findViewById(R.id.max_price_text)).getText().toString();

		if (searchText.length() == 0)
			searchText = ENTIRE_CATEGORY;

		query.setSearchString(searchText);
		query.setHasImage(hasImage);
		query.setTitleOnly(titleOnly);
		query.setMinAge(minAgeText);
		query.setMaxAge(maxAgeText);
		query.setMinPrice(minPriceText);
		query.setMaxPrice(maxPriceText);

		mListener.onSearchEntered(query);
		dismiss();
	}
}
