package es.test.backup;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {

	private int id;
	private String firstName;
	private String lastName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String value) {
		firstName = value;
	}

	public String getLastName() {
		return lastName;
	}


	public void setLastName(String value) {
		lastName = value;
	}
}
