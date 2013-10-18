package com.smp.findlocal;

import static com.smp.findlocal.Constants.*;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import android.graphics.Bitmap;

public class ResultDetails implements Comparable<ResultDetails>
{
	private String heading;
	private String body;
	private String timestamp;
	private String externalURL;
	private volatile Bitmap thumbnail;
	private volatile String thumbnailURL;
	private String price;
	private volatile boolean thumbNeedsToBeSet = true;

	public boolean isThumbNeedsToBeSet()
	{
		return thumbNeedsToBeSet;
	}

	public void setThumbNeedsToBeSet(boolean thumbNeedsToBeSet)
	{
		this.thumbNeedsToBeSet = thumbNeedsToBeSet;
	}

	public boolean hasHeading()
	{
		return heading != null;
	}

	public boolean hasBody()
	{
		return body != null;
	}

	public boolean hasTimestamp()
	{
		return timestamp != null;
	}

	public boolean hasThumbnailURL()
	{
		return thumbnailURL != null;
	}

	public boolean hasThumbnail()
	{
		return thumbnail != null;
	}

	public Bitmap getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(Bitmap thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	private boolean newPost = true;

	@Override
	public boolean equals(Object obj)
	{
		ResultDetails other = (ResultDetails) obj;
		return this.externalURL.equals(other.externalURL);
	}

	public boolean isNewPost()
	{
		return newPost;
	}

	public void setNewPost(boolean newPost)
	{
		this.newPost = newPost;
	}

	public String getHeading()
	{
		return heading;
	}

	public void setHeading(String heading)
	{
		this.heading = heading;
	}

	public String getTimestamp(boolean readable)
	{
		if (readable)
		{
			Date date = new Date(Long.parseLong(timestamp) * SECONDS_TO_MILLISECONDS);

			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());
			df.setTimeZone(TimeZone.getDefault());
			return df.format(date);
		}
		else
			return timestamp;
	}

	public void setTimestamp(String time)
	{
		this.timestamp = time;
	}

	public String getBody(boolean truncate)
	{
		if (truncate && body.length() > MAX_CHARS_BODY)
		{
			return body;
			// return body.substring(0, MAX_CHARS_BODY - 1) + " " + CONT_STRING;
		}
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getExternalURL()
	{
		return externalURL;
	}

	public void setExternalURL(String externalURL)
	{
		this.externalURL = externalURL;
	}

	public String getThumbnailURL()
	{
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL)
	{
		this.thumbnailURL = thumbnailURL;
	}

	public String getPrice()
	{
		return "$" + price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	public boolean hasPrice()
	{
		return price != null;
	}

	@Override
	public int compareTo(ResultDetails other)
	{
		if (Integer.parseInt(this.timestamp) > Integer.parseInt(other.timestamp))
			return -1;
		if (Integer.parseInt(this.timestamp) < Integer.parseInt(other.timestamp))
			return 1;
		return 0;
	}
}