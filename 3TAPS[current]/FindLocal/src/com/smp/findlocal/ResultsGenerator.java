package com.smp.findlocal;

import java.io.BufferedReader;
import static com.smp.findlocal.UtilityMethods.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.smp.findlocal.Constants.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class ResultsGenerator
{
	public final static int SEARCH = 0;
	public final static int CATEGORIES = 1;

	private int operation;

	private String searchURL;

	private ArrayList<ResultDetails> results = new ArrayList<ResultDetails>();

	private volatile static CategoryResults categories = null;
	private boolean updateSucceeded = false;

	public ResultsGenerator(String searchURL, int operation)
	{
		this.searchURL = searchURL;
		this.operation = operation;
	}

	public ResultsGenerator(int operation)
	{
		this.operation = operation;
	}

	public void generate()
	{
		try
		{
			JSONObject json = null;

			if (operation == SEARCH)
			{
				json = getJSONObject(getResponseText(SEARCH_URL + AUTH_TOKEN, searchURL));
				results = updateResults(json);
			}
			else if (operation == CATEGORIES)
			{
				json = getJSONObject(getResponseText(CATEGORIES_URL + AUTH_TOKEN, ""));
				categories = generateCategories(json);
			}

			updateSucceeded = true;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			updateSucceeded = false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			updateSucceeded = false;
		}
		// We want to get the thumbnails separately for reliability purposes and
		// to not display toast if only thumbnails fail.

	}

	public static CategoryResults getCategories()
	{
		return categories;
	}

	private CategoryResults generateCategories(JSONObject json) throws JSONException
	{
		CategoryResults results = new CategoryResults();
		JSONArray groups = json.getJSONArray(CATEGORIES_STRING);
		for (int i = 0; i < groups.length(); ++i)
		{
			String groupName = groups.getJSONObject(i).getString(GROUP_NAME);
			String groupCode = groups.getJSONObject(i).getString(GROUP_CODE);
			if (!results.hasGroup(groupName))
			{
				results.addCategoryGroup(groupName, groupCode);
			}
			String categoryName = groups.getJSONObject(i).getString(CATEGORY_NAME);
			String categoryCode = groups.getJSONObject(i).getString(CATEGORY_CODE);
			results.addToGroup(groupName, categoryName, categoryCode);
		}

		return results;
	}

	public boolean isUpdateSucceeded()
	{
		return updateSucceeded;
	}

	public ArrayList<ResultDetails> getResults()
	{
		return results;
	}

	private ArrayList<ResultDetails> updateResults(JSONObject json) throws JSONException
	{
		ArrayList<ResultDetails> results = new ArrayList<ResultDetails>();
		// throw exception if succeed != true
		JSONArray postings = json.getJSONArray(POSTINGS);
		for (int i = 0; i < postings.length(); ++i)
		{

			JSONObject posting = postings.getJSONObject(i);
			ResultDetails result = new ResultDetails();
			if (posting.has(HEADING))
				result.setHeading(posting.getString(HEADING));
			if (posting.has(BODY))
				result.setBody(posting.getString(BODY));
			result.setTimestamp(posting.getString(TIMESTAMP));
			if (posting.has(PRICE))
				result.setPrice(posting.getString(PRICE));
			if (posting.has(EXTERNAL_URL))
				result.setExternalURL(posting.getString(EXTERNAL_URL));
			JSONArray images = posting.getJSONArray(IMAGES);
			for (int j = 0; j < images.length(); ++j)
			{
				JSONObject image = images.getJSONObject(j);
				/*
				 * if (image.has(THUMBNAIL)) {
				 * result.setThumbnailURL(image.getString(THUMBNAIL)); break; }
				 */
				if (image.has(FULL_IMAGE))
				{
					result.setThumbnailURL(image.getString(FULL_IMAGE));
					break;
				}
			}
			results.add(result);
		}
		return results;
	}

	private String getResponseText(String base, String urlStr) throws IOException
	{
		StringBuilder response = new StringBuilder();
		HttpURLConnection client = null;
		BufferedReader input = null;

		URL url = new URL(base + urlStr);
		try
		{
			client = openConnectionWithTimeout(url);

			if (client.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String line = null;
				while ((line = input.readLine()) != null)
				{
					response.append(line);
				}
			}
		}
		finally
		{
			if (client != null)
				client.disconnect();
			if (input != null)
				input.close();
		}
		//Log.d("Response", response.toString());
		return response.toString();
	}

	public String getSearchURL()
	{
		return searchURL;
	}

	public void setSearchURL(String searchURL)
	{
		this.searchURL = searchURL;
	}

	private JSONObject getJSONObject(String text) throws JSONException
	{
		return new JSONObject(text);
	}

	public void updateThumbnails(ArrayList<ResultDetails> results)
	{
		if (updateSucceeded && operation == SEARCH)
		{
			for (ResultDetails result : results)
			{
				if (result.hasThumbnailURL())
				{
					result.setThumbnail(getBitmapFromURL(result.getThumbnailURL()));
				}
			}
		}
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
			// opt.inPurgeable=true;
			opt.inSampleSize = 6;
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
