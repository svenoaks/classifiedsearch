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

import android.content.SharedPreferences;

public class SearchCriteria{
	private static SearchCriteria instance;
	//class containing all the search criteria used in the search
	private String cityName, cityCode, categoryName, categoryCode, searchQuery, sectionName;
	private String srchType, addOne, addTwo, addThree, addFour, addFive, hasPic, bedrooms;
	private String minAsk, maxAsk;
	private boolean hasOptions;
	
	//constructor
	private SearchCriteria(SharedPreferences data){
		this.categoryCode = data.getString(Constants.LAST_CATEGORY_CODE, null);
		this.categoryName = data.getString(Constants.LAST_CATEGORY, "Category");
		this.cityCode = data.getString(Constants.LAST_CITY_CODE, null);
		this.cityName = data.getString(Constants.LAST_CITY, "Region");
		this.searchQuery = data.getString(Constants.LAST_QUERY, "");
		this.sectionName = data.getString(Constants.LAST_SECTION, "Section");
		this.srchType = data.getString(Constants.SRCH_TYPE, "A");
		this.addOne = data.getString(Constants.ADD_ONE, "");
		this.addTwo = data.getString(Constants.ADD_TWO, "");
		this.addThree = data.getString(Constants.ADD_THREE, "");
		this.addFour = data.getString(Constants.ADD_FOUR, "");
		this.addFive = data.getString(Constants.ADD_FIVE, "");
		this.hasPic = data.getString(Constants.HAS_PIC, "");
		this.bedrooms = data.getString(Constants.BEDROOMS, "");
		this.minAsk = data.getString(Constants.MIN_ASK, "");
		this.maxAsk = data.getString(Constants.MAX_ASK, "");
		this.hasOptions = data.getBoolean(Constants.HAS_OPTIONS, false);
	}
	
	public static void initInstance(SharedPreferences prefs)
	  {
	    if (instance == null)
	    {
	      // Create the instance
	      instance = new SearchCriteria(prefs);
	    }
	  }
	
	public static SearchCriteria getInstance()
	  {
	    // Return the instance
	    return instance;
	  }

	//setters and getters
	public String getCityName() {
		return cityName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getSrchType() {
		return srchType;
	}

	public void setSrchType(String srchType) {
		this.srchType = srchType;
	}

	public String getAddOne() {
		return addOne;
	}

	public void setAddOne(String addOne) {
		this.addOne = addOne;
	}

	public String getAddTwo() {
		return addTwo;
	}

	public void setAddTwo(String addTwo) {
		this.addTwo = addTwo;
	}

	public String getAddThree() {
		return addThree;
	}

	public void setAddThree(String addThree) {
		this.addThree = addThree;
	}

	public String getAddFour() {
		return addFour;
	}

	public void setAddFour(String addFour) {
		this.addFour = addFour;
	}

	public String getAddFive() {
		return addFive;
	}

	public void setAddFive(String addFive) {
		this.addFive = addFive;
	}

	public String getHasPic() {
		return hasPic;
	}

	public void setHasPic(String hasPic) {
		this.hasPic = hasPic;
	}

	public String getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(String bedrooms) {
		this.bedrooms = bedrooms;
	}

	public String getMinAsk() {
		return minAsk;
	}

	public void setMinAsk(String minAsk) {
		this.minAsk = minAsk;
	}

	public String getMaxAsk() {
		return maxAsk;
	}

	public void setMaxAsk(String maxAsk) {
		this.maxAsk = maxAsk;
	}

	public boolean isHasOptions() {
		return hasOptions;
	}

	public void setHasOptions(boolean hasOptions) {
		this.hasOptions = hasOptions;
	}
	
}
