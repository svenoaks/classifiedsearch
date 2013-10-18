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

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.content.Intent;

public class OptionsActivity extends Activity implements OnItemSelectedListener {

	private SearchCriteria criteria;
	private Spinner bdrmSpinner;
	private RadioButton entirePost, title, pay, noPay, all;
	private CheckBox hasImage, cats, dogs, telecommute, contract, internship,
			partTime, nonProfit;
	private EditText minAsk, maxAsk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		criteria = SearchCriteria.getInstance();

		entirePost = (RadioButton) findViewById(R.id.entire_post);
		title = (RadioButton) findViewById(R.id.title_only);
		pay = (RadioButton) findViewById(R.id.pay_opt);
		noPay = (RadioButton) findViewById(R.id.no_pay);
		all = (RadioButton) findViewById(R.id.all_pay);

		hasImage = (CheckBox) findViewById(R.id.has_image);
		cats = (CheckBox) findViewById(R.id.cats_opt);
		dogs = (CheckBox) findViewById(R.id.dogs_opt);
		telecommute = (CheckBox) findViewById(R.id.telecommute);
		contract = (CheckBox) findViewById(R.id.contract);
		internship = (CheckBox) findViewById(R.id.internship);
		partTime = (CheckBox) findViewById(R.id.part_time);
		nonProfit = (CheckBox) findViewById(R.id.non_profit);

		minAsk = (EditText) findViewById(R.id.min_ask);
		maxAsk = (EditText) findViewById(R.id.max_ask);

		// populate bedroom spinner choices
		bdrmSpinner = (Spinner) findViewById(R.id.bedrooms_opt);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bedrooms_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bdrmSpinner.setAdapter(adapter);
		bdrmSpinner.setOnItemSelectedListener(this);

		if (criteria.getSrchType().equalsIgnoreCase("A")) {
			entirePost.toggle();
		} else {
			title.toggle();
		}

		if (criteria.getHasPic().equalsIgnoreCase("1")) {
			hasImage.setChecked(true);
		} else {
			hasImage.setChecked(false);
		}

