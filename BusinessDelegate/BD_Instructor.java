package BusinessDelegate;

import java.util.ArrayList;
import java.util.Iterator;

import transferObject.Instructor;
import transferObject.Location;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;
import DAO.DMLUniversity;

public class BD_Instructor {
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

	private Instructor parseCreateInstructorRequest(String request)
			throws ParsingException {

		String[] params = parseRequest(request);
		Instructor instructor = new Instructor();
		instructor.setEmployeeID(params[3]);
		instructor.setFirstName(params[4]);
		instructor.setLastName(params[5]);
		instructor.setAddress(params[6]);
		instructor.setCity(params[7]);
		instructor.setState(params[8]);
		instructor.setZipCode(params[9]);
		instructor.setDepartment(params[10]);
		return instructor;
	}

	public String createInstructor(String request) {
		try {
			Instructor i = parseCreateInstructorRequest(request);
			String result = dao.executeStoredProcedure_createInstructor(i);
			return requestID + "\n" + result;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	public String getDepartment(String request) {

		String[] params = null;
		try {
			params = parseRequest(request);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		if (UMSValidations.isValidID(params[3]) == true) {
			String result = dao.executeGetDepartment(params[3]);
			if (result.equals("0"))
				return requestID + "\n" + UMSException.no_such_person;

			return requestID + "\n0\n" + result;
		} else
			return requestID + "\n" + UMSException.malformed_id;

	}

	public String setDepartment(String request) {

		String[] params;
		try {
			params = parseRequest(request);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		if (UMSValidations.isValidID(params[3]) == true) {
			String result = dao.executeSetDepartment(params[3], params[4]);
			return requestID + "\n0" + result;
		}

		return requestID + "\n" + UMSException.malformed_id;
	}

	public String getOfficeHours(String request) {

		String[] params;
		try {
			params = parseRequest(request);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		if (UMSValidations.isValidID(params[3]) == true) {
			String result = dao.executeGetOfficeHours(params[3]);
			return requestID + "\n0" + result;
		}
		return requestID + "\n" + UMSException.malformed_id;
	}

	public String addOfficeHours(String request) {
		try {
			ArrayList<Location> sc, rb;
			Location loc;
			String[] params = parseRequest(request);
			String valid = dao.getInstructorPK(params[3]);

			if (valid.equals("0\n")) {
				return "" + UMSException.no_such_person;
			}
			if (!UMSValidations.isValidID(params[3])) {
				throw new ParsingException(UMSException.malformed_id);
			}
			loc = new Location(params[4]);
			// check room booked
			rb = dao.getAllExistingLocation();
			Iterator<Location> rbi = rb.iterator();
			while (rbi.hasNext()) {
				if (UMSValidations.isRoomConflict(rbi.next(), loc)) {
					throw new ParsingException(UMSException.room_booked);
				}
			}
			// check Schedule Conflict
			sc = dao.getAllLocationForInstructor(params[3]);
			Iterator<Location> sci = sc.iterator();
			while (sci.hasNext()) {
				if (UMSValidations.isScheduleConflict(sci.next(), loc)) {
					throw new ParsingException(UMSException.schedule_conflict);
				}
			}
			String result = dao.executeAddOfficeHours(params[3], params[4]);
			return requestID + "\n" + result ;
		} catch (ParsingException pE) {
			return requestID + "\n" + pE.getErrorCode();
		}
	}

	public String removeOfficeHours(String request) {

		String[] params;
		try {
			params = parseRequest(request);
			if (UMSValidations.isValidID(params[3])) {
				String result = dao.executeRemoveOfficeHours(params[3],
						params[4]);
				return requestID + "\n" + result;
			}
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + UMSException.malformed_id;
	}
}
