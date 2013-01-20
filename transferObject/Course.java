package transferObject;

public class Course {
	private int pk;
	private String courseName;
	private String courseSection;
	private String start;
	private String end;
	private String dow;
	private int instructor_FK;
	private int units;
	private String requestID;
	public Location location;
	public boolean ForceUnenroll;

	public boolean isForceUnenroll() {
		return ForceUnenroll;
	}

	public void setForceUnenroll(boolean forceUnenroll) {
		ForceUnenroll = forceUnenroll;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDow() {
		return dow;
	}

	public void setDow(String dow) {
		this.dow = dow;
	}

	public int getInstructor_FK() {
		return instructor_FK;
	}

	public void setInstructor_FK(int instructorFK) {
		instructor_FK = instructorFK;
	}

	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseSection() {
		return courseSection;
	}

	public void setCourseSection(String courseSection) {
		this.courseSection = courseSection;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

}
