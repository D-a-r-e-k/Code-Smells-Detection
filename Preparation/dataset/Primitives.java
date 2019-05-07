/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
 * Written: 6/09/2006
*/

package fitlibrary.specify.domain;

import fitlibrary.object.DomainFixtured;

public class Primitives implements DomainFixtured {
	private int anInt = 3;
	private double aDouble = 3.14159;
	private boolean aBoolean = true;
	private String aString = "my value";
	
	public boolean isABoolean() {
		return aBoolean;
	}
	public void setABoolean(boolean boolean1) {
		aBoolean = boolean1;
	}
	public double getADouble() {
		return aDouble;
	}
	public void setADouble(double double1) {
		aDouble = double1;
	}
	public int getAnInt() {
		return anInt;
	}
	public void setAnInt(int anInt) {
		this.anInt = anInt;
	}
	public String getAString() {
		return aString;
	}
	public void setAString(String string) {
		aString = string;
	}
	
}
