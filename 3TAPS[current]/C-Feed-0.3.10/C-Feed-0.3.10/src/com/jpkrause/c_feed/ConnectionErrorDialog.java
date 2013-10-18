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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConnectionErrorDialog {
	public static AlertDialog create(final Context context) {
		final Activity activity = (Activity) context;
		  return new AlertDialog.Builder(context)
		   .setTitle(R.string.error_title)
		   .setMessage(R.string.error_message)
		   .setCancelable(false)
		   .setIcon(R.drawable.ic_launcher_drawn)
		   .setPositiveButton(R.string.error_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				activity.finish();
				
			}
		})
		   .create();
		 }

}
