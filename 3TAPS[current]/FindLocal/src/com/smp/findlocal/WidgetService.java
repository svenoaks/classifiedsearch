package com.smp.findlocal;

import static com.smp.findlocal.UtilityMethods.*;
import static com.smp.findlocal.UtilityMethods.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static com.smp.findlocal.Constants.*;

import java.util.List;

import junit.framework.Assert;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

public class WidgetService extends RemoteViewsService
{
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent)
	{
		return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
	// private final String url =
	// "http://search.3taps.com/?auth_token=6b8ce40af30362f1150e1219905587f2&radius=100mi&lat=26.244&long=-80.206&category=JWEB&rpp=100&retvals=heading,body,timestamp,external_url,images,price";
	private RemoteViews[] cachedViews = new RemoteViews[MAX_RESULTS];
	private Context mContext;
	private int mAppWidgetId;
	private static Bitmap noImageBitmap;
	private List<ResultsGenerator> generators = new ArrayList<ResultsGenerator>();
	private boolean hasData = false;
	private boolean hasThumbs = false;
	private List<SearchQuery> searches = new ArrayList<SearchQuery>();
	private List<ResultDetails> masterResults = new ArrayList<ResultDetails>(MAX_RESULTS);

	public ListRemoteViewsFactory(Context context, Intent intent)
	{
		mContext = context;
		// mAppWidgetId =
		// intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
		// AppWidgetManager.INVALID_APPWIDGET_ID);

		mAppWidgetId =
				Integer.valueOf(intent.getData().getSchemeSpecificPart()) - WidgetProvider.randomNumber;
	}

	@SuppressWarnings("unchecked")
	public void onCreate()
	{
		Object obj = readObjectFromFile(mContext, SEARCHES_FILENAME);
		if (obj != null)
		{
			searches.addAll((List<SearchQuery>) obj);
		}
		Object obj2 = readObjectFromFile(mContext, SOURCES_FILENAME);
		if (obj2 != null)
		{
			List<ClassifiedSources> sources = (List<ClassifiedSources>) obj2;
			for (SearchQuery query : searches)
			{
				query.setSource(sources);
			}
		}
		SharedPreferences pref = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		String latitude = pref.getString(LATITUDE, LOC_NOT_SET);
		String longitude = pref.getString(LONGITUDE, LOC_NOT_SET);
		String distance = distances[pref.getInt(DISTANCE, DEFAULT_DISTANCE_POS)];
		Assert.assertTrue(!latitude.equals(LOC_NOT_SET) && !longitude.equals(LOC_NOT_SET));
		for (SearchQuery query : searches)
		{
			query.setLatitude(latitude);
			query.setLongitude(longitude);
			query.setDistance(distance);
		}
		Resources res = mContext.getResources();
		noImageBitmap = BitmapFactory.decodeResource(res, R.drawable.craigslist_icon);
	}

	public void onDestroy()
	{
	}

	public int getCount()
	{
		return masterResults.size();
	}

	public RemoteViews getViewAt(int position)
	{
		ResultDetails result = masterResults.get(position);
		if (cachedViews[position] == null)
		{
			RemoteViews rv;
			rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item);
			// ArrayList<ResultDetails> mResults =
			// mSearchResultsGenerator.getResults();
			if (result.hasHeading())
				rv.setTextViewText(R.id.title, result.getHeading());
			if (result.hasBody())
				rv.setTextViewText(R.id.description, result.getBody(true));
			if (result.hasTimestamp())
				rv.setTextViewText(R.id.date, result.getTimestamp(true));
			if (result.hasPrice() && !result.getPrice().equals("$-1.0"))
				rv.setTextViewText(R.id.price, result.getPrice());
			Log.i("price", result.getPrice());
			Bundle extras = new Bundle();
			extras.putString(LINK_ITEM, result.getExternalURL());
			Intent fillInIntent = new Intent();
			fillInIntent.putExtras(extras);
			rv.setOnClickFillInIntent(R.id.list_item, fillInIntent);

			if (result.hasThumbnail())
			{
				rv.setImageViewBitmap(R.id.thumbnail, result.getThumbnail());
				result.setThumbNeedsToBeSet(false);
			}
			else
			{
				rv.setImageViewBitmap(R.id.thumbnail, noImageBitmap);
			}
			cachedViews[position] = rv;
			return rv;
		}
		else if (result.hasThumbnail() && result.isThumbNeedsToBeSet())
		{
			cachedViews[position].setImageViewBitmap(R.id.thumbnail, result.getThumbnail());
			result.setThumbNeedsToBeSet(false);
		}
		return cachedViews[position];
	}

	public RemoteViews getLoadingView()
	{
		// You can create a custom loading view (for instance when getViewAt()
		// is slow.) If you
		// return null here, you will get the default loading view.
		return null;
	}

	public int getViewTypeCount()
	{
		return 1;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	public void onDataSetChanged()
	{
		if (!hasData)
		{
			for (SearchQuery query : searches)
			{
				ResultsGenerator gen = new ResultsGenerator(query.createURLStr(), ResultsGenerator.SEARCH);
				List<ResultDetails> theseDetails = new ArrayList<ResultDetails>();
				while (theseDetails.size() < MAX_RESULTS && query.getTier() <= MAX_TIERS)
				{
					gen.generate();
					if (gen.isUpdateSucceeded())
					{
						theseDetails.addAll(gen.getResults());
						query.setTier(query.getTier() + 1);
						gen.setSearchURL(query.createURLStr());
					}
					else
					{
						break;
					}
				}
				masterResults.addAll(theseDetails);
			}
			Collections.sort(masterResults);
			if (masterResults.size() > MAX_RESULTS)
				masterResults = new
						ArrayList<ResultDetails>(masterResults.subList(0,
								MAX_RESULTS - 1));
			if (masterResults.size() > 0)
			{
				AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);
				hasData = true;
				new ThumbnailAsyncTask(mgr, masterResults, mContext).execute();
			}
		}
	}

	private void makeNoConnectionToast()
	{
		Intent noConnectionIntent = new Intent();
		noConnectionIntent.setAction(NO_CONNECTION_ACTION);
		mContext.sendBroadcast(noConnectionIntent);
	}

	class ThumbnailAsyncTask extends AsyncTask<Void, Void, Void>
	{
		private AppWidgetManager widgetManager;
		private List<ResultDetails> details;
		private Context context;

		public ThumbnailAsyncTask(AppWidgetManager
				appWidgetManager, List<ResultDetails> details, Context context)
		{
			this.details = details;
			this.context = context;
			this.widgetManager = appWidgetManager;
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			for (ResultDetails item : details)
			{
				if (item.hasThumbnailURL())
				{
					item.setThumbnail(getBitmapFromURL(item.getThumbnailURL()));
				}
			}
			widgetManager.notifyAppWidgetViewDataChanged(widgetManager.getAppWidgetIds(
					new ComponentName(context, WidgetProvider.class)), R.id.list_view);
			hasThumbs = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{

		}

		private Bitmap getBitmapFromURL(String str)
		{
			Bitmap bmp = null;
			HttpURLConnection connection = null;

			try
			{
				URL url = new URL(str);
				connection = openConnectionWithTimeout(url);
				BitmapFactory.Options opt = new Options();
				// opt.inPurgeable = true;
				opt.inSampleSize = 8;
				bmp = BitmapFactory.decodeStream(connection.getInputStream(), null, opt);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (connection != null)
					connection.disconnect();
			}
			return bmp;
		}

	}

}