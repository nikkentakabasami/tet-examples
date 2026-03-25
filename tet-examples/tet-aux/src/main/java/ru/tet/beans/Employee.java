package ru.tet.beans;

import lombok.Data;

@Data
public class Employee {

	int id;
	String firstName;

	public Employee(int id, String firstName) {
		this.id = id;
		this.firstName = firstName;
	}

	public Employee() {
	}

}
