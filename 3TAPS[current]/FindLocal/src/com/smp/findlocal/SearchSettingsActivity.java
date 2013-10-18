package com.smp.findlocal;

import static com.smp.findlocal.UtilityMethods.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.smp.findlocal.CategoryDialog.CategoryDialogListener;
import com.smp.findlocal.LocationDialog.LocationDialogListener;
import com.smp.findlocal.SearchTextDialog.SearchTextDialogListener;
import com.smp.findlocal.SourcesDialog.SourcesDialogListener;

import static com.smp.findlocal.Constants.*;

import android.app.Activity;
import android.app.DialogFragment;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchSettingsActivity extends Activity implements CategoryDialogListener, SearchTextDialogListener, 
OnItemClickListener, SourcesDialogListener, LocationDialogListener, OnItemSelectedListener
{
	final int NOTHING_SELECTED = -1;
	
	int lastSelectedPosition = NOTHING_SELECTED;
	View lastSelected;
	private SearchesArrayAdapter searchesAdapter;
	private SearchQuery pendingQuery;
	private boolean catSuccessful = false;
	private boolean isTrying = false;
	BroadcastReceiver networkStateReceiver;
	IntentFilter connectFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	private CategoryResults catResults;
	private List<SearchQuery> searches = new ArrayList<SearchQuery>();
	private ListView searchView;
	Drawable yellowBackground, defaultBackground;
	private int mAppWidgetId;
	Button sourcesButton, locationButton, distanceButton;
	Spinner distSpinner;

	@Override
	protected void onPause()
	{
		super.onPause();
		if (networkStateReceiver != null)
			unregisterReceiver(networkStateReceiver);

		writeObjectToFile(this, SEARCHES_FILENAME, searches);

		selectNothing();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onResume()
	{
		super.onResume();
		if (networkStateReceiver != null)
			registerReceiver(networkStateReceiver, connectFilter);

		searchesAdapter.clear();
		Object obj = readObjectFromFile(this, SEARCHES_FILENAME);
		if (obj != null)
		{
			searchesAdapter.addAll((List<SearchQuery>) obj);
		}
		Object obj2 = readObjectFromFile(this, SOURCES_FILENAME);
		if (obj2 != null)
		{
			setSourceText((List<ClassifiedSources>) obj2);
		}
	}

	private void selectNothing()
	{
		if (lastSelected != null)
			lastSelected.setBackgroundDrawable(defaultBackground);
		lastSelected = null;
		lastSelectedPosition = NOTHING_SELECTED;
	}

	private class getCategoriesRunnable implements Runnable
	{
		@Override
		public void run()
		{
			ResultsGenerator generator = new ResultsGenerator(ResultsGenerator.CATEGORIES);
			generator.generate();
			if (generator.isUpdateSucceeded())
				catResults = ResultsGenerator.getCategories();
			catSuccessful = generator.isUpdateSucceeded();
			isTrying = false;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putParcelable(SEARCH_QUERY, pendingQuery);
		outState.putParcelable(CATEGORY_RESULTS, catResults);
		outState.putBoolean(CAT_SUCCESSFUL, catSuccessful);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_settings_activity);

		setResult(RESULT_CANCELED);
		if (savedInstanceState != null)
		{
			pendingQuery = savedInstanceState.getParcelable(SEARCH_QUERY);
			catResults = savedInstanceState.getParcelable(CATEGORY_RESULTS);
			catSuccessful = savedInstanceState.getBoolean(CAT_SUCCESSFUL);
		}

		if (catResults == null)
			catResults = ResultsGenerator.getCategories();

		if (catResults == null)
		{
			isTrying = true;
			new Thread(new getCategoriesRunnable()).start();

			networkStateReceiver = new BroadcastReceiver()
			{

				@Override
				public void onReceive(Context arg0, Intent arg1)
				{
					if (!catSuccessful)
					{
						isTrying = true;
						new Thread(new getCategoriesRunnable()).start();
					}
				}
			};
		}
		else
		{
			catSuccessful = true;
		}
		searchView = (ListView) findViewById(R.id.searches_view);
		searchView.setOnItemClickListener(this);

		searchesAdapter = new SearchesArrayAdapter(this, R.layout.search_list_item, android.R.id.text1, searches);
		searchesAdapter.setNotifyOnChange(true);
		searchView.setAdapter(searchesAdapter);

		defaultBackground = getResources().getDrawable(R.drawable.search_item_default_inset);
		yellowBackground = getResources().getDrawable(R.drawable.search_item_yellow_inset);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
		{
			finish();
		}

		sourcesButton = (Button) findViewById(R.id.sources);
		locationButton = (Button) findViewById(R.id.location);
		distSpinner = (Spinner) findViewById(R.id.within);

		SharedPreferences pref = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		locationButton.setText(pref.getString(LOC_DESC, LOC_NOT_SET));
		
		ArrayAdapter<CharSequence> distAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner, distances);
		distAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		distSpinner.setAdapter(distAdapter);
		distSpinner.setSelection(pref.getInt(DISTANCE, DEFAULT_DISTANCE_POS));
		distSpinner.setOnItemSelectedListener(this);
			
	}

	public void setCatSuccessful(boolean result)
	{
		catSuccessful = result;
	}

	public void finished(View view)
	{
		if (locationButton.getText().toString().equals(LOC_NOT_SET))
		{
			makeMustSetLocationToast();
		}
		else if (searches.size() == 0)
		{
			addSearchToast();
		}
		else
		{
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);

			Intent updateIntent = new Intent(this, WidgetProvider.class);
			updateIntent.setAction(REFRESH_ACTION);
			sendBroadcast(updateIntent);
			finish();
		}
	}

	private void addSearchToast()
	{
		Toast.makeText(this, "Add a search to your widget", Toast.LENGTH_SHORT).show();
		
	}

	public void addSearch(View view)
	{
		if (isTrying)
		{
			makeTryingToConnectToast();
			return;
		}
		if (catSuccessful)
		{
			if (searchesAdapter.getCount() < MAX_SEARCHES)
			{
				selectNothing();
				DialogFragment dialog = new CategoryDialog();
				Bundle args = new Bundle();
				args.putParcelable(CATEGORY_RESULTS, catResults);

				dialog.setArguments(args);
				dialog.show(getFragmentManager(), "categories");
			}
			else
				Toast.makeText(this, "A maximum of 10 searches is allowed", Toast.LENGTH_SHORT).show();
		}
		else
		{
			makeNoConnectionToast();
		}
	}

	public void removeSearch(View view)
	{
		if (lastSelectedPosition == NOTHING_SELECTED)
		{
			makeRemoveHelpToast();
		}
		else
		{
		searchesAdapter.remove((SearchQuery) (searchView.getItemAtPosition(lastSelectedPosition)));
		selectNothing();
		}
	}

	private void makeRemoveHelpToast()
	{
		Toast.makeText(this, "Touch a search to remove it", Toast.LENGTH_LONG).show();
		
	}

	public void sourcesClick(View view)
	{
		DialogFragment dialog = new SourcesDialog();
		dialog.show(getFragmentManager(), "sources_dialog");
	}

	public void locationClick(View view)
	{
		DialogFragment dialog = new LocationDialog();
		dialog.show(getFragmentManager(), "location_dialog");
		//dialog.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	

	private void makeNoConnectionToast()
	{
		Toast.makeText(this, "You must be online", Toast.LENGTH_LONG).show();
	}

	private void makeTryingToConnectToast()
	{
		Toast.makeText(this, "Loading categories, try again in a second", Toast.LENGTH_SHORT).show();
	}

	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	public boolean isTrying()
	{
		return isTrying;
	}

	public void setTrying(boolean isTrying)
	{
		this.isTrying = isTrying;
	}

	@Override
	public void onCategorySelected(String categoryGroup, String categoryName)
	{
		pendingQuery = new SearchQuery();
		pendingQuery.setCategoryGroupName(categoryGroup);
		pendingQuery.setCategoryGroupCode(catResults.getCategoryGroupCode(categoryGroup));

		pendingQuery.setCategoryName(categoryName);
		if (categoryName != ALL_CATEGORIES)
		{
			pendingQuery.setCategoryCode(catResults.getCategoryCode(categoryGroup, categoryName));
		}

		DialogFragment dialog = new SearchTextDialog();
		Bundle args = new Bundle();
		args.putParcelable(CATEGORY_RESULTS, pendingQuery);

		dialog.setArguments(args);
		dialog.show(getFragmentManager(), "search_text");
	}

	@Override
	public void onSearchEntered(SearchQuery query)
	{
		searchesAdapter.add(query);
	}

	@Override
	public void onItemClick(AdapterView<?> searchesAdapter, View view, int position, long id)
	{
		if (lastSelected != null)
			lastSelected.setBackgroundDrawable(defaultBackground);

		view.setBackgroundDrawable(yellowBackground);
		lastSelected = view;
		lastSelectedPosition = position;
	}

	@Override
	public void onSourcesEntered(List<ClassifiedSources> sources)
	{
		setSourceText(sources);
	}

	public void setSourceText(List<ClassifiedSources> sources)
	{
		if (sources.size() == 1)
			sourcesButton.setText("One");
		else if (sources.size() == ClassifiedSources.values().length)
			sourcesButton.setText("All");
		else
			sourcesButton.setText("Some");
	}

	@Override
	public void onLocationEntered(String location)
	{
		Geocoder geo = new Geocoder(this);
		Address addy = null;
		if (isOnline(this))
		{
			try
			{
				List<Address> adds = geo.getFromLocationName(location, 1);
				if (adds != null && adds.size() > 0)
					addy = adds.get(0);
			}
			catch (IOException e)
			{
				makeNoConnectionToast();
				e.printStackTrace();
			}
		}
		else
			makeNoConnectionToast();
		SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		if (addy != null)
		{
			String latitude = String.valueOf(addy.getLatitude());
			String longitude = String.valueOf(addy.getLongitude());
			String desc = null;
			boolean valid = latitude != null && longitude != null;
			if (valid && addy.getSubLocality() != null)
				locationButton.setText(desc = addy.getSubLocality());
			else if (valid && addy.getLocality() != null)
				locationButton.setText(desc = addy.getLocality());
			else if (valid && addy.getAdminArea() != null)
				locationButton.setText(desc = addy.getAdminArea());
			else if (valid && addy.getPostalCode() != null)
				locationButton.setText(desc = addy.getPostalCode());
			else
			{
				makeLocationToast();
				valid = false;
			}
			if (valid)
			{
				ed.putString(LATITUDE, latitude);
				ed.putString(LONGITUDE, longitude);
				ed.putString(LOC_DESC, desc);
				ed.apply();
			}
		}
		else
		{
			makeLocationToast();
		}

	}

	private void makeLocationToast()
	{
		Toast.makeText(this, "The location could not be determined", Toast.LENGTH_SHORT).show();
	}

	private void makeMustSetLocationToast()
	{
		Toast.makeText(this, "You must set a location", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3)
	{
		SharedPreferences prefs = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor ed = prefs.edit();
		ed.putInt(DISTANCE, pos);	
		ed.apply();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
