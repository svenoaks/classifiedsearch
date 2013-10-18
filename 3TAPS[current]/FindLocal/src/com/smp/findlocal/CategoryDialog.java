package com.smp.findlocal;

import static com.smp.findlocal.Constants.*;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryDialog extends DialogFragment implements OnItemClickListener
{

	private View lastSelected;
	private List<String> groupList;
	private ListView categoryGroups, categories;
	private ArrayAdapter<String> categoriesAdapter, groupsAdapter;
	private CategoryResults catResults;

	public CategoryDialog()
	{
	}

	public interface CategoryDialogListener
	{
		public void onCategorySelected(String categoryGroup, String categoryName);
	}

	CategoryDialogListener mListener;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try
		{
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (CategoryDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement CategoryDialogListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		catResults = (CategoryResults) args.getParcelable(CATEGORY_RESULTS);
		groupList = catResults.getCategoryGroups();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Choose a Category");
		View view = inflater.inflate(R.layout.category_dialog, container);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		categoryGroups = (ListView) getView().findViewById(R.id.category_groups_list);
		categories = (ListView) getView().findViewById(R.id.categories_list);

		groupsAdapter =
				new AnArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, groupList);

		groupsAdapter.setNotifyOnChange(true);
		categoryGroups.setOnItemClickListener(this);
		categoryGroups.setAdapter(groupsAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
	{
		if (adapter.equals(categoryGroups))
		{
			if (lastSelected != null)
				lastSelected.setBackgroundColor(Color.parseColor(DEFAULT_HOLO_LIGHT));

			view.setBackgroundColor(Color.parseColor(MY_YELLOW));
			lastSelected = view;

			String group = groupsAdapter.getItem(position);
			if (categoriesAdapter != null)
			{
				categoriesAdapter.clear();
				categoriesAdapter.add(ALL + SPACE + group);
				categoriesAdapter.addAll(catResults.getCategoryNames(group));
			}
			else
			{
				categoriesAdapter =
						new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<String>());
				categoriesAdapter.setNotifyOnChange(true);
				categoriesAdapter.add(ALL + SPACE + group);
				categoriesAdapter.addAll(catResults.getCategoryNames(group));
				categories.setOnItemClickListener(this);
				categories.setAdapter(categoriesAdapter);
			}
		}
		else
		{
			TextView groupText = (TextView) (lastSelected.findViewById(android.R.id.text1));
			TextView catText = (TextView) (view.findViewById(android.R.id.text1));

			String groupString = groupText.getText().toString();
			String catString = catText.getText().toString();

			if (catString.equals(ALL + SPACE + groupString))
				catString = ALL_CATEGORIES;

			mListener.onCategorySelected(groupString, catString);
			dismiss();
		}
	}
}

class AnArrayAdapter<T> extends ArrayAdapter<T>
{
	public AnArrayAdapter(Activity activity, int simpleListItem1, List<T> arrayList)
	{
		super(activity, simpleListItem1, arrayList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return super.getView(position, null, parent);
	}
}
