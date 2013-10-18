/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smp.findlocal;

import java.util.Random;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest.ErrorCode;

import android.app.PendingIntent;
import static com.smp.findlocal.Constants.*;
import static com.smp.findlocal.UtilityMethods.*;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider
{
	
	private boolean justBuilt = false;
	private static Random random = new Random();
	public static int randomNumber = 6453;
	private static int counter = 1;

	// private static RemoteViews rv;

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context)
	{
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context)
	{
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = mgr.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
		RemoteViews rv = buildRemoteViews(context, mgr, appWidgetIds);
		mgr.updateAppWidget(appWidgetIds, rv);
		justBuilt = true;
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);

		if (intent.getAction().equals(FOLLOW_LINK_ACTION))
		{
			String link = intent.getStringExtra(LINK_ITEM);
			//show an ad every PAGES_PER_AD clicks
			if (counter % PAGES_PER_AD != 0)
			{
				Intent linkIntent = new Intent(Intent.ACTION_VIEW);
				linkIntent.setData(Uri.parse(link));
				linkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(linkIntent);
			}
			else
			{
				Intent ad = new Intent(context, com.smp.findlocal.AdActivity.class);
				ad.setData(Uri.parse(link));
				ad.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(ad);
			}
			++counter;
		}
		if (intent.getAction().equals(REFRESH_ACTION))
		{
			int[] appWidgetIds = mgr.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
			RemoteViews rv = buildRemoteViews(context, mgr, appWidgetIds);
			mgr.updateAppWidget(appWidgetIds, rv);
			justBuilt = true;
			// mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
		}
		if (intent.getAction().equals(NO_CONNECTION_ACTION))
		{
			Toast.makeText(context, NO_CONNECTION_TOAST, Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		
		if (!justBuilt && isOnline(context))
		{
			RemoteViews rv = buildRemoteViews(context, appWidgetManager, appWidgetIds);
			appWidgetManager.updateAppWidget(appWidgetIds, rv);
			super.onUpdate(context, appWidgetManager, appWidgetIds);
		}
		justBuilt = false;
	}

	public static RemoteViews buildRemoteViews(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		randomNumber = random.nextInt();

		Intent intent = new Intent(context, WidgetService.class);
		// intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
		// appWidgetIds[i]);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		intent.setData(Uri.fromParts("content", String.valueOf(appWidgetIds[0] + randomNumber), null));
		rv.setRemoteAdapter(R.id.list_view, intent);
		rv.setEmptyView(R.id.list_view, R.id.empty_view);

		// Refresh button intent
		Intent refreshIntent = new Intent(context, WidgetProvider.class);
		refreshIntent.setAction(REFRESH_ACTION);
		refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent);

		// Search button intent
		Intent searchIntent = new Intent(context, SearchSettingsActivity.class);
		searchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
		PendingIntent searchPendingIntent = PendingIntent.getActivity(context, 0, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setOnClickPendingIntent(R.id.settings_button, searchPendingIntent);

		Intent linkIntent = new Intent(context, WidgetProvider.class);
		linkIntent.setAction(FOLLOW_LINK_ACTION);

		intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, linkIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		rv.setPendingIntentTemplate(R.id.list_view, toastPendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, rv);
		return rv;
	}

	
}