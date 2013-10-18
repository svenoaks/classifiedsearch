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

import android.app.Application;
import android.content.SharedPreferences;

public class CFeedApplication extends Application {
	
	@Override
	  public void onCreate()
	  {
	    super.onCreate();
	     
	    // Initialize the singletons so their instances
	    // are bound to the application process.
	    initSingletons();
	  }
	 
	  protected void initSingletons()
	  {
	    // Initialize the instance of MySingleton
		SharedPreferences prefs = getSharedPreferences(Constants.PREFS, 0);
	    SearchCriteria.initInstance(prefs);
	  }
}
