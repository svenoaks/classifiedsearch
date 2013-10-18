package com.smp.findlocal;

import static com.smp.findlocal.Constants.*;
import static com.smp.findlocal.UtilityMethods.readObjectFromFile;
import static com.smp.findlocal.UtilityMethods.writeObjectToFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.smp.findlocal.SearchTextDialog.SearchTextDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SourcesDialog extends DialogFragment
{
	private List<ClassifiedSources> mSelectedItems = new ArrayList<ClassifiedSources>();

	public interface SourcesDialogListener
	{
		public void onSourcesEntered(List<ClassifiedSources> sources);
	}

	SourcesDialogListener mListener;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			mListener = (SourcesDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement SourcesDialogListener");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Object src = readObjectFromFile(getActivity(), SOURCES_FILENAME);
		if (src != null)
			mSelectedItems.addAll((List<ClassifiedSources>) src);
		else
			mSelectedItems.addAll(Arrays.asList(ClassifiedSources.values()));
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Set the dialog title
		String[] sources = new String[ClassifiedSources.values().length];

		boolean[] checked = new boolean[sources.length];
		for (ClassifiedSources source : mSelectedItems)
		{
			checked[source.ordinal()] = true;
		}
		for (int i = 0; i < sources.length; ++i)
		{
			sources[i] = ClassifiedSources.values()[i].toString();
		}
		builder.setTitle("Choose Sources")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setMultiChoiceItems(sources, checked,
						new DialogInterface.OnMultiChoiceClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which,
									boolean isChecked)
							{
								if (isChecked)
								{
									// If the user checked the item, add it to
									// the selected items
									mSelectedItems.add(ClassifiedSources.values()[which]);
								}
								else if (mSelectedItems.contains(ClassifiedSources.values()[which]))
								{
									mSelectedItems.remove(ClassifiedSources.values()[which]);
								}
							}
						})

				.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						if (mSelectedItems.size() == 0)
						{
							Toast.makeText(getActivity(), "You must select at least one", Toast.LENGTH_SHORT).show();
							mSelectedItems.addAll(Arrays.asList(ClassifiedSources.values()));
						}
						writeObjectToFile(getActivity(), SOURCES_FILENAME, mSelectedItems);
						mListener.onSourcesEntered(mSelectedItems);
					}
				})
				/*.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{

					}
				})*/;
		
		return builder.create();
	}
	
	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
	}
}
