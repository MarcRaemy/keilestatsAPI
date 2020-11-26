package ch.keile.keilestatsAPI.datatemplates;

/*Class to help to pass data to the POST- and PUT method of the player controller.
 * Template for collecting, saving and presenting player data*/
public class PlayerTemplate {

	private String lastname;
	private String firstname;
	private String position;
	private String email;
	private String address;
	private String phone;

	public PlayerTemplate() {
	}

	public PlayerTemplate(String lastname, String firstname, String position, String email, String address,
			String phone) {
		super();
		this.lastname = lastname;
		this.firstname = firstname;
		this.position = position;
		this.email = email;
		this.address = address;
		this.phone = phone;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		return result;
	}

	//only considers firstname and lastname
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerTemplate other = (PlayerTemplate) obj;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlayerTemplate [lastname=" + lastname + ", firstname=" + firstname + ", position=" + position
				+ ", email=" + email + ", address=" + address + ", phone=" + phone + "]";
	}
}