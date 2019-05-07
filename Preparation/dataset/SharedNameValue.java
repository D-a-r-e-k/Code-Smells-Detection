package org.lnicholls.galleon.util;

/*
 * Copyright (C) 2005  Leon Nicholls
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * See the file "COPYING" for more details.
 */

import java.io.Serializable;

public class SharedNameValue extends NameValue implements Serializable {

	public static final String PUBLIC = "Public";

	public static final String PRIVATE = "Private";

	public static final String FRIENDS = "Friends";

	public SharedNameValue() {
	}

	public SharedNameValue(String name, String value, String description, String tags, String privacy) {
		super(name, value);
		mDescription = description;
		mTags = tags;
		mPrivacy = privacy;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String value) {
		mDescription = value;
	}

	public String getTags() {
		return mTags;
	}

	public void setTags(String value) {
		mTags = value;
	}

	public String getPrivacy() {
		return mPrivacy;
	}

	public void setPrivacy(String value) {
		mPrivacy = value;
	}

	private String mDescription;

	private String mTags;

	private String mPrivacy = PRIVATE;
}