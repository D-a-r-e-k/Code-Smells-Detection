package org.lnicholls.galleon.apps.upcoming;

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

public class UpcomingConfiguration implements AppConfiguration {

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

	public void setModified(boolean value) {
		mModified = value;
	}

	public boolean isModified() {
		return mModified;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static class Location implements Serializable {
		public Location() {

		}

		public Location(String country, String state, String metro) {
			mCountry = country;
			mState = state;
			mMetro = metro;
		}

		public String getCountry() {
			return mCountry;
		}

		public void setCountry(String value) {
			mCountry = value;
		}

		public String getState() {
			return mState;
		}

		public void setState(String value) {
			mState = value;
		}

		public String getMetro() {
			return mMetro;
		}

		public void setMetro(String value) {
			mMetro = value;
		}

		private String mCountry;

		private String mState;

		private String mMetro;
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