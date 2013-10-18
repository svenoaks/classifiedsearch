package com.smp.findlocal;

import static com.smp.findlocal.Constants.MAX_TIME;
import static com.smp.findlocal.Constants.SEARCHES_FILENAME;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UtilityMethods
{
	public static HttpURLConnection openConnectionWithTimeout(URL url) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(MAX_TIME);
		connection.setReadTimeout(MAX_TIME);
		return connection;
	}

	public static boolean isOnline(Context context)
	{
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfoMob = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo netInfoWifi = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return ((netInfoMob != null && netInfoMob.isConnectedOrConnecting()) || (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()));
	}

	@SuppressWarnings("unchecked")
	public static Object readObjectFromFile(Context context, String fileName)
	{
		Object result = null;
		FileInputStream fis = null;
		try
		{
			fis = context.openFileInput(fileName);
			ObjectInputStream objectIn = new ObjectInputStream(fis);
			result = (List<SearchQuery>) objectIn.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fis != null)
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		return result;
	}

	public static void writeObjectToFile(Context context, String fileName, Object obj)
	{
		FileOutputStream fos = null;
		try
		{
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream objectOut = new ObjectOutputStream(fos);
			objectOut.writeObject(obj);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
