/*
 * Copyright (c) 2006 Rick Mugridge, www.RimuResearch.com
 * Released under the terms of the GNU General Public License version 2 or later.
*/
package fitlibrary.specify.domain;

import fitlibrary.object.DomainFixtured;

public class BadConstructorsInNestedObjects implements DomainFixtured {
	private Employee employee = new Employee();
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public static class Employee {
		private String name;
		private Manager manager;
		private Department department;
		
		public Manager getManager() {
			return manager;
		}
		public void setManager(Manager manager) {
			this.manager = manager;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Department getDepartment() {
			return department;
		}
		public void setDepartment(Department department) {
			this.department = department;
		}
	}
	public static class Manager extends Employee {
		public Manager(@SuppressWarnings("unused") String name) {
			//
		}
	}
	@SuppressWarnings("unused")
	public static class Department {
		private String city;
		
		private Department() {
			//
		}
		private void setCity(String city) {
			this.city = city;
		}
	}
}
