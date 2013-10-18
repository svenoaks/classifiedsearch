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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class aboutDialog {
	
	public static AlertDialog create(final Context context) {
		  final TextView message = new TextView(context);
		  final SpannableString s = 
		               new SpannableString(context.getText(R.string.help_message));
		  Linkify.addLinks(s, Linkify.ALL);
		  message.setText(s);
		  message.setMovementMethod(LinkMovementMethod.getInstance());

		  return new AlertDialog.Builder(context)
		   .setTitle(R.string.help_title)
		   .setCancelable(true)
		   .setIcon(R.drawable.ic_launcher_drawn)
		   .setPositiveButton(R.string.help_ok, null)
		   .setNeutralButton(R.string.help_donate, new DialogInterface.OnClickListener() {
			    
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			    	Uri uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=jkrause%40lavabit%2ecom&lc=US&item_name=JKrause%20Software&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);
			     
			    }
			   })
		   .setView(message)
		   .create();
		 }

}
