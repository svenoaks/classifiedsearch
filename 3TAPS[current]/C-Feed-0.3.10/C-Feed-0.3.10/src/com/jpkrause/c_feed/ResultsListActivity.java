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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ResultsListActivity extends ListActivity {

	private ArrayList<ResultsDetails> headlines;
	private List<String> links;
	private ResultsDetails Detail;
	private ContentValues values;
	private String url;
	private Context context = this;
	private Cursor cur;
	private ProgressDialog pBarDialog;
	private CustomAdapter custAdap;
	public static final int NOT_SEEN = 0;
	public DBHelper dbHelper;
	public SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results_list);

		if (isOnline()) {
			// set up progress dialog
			pBarDialog = new ProgressDialog(this);
			pBarDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pBarDialog.setMessage("Loading Results...");
			pBarDialog.setCancelable(false);
			pBarDialog.dismiss();

			// initializing instance variables
			Intent i = getIntent();
			url = i.getExtras().getString("url");
			headlines = new ArrayList<ResultsDetails>();
			links = new ArrayList<String>();
			values = new ContentValues();

			new PostTask().execute(url);
		}
		else{
			showConnectionErrorDialog();
		}
	}

	// definition of task class
	private class PostTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// display Progress Bar
			pBarDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String urlGiven = params[0];
			String valueLink;
			try {

				// open database
				dbHelper = new DBHelper(ResultsListActivity.this);
				db = dbHelper.getWritableDatabase();

				URL url2 = new URL(urlGiven);

				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser xpp = factory.newPullParser();

				// we will get the XML from an input stream
				xpp.setInput(getInputStream(url2), "UTF_8");

				/*
				 * We will parse the XML content looking for the "<title>" tag
				 * which appears inside the "<item>" tag. However, we should
				 * take in consideration that the rss feed name also is enclosed
				 * in a "<title>" tag. As we know, every feed begins with these
				 * lines: "<channel><title>Feed_Name</title>...." so we should
				 * skip the "<title>" tag which is a child of "<channel>" tag,
				 * and take in consideration only "<title>" tag which is a child
				 * of "<item>"
				 * 
				 * In order to achieve this, we will make use of a boolean
				 * variable.
				 */

				boolean insideItem = false;

				// Returns the type of current event: Start_TAG, END_TAG, etc.
				int eventType = xpp.getEventType();
				for (int j = 1; eventType != XmlPullParser.END_DOCUMENT; j++) {
					if (eventType == XmlPullParser.START_TAG) {
						if (xpp.getName().equalsIgnoreCase("item")) {
							insideItem = true;
							Detail = new ResultsDetails();
						} else if (xpp.getName().equalsIgnoreCase("title")) {
							if (insideItem) {
								Detail.setTitle(xpp.nextText());// extract
																// headline
							}
						} else if (xpp.getName().equalsIgnoreCase("link")) {
							if (insideItem) {
								valueLink = xpp.nextText();
								links.add(valueLink);// extract link
								values.put(DBHelper.C_LINK, valueLink);
								db.insertWithOnConflict(DBHelper.TABLE, null,
										values, SQLiteDatabase.CONFLICT_IGNORE);// for
																				// tracking
																				// if
																				// the
																				// listing
																				// is
																				// new
								values.clear();
							}
						} else if (xpp.getName()
								.equalsIgnoreCase("description")) {
							if (insideItem) {
								Detail.setDesc(xpp.nextText());// extract
																// description
							}
						} else if (xpp.getName().equalsIgnoreCase("dc:date")) {
							if (insideItem) {
								Detail.setDate(xpp.nextText());// extract
																// timestamp
							}
						}
					} else if (eventType == XmlPullParser.END_TAG
							&& xpp.getName().equalsIgnoreCase("item")) {
						insideItem = false;
						headlines.add(Detail);
					}
					eventType = xpp.next();
					publishProgress((int) (j / 40));
				}

				for (int i = 0; i < links.size(); i++) {
					publishProgress(i);
					// cur =
					// db.rawQuery("SELECT "+DBHelper.C_LINK+" as c_seen FROM "+DBHelper.TABLE+" WHERE "+DBHelper.C_LINK+" = '"+links.get(i)+"'",
					// null);
					cur = db.query(DBHelper.TABLE,
							new String[] { DBHelper.C_SEEN }, DBHelper.C_LINK
									+ "=?", new String[] { links.get(i) },
							null, null, null);
					cur.moveToFirst();
					if (cur.getInt(cur.getColumnIndex(DBHelper.C_SEEN)) == 0) {
						headlines.get(i).setIcon(R.raw.listnew);
					} else {
						headlines.get(i).setIcon(R.raw.listicon);
					}
					cur.close();
				}

				// close db
				db.close();
				dbHelper.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return url;

			// Binding data
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			pBarDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pBarDialog.dismiss();
			if(links.size() == 0){
				showNoResultsDialog();
			}
			else{
			custAdap = new CustomAdapter(headlines, context);
			setListAdapter(custAdap);
			}
		}

	}

	public InputStream getInputStream(URL url) {

		try {
			InputStream stream = url.openConnection().getInputStream();
			return (stream);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// set listing to seen in database
		values.clear();
		values.put(DBHelper.C_SEEN, 1);
		dbHelper = new DBHelper(ResultsListActivity.this);
		db = dbHelper.getWritableDatabase();
		db.update(DBHelper.TABLE, values, DBHelper.C_LINK + "=?",
				new String[] { links.get(position) });
		db.close();
		dbHelper.close();
		headlines.get(position).setIcon(R.drawable.ic_launcher_drawn);

		Intent intent = new Intent(getApplicationContext(),
				ViewListingActivity.class);
		intent.putExtra("urlLink", links.get(position));
		startActivity(intent);
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
	
	private void showConnectionErrorDialog() {
		final AlertDialog error = ConnectionErrorDialog.create(this);
		error.show();
	}
	
	private void showNoResultsDialog() {
		//TODO
		final AlertDialog noResults = NoResultsErrorDialog.create(this);
		noResults.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getTitle().toString().equalsIgnoreCase("about")) {
			openAboutDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	// verify Internet connection method
	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfoMob = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo netInfoWifi = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if ((netInfoMob != null || netInfoWifi != null)
				&& (netInfoMob.isConnectedOrConnecting() || netInfoWifi
						.isConnectedOrConnecting())) {
			return true;
		}
		return false;
	}

}
