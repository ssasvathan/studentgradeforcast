package cen.comp313.student_portal;

public class Student {
	private int _ID;
	private String userName;
	private String password;
	private String firstName;
	private String lastName;

	public int getID() {
		return _ID;
	}

	public void setID(int _ID) {
		this._ID = _ID;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
