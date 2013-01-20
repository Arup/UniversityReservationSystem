package transferObject;

import BusinessDelegate.ParsingException;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class Student extends Person
{
	private int pk;
	private String requestID;
	public int iForceUnEnroll=0;
	
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	private String studentID;
//	int exception;
	
	
	public int getPk() {
		return pk;
	}
	public void setPk(int pk) {
		this.pk = pk;
	}
	
	public String getStudentID() 
	{
		return studentID;
	}
	public void setStudentID(String _studentID) throws ParsingException 
	{
		if(! UMSValidations.isValidID(_studentID))
			throw new BusinessDelegate.ParsingException(UMSException.malformed_id);
		
		this.studentID = _studentID;
	}
}
