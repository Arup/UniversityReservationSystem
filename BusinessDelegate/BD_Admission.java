package BusinessDelegate;

import java.util.ArrayList;
import java.util.Iterator;
import transferObject.Location;
import DAO.DMLUniversity;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class BD_Admission {
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

	public String enrollStudent(String request) {
		String result = "";
		try {
			String params[] = parseRequest(request);
			String id = params[3];
			String cName = params[4];
			String cSec = params[5];
			if (!UMSValidations.isValidID(id))
				throw new ParsingException(UMSException.malformed_id, requestID);

			ArrayList<Location> sc;
			Location locObject = null;

			// check Schedule Conflict
			String loc = dao.getLocation(cName, cSec);
			if (!loc.equals("")) {
				locObject = new Location(loc);
				sc = dao.getAllLocationForStudent(id);
				Iterator<Location> sci = sc.iterator();
				while (sci.hasNext()) {
					if (UMSValidations
							.isScheduleConflict(sci.next(), locObject)) {
						throw new ParsingException(
								UMSException.schedule_conflict);
					}
				}
				result = dao.enrollStudentProcedure(id, cName, cSec);
			} else
				throw new ParsingException(UMSException.no_such_course);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + result;
	}

	public String unenrollStudent(String request) {
		String result = "";
		try {
			String params[] = parseRequest(request);
			String id = params[3];
			String cName = params[4];
			String cSec = params[5];
			result = dao.unenrollStudentProcedure(id, cName, cSec);
		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
		return requestID + "\n" + result;
	}

	public String calculateBill(String request) {
		double fees = 0;
		try {
			String params[] = parseRequest(request);
			if (!UMSValidations.isNumeric(params[3])
					|| !UMSValidations.isValidID(params[3]))
				return requestID + "\n" + UMSException.malformed_id;
			String returnVals[] = (dao.getFees(params[3])).split("@");
			if (returnVals[1].equals("0")) {
				fees = Double.parseDouble(returnVals[0]);
				return requestID + "\n0\n1\n" + fees;
			} else
				return requestID + "\n" + returnVals[1];

		} catch (ParsingException e) {
			return requestID + "\n" + e.getErrorCode();
		}
	}
}
