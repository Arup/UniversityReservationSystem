package transferObject;

import BusinessDelegate.ParsingException;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class Person {
	protected String firstName;
	protected String lastName;
	protected String address;
	protected String city;
	protected String state;
	protected String zipCode;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) throws ParsingException {
		// if(UMSValidations.isValidState(state))
		// this.state = state;
		// else
		// throw new ParsingException(UMSException.malformed_state);
		this.state = UMSValidations.getStateNormalized(state);
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) throws ParsingException {
		if (UMSValidations.isValidZip(zipCode))
			this.zipCode = zipCode;
		else
			throw new ParsingException(UMSException.malformed_zip);
	}

}