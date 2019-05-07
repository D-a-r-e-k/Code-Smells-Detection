package fitlibrary.specify.entityParser;

import java.util.ArrayList;
import java.util.List;

import fitlibrary.traverse.DomainAdapter;

public class NullString implements DomainAdapter {

	public Object getSystemUnderTest() {
		return null;
	}
	public String nullString() {
		return null;
	}
	public String otherString(String s) {
		return s;
	}
	public int positiveInt(int i) {
		return i;
	}
	public String findString(String s) {
		if ("".equals(s))
			return null;
		return s;
	}
	public Integer findInt(String s) {
		Integer result;
		if ("".equals(s))
			result = new Integer(0);
		else result = new Integer(Math.abs(Integer.valueOf(s).intValue()));
		return result;
	}
	@SuppressWarnings("unchecked")
	public List list() {
		ArrayList list = new ArrayList();
		list.add(new Person("Lars"));
		list.add(new Person(null));
		return list;
	}
	public static class Person {
		private String name;

		public String getName() {
			return name;
		}
		public Person(String name) {
			this.name = name;
		}
	}
}
