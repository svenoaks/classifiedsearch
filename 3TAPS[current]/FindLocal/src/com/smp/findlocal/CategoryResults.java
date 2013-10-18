package com.smp.findlocal;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class CategoryResults implements Parcelable
{
	public static final int NAME = 0;
	public static final int CODE = 1;

	private LinkedHashMap<String[], List<String[]>> categories = new LinkedHashMap<String[], List<String[]>>();

	/*
	 * public CategoryResults(List<String> categoryGroups) { for (String group :
	 * categoryGroups) { categories.put(group, new ArrayList<String[]>()); } }
	 */

	public CategoryResults()
	{
	}

	public void addCategoryGroup(String groupName, String groupCode)
	{
		categories.put(new String[]
		{ groupName, groupCode }, new ArrayList<String[]>());
	}

	public void addToGroup(String groupName, String newEntryName, String newEntryCode)
	{
		List<String[]> catGroup = categories.get(getCategoryGroupKey(groupName));
		String[] newEntry =
		{ newEntryName, newEntryCode };
		catGroup.add(newEntry);
	}

	public String getCategoryGroupCode(String groupName)
	{
		List<String[]> catGroups = new ArrayList<String[]>(categories.keySet());
		for (String[] groups : catGroups)
		{
			if (groups[NAME].equals(groupName))
			{
				return groups[CODE];
			}
		}
		throw new IllegalArgumentException("Not a valid category name");
	}

	private String[] getCategoryGroupKey(String groupName)
	{
		List<String[]> catGroups = new ArrayList<String[]>(categories.keySet());
		for (int i = 0; i < catGroups.size(); ++i)
		{
			if (catGroups.get(i)[NAME].equals(groupName))
			{
				return catGroups.get(i);
			}
		}
		throw new IllegalArgumentException("Not a valid category name");
	}

	public String getCategoryCode(String groupName, String categoryName)
	{
		List<String[]> catGroup = categories.get(getCategoryGroupKey(groupName));
		for (String[] groupsCategories : catGroup)
		{
			if (groupsCategories[NAME].equals(categoryName))
			{
				return groupsCategories[CODE];
			}
		}
		throw new IllegalArgumentException("Not a valid category name");
	}

	public List<String> getCategoryNames(String groupName)
	{
		List<String> names = new ArrayList<String>();
		List<String[]> catGroup = categories.get(getCategoryGroupKey(groupName));
		for (String[] groupsCategories : catGroup)
		{
			names.add(groupsCategories[NAME]);
		}
		return names;
	}

	public boolean hasGroup(String groupName)
	{
		List<String[]> catGroups = new ArrayList<String[]>(categories.keySet());
		for (int i = 0; i < catGroups.size(); ++i)
		{
			if (catGroups.get(i)[NAME].equals(groupName))
			{
				return true;
			}
		}
		return false;
	}

	public List<String> getCategoryGroups()
	{
		List<String> results = new ArrayList<String>();
		List<String[]> groups = new ArrayList(categories.keySet());
		for (String[] group : groups)
		{
			results.add(group[NAME]);
		}
		return results;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeSerializable(categories);
	}

	public static final Parcelable.Creator<CategoryResults> CREATOR = new Parcelable.Creator<CategoryResults>()
	{
		public CategoryResults createFromParcel(Parcel in)
		{
			return new CategoryResults(in);
		}

		public CategoryResults[] newArray(int size)
		{
			return new CategoryResults[size];
		}
	};

	@SuppressWarnings("unchecked")
	private CategoryResults(Parcel in)
	{
		categories = (LinkedHashMap<String[], List<String[]>>) in.readSerializable();
	}
}
