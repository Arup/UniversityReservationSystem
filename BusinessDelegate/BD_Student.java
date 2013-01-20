package BusinessDelegate;

import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;
import DAO.DMLUniversity;


public class BD_Student 
{
	private DMLUniversity dao = new DMLUniversity();
	private String requestID;
	private transferObject.Student parseCreateStudentRequest(String request) throws Exception
	{
    		
		String[] params = request.split("\n");		
		requestID=params[1];
				
		int pCount = Integer.parseInt(params[2]);
		
		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,requestID); // malformed request	
		
		transferObject.Student student=new transferObject.Student();
		student.setFirstName(params[3]);        
		student.setLastName(params[4]);
		student.setStudentID(params[5]);
		student.setAddress(params[6]);
		student.setCity(params[7]);
		student.setState(params[8]);
		student.setZipCode(params[9]);		
		student.setRequestID(requestID);
		return student;
		
	}
	public String createStudent(String request)
	{
		try
		{
			String result = dao.createStudentProcedure(parseCreateStudentRequest(request));
			return requestID+"\n" + result;
		}
		catch(ParsingException pE)
		{
			return requestID+"\n"+pE.getErrorCode();
		}
		catch(Exception exp)
		{
			System.out.println(exp.getMessage());
			exp.printStackTrace();			
			return requestID+"\n"+UMSException.create_exception;
			
		}
		//return requestID+"\n0\n0";	
	}
	private transferObject.Student parseRemoveStudentRequest(String request) throws Exception
	{
    		
		String[] params = request.split("\n");		
		requestID=params[1];
				
		int pCount = Integer.parseInt(params[2]);
		
		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,requestID); // malformed request	
		
		transferObject.Student student=new transferObject.Student();
		student.setStudentID(params[3]);
		student.setRequestID(requestID);
		student.iForceUnEnroll=Integer.parseInt(params[4]);
		
		return student;
	}
	public String RemoveStudent(String request)
	{
		try
		{
			String result = dao.removeStudentProcedure(parseRemoveStudentRequest(request));
			return requestID+"\n"+result;
		}
		catch(ParsingException pE)
		{
			return requestID+"\n"+pE.getErrorCode();
		}
		catch(Exception exp)
		{	
			return requestID+"\n"+UMSException.create_exception;
		}		
	}
	public String getEnrolledUnits(String request)
	{
		String[] params = request.split("\n");		
		requestID=params[1];
				
		int pCount = Integer.parseInt(params[2]);
		
		if (pCount + 3 != params.length)
			return requestID+"\n"+UMSException.malformed_message; // malformed request	
		
		if(! UMSValidations.isValidID(params[3]))
			return requestID+"\n"+UMSException.malformed_id;
		
		
		String lsResponse= dao.getEnrolledUnits(params[3]);        
		String arr[]=lsResponse.split(" ");
		if(arr[1].equals("0"))
		{
			return requestID+"\n0\n1\n"+arr[0];
		}
		
		return requestID+"\n"+arr[1];
		
	}
	//Getters Setters for all fields
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}


}