		if (criteria.getSectionName().equalsIgnoreCase("community")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			minAsk.setVisibility(EditText.GONE);
			maxAsk.setVisibility(EditText.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			telecommute.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
		}

		else if (criteria.getSectionName().equalsIgnoreCase("housing")) {
			telecommute.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
			if(criteria.getBedrooms().equalsIgnoreCase(""))
			bdrmSpinner.setSelection(0);
			else if (criteria.getBedrooms().equalsIgnoreCase("1"))
				bdrmSpinner.setSelection(1);
			else if (criteria.getBedrooms().equalsIgnoreCase("2"))
				bdrmSpinner.setSelection(2);
			else if (criteria.getBedrooms().equalsIgnoreCase("3"))
				bdrmSpinner.setSelection(3);
			else if (criteria.getBedrooms().equalsIgnoreCase("4"))
				bdrmSpinner.setSelection(4);
			else if (criteria.getBedrooms().equalsIgnoreCase("5"))
				bdrmSpinner.setSelection(5);
			else if (criteria.getBedrooms().equalsIgnoreCase("6"))
				bdrmSpinner.setSelection(6);
			else if (criteria.getBedrooms().equalsIgnoreCase("7"))
				bdrmSpinner.setSelection(7);
			else if (criteria.getBedrooms().equalsIgnoreCase("8"))
				bdrmSpinner.setSelection(8);
			minAsk.setText(criteria.getMinAsk());
			maxAsk.setText(criteria.getMaxAsk());
			if(criteria.getAddTwo().equalsIgnoreCase("purrr"))
				cats.setChecked(true);
			else
				cats.setChecked(false);
			if(criteria.getAddThree().equalsIgnoreCase("wooof"))
				dogs.setChecked(true);
			else
				dogs.setChecked(false);
		}

		else if (criteria.getSectionName().equalsIgnoreCase("for sale")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			telecommute.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
			minAsk.setText(criteria.getMinAsk());
			maxAsk.setText(criteria.getMaxAsk());
		}

		else if (criteria.getSectionName().equalsIgnoreCase("services")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			minAsk.setVisibility(EditText.GONE);
			maxAsk.setVisibility(EditText.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			telecommute.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
		}

		else if (criteria.getSectionName().equalsIgnoreCase("jobs")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			minAsk.setVisibility(EditText.GONE);
			maxAsk.setVisibility(EditText.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			if(criteria.getAddOne().equalsIgnoreCase("telecommuting"))
				telecommute.setChecked(true);
			else
				telecommute.setChecked(false);
			if(criteria.getAddTwo().equalsIgnoreCase("contract"))
				contract.setChecked(true);
			else
				contract.setChecked(false);
			if(criteria.getAddThree().equalsIgnoreCase("internship"))
				internship.setChecked(true);
			else
				internship.setChecked(false);
			if(criteria.getAddFour().equalsIgnoreCase("part-time"))
				partTime.setChecked(true);
			else
				partTime.setChecked(false);
			if(criteria.getAddFive().equalsIgnoreCase("non-profit"))
				nonProfit.setChecked(true);
			else
				nonProfit.setChecked(false);
		}

		else if (criteria.getSectionName().equalsIgnoreCase("gigs")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			minAsk.setVisibility(EditText.GONE);
			maxAsk.setVisibility(EditText.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			telecommute.setVisibility(CheckBox.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
			if(criteria.getAddThree().equalsIgnoreCase(""))
				all.setChecked(true);
			else if(criteria.getAddThree().equalsIgnoreCase("nopay"))
				noPay.setChecked(true);
			else if(criteria.getAddThree().equalsIgnoreCase("forpay"))
				pay.setChecked(true);
		}

		else if (criteria.getSectionName().equalsIgnoreCase("resumes")) {
			bdrmSpinner.setVisibility(Spinner.GONE);
			minAsk.setVisibility(EditText.GONE);
			maxAsk.setVisibility(EditText.GONE);
			cats.setVisibility(CheckBox.GONE);
			dogs.setVisibility(CheckBox.GONE);
			telecommute.setVisibility(CheckBox.GONE);
			pay.setVisibility(RadioButton.GONE);
			noPay.setVisibility(RadioButton.GONE);
			all.setVisibility(RadioButton.GONE);
			contract.setVisibility(CheckBox.GONE);
			internship.setVisibility(CheckBox.GONE);
			partTime.setVisibility(CheckBox.GONE);
			nonProfit.setVisibility(CheckBox.GONE);
		}

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
		if (item.getTitle().toString().equalsIgnoreCase("about")) {
			openAboutDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (criteria.getAddFive().equalsIgnoreCase("") 
					&& criteria.getAddFour().equalsIgnoreCase("")
					&& criteria.getAddOne().equalsIgnoreCase("")
					&& criteria.getAddThree().equalsIgnoreCase("")
					&& criteria.getAddTwo().equalsIgnoreCase("")
					&& criteria.getBedrooms().equalsIgnoreCase("")
					&& criteria.getHasPic().equalsIgnoreCase("")
					&& criteria.getMaxAsk().equalsIgnoreCase("")
					&& criteria.getMinAsk().equalsIgnoreCase("")) {
				criteria.setHasOptions(false);
			} else {
				criteria.setHasOptions(true);
			}
			criteria.setMaxAsk(maxAsk.getText().toString());
			criteria.setMinAsk(minAsk.getText().toString());
			Intent r = new Intent();
			setResult(Constants.RESULT_OK, r);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String selected = (String) parent.getItemAtPosition(pos);
		if (selected.equalsIgnoreCase("0+ Bedrooms")) {
			criteria.setBedrooms("");
		} else if (selected.equalsIgnoreCase("1 Bedroom")) {
			criteria.setBedrooms("1");
		} else if (selected.equalsIgnoreCase("2+ Bedrooms")) {
			criteria.setBedrooms("2");
		} else if (selected.equalsIgnoreCase("3+ Bedrooms")) {
			criteria.setBedrooms("3");
		} else if (selected.equalsIgnoreCase("4+ Bedrooms")) {
			criteria.setBedrooms("4");
		} else if (selected.equalsIgnoreCase("5+ Bedrooms")) {
			criteria.setBedrooms("5");
		} else if (selected.equalsIgnoreCase("6+ Bedrooms")) {
			criteria.setBedrooms("6");
		} else if (selected.equalsIgnoreCase("7+ Bedrooms")) {
			criteria.setBedrooms("7");
		} else if (selected.equalsIgnoreCase("8+ Bedrooms")) {
			criteria.setBedrooms("8");
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.all_pay:
			if (checked)
				criteria.setAddThree("");
			break;
		case R.id.entire_post:
			if (checked)
				criteria.setSrchType("A");
			break;
		case R.id.no_pay:
			if (checked)
				criteria.setAddThree("nopay");
			break;
		case R.id.pay_opt:
			if (checked)
				criteria.setAddThree("forpay");
			break;
		case R.id.title_only:
			if (checked)
				criteria.setSrchType("T");
			break;
		}
	}

	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();

		// Check which checkbox was clicked
		switch (view.getId()) {
		case R.id.cats_opt:
			if (checked)
				criteria.setAddTwo("purrr");
			else
				criteria.setAddTwo("");
			break;
		case R.id.contract:
			if (checked)
				criteria.setAddTwo("contract");
			else
				criteria.setAddTwo("");
			break;
		case R.id.dogs_opt:
			if (checked)
				criteria.setAddThree("wooof");
			else
				criteria.setAddThree("");
			break;
		case R.id.has_image:
			if (checked)
				criteria.setHasPic("1");
			else
				criteria.setHasPic("");
			break;
		case R.id.internship:
			if (checked)
				criteria.setAddThree("internship");
			else
				criteria.setAddThree("");
			break;
		case R.id.non_profit:
			if (checked)
				criteria.setAddFive("non-profit");
			else
				criteria.setAddFive("");
			break;
		case R.id.part_time:
			if (checked)
				criteria.setAddFour("part-time");
			else
				criteria.setAddFour("");
			break;
		case R.id.telecommute:
			if (checked)
				criteria.setAddOne("telecommuting");
			else
				criteria.setAddOne("");
			break;
		}
	}

}
