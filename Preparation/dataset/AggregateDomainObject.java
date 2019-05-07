/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify;

import java.util.List;
import java.util.Set;

import fitlibrary.DomainObjectCheckFixture;
import fitlibrary.DomainObjectSetUpFixture;

public class AggregateDomainObject {
	Book book = new Book();

	public DomainObjectSetUpFixture createBook() {
		return new DomainObjectSetUpFixture(book);
	}
	public DomainObjectCheckFixture checkBook() { 
		return new DomainObjectCheckFixture(book);
	}
    public KeyValue nameValue(String name, String value) { 
		return new KeyValue(name,value);
	}
    public Author namePhonesAddress(String name, Set<Phone> phones, Address address) { 
    	return new Author(name,phones,address);
    }
    public Phone phone(String phone) { 
    	return new Phone(phone);
    }
	public static class KeyValue {
		private String name;
		private String value;

		public KeyValue(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
	}
	@SuppressWarnings("unchecked")
	public static class Book {
		private List attributes;
		private List<Author> authors;
		private Publisher publisher;

		public void setAttributes(List attributes) {
			this.attributes = attributes;
		}
		public List getAttributes() {
			return attributes;
		}
		public void setAuthors(List<Author> authors) {
			this.authors = authors;
		}
		public List<Author> getAuthors() {
			return authors;
		}
		public Publisher getPublisher() {
			return publisher;
		}
		public void setPublisher(Publisher publisher) {
			this.publisher = publisher;
		}
	}
	public static class Author {
		private String name;
		private Set<Phone> phones;
		private Address address;

		public Author(String name, Set<Phone> phones, Address address) {
			this.name = name;
			this.phones = phones;
			this.address = address;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public Set<Phone> getPhones() {
			return phones;
		}
		public void setPhones(Set<Phone> phones) {
			this.phones = phones;
		}
		public Address getAddress() {
			return address;
		}
		public void setAddress(Address address) {
			this.address = address;
		}
	}
	public static class Phone {
		private String phone;

		public Phone(String phone) {
			this.phone = phone;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
	}
	public static class Address {
		private String address1, address2;

		public String getAddress1() {
			return address1;
		}
		public void setAddress1(String address1) {
			this.address1 = address1;
		}
		public String getAddress2() {
			return address2;
		}
		public void setAddress2(String address2) {
			this.address2 = address2;
		}
	}
	public static class Publisher {
		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
