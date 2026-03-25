package ru.tet.data;

import ru.tet.beans.User;
import ru.tet.beans.User.Gender;

public class BeansSamples {


	public static User createTestUserBean() {
		User u = new User();
		u.setGender(Gender.FEMALE);
		u.setAge(23);
		u.getName().setFirst("bob");
		u.getName().setLast("show");
		u.setKeys(new Integer[] {123,528, 951});
		return u;
	}	
	
	
	
}
