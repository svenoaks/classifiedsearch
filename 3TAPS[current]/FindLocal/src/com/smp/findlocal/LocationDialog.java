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
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LocationDialog extends DialogFragment implements OnClickListener
{
	public interface LocationDialogListener
	{
		public void onLocationEntered(String location);
	}
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		try
		{
			listener = (LocationDialogListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString()
					+ " must implement LocationDialogListener");
		}
	}
	LocationDialogListener listener;
	EditText text;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().setTitle("Enter Location");
		View view = inflater.inflate(R.layout.location_layout, container, false);
		return view;
	}
	

	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		getDialog().findViewById(R.id.loc_ok).setOnClickListener(this);
		text = (EditText) getDialog().findViewById(R.id.location_text);
	}
	@Override
	public void onClick(View v)
	{
		listener.onLocationEntered(text.getText().toString());
		dismiss();
	}
	
}
