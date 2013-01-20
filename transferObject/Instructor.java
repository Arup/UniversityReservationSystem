package transferObject;

import BusinessDelegate.ParsingException;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class Instructor extends Person {
	private int pk;
	private String employeeID;
	private String department;
	private OfficeHours[] oh;

	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) throws ParsingException {
		if (UMSValidations.isValidID(employeeID) == true)
			this.employeeID = employeeID;
		else
			throw new ParsingException(UMSException.malformed_id);
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public OfficeHours[] getOh() {
		return oh;
	}

	public void setOh(OfficeHours[] oh) {
		this.oh = oh;
	}

}
