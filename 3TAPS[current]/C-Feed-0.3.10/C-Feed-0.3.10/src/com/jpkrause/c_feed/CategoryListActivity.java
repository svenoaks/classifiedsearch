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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CategoryListActivity extends ListActivity {
	
	List<String> categories, searchCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
		
		Intent i = getIntent();
		String section = i.getExtras().getString(Constants.SELECTED_SECTION);
		String openFile = checkSection(section);
		
		
		//declare reader class and input stream
		ReadFile fileReader;
		InputStream input = null;
		
		//try to read categories file
		fileReader = new ReadFile();
		categories = new ArrayList<String>();
		searchCat = new ArrayList<String>();
		try {
			input = getAssets().open(openFile+".txt");
			categories = fileReader.OpenFile(input);
			input = getAssets().open(openFile+"links.txt");
			searchCat = fileReader.OpenFile(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//populate the list
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,categories);
		setListAdapter(adapter);
	}
	
	//return selected category when an item is selected
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent r = new Intent();
		r.putExtra(Constants.SELECTED_CATEGORY, categories.get(position));
		r.putExtra(Constants.SELECTED_CATEGORY_CODE, searchCat.get(position));
		setResult(Constants.RESULT_OK,r);
		finish();
	}
	
	private void openAboutDialog() {
		// create about dialog
		final AlertDialog aboutC = aboutDialog.create(this);
		aboutC.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getTitle().toString().equalsIgnoreCase("about")) {
			openAboutDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private String checkSection(String selected){
		if(selected.equalsIgnoreCase("Community")){
			return "community";
		}
		if(selected.equalsIgnoreCase("Discussion Forums")){
			return "discussion";
		}
		if(selected.equalsIgnoreCase("Housing")){
			return "housing";
		}
		if(selected.equalsIgnoreCase("For Sale")){
			return "forsale";
		}
		if(selected.equalsIgnoreCase("Services")){
			return "services";
		}
		if(selected.equalsIgnoreCase("Jobs")){
			return "jobs";
		}
		if(selected.equalsIgnoreCase("Gigs")){
			return "gigs";
		}
		if(selected.equalsIgnoreCase("Resumes")){
			return "resumes";
		}
		else{
			return "NULL";
		}
	}

}
