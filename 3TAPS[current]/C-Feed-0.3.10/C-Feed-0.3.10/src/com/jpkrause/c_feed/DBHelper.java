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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String TAG = DBHelper.class.getSimpleName();
	
	public static final String DB_NAME = "cfeed.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "feeds";
	public static final String C_LINK = "c_link";
	public static final String C_SEEN = "c_seen";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = String.format("create table %s (%s TEXT primary key, %s INT)", TABLE, C_LINK, C_SEEN);
	
		Log.d(TAG, "onCreate sql: "+sql);
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//currently drops db, should alter it or merge the original data
		db.execSQL("drop table if exists " + TABLE);
		this.onCreate(db);
	}

}
