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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

public class CityListActivity extends ListActivity {

	private List<String> cities, searchCities;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_region_list);
		
		Intent i = getIntent();
		String state = i.getExtras().getString(Constants.SELECTED_STATE);
		String openFile = checkState(state);

		// declare reader class and input stream
		ReadFile fileReader;
		InputStream input = null;

		// try to read categories file
		fileReader = new ReadFile();
		cities = new ArrayList<String>();
		searchCities = new ArrayList<String>();
		try {
			input = getAssets().open(openFile+".txt");
			cities = fileReader.OpenFile(input);
			input = getAssets().open(openFile+"links.txt");
			searchCities = fileReader.OpenFile(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cities);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent r = new Intent();
		r.putExtra(Constants.SELECTED_CITY, cities.get(position));
		r.putExtra(Constants.SELECTED_CITY_CODE, searchCities.get(position));
		setResult(Constants.RESULT_OK,r);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void openAboutDialog() {
		// create about dialog
		final AlertDialog aboutC = aboutDialog.create(this);
		aboutC.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		if (item.getTitle().toString().equalsIgnoreCase("about")) {
			openAboutDialog();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String checkState(String selected){
		if(selected.equalsIgnoreCase("District of Columbia")){
			return "DC";
		}
		else if(selected.equalsIgnoreCase("Alabama")){
			return "AL";
		}
		else if(selected.equalsIgnoreCase("Alaska")){
			return "AK";
		}
		else if(selected.equalsIgnoreCase("Arizona")){
			return "AZ";
		}
		else if(selected.equalsIgnoreCase("Arkansas")){
			return "AR";
		}
		else if(selected.equalsIgnoreCase("California")){
			return "CA";
		}
		else if(selected.equalsIgnoreCase("Colorado")){
			return "CO";
		}
		else if(selected.equalsIgnoreCase("Connecticut")){
			return "CT";
		}
		else if(selected.equalsIgnoreCase("Delaware")){
			return "DE";
		}
		else if(selected.equalsIgnoreCase("Florida")){
			return "FL";
		}
		else if(selected.equalsIgnoreCase("Georgia")){
			return "GA";
		}
		else if(selected.equalsIgnoreCase("Hawaii")){
			return "HI";
		}
		else if(selected.equalsIgnoreCase("Idaho")){
			return "ID";
		}
		else if(selected.equalsIgnoreCase("Illinois")){
			return "IL";
		}
		else if(selected.equalsIgnoreCase("Indiana")){
			return "IN";
		}
		else if(selected.equalsIgnoreCase("Iowa")){
			return "IA";
		}
		else if(selected.equalsIgnoreCase("Kansas")){
			return "KS";
		}
		else if(selected.equalsIgnoreCase("Kentucky")){
			return "KY";
		}
		else if(selected.equalsIgnoreCase("Louisiana")){
			return "LA";
		}
		else if(selected.equalsIgnoreCase("Maine")){
			return "ME";
		}
		else if(selected.equalsIgnoreCase("Maryland")){
			return "MD";
		}
		else if(selected.equalsIgnoreCase("Massachusetts")){
			return "MA";
		}
		else if(selected.equalsIgnoreCase("Michigan")){
			return "MI";
		}
		else if(selected.equalsIgnoreCase("Minnesota")){
			return "MN";
		}
		else if(selected.equalsIgnoreCase("Mississippi")){
			return "MS";
		}
		else if(selected.equalsIgnoreCase("Missouri")){
			return "MO";
		}
		else if(selected.equalsIgnoreCase("Montana")){
			return "MT";
		}
		else if(selected.equalsIgnoreCase("Nebraska")){
			return "NE";
		}
		else if(selected.equalsIgnoreCase("Nevada")){
			return "NV";
		}
		else if(selected.equalsIgnoreCase("New Hampshire")){
			return "NH";
		}
		else if(selected.equalsIgnoreCase("New Jersey")){
			return "NJ";
		}
		else if(selected.equalsIgnoreCase("New Mexico")){
			return "NM";
		}
		else if(selected.equalsIgnoreCase("New York")){
			return "NY";
		}
		else if(selected.equalsIgnoreCase("North Carolina")){
			return "NC";
		}
		else if(selected.equalsIgnoreCase("North Dakota")){
			return "ND";
		}
		else if(selected.equalsIgnoreCase("Ohio")){
			return "OH";
		}
		else if(selected.equalsIgnoreCase("Oklahoma")){
			return "OK";
		}
		else if(selected.equalsIgnoreCase("Oregon")){
			return "OR";
		}
		else if(selected.equalsIgnoreCase("Pennsylvania")){
			return "PA";
		}
		else if(selected.equalsIgnoreCase("Rhode Island")){
			return "RI";
		}
		else if(selected.equalsIgnoreCase("South Carolina")){
			return "SC";
		}
		else if(selected.equalsIgnoreCase("South Dakota")){
			return "SD";
		}
		else if(selected.equalsIgnoreCase("Tennessee")){
			return "TN";
		}
		else if(selected.equalsIgnoreCase("Texas")){
			return "TX";
		}
		else if(selected.equalsIgnoreCase("Utah")){
			return "UT";
		}
		else if(selected.equalsIgnoreCase("Vermont")){
			return "VT";
		}
		else if(selected.equalsIgnoreCase("Virginia")){
			return "VA";
		}
		else if(selected.equalsIgnoreCase("Washington")){
			return "WA";
		}
		else if(selected.equalsIgnoreCase("West Virginia")){
			return "WV";
		}
		else if(selected.equalsIgnoreCase("Wisconsin")){
			return "WI";
		}
		else if(selected.equalsIgnoreCase("Wyoming")){
			return "WY";
		}
		else if(selected.equalsIgnoreCase("US Territories")){
			return "USTR";
		}
		else{
			return "NULL";
		}
	}

}
