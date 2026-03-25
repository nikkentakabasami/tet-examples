package ru.tet.beans;

import lombok.Data;

@Data
public class User {

	public enum Gender {
		MALE, FEMALE
	};

	@Data
	public static class Name {
		String first, last;

	}

	Gender gender;
	int age;

	Name name = new Name();
	boolean verified;
	byte[] userImage;

	Integer[] keys;

}
