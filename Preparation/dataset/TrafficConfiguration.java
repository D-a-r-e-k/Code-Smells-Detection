package org.lnicholls.galleon.apps.traffic;

/*
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lnicholls.galleon.app.AppConfiguration;

public class TrafficConfiguration implements AppConfiguration {

	public String getName() {
		return mName;
	}

	public void setName(String value) {
		if (mName != null && !mName.equals(value))
			mModified = true;
		mName = value;
	}

	public List getLocations() {
		return mLocations;
	}

	public void setLocations(List value) {
		mLocations = value;
	}

	public void addLocation(Location value) {
		mLocations.add(value);
	}

	public static class Location implements Serializable {
		public Location() {

		}

		public Location(String street, String city, String state, String zip, String radius) {
			mStreet = street;
			mCity = city;
			mState = state;
			mZip = zip;
			mRadius = radius;
		}

		public String getStreet() {
			return mStreet;
		}

		public void setStreet(String value) {
			mStreet = value;
		}

		public String getCity() {
			return mCity;
		}

		public void setCity(String value) {
			mCity = value;
		}

		public String getState() {
			return mState;
		}

		public void setState(String value) {
			mState = value;
		}

		public String getZip() {
			return mZip;
		}

		public void setZip(String value) {
			mZip = value;
		}

		public String getRadius() {
			return mRadius;
		}

		public void setRadius(String value) {
			mRadius = value;
		}

		private String mStreet;

		private String mCity;

		private String mState;

		private String mZip;

		private String mRadius;
	}

	public void setModified(boolean value) {
		mModified = value;
	}

	public boolean isModified() {
		return mModified;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public boolean isShared()
    {
    	return mShared;
    }
    
    public void setShared(boolean value)
    {
    	mShared = value;
    }
    
    private boolean mShared;	

	private String mName;

	private List mLocations = new ArrayList();

	private boolean mModified;
}