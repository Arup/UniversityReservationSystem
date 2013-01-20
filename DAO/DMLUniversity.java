package DAO;

import java.sql.*;
import java.util.ArrayList;

import UniversityRecordsSystem.UMSException;
import UniversityRecordsSystem.UMSValidations;

import BusinessDelegate.ParsingException;

import transferObject.Course;
import transferObject.Location;

public class DMLUniversity {
	String dbtime = "";
	String dbUrl = "jdbc:mysql://localhost/university";
	String dbClass = "com.mysql.jdbc.Driver";
	Connection con;

	public DMLUniversity() {
		try {
			Class.forName(dbClass);
			con = DriverManager.getConnection(dbUrl, "root", "root");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound E : " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL E : " + e.getMessage());
			e.printStackTrace();
		}
	}

	// ******************** COMMON METHODS **********************************
	public String executeStatement(String query) {
		Statement st;
		String result = "";
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			String s = "";
			int i = 0;
			while (rs.next()) {
				s = rs.getString(1) + "\n";
				result += s;
				i++;
			}
			result = i + "\n" + result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String executeUpdate(String query) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			boolean rs = stmt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	// ********************** COURSE OPERATIONS *******************

	public String createCourse(Course course) {
		String result = "";

		try {
			CallableStatement cs = con
					.prepareCall("{CALL Create_Course(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, course.getCourseName());
			cs.setString(2, course.getCourseSection());
			cs.setInt(3, course.getUnits());
			cs.setString(4, course.location.getStartTime());
			cs.setString(5, course.location.getEndTime());
			cs.setString(6, course.location.getDOW());
			cs.setString(7, course.getLocation().getBuilding_Room());
			cs.setInt(8, course.getInstructor_FK());
			cs.registerOutParameter(9, Types.INTEGER);
			cs.executeQuery();
			result = cs.getString(9);
			System.out.println("result of execute : " + result);
			con.close();
			return result;
		} // end try

		catch (SQLException e) {
			System.out.println("SQL E : " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public String removeCourse(String name, String sec, int fe) {
		String result = "";
		try {
			CallableStatement cs = con
					.prepareCall("{Call remove_course(?,?,?,?)}");
			cs.setString(1, name);
			cs.setString(2, sec);
			cs.setInt(3, fe);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.executeQuery();
			result = cs.getString(4);
			System.out.println("Result : " + result);
		} catch (SQLException s) {
			s.printStackTrace();
		}
		return result;
	}

	public String setCourseName(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String currentCourseName = params[0];
		String currentCourseSection = params[1];
		String newCourseName = params[2];
		String checkCourseNameQuery = "Select course_pk from Course where Course_Name='"
				+ newCourseName.trim()
				+ "' "
				+ "and course_section='"
				+ currentCourseSection.trim() + "'";

		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		if (result.equals("0\n")) {
			updateQuery = "update course set Course_Name='"
					+ newCourseName.trim() + "' where course_name='"
					+ currentCourseName.trim() + "' and course_section='"
					+ currentCourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		} else
			throw new ParsingException(UMSException.course_exists);
		return result;
	}

	public String setCourseSection(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String currentCourseName = params[0];
		String currentCourseSection = params[1];
		String newCourseSection = params[2];
		String checkCourseNameQuery = "Select course_pk from Course where Course_Section='"
				+ newCourseSection.trim()
				+ "' "
				+ "and Course_Name='"
				+ currentCourseName.trim() + "'";

		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		if (result.equals("0\n")) {
			updateQuery = "update course set Course_Section='"
					+ newCourseSection.trim() + "' where course_name='"
					+ currentCourseName.trim() + "' and course_section='"
					+ currentCourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		} else
			throw new ParsingException(UMSException.course_exists);
		return result;
	}

	public String getCourseUnits(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String CourseName = params[0];
		String CourseSection = params[1];

		String getcourseunits = "Select Units from Course where Course_Name='"
				+ CourseName.trim() + "' and course_section='"
				+ CourseSection.trim() + "'";
		String result = executeStatement(getcourseunits);
		System.out.println(result);
		return result;
	}

	public String getCourseInstructor(String courseString)
			throws ParsingException {
		String[] params = courseString.split("\n");
		String CourseName = params[0];
		String CourseSection = params[1];

		String getcourseunits = "Select Instructor_Emp_ID from Instructor where Instructor_PK = "
				+ "(Select Instructor_FK from course where Course_Name='"
				+ CourseName.trim()+ "' and course_section='"+ CourseSection.trim() + "')";
		String result = executeStatement(getcourseunits);
		return result;
	}

	public String setCourseInstructor(String courseString) {
		String[] params = courseString.split("\n");

		String currentCourseName = params[1];
		String currentCourseSection = params[2];
		String instructorID = params[0];

		String updateQuery = "update course set Instructor_FK = (Select Instructor_PK from "
				+ "Instructor where Instructor_Emp_ID='"
				+ instructorID.trim()
				+ "')"
				+ "where course_name='"
				+ currentCourseName.trim()
				+ "' and course_section='" + currentCourseSection.trim() + "'";
		String result = executeUpdate(updateQuery);

		return result;
	}

	public String getStudents(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String CourseName = params[0];
		String CourseSection = params[1];

		String getStudents = "select Student_ID from student,student_courses,course "
				+ "where Course_Name='"
				+ CourseName.trim()
				+ "' and Course_Section='"
				+ CourseSection.trim()
				+ "'and Student_PK=Student_FK and Course_FK=Course_PK";

		String result = executeStatement(getStudents);
		return result;
	}

	public String setCourseUnits(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String CourseName = params[0];
		String CourseSection = params[1];
		String units = params[2];
		String checkCourseNameQuery = "Select course_pk from Course where Course_Section='"
				+ CourseSection.trim()
				+ "' "
				+ "and Course_Name='"
				+ CourseName.trim() + "'";

		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		if (result.equals("0\n")) {
			throw new ParsingException(UMSException.no_such_course);
		} else {
			result = "";
			updateQuery = "update course set Units='" + units.trim()
					+ "' where course_name='" + CourseName.trim()
					+ "' and course_section='" + CourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		}
		return result;
	}

	public String getInstructorPK(String ID) {
		String result;
		String query = "select Instructor_PK from instructor where Instructor_Emp_ID="
				+ ID;
		result = executeStatement(query);
		return result;
	}

	// ******************** ADDITIONAL STUDENT OPERATIONS *******************
	public String createStudentProcedure(transferObject.Student student) {

		try {
			// CALL
			// Create_Student('"+firstName+"','"+lastName+"','"+address+"','"+city+"',"+
			// "'"+state+"','"+zipCode+"','"+studentID+"',?)";}");
			CallableStatement cs = con
					.prepareCall("{CALL Create_Student(?,?,?,?,?,?,?,?)}");
			cs.setString(1, student.getFirstName());
			cs.setString(2, student.getLastName());
			cs.setString(3, student.getAddress());
			cs.setString(4, student.getCity());
			cs.setString(5, student.getState());
			cs.setString(6, student.getZipCode());
			cs.setString(7, student.getStudentID());
			cs.registerOutParameter(8, Types.INTEGER);
			cs.executeQuery();
			String result = cs.getString(8);

			System.out.println("result of execute : " + result);

			con.close();

			return result;
		} // end try

		catch (SQLException e) {
			System.out.println("SQL E : " + e.getMessage());
			e.printStackTrace();
		}
		return dbtime;
	}

	public String removeStudentProcedure(transferObject.Student student) {

		try {
			// CALL
			// Create_Student('"+firstName+"','"+lastName+"','"+address+"','"+city+"',"+
			// "'"+state+"','"+zipCode+"','"+studentID+"',?)";}");
			CallableStatement cs = con
					.prepareCall("{CALL remove_Student(?,?,?)}");
			cs.setString(1, student.getStudentID());
			cs.setString(2, "" + student.iForceUnEnroll);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.executeQuery();
			String result = cs.getString(3);

			System.out.println("result of execute : " + result);

			con.close();

			return result;
		} // end try
		catch (SQLException e) {
			System.out.println("SQL E : " + e.getMessage());
			e.printStackTrace();
		}
		return dbtime;

	}

	// ********************* SEARCH OPERATIONS ******************************
	public String executeFindPersonsQuery(String type) throws ParsingException {
		String query = "";

		int personType = Integer.parseInt(type);
		if (personType == 1) {
			query = "Select Instructor_Emp_ID from instructor";
		} else if (personType == 0) {
			query = "Select Student_ID from student";
		} else if (personType == 2) {
			query = "SELECT Student_ID FROM student UNION Select Instructor_Emp_ID from instructor";
		} else {
			throw new ParsingException(UMSException.malformed_message);
		}
		return executeStatement(query);
	}

	public String executeFindCoursesByInstuctorQuery(String ID)
			throws ParsingException {
		String query = "Select concat(Course_Name,' ',Course_Section) from course,instructor "
				+ "where Instructor_FK=Instructor_PK and Instructor_Emp_ID="
				+ ID.trim();
		return executeStatement(query);
	}

	public String executeFindCoursesByLocationQuery(String loc)
			throws ParsingException {
		String query = "Select concat(Course_Name,' ',Course_Section) from course "
				+ "where concat(Location,' ',DOW,' ',start,'-',end)='"
				+ loc
				+ "'";
		return executeStatement(query);
	}

	public String executeFindCoursesByCourseNameQuery(String courseName)
			throws ParsingException {
		String query = "Select concat(Course_Name,' ',Course_Section) from course "
				+ "where Course_Name like '" + courseName + "%'";
		return executeStatement(query);
	}

	public String executeFindInsructorsByDepartmentQuery(String department)
			throws ParsingException {
		String query = "Select Instructor_Emp_ID from instructor where "
				+ "instructor.department like '" + department.trim() + "%'";
		return executeStatement(query);
	}

	public String executeFindCoursesQuery() throws ParsingException {
		String query = "Select concat(Course_Name,' ',Course_Section) from course";
		return executeStatement(query);
	}

	public String executeFindPersonsQuery(String type, String pattern,
			String option) throws ParsingException {
		String result = "";
		String query1 = "", query2 = "", query = "";
		String op = "";

		if (option.equals("Name")) {
			op = "Concat(person.First_Name,' ',person.Last_name)";
		} else if (option.equals("State")) {
			op = "person.State";
		} else if (option.equals("City")) {
			op = "person.City";
		} else if (option.equals("Zip")) {
			op = "person.Zip_Code";
		} else {
			throw new ParsingException(UMSException.malformed_message);
		}

		int personType = Integer.parseInt(type);
		if (personType == 1) {
			query1 = "Select Instructor_Emp_ID from instructor, person where ";
			query2 = " = ? and instructor.Instructor_Person_FK=person.person_pk";
			query = query1 + op + query2;
		} else if (personType == 0) {
			query1 = "select Student_ID from student, person where ";
			query2 = " = ? and student.Student_Person_FK=person.person_pk";
			query = query1 + op + query2;
		} else if (personType == 2) {
			query1 = "select Student_ID from student, person where " + op
					+ " = ? and student.Student_Person_FK=person.person_pk ";
			query2 = " Select Instructor_Emp_ID from instructor, person where "
					+ op + " = '" + pattern
					+ "' and instructor.Instructor_Person_FK=person.person_pk";
			query = query1 + "UNION" + query2;
		}

		PreparedStatement ps;

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, pattern);
			System.out.println("query : " + ps);
			ResultSet rs = ps.executeQuery();
			String s = "";
			int i = 0;
			while (rs.next()) {
				s = rs.getString(1) + "\n";
				System.out.println(s);
				result += s;
				i++;
			}
			result = i + "\n" + result;
		} catch (SQLException e) {
		}
		return result;
	}

	// *********************** STUDENT INSTRUCTOR COMMON OPERATIONS.. GET/SET
	// FIELDS ******************

	public String executeGetCourses(String personID) {
		String result = "";

		PreparedStatement ps;
		String query = "call get_Courses(?)";
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, personID);

			System.out.println("query : " + ps);
			ResultSet rs = ps.executeQuery();
			int cnt = 0;
			while (rs.next()) {
				cnt++;
				result = result + rs.getString("Course_Name");
				result = result + " " + rs.getString("Course_Section");
				result = result + "\n";
			}
			result = "" + cnt + "\n" + result.trim();
		} catch (SQLException e) {
		}
		return result;
	}

	public String executeGetPersonField(String personID, String fieldName) {
		String result = "";

		PreparedStatement ps;
		fieldName = getPersonTableFieldName(fieldName);

		String query = "Select "
				+ fieldName
				+ " from Person where Person_PK=("
				+ "select  Student_Person_FK from student where student_ID = ?"
				+ " UNION select Instructor_Person_FK from instructor where instructor_Emp_ID=?)";
		try {
			ps = con.prepareStatement(query);
			ps.setString(1, personID);
			ps.setString(2, personID);

			System.out.println("query : " + ps);
			ResultSet rs = ps.executeQuery();
			rs.next();
			result = rs.getString(fieldName);
		} catch (SQLException e) {
		}
		return result;
	}

	public String executeSetPersonField(String personID, String fieldName,
			String value) throws SQLException {

		String result = "";
		fieldName = getPersonTableFieldName(fieldName);
		try {
			if (fieldName.toUpperCase().equals("ID")) {
				CallableStatement cs = con
						.prepareCall("call Update_PersonID(?,?,?)");
				cs.setString(1, personID);
				cs.setString(2, value);
				cs.registerOutParameter(3, Types.INTEGER);
				cs.executeQuery();
				result = cs.getString(3);
				return result;
			} else {

				CallableStatement cs = con
						.prepareCall("call Update_PersonFields(?,?,?,?)");
				cs.setString(1, personID);
				cs.setString(2, fieldName);
				cs.setString(3, value);
				cs.registerOutParameter(4, Types.INTEGER);
				cs.executeQuery();
				result = cs.getString(4);
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getPersonTableFieldName(String fieldName) {
		if (fieldName.toUpperCase().equals("FIRST_NAME"))
			return "FIRST_NAME";
		else if (fieldName.toUpperCase().equals("LAST_NAME"))
			return "LAST_NAME";
		else if (fieldName.toUpperCase().equals("LAST_NAME"))
			return "LAST_NAME";
		else if (fieldName.toUpperCase().equals("ADDRESS"))
			return "ADDRESS";
		else if (fieldName.toUpperCase().equals("CITY"))
			return "CITY";
		else if (fieldName.toUpperCase().equals("STATE"))
			return "STATE";
		else if (fieldName.toUpperCase().equals("ZIP"))
			return "ZIP_CODE";
		else if (fieldName.toUpperCase().equals("ID"))
			return "ID";

		return "";

	}

	// ********************* ADDITIONAL INSTRUCTOR OPERATIONS
	// *********************

	public String executeStoredProcedure_createInstructor(
			transferObject.Instructor instructor) {
		try {

			// CALL
			// Create_Student('"+firstName+"','"+lastName+"','"+address+"','"+city+"',"+
			// "'"+state+"','"+zipCode+"','"+studentID+"',?)";}");
			CallableStatement cs = con
					.prepareCall("{CALL Create_Instructor(?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, instructor.getEmployeeID());
			cs.setString(2, instructor.getFirstName());
			cs.setString(3, instructor.getLastName());
			cs.setString(4, instructor.getAddress());
			cs.setString(5, instructor.getCity());
			cs.setString(6, instructor.getState());
			cs.setString(7, instructor.getZipCode());
			cs.setString(8, instructor.getDepartment());
			cs.registerOutParameter(9, Types.INTEGER);
			cs.executeQuery();
			String result = cs.getString(9);

			System.out.println("result of execute : " + result);

			con.close();

			return result;
		} // end try

		catch (SQLException e) {
			System.out.println("SQL E : " + e.getMessage());
			e.printStackTrace();
		}
		return dbtime;

	}

	public String executeGetDepartment(String request) {
		String query = "Select Department From instructor Where Instructor_Emp_ID ="
				+ request.trim();
		return executeStatement(query);
	}

	public String executeSetDepartment(String empID, String dept) {
		String query = "update instructor Set Department='" + dept.trim() + "'"
				+ " Where Instructor_Emp_ID =" + empID.trim();
		return executeUpdate(query);
	}

	public String executeGetOfficeHours(String empID) {
		String query = "select concat(Location,' ',DOW,' ',Start,' ',End) from instructor_oh,instructor "
				+ "Where Instructor_oh_FK=Instructor_PK and Instructor_Emp_ID="
				+ empID.trim();
		return executeStatement(query);
	}

	public String executeAddOfficeHours(String empID, String OfficeHours)
			throws ParsingException {
		transferObject.Location loc = new Location(OfficeHours);
		String start = loc.getStartTime();
		String end = loc.getEndTime();
		String bldg_room = loc.getBuilding_Room();
		String DOW = loc.getDOW();
		String query = "insert into instructor_oh (Start,End,DOW,"
				+ "Location,Instructor_oh_FK)"				+ " Values('"
				+ start				+ "','"				+ end
				+ "','"				+ DOW				+ "','"
				+ bldg_room	+ "',(select Instructor_PK From instructor where Instructor_Emp_ID ="
				+ empID + "))";
		System.out.println(query);
		String r =  executeUpdate(query);
		return "0";
	}

	public String executeRemoveOfficeHours(String empID, String officeHours)
			throws ParsingException {
		transferObject.Location loc = new Location(officeHours);
		String start = loc.getStartTime();
		String end = loc.getEndTime();
		String DOW = loc.getDOW();
		String location = loc.getBuilding_Room();
		try {
			CallableStatement cs = con
					.prepareCall("{CALL Remove_Office_Hours(?,?,?,?,?,?)}");
			cs.setString(1, empID);
			cs.setString(2, start);
			cs.setString(3, end);
			cs.setString(4, DOW);
			cs.setString(5, location);
			cs.registerOutParameter(6, Types.INTEGER);
			cs.executeQuery();
			String result = cs.getString(6);
			System.out.println("result of execute : " + result);

			con.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "0";
	}

	// ********************* ADMISSION OPERATIONS ****************************

	public String enrollStudentProcedure(String id, String cName, String cSec) {
		String result = "";
		try {
			CallableStatement cs = con
					.prepareCall("{call Enroll_Student(?,?,?,?)}");
			cs.setString(1, id);
			cs.setString(2, cName);
			cs.setString(3, cSec);
			cs.registerOutParameter(4, Types.INTEGER);
			System.out.println(cs.toString());
			cs.executeQuery();
			result = cs.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public String unenrollStudentProcedure(String id, String cName, String cSec) {
		String result = "";
		try {
			CallableStatement cs = con
					.prepareCall("{call Unenroll_Student(?,?,?,?)}");
			cs.setString(1, id);
			cs.setString(2, cName);
			cs.setString(3, cSec);
			cs.registerOutParameter(4, Types.INTEGER);
			System.out.println(cs.toString());
			cs.executeQuery();
			result = cs.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public String getEnrolledUnits(String id) {
		String result = "";
		try {
			CallableStatement cs = con
					.prepareCall("call Calculate_EnrolledUnits(?,?,?)");
			cs.setString(1, id);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.INTEGER);

			System.out.println(cs.toString());
			cs.executeQuery();
			result = cs.getString(2);
			result = result + " " + cs.getString(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getFees(String id) {
		String result = "";
		try {
			CallableStatement cs = con
					.prepareCall("call calculate_fees(?,?,?)");
			cs.setString(1, id);
			cs.registerOutParameter(2, Types.INTEGER);
			cs.registerOutParameter(3, Types.INTEGER);

			System.out.println(cs.toString());
			cs.executeQuery();
			result = cs.getString(2);
			result = result + "@" + cs.getString(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// *********** CONFLICT CHECKING *********************

	public String getLocation(String sCourseName, String sCourseSection)
			throws ParsingException {
		String result = "";

		PreparedStatement ps;
		String query = "select Start,End,DOW,Location from Course where Course_Name=? and Course_Section=?";

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, sCourseName);
			ps.setString(2, sCourseSection);

			System.out.println("query : " + ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = result + rs.getString("Location") + " ";
				if (rs.getString("DOW").toUpperCase().equals("TBA")
						|| rs.getString("Start").toUpperCase().equals("TBA")
						|| rs.getString("End").toUpperCase().equals("TBA")) {
					result = result + " TBA";
				} else {
					result = result + rs.getString("DOW") + " ";
					result = result + rs.getString("Start") + "-";
					result = result + rs.getString("End");
				}
			}
			if (result.equals(""))
				throw new ParsingException(UMSException.no_such_course);
			result = result.trim();
		} catch (SQLException e) {
		}
		return result;

	}

	public ArrayList<transferObject.Location> getAllExistingLocation()
			throws ParsingException {
		String result = "";

		ArrayList<transferObject.Location> arrLocations = new ArrayList<Location>();

		PreparedStatement ps;
		String query = "select ifnull(Start,'TBA') Start,ifnull(End,'TBA') End,ifnull(DOW,'TBA') DOW,ifnull(Location,'TBA') Location from Course "
				+ "Union all select ifnull(Start,'TBA') Start,ifnull(End,'TBA') End,ifnull(DOW,'TBA') DOW,ifnull(Location,'TBA') Location from instructor_oh";

		try {
			ps = con.prepareStatement(query);

			System.out.println("query : " + ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Location loc = new Location();
				loc.setBuilding_Room(rs.getString("Location"));
				loc.setDOW(rs.getString("DOW"));
				loc.setStartTime(rs.getString("Start"));
				loc.setEndTime(rs.getString("End"));
				arrLocations.add(loc);
			}
			result = result.trim();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrLocations;
	}

	public String setLocation(String sCourseName, String sCourseSection,
			Location loc) {
		String result = "";

		try {
			CallableStatement cs = con
					.prepareCall("call set_Location(?,?,?,?,?,?,?);");
			cs.setString(1, sCourseName);
			cs.setString(2, sCourseSection);
			cs.setString(3, loc.getStartTime());
			cs.setString(4, loc.getEndTime());
			cs.setString(5, loc.getDOW());
			cs.setString(6, loc.getBuilding_Room());
			cs.registerOutParameter(7, Types.INTEGER);
			System.out.println(cs.toString());
			cs.executeQuery();
			result = cs.getString(7);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<transferObject.Location> getAllLocationForInstructor(
			String ID) throws ParsingException {
		ArrayList<transferObject.Location> arrLocations = new ArrayList<Location>();

		PreparedStatement ps;
		String query = "select Start,End,DOW,Location from university.Course, "
				+ "university.instructor where Instructor_FK=Instructor_PK and "
				+ "Instructor_Emp_ID=? Union all select Start, End, DOW, "
				+ "Location from university.instructor_oh,university.instructor "
				+ "where Instructor_PK=Instructor_oh_FK and Instructor_Emp_ID=?";

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, ID);
			ps.setString(2, ID);
			System.out.println("getAllLocationForInstructor query : " + ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Location loc = new Location();
				loc.setBuilding_Room(rs.getString("Location"));
				loc.setDOW(rs.getString("DOW"));
				loc.setStartTime(rs.getString("Start"));
				loc.setEndTime(rs.getString("End"));
				arrLocations.add(loc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrLocations;
	}

	public ArrayList<Location> getAllLocationForStudent(String ID)
			throws ParsingException {
		ArrayList<Location> arrLocations = new ArrayList<Location>();
		PreparedStatement ps;
		String query = "select Start,End,DOW,Location,Student_ID from course,student,student_courses"
				+ " where Course_FK=Course_PK and Student_PK=Student_FK and Student_ID=?";

		try {
			ps = con.prepareStatement(query);
			ps.setString(1, ID);
			System.out.println("getAllLocationForStudent query : " + ps);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Location loc = new Location();
				loc.setBuilding_Room(rs.getString("Location"));
				loc.setDOW(rs.getString("DOW"));
				loc.setStartTime(rs.getString("Start"));
				loc.setEndTime(rs.getString("End"));
				arrLocations.add(loc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arrLocations;
	}

}
