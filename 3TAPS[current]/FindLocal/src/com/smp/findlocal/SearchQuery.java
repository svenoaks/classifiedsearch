package com.smp.findlocal;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import static com.smp.findlocal.Constants.*;

public class SearchQuery implements Parcelable, Serializable
{
	private static final long serialVersionUID = 2252382706712554942L;

	private int resultsPerPage = RPP;
	private int tier = 0;
	private String minAge;
	private String maxAge;
	private String minPrice;
	private String maxPrice;

	private boolean hasImage;
	private boolean titleOnly;

	private String searchString;
	private String categoryGroupName;
	private String categoryGroupCode;
	private String categoryName;
	private String categoryCode;

	private List<ClassifiedSources> sources = new ArrayList<ClassifiedSources>();
	private String latitude = "26.244";
	
	private String longitude = "-80.2";
	private String distance = "200mi";

	public SearchQuery()
	{
		sources.addAll(Arrays.asList(ClassifiedSources.values()));
	}

	public void setLocation(String zip)
	{

	}
	public String getLongitude()
	{
		return longitude;
	}

	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}

	public String getLatitude()
	{
		return latitude;
	}

	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}

	public String createURLStr()
	{
		StringBuilder sourceString = new StringBuilder();
		sourceString.append("&source=");
		for (int i = 0; i < sources.size(); ++i)
		{
			sourceString.append(sources.get(i).getCode());
			if (i < sources.size() - 1)
				sourceString.append("|");
		}
		String search = "";
		if (!searchString.equals(ENTIRE_CATEGORY))
		{
			try
			{
				search = (titleOnly ? "&heading=" : "&text=") + URLEncoder.encode(searchString, "utf-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			;
		}
		String price = "";
		if (minPrice.length() > 0 || maxPrice.length() > 0)
		{
			price = "&price=" + minPrice + ".." + maxPrice;
		}
		String age = "";
		if (minAge.length() > 0 || maxAge.length() > 0)
		{
			StringBuilder str = new StringBuilder("&annotations={age:");
			int min, max;
			if (minAge.length() > 0)
				min = Integer.parseInt(minAge);
			else
				min = 0;
			if (maxAge.length() > 0)
				max = Integer.parseInt(maxAge);
			else
				max = 0;

			for (; min <= max; ++min)
			{
				str.append(min);
				if (min != max)
					str.append("%20OR%20age:");
				else
					str.append("}");
			}

			age = str.toString();

		}
		String result =

				"&tier=" + tier + "&" + RETVALS + "&" + "rpp=" + RPP + sourceString + search +
						"&" + (categoryName.equals(ALL_CATEGORIES) ? "category_group=" + categoryGroupCode : "category=" + categoryCode) +
						"&" + "radius=" + distance + "&" + "lat=" + latitude + "&" + "long=" + longitude + (hasImage ? "&has_image=1" : "")
						+ price + age;

		return result;

	}

	public int getTier()
	{
		return tier;
	}

	public void setTier(int tier)
	{
		this.tier = tier;
	}

	/*
	 * @Override public boolean equals(Object other) { SearchQuery sq =
	 * (SearchQuery) other; return this.searchString.equals(sq.searchString); }
	 */
	@Override
	public String toString()
	{
		return searchString;
	}

	private List<Integer> getOrdinals()
	{
		List<Integer> results = new ArrayList<Integer>();
		if (sources != null)
			for (ClassifiedSources source : sources)
			{
				results.add(source.ordinal());
			}
		return results;
	}

	private List<ClassifiedSources> getFromOrdinals(List<Integer> ints)
	{
		List<ClassifiedSources> result = new ArrayList<ClassifiedSources>();
		for (Integer i : ints)
		{
			result.add(ClassifiedSources.values()[i]);
		}
		return result;
	}

	public String getCategoryGroupName()
	{
		return categoryGroupName;
	}

	public void setCategoryGroupName(String categoryGroupName)
	{
		this.categoryGroupName = categoryGroupName;
	}

	public String getCategoryName()
	{
		if (categoryName.equals(ALL_CATEGORIES))
			return categoryGroupName;

		return categoryName;
	}

	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public String getCategoryCode()
	{
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode)
	{
		this.categoryCode = categoryCode;
	}

	public List<ClassifiedSources> getSources()
	{
		return sources;
	}

	public void setSource(List<ClassifiedSources> sources)
	{
		this.sources = sources;
	}

	public String getDistance()
	{
		return distance;
	}

	public void setDistance(String distance)
	{
		this.distance = distance;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(resultsPerPage);
		dest.writeString(minAge);
		dest.writeString(maxAge);
		dest.writeString(minPrice);
		dest.writeString(maxPrice);
		dest.writeByte((byte) (hasImage ? 1 : 0));
		dest.writeByte((byte) (titleOnly ? 1 : 0));
		dest.writeString(searchString);
		dest.writeString(categoryGroupName);
		dest.writeString(categoryGroupCode);
		dest.writeString(categoryName);
		dest.writeString(categoryCode);
		dest.writeList(getOrdinals());
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(distance);
	}

	private SearchQuery(Parcel in)
	{
		resultsPerPage = in.readInt();
		minAge = in.readString();
		maxAge = in.readString();
		minPrice = in.readString();
		maxPrice = in.readString();
		hasImage = in.readByte() == 1;
		titleOnly = in.readByte() == 1;
		searchString = in.readString();
		categoryGroupName = in.readString();
		categoryGroupCode = in.readString();
		categoryName = in.readString();
		categoryCode = in.readString();
		List<Integer> ints = new ArrayList<Integer>();
		in.readList(ints, null);
		sources = getFromOrdinals(ints);
		latitude = in.readString();
		longitude = in.readString();
		distance = in.readString();
	}

	public String getMinAge()
	{
		return minAge;
	}

	public void setMinAge(String minAge)
	{
		this.minAge = minAge;
	}

	public String getMaxAge()
	{
		return maxAge;
	}

	public void setMaxAge(String maxAge)
	{
		this.maxAge = maxAge;
	}

	public String getMinPrice()
	{
		return minPrice;
	}

	public void setMinPrice(String minPrice)
	{
		this.minPrice = minPrice;
	}

	public String getMaxPrice()
	{
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice)
	{
		this.maxPrice = maxPrice;
	}

	public boolean isHasImage()
	{
		return hasImage;
	}

	public void setHasImage(boolean hasImage)
	{
		this.hasImage = hasImage;
	}

	public boolean isTitleOnly()
	{
		return titleOnly;
	}

	public void setTitleOnly(boolean titleOnly)
	{
		this.titleOnly = titleOnly;
	}

	public String getSearchString()
	{
		return searchString;
	}

	public void setSearchString(String searchString)
	{
		this.searchString = searchString;
	}

	public int getResultsPerPage()
	{
		return resultsPerPage;
	}

	public void setResultsPerPage(int resultsPerPage)
	{
		this.resultsPerPage = resultsPerPage;
	}

	public String getCategoryGroupCode()
	{
		return categoryGroupCode;
	}

	public void setCategoryGroupCode(String categoryGroupCode)
	{
		this.categoryGroupCode = categoryGroupCode;
	}

	public static final Parcelable.Creator<SearchQuery> CREATOR = new Parcelable.Creator<SearchQuery>()
	{
		public SearchQuery createFromParcel(Parcel in)
		{
			return new SearchQuery(in);
		}

		public SearchQuery[] newArray(int size)
		{
			return new SearchQuery[size];
		}
	};
}
