package ru.tet.beans;

public class UserDTO {

	String username;

	String password;

	String secondPassword;

	String email;
	
	boolean enabled = true;

	

  
	
	public UserDTO() {
  }
	
	public UserDTO(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }
	

  public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecondPassword() {
		return secondPassword;
	}
	
	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return username + ":" + password;
	}

}
