package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import BusinessDelegate.ParsingException;
import UniversityRecordsSystem.UMSException;

import transferObject.Course;

public class CourseDAO {
	String dbtime = "";
	String dbUrl = "jdbc:mysql://localhost/university";
	String dbClass = "com.mysql.jdbc.Driver";
	Connection con;

	public CourseDAO() {
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
		}
		return "";
	}

	public String createCourse(Course course) {
		String result = "";

		try {
			CallableStatement cs = con
					.prepareCall("{CALL Create_Course(?,?,?,?,?,?,?,?)}");
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

	public String setCourseName(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String currentCourseName = params[0];
		String currentCourseSection = params[1];
		String newCourseName = params[2];
		String checkCourseNameQuery = "Select count(*) from Course where Course_Name='"
				+ newCourseName.trim()
				+ "' "
				+ "and course_section='"
				+ currentCourseSection.trim() + "'";

		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		String[] getCourse = result.split("\n");
		result = getCourse[1];
		if (Integer.parseInt(result) >= 1) {
			result = "1";
		} else {
			updateQuery = "update course set Course_Name='"
					+ newCourseName.trim() + "' where course_name='"
					+ currentCourseName.trim() + "' and course_section='"
					+ currentCourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		}

		return result;
	}

	public String setCourseSection(String courseString) throws ParsingException {
		String[] params = courseString.split("\n");
		String currentCourseName = params[0];
		String currentCourseSection = params[1];
		String newCourseSection = params[2];
		String checkCourseNameQuery = "Select count(*) from Course where Course_Section='"
				+ newCourseSection.trim()
				+ "' "
				+ "and Course_Name='"
				+ currentCourseName.trim() + "'";

		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		String[] getCourse = result.split("\n");
		result = getCourse[1];
		if (Integer.parseInt(result) >= 1) {
			result = "1";
		} else {
			updateQuery = "update course set Course_Section='"
					+ newCourseSection.trim() + "' where course_name='"
					+ currentCourseName.trim() + "' and course_section='"
					+ currentCourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		}

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
				+ CourseName.trim()
				+ "' and course_section='"
				+ CourseSection.trim() + "')";
		String result = executeStatement(getcourseunits);
		System.out.println(result);
		return result;
	}

	public String setCourseInstructor(String courseString) {
		String[] params = courseString.split("\n");

		String currentCourseName = params[0];
		String currentCourseSection = params[1];
		String instructorID = params[2];
		String checkCourseNameQuery = "Select count(*) from person where Person_PK in"
				+ "(Select Instructor_Person_FK from Instructor where Instructor_Emp_ID='"
				+ instructorID + "')";
		String result = executeStatement(checkCourseNameQuery);
		String updateQuery = null;
		String[] getCourse = result.split("\n");
		result = getCourse[1];
		if (Integer.parseInt(result) == 0) {
			result = "1";
		} else {
			updateQuery = "update course set Instructor_FK = (Select Instructor_PK from Instructor where Instructor_Emp_ID='"
					+ instructorID.trim()
					+ "')"
					+ "where course_name='"
					+ currentCourseName.trim()
					+ "' and course_section='"
					+ currentCourseSection.trim() + "'";
			result = executeUpdate(updateQuery);
		}

		return result;
	}

	public String getStudents(String courseString) throws ParsingException {
		{
			String[] params = courseString.split("\n");
			String CourseName = params[0];
			String CourseSection = params[1];

			String getStudents = "Select Students from Course where Course_Name='"
					+ CourseName.trim()
					+ "' and course_section='"
					+ CourseSection.trim() + "'";
			String result = executeStatement(getStudents);
			System.out.println(result);
			return result;
		}
	}

}
