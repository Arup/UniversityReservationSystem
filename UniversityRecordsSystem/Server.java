package UniversityRecordsSystem;

import java.util.Properties;
import javax.jms.*;
import javax.naming.*;

public class Server implements MessageListener {
	private Connection connection;
	private Session session;
	private Topic UnivRequest;
	private MessageConsumer consumer;

	public static void main(String args[]) {
		new Server();
	}

	public void sendReply(Message request, String response) {
		try {
			MessageProducer MP = session.createProducer(null);
			Destination replyDest = request.getJMSReplyTo();
			TextMessage TM = session.createTextMessage();
			TM.setText(response);
			MP.send(replyDest, TM);
			MP.close();
		} catch (JMSException JMSE) {
			System.out.println("JMS Exception: " + JMSE);
		}
	}

	public void onMessage(Message message) {
		TextMessage TM = (TextMessage) message;
		String result = "blank";

		try {
			System.out.println("Received Message:\n" + TM.getText());

			if (TM.getText().startsWith("Create_Student")) {
				BusinessDelegate.BD_Student s = new BusinessDelegate.BD_Student();
				System.out.println("Calling Business Delegate - StudentDAO");
				result = s.createStudent(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Remove_Student")) {
				BusinessDelegate.BD_Student s = new BusinessDelegate.BD_Student();
				System.out.println("Calling Business Delegate - StudentDAO");
				result = s.RemoveStudent(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Enrolled_Units")) {
				BusinessDelegate.BD_Student s = new BusinessDelegate.BD_Student();
				System.out.println("Calling Business Delegate - StudentDAO");
				result = s.getEnrolledUnits(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Courses")) {
				BusinessDelegate.BD_Person p = new BusinessDelegate.BD_Person();
				System.out
						.println("Calling Business Delegate - Get Person First Name. ");
				result = p.Get_Courses(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_Persons_By_")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				System.out.println("Calling Business Delegate - Search");
				result = s.findPersons(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_All_Persons")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findAllPersons(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_All_Courses")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findAllCourses(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_Courses_By_Instructor")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findAllCoursesByInstructor(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_Courses_By_Location")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findAllCoursesByLocation(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Find_Courses_By_Course_Name")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findAllCoursesByCourseName(TM.getText());
				sendReply(message, result);
			} else if (TM.getText()
					.startsWith("Find_Instructors_By_Department")) {
				BusinessDelegate.BD_Search s = new BusinessDelegate.BD_Search();
				result = s.findInsructorsByDepartment(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Calculate_Bill")) {
				BusinessDelegate.BD_Admission s = new BusinessDelegate.BD_Admission();
                System.out.println("Calling Business Delegate - Course");
                result = s.calculateBill(TM.getText());
                sendReply(message, result);
			} else if (TM.getText().startsWith("Create_Instructor")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate - ");
				result = i.createInstructor(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Department")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate - ");
				result = i.getDepartment(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Department")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate-");
				result = i.setDepartment(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Office_Hours")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate-");
				result = i.getOfficeHours(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Add_Office_Hours")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate-");
				result = i.addOfficeHours(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Remove_Office_Hours")) {
				BusinessDelegate.BD_Instructor i = new BusinessDelegate.BD_Instructor();
				System.out.println("Calling Business Delegate-");
				result = i.removeOfficeHours(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Enroll_Student")) {
				BusinessDelegate.BD_Admission s = new BusinessDelegate.BD_Admission();
				result = s.enrollStudent(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Unenroll_Student")) {
				BusinessDelegate.BD_Admission s = new BusinessDelegate.BD_Admission();
				result = s.unenrollStudent(TM.getText());
				sendReply(message, result);
			} else if ((TM.getText().startsWith("Get_First_Name"))
					|| (TM.getText().startsWith("Get_Last_Name"))
					|| (TM.getText().startsWith("Get_Address"))
					|| (TM.getText().startsWith("Get_City"))
					|| (TM.getText().startsWith("Get_State"))
					|| (TM.getText().startsWith("Get_Zip"))) {
				BusinessDelegate.BD_Person p = new BusinessDelegate.BD_Person();
				System.out
						.println("Calling Business Delegate - Get Person First Name. ");
				result = p.Get_FieldValue(TM.getText());
				sendReply(message, result);
			} else if ((TM.getText().startsWith("Set_First_Name"))
					|| (TM.getText().startsWith("Set_Last_Name"))
					|| (TM.getText().startsWith("Set_Address"))
					|| (TM.getText().startsWith("Set_City"))
					|| (TM.getText().startsWith("Set_State"))
					|| (TM.getText().startsWith("Set_Zip"))
					|| (TM.getText().startsWith("Set_ID"))) {
				BusinessDelegate.BD_Person p = new BusinessDelegate.BD_Person();
				System.out
						.println("Calling Business Delegate - Get Person First Name. ");
				result = p.Set_FieldValue(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Create_Course")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.createCourse(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Remove_Course")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.removeCourse(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Course_Name")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.setCourseName(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Course_Section")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.setCourseSection(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Course_Units")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.GetCourseUnits(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Course_Units")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.SetCourseUnits(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Course_Instructor")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.getCourseInstructor(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Course_Instructor")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.setCourseInstructor(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Students")) {
				BusinessDelegate.BD_Course s = new BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.GetStudents(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Get_Location")) {
				BusinessDelegate.BD_Course s = new	BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.getLocation(TM.getText());
				sendReply(message, result);
			} else if (TM.getText().startsWith("Set_Location")) {
				BusinessDelegate.BD_Course s = new
				BusinessDelegate.BD_Course();
				System.out.println("Calling Business Delegate - Course");
				result = s.setLocation(TM.getText());
				sendReply(message, result);
			} else {
				String p[] = TM.getText().split("\n");
				String requestID = p[1];
				sendReply(message, requestID + "\n20"); // exception code for
														// malformed message
			}
		} catch (JMSException JMSE) {
			System.out.println("JMS Exception: " + JMSE);
		} 
	}

	public Server() {
		try {
			Properties properties = new Properties();
			properties.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
			properties.put(Context.PROVIDER_URL, "localhost");

			InitialContext jndi = new InitialContext(properties);
			ConnectionFactory conFactory = (ConnectionFactory) jndi
					.lookup("XAConnectionFactory");
			connection = conFactory.createConnection();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			try {
				UnivRequest = (Topic) jndi.lookup("Univ_Request");
			} catch (NamingException NE) {
				System.out.println("NamingException: " + NE
						+ " : Continuing anyway...");
			}

			if (null == UnivRequest) {
				UnivRequest = session.createTopic("CounterTopic");
				jndi.bind("CounterTopic", UnivRequest);
			}

			consumer = session.createConsumer(UnivRequest);
			consumer.setMessageListener(this);

			System.out.println("waiting .............");
			connection.start();

			Thread.currentThread().join();
		} catch (NamingException NE) {
			System.out.println("Naming Exception: " + NE);
		} catch (Exception JMSE) {
			System.out.println("JMS Exception: " + JMSE);
		}
	}

}
