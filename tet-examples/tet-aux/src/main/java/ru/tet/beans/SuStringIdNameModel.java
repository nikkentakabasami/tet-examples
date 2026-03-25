package ru.tet.beans;

import lombok.Data;

@Data
public class SuStringIdNameModel {

	String id;

	String name;

	public SuStringIdNameModel(String id, String name) {
		this.id = id;
		this.name = name;
	}

}
