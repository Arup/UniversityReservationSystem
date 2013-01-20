package BusinessDelegate;

import UniversityRecordsSystem.UMSException;
import DAO.DMLUniversity;

public class BD_Search {

	private DMLUniversity dao = new DMLUniversity();
	private String requestID;

	public String[] parseRequest(String request) throws ParsingException {
		String[] params = request.split("\n");
		requestID = params[1];
		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);
		return params;
	}

	public String findPersons(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);

			String pattern, type, option;
			type = params[4];
			pattern = params[3];
			option = params[0].substring("Find_Persons_By_".length());

			rows = dao.executeFindPersonsQuery(type, pattern, option);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}

	public String findAllPersons(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);
			String type = params[3];

			rows = dao.executeFindPersonsQuery(type);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}

	public String findAllCourses(String request) {
		String rows = "";
		try {
			parseRequest(request);
			rows = dao.executeFindCoursesQuery();
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n0\n" + rows;
	}

	public String findAllCoursesByInstructor(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);
			rows = dao.executeFindCoursesByInstuctorQuery(params[3]);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}

	public String findAllCoursesByLocation(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);
			rows = dao.executeFindCoursesByLocationQuery(params[3]);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}

	public String findAllCoursesByCourseName(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);
			rows = dao.executeFindCoursesByCourseNameQuery(params[3]);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}

	public String findInsructorsByDepartment(String request) {
		String rows = "";
		try {
			String params[] = parseRequest(request);
			rows = dao.executeFindInsructorsByDepartmentQuery(params[3]);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + 0 + "\n" + rows;
	}
	
//	public String try1(){
//		return dao.executeUpdate("Update instructor Set Department = 'cmpe' Where Instructor_Emp_ID = 1234995");
//	}
}
