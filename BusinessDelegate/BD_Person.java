package BusinessDelegate;

import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;
import DAO.DMLUniversity;

public class BD_Person {
	private DMLUniversity dao = new DMLUniversity();
	private String requestID;

	public String Get_Courses(String request) {
		try {
			String[] params = request.split("\n");
			requestID = params[1];

			int pCount = Integer.parseInt(params[2]);

			if (pCount + 3 != params.length)
				return requestID + "\n" + UMSException.malformed_message;

			String personID = "";
			if (UMSValidations.isValidID(params[3]))
				personID = params[3];
			else
				return requestID + "\n" + UMSException.malformed_id;

			String lsResult = dao.executeGetCourses(personID);
			if (lsResult == "")
				return requestID + "\n" + UMSException.no_such_person;

			return requestID + "\n0\n" + lsResult;
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		// NEED TO CHECK
		return "";
	}

	public String Set_FieldValue(String request) {

		try {
			String[] params = request.split("\n");
			requestID = params[1];

			int pCount = Integer.parseInt(params[2]);

			if (pCount + 3 != params.length)
				return requestID + "\n" + UMSException.malformed_message;

			String personID = "";
			if (UMSValidations.isValidID(params[3]))
				personID = params[3];
			else
				return requestID + "\n" + UMSException.malformed_id;

			String fieldName = params[0].toUpperCase().replace("SET_", "");
			if (!(fieldName.equals("FIRST_NAME")
					|| fieldName.equals("LAST_NAME")
					|| fieldName.equals("ADDRESS") || fieldName.equals("CITY")
					|| fieldName.equals("STATE") || fieldName.equals("ZIP") || fieldName
					.equals("ID")))
				return requestID + "\n" + UMSException.malformed_message;

			if (fieldName == "ID" && !UMSValidations.isNumeric(params[4]))
				return requestID + "\n" + UMSException.malformed_id;
			if (fieldName.equals("STATE"))
				params[4] = UMSValidations.getStateNormalized(params[4]);
			if (fieldName.equals("ZIP")
					&& !UMSValidations.isValidZip(params[4]))
				return requestID + "\n" + UMSException.malformed_zip;
			String lsResult = dao.executeSetPersonField(personID, fieldName,
					params[4]);
			return requestID + "\n" + lsResult;

		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		// NEED TO CHECK
		return "";
	}

	public String Get_FieldValue(String request) {

		try {
			String[] params = request.split("\n");
			requestID = params[1];

			int pCount = Integer.parseInt(params[2]);

			if (pCount + 3 != params.length)
				return requestID + "\n" + UMSException.malformed_message;

			String personID = "";
			if (UMSValidations.isValidID(params[3]))
				personID = params[3];
			else
				return requestID + "\n" + UMSException.malformed_id;
			String fieldName = params[0].toUpperCase().replace("GET_", "");

			if (!(fieldName.equals("FIRST_NAME")
					|| fieldName.equals("LAST_NAME")
					|| fieldName.equals("ADDRESS") || fieldName.equals("CITY")
					|| fieldName.equals("STATE") || fieldName.equals("ZIP")))
				return requestID + "\n" + UMSException.malformed_message;

			String lsResult = dao.executeGetPersonField(personID, fieldName);
			if (lsResult == "")
				return requestID + "\n" + UMSException.no_such_person;

			return requestID + "\n0\n1\n" + lsResult;
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		// NEED TO CHECK
		return "";

	}
}