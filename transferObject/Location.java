package transferObject;

import BusinessDelegate.ParsingException;
import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

public class Location {
	private int pk;
	private String requestID;
	private String start;
	private String end;
	private String DOW;
	private String building_Room;

	public String getBuilding_Room() {
		return building_Room;
	}

	public void setBuilding_Room(String buildingRoom) {
		building_Room = buildingRoom.trim();
	}

	private String hours;

	public Location() {
	}

	public Location(String _location) throws BusinessDelegate.ParsingException {
		_location = _location.trim();
		if (!UMSValidations.isValidLocation(_location))
			throw new ParsingException(UMSException.malformed_location);

		String[] parse_loc = _location.split(" ");
		int parseCount = parse_loc.length;
		setHours(parse_loc[parseCount - 1]);
		if (parse_loc[parseCount - 1].toUpperCase().equals("TBA"))
			setDOW("TBA");
		else
			setDOW(parse_loc[parseCount - 2]);

		String bldg_room = "";
		for (int i = 0; i <= parseCount - 3; i++) {
			bldg_room = bldg_room + parse_loc[i] + " ";
		}
		bldg_room.trim();
		setBuilding_Room(bldg_room);
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) throws BusinessDelegate.ParsingException {
		hours = hours.trim();
		if (hours.equals("TBA")) {
			this.hours = "TBA";
			this.start = "TBA";
			this.end = "TBA";
			return;
		}

		String[] _hours = hours.split("-");
		if ((!UMSValidations.isValid24HoursFormat(_hours[0]))
				|| (!UMSValidations.isValid24HoursFormat(_hours[1])))
			throw new ParsingException(UMSException.malformed_hours);

		this.hours = hours;

		if (!UMSValidations.isValidTimeSpan(_hours[0], _hours[1])) {
			throw new BusinessDelegate.ParsingException(
					UMSException.malformed_hours);
		}
		this.setStartTime(_hours[0]);
		this.setEndTime(_hours[1]);
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getStartTime() {
		return start;
	}

	public void setStartTime(String starttime) throws ParsingException {
		starttime = starttime.trim();
		if ((!starttime.equals("TBA"))
				&& !(UMSValidations.isValid24HoursFormat(starttime)))
			throw new BusinessDelegate.ParsingException(
					UMSException.malformed_hours);

		this.start = starttime;

	}

	public void setEndTime(String endTime) throws ParsingException {
		endTime = endTime.trim();
		if ((!endTime.equals("TBA"))
				&& !(UMSValidations.isValid24HoursFormat(endTime)))
			throw new BusinessDelegate.ParsingException(
					UMSException.malformed_hours);

		this.end = endTime;
	}

	public String getEndTime() {
		return end;
	}

	public void setDOW(String dayOfWeek) {
		dayOfWeek = dayOfWeek.trim();
		this.DOW = dayOfWeek;
	}

	public String getDOW() {
		return DOW;
	}

	@Override
	public String toString() {
		String loc = this.building_Room + " ";
		if (this.hours.toUpperCase().equals("TBA"))
			loc = loc + " TBA";
		else
			loc = loc + " " + this.DOW + " " + this.start + " " + this.end;
		return loc;
	}
}
