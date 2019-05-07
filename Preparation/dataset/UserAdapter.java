/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.domain;

import fitlibrary.object.DomainFixtured;
import fitlibrary.specify.eg.Account;
import fitlibrary.specify.eg.Phone;
import fitlibrary.specify.eg.User;

public class UserAdapter implements DomainFixtured {
	private User user = new User();
	
	public User getSlowPayingUser() {
		User poorPayer = new User("Poor Payer",10000.00);
		poorPayer.addPhone(64,9,3737598);
		poorPayer.addPhone(64,27,4556112);
		poorPayer.setAccount(new Account(456778,"poor"));
		return poorPayer;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	// Factory method to create a phone from the embedded setup table
	public Phone countryRegionNumber(int country, int region, int number) {
		return new Phone(country,region,number);
	}
}
