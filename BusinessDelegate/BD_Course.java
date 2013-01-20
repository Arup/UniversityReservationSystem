package BusinessDelegate;

import java.util.ArrayList;
import java.util.Iterator;

import transferObject.Course;
import transferObject.Location;
import DAO.DMLUniversity;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class BD_Course {
	DMLUniversity dao = new DMLUniversity();
	private String requestID;

	private Course parseCreateCourseRequest(String request)
			throws ParsingException {
		Course course = new Course();

		String[] params = request.split("\n");
		requestID = params[1];
		Location locObject = new Location(params[6]);
		int pCount = Integer.parseInt(params[2]);

		// All the check validations for create course: Start
		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);
		if (Integer.parseInt(params[7]) < 0)
			throw new ParsingException(UMSException.malformed_units, requestID);
		if (!UMSValidations.isValidID(params[5]))
			throw new ParsingException(UMSException.malformed_id);
		if ((dao.getInstructorPK(params[5])).equals("0\n"))
			throw new ParsingException(UMSException.no_such_person, requestID);

		ArrayList<Location> sc, rb;

		// check Schedule Conflict
		sc = dao.getAllLocationForInstructor(params[5]);
		Iterator<Location> sci = sc.iterator();
		while (sci.hasNext()) {
			if (UMSValidations.isScheduleConflict(sci.next(), locObject)) {
				throw new ParsingException(UMSException.schedule_conflict);
			}
		}
		// check room booked
		rb = dao.getAllExistingLocation();
		Iterator<Location> rbi = rb.iterator();
		while (rbi.hasNext()) {
			if (UMSValidations.isRoomConflict(rbi.next(), locObject)) {
				throw new ParsingException(UMSException.room_booked);
			}
		}

		// All the check validations for create course: End
		course.setCourseName(params[3]);
		course.setCourseSection(params[4]);
		course.setInstructor_FK(Integer.parseInt(params[5]));
		course.setLocation(locObject);
		course.setUnits(Integer.parseInt(params[7]));
		course.setRequestID(request);

		return course;
	}

	public String createCourse(String request) {
		try {

			Course course = parseCreateCourseRequest(request);
			String result = dao.createCourse(course);
			return requestID + "\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	public String removeCourse(String request) {
		try {
			String params[] = parseRemoveCourseRequest(request);
			String result = dao.removeCourse(params[3], params[4], Integer
					.parseInt(params[5]));

			return requestID + "\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			return requestID + "\n" + UMSException.malformed_message;
		}
	}

	private String[] parseRemoveCourseRequest(String request)
			throws ParsingException {
		String[] params = request.split("\n");
		int pCount = Integer.parseInt(params[2]);
		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request
		return params;
	}

	public String setCourseName(String request) {

		try {
			String courseString = parseSetCourseNameRequest(request);
			String result = dao.setCourseName(courseString);

			System.out.println("Result is: " + result);
			System.out.println("Request id is: " + requestID);

			return requestID + "\n0";

		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	private String parseSetCourseNameRequest(String request)
			throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request

		courseString = params[3] + "\n" + params[4] + "\n" + params[5];

		return courseString;
	}

	public String setCourseSection(String request) {
		try {
			String courseString = parseSetCourseSectionRequest(request);
			String result = dao.setCourseSection(courseString);

			System.out.println("Result is: " + result);
			System.out.println("Request id is: " + requestID);

			return requestID + "\n0";
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		} catch (Exception exp) {
			return requestID + "\n" + UMSException.create_exception;
		}
		// return requestID+"\n0\n0";

	}

	private String parseSetCourseSectionRequest(String request)
			throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request

		courseString = params[3] + "\n" + params[4] + "\n" + params[5];

		return courseString;
	}

	public String setCourseInstructor(String request) {
		String result = "";
		try {
			String params[] = parseRequest(request);
			String id = params[3].trim();
			String cName = params[4].trim();
			String cSec = params[5].trim();
			if (!UMSValidations.isValidID(id))
				throw new ParsingException(UMSException.malformed_id, requestID);
			if (dao.getInstructorPK(id).equals("0\n"))
				throw new ParsingException(UMSException.no_such_person,
						requestID);

			ArrayList<Location> sc;
			Location locObject = null;

			// check Schedule Conflict
			String loc = dao.getLocation(cName, cSec);
			if (!loc.equals("")) {
				locObject = new Location(loc);
				sc = dao.getAllLocationForInstructor(id);
				Iterator<Location> sci = sc.iterator();
				while (sci.hasNext()) {
					if (UMSValidations
							.isScheduleConflict(sci.next(), locObject)) {
						throw new ParsingException(
								UMSException.schedule_conflict);
					}
				}
				String courseString = parseSetCourseSectionRequest(request);
				result = dao.setCourseInstructor(courseString);
			}
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n0";
	}

	private String parsegetCourseUnits(String request) throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request

		courseString = params[3] + "\n" + params[4] + "\n";

		return courseString;
	}

	public String GetCourseUnits(String request) {

		try {
			String courseString = parsegetCourseUnits(request);
			String result = dao.getCourseUnits(courseString);

			return requestID + "\n0\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	private String parseSetCourseUnitsRequest(String request)
			throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request
		try {
			if (Integer.parseInt(params[5]) < 0)
				throw new ParsingException(UMSException.malformed_units);
		} catch (NumberFormatException n) {
			throw new ParsingException(UMSException.malformed_units);
		}
		courseString = params[3] + "\n" + params[4] + "\n" + params[5];

		return courseString;
	}

	public String SetCourseUnits(String request) {
		try {
			String courseString = parseSetCourseUnitsRequest(request);
			String result = dao.setCourseUnits(courseString);

			return requestID + "\n0";
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}

	}

	public String getCourseInstructor(String request) {
		String result = "";
		try {
			String courseString = parsegetCourseInstructor(request);
			result = dao.getCourseInstructor(courseString);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n0\n" + result;
	}

	private String parsegetCourseInstructor(String request)
			throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request

		courseString = params[3] + "\n" + params[4];

		return courseString;
	}

	public String GetStudents(String request) {
		try {
			String courseString = parsegetStudents(request);
			String result = dao.getStudents(courseString);

			return requestID + "\n0\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		} 
	}

	private String parsegetStudents(String request) throws ParsingException {
		String courseString = new String();

		String[] params = request.split("\n");
		requestID = params[1];

		int pCount = Integer.parseInt(params[2]);

		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);// malformed request

		courseString = params[3] + "\n" + params[4] + "\n";

		return courseString;
	}

	public String getLocation(String request) {
		try {
			String params[] = parseRequest(request);
			String result = dao.getLocation(params[3], params[4]);
			
			return requestID + "\n0\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		} 

	}

	public String setLocation(String request) {
		try {
			String params[] = parseRequest(request);
			Location newLocation = new Location(params[3]);
			ArrayList<Location> arrExistingLocations = dao
					.getAllExistingLocation();
			if (!arrExistingLocations.isEmpty()) {
				Iterator<Location> itLocation = arrExistingLocations.iterator();
				while (itLocation.hasNext()) {
					if (UMSValidations.isRoomConflict(newLocation, itLocation
							.next()))
						return requestID + "\n" + UMSException.room_booked;
				}
			}
			String lsResult = "";
			lsResult = dao.setLocation(params[4], params[5], newLocation);
			return requestID + "\n" + lsResult;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	private String[] parseRequest(String request) throws ParsingException {
		String[] params = request.split("\n");
		requestID = params[1];
		int pCount = Integer.parseInt(params[2]);
		if (pCount + 3 != params.length)
			throw new ParsingException(UMSException.malformed_message,
					requestID);
		return params;
	}

}
