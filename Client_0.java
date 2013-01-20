

import javax.jms.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import javax.naming.*;

public class Client_0 
{
	private Connection connection;
	private Session session;
	private Topic UnivRequest;
	private MessageConsumer consumer;
	private Topic replyTopic;
	static InputStreamReader converter = new InputStreamReader(System.in);
    static BufferedReader in = new BufferedReader(converter);
	
	public static void main(String args[])
	{
		new Client_0();
	}
	public String sendRequest(String lsRequest) throws JMSException
	{
		int sum;
		MessageProducer MP = session.createProducer(UnivRequest);
		
		//TextMessage TM1 = session.createTextMessage(generateCreateStudentRequest());
		
		TextMessage TM1 = session.createTextMessage(lsRequest);
		
		replyTopic = session.createTemporaryTopic();		
		consumer = session.createConsumer( replyTopic );
		
		TM1.setJMSReplyTo(replyTopic);
		MP.send(TM1);
		TextMessage Reply = (TextMessage)consumer.receive();
		
		return Reply.getText();
	}
	private String generateCreateStudentRequest()
	{
		/*
		Create_Student
		malformed_id, malformed_state, malformed_zip, person_exists, create_exception
		none
		First Name
		Last Name
		StudentDAO ID
		Address
		City
		State
		Zip
		 */
		String requestid,FirstName,LastName,StudentID,Address,City,State,Zip;
		try {
				System.out.print("Enter Request Id: ");
	            requestid=in.readLine();
	            System.out.print("Enter First name: ");
	            FirstName=in.readLine();
	            System.out.print("Enter last name: ");
	            LastName=in.readLine();
	            System.out.print("Enter Student Id: ");
	            StudentID=in.readLine();
	            System.out.print("Enter address: ");
	            Address=in.readLine();
	            System.out.print("Enter City: ");
	            City=in.readLine();
	            System.out.print("Enter State: ");
	            State=in.readLine();
	            System.out.print("Enter ZipCode: ");
	            Zip=in.readLine();
	            return "Create_Student\n"+requestid+"\n7\n"+FirstName+"\n"+LastName+"\n"+StudentID+"\n"+Address+"\n"+
				City+"\n"+State+"\n"+Zip;
	                        
		}
		catch(Exception ex)
		{}
		return "";
	}
	public String generateRemoveStudentRequest()
	{
		/*
		 * Remove_Student
		malformed_id, no_such_person, has_courses
		none
		Student ID to remove
		Force Unenroll (1=Yes, 0=No)
		 */
		
		String requestid,StudentID;
		try {
				System.out.print("Enter Request Id: ");
	            requestid=in.readLine();
	            System.out.print("Enter Student Id: ");
	            StudentID=in.readLine();
	            return "Remove_Student\n"+requestid+"\n2\n"+StudentID+"\n0";
		}
		catch(Exception exp)
		{
			System.out.println(exp.getMessage());
			
		}
		
		return "Remove_Student\n0002\n2\n07508917\n0";
	}
	public Client_0()
	{
		try
		{
		    Properties properties = new Properties();
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		    properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
		    properties.put(Context.PROVIDER_URL, "localhost");
			
			InitialContext jndi = new InitialContext(properties);
			ConnectionFactory conFactory = (ConnectionFactory)jndi.lookup("XAConnectionFactory");
			connection = conFactory.createConnection();
			
			session = connection.createSession( false, Session.AUTO_ACKNOWLEDGE );
			UnivRequest = (Topic)jndi.lookup("Univ_Request");
			
			connection.start();
			String lsRequest=displayStudentMenu();
			//String lsRequest=displayCourseMenu();
			System.out.println("Result : "+ sendRequest(lsRequest));
			

		}
		catch(NamingException NE)
		{
			System.out.println("Naming Exception: "+NE);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}
	}
	public String displayStudentMenu() throws JMSException {
		int choice = 0;
		while (true) {
			System.out.println("***** Student Menu *****");
			System.out.println("1. create student");
			System.out.println("2. remove student");
			System.out.println("3. Get First Name");
			System.out.println("4. Get Last Name");
			System.out.println("5. Get Address");
			System.out.println("6. Get City");
			System.out.println("7. Get State");
			System.out.println("8. Get Zip");
			System.out.println("9. Get_Courses");
			System.out.println("10. Calculate Fees");
			
			System.out.println("11. exit");
			System.out.print("Enter your choice: ");
			try {
				choice = Integer.parseInt(in.readLine());
				if (choice == 11)
					break;
			} catch (Exception ex) {
				// Logger.getLogger(Client.class.getName()).log(Level.SEVERE,
				// null, ex);
			} finally {
			}

			System.out.println("You selected:::" + choice);
			String lsRequest = "";
			switch (choice) {
			case 1:
				lsRequest = generateCreateStudentRequest();
				System.out.println(sendRequest(lsRequest));
				break;
			case 2:
				lsRequest = generateRemoveStudentRequest();
				System.out.println(sendRequest(lsRequest));
				break;
			case 3:
				lsRequest=generateGetPersonFieldRequest("Get_First_Name");
				System.out.println(sendRequest(lsRequest));
				break;
			case 4:
				lsRequest=generateGetPersonFieldRequest("Get_Last_Name");
				System.out.println(sendRequest(lsRequest));
				break;
			case 5:
				lsRequest=generateGetPersonFieldRequest("Get_Address");
				System.out.println(sendRequest(lsRequest));
				break;
			case 6:
				lsRequest=generateGetPersonFieldRequest("Get_City");
				System.out.println(sendRequest(lsRequest));
				break;
			case 7:
				lsRequest=generateGetPersonFieldRequest("Get_State");
				System.out.println(sendRequest(lsRequest));
				break;
			case 8:
				lsRequest=generateGetPersonFieldRequest("Get_Zip");
				System.out.println(sendRequest(lsRequest));
				break;
			case 9:
				lsRequest=generateGetPersonFieldRequest("Get_Courses");
				System.out.println(sendRequest(lsRequest));
				break;
				
			case 10:
				lsRequest=generateCalculateFeesRequest();
				System.out.println(sendRequest(lsRequest));
				break;
			default:
				System.out.println("Invalid input..");
				break;
			}
		}
		return "";
	}
	public String generateCalculateFeesRequest()
	{
		String requestid, StudentID;
		try {
			System.out.print("Enter Request Id: ");
			requestid = in.readLine();
			System.out.print("Enter Student Id: ");
			StudentID = in.readLine();
			return "Calculate_Bill\n" + requestid + "\n1\n" + StudentID ;
		} 
		catch (Exception exp) 
		{
			System.out.println(exp.getMessage());
		}
		return "";
	
	}
	public String generateGetPersonFieldRequest(String operation)
	{
		String requestid, StudentID;
		try {
			System.out.print("Enter Request Id: ");
			requestid = in.readLine();
			System.out.print("Enter Person Id: ");
			StudentID = in.readLine();
			return operation+"\n" + requestid + "\n2\n" + StudentID + "\n0";
		} 
		catch (Exception exp) 
		{
			System.out.println(exp.getMessage());
		}
		return "";
	}
	private String displayCourseMenu() {

		int choice = 0;
        while (true) 
        {
            System.out.println("***** Course Menu *****");
            System.out.println("1. create Course");
            System.out.println("2. remove Course");
            System.out.println("3. set course");
            System.out.println("4. set Section");
            System.out.println("5. get Units");
            System.out.println("6. set Instructor");
            System.out.println("7. get Instructor");
            System.out.println("6. exit");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(in.readLine());
                if(choice==8)
                	break;
                } catch (Exception ex) 
            {
               // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally 
            {
            }
       
        System.out.println("You selected:::" + choice);
        String courseRequest="";
        switch(choice)
        {
        	case 1:
        		courseRequest=generateCreateCourseRequest();
        		System.out.println("CourseRequest is"+courseRequest);
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 2:
        		//courseRequest=generateCreateCourseRequest();
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 3:
        		courseRequest=generateSetCourseNameRequest();
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 4:
        		courseRequest=generateSetCourseSectionRequest();
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 5:
        		courseRequest=generateGetCourseUnitsRequest();
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 6:
        		courseRequest=generateGetSetCourseInstructorRequest("Set");
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	case 7:
        		courseRequest=generateGetSetCourseInstructorRequest("Get");
			try {
				System.out.println(sendRequest(courseRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        		break;
        	
        	default:
        		System.out.println("Invalid input..");
        		break;        
        	}
        }
        return "";
	
	}
	private String generateSetCourseSectionRequest() {
		/*
		Set_Course_Name
		course_exists
		none
		Current Course Name
		Current Course Section
		New Course Name

		 */
		String courseName;
		String courseSection;
		String newCourseSection;
		String requestID;
		try {
				System.out.println("Enter Request Id: ");
				requestID=in.readLine();
	            System.out.println("Enter course Name: ");
	            courseName=in.readLine();
	            System.out.println("Enter course Section: ");
	            courseSection=in.readLine();
	            System.out.println("Enter New Course Section: ");
	            newCourseSection=in.readLine();
	            
	            return "Set_Course_Section\n"+requestID+"\n3\n"+courseName+"\n"+courseSection+"\n"+
	            newCourseSection+"\n";
	                        
		}
		catch(Exception ex)
		{ex.printStackTrace();}
		return "";
	}
	private String generateGetSetCourseInstructorRequest(String s) {
		/*
		Set_Course_Name
		course_exists
		none
		Current Course Name
		Current Course Section
		New Course Name

		 */
		String courseName;
		String courseSection;
		String instructorID;
		String requestID;
		try {
	            System.out.println("Enter Instructor ID: ");
	            instructorID=in.readLine();            
				System.out.println("Enter Request Id: ");
				requestID=in.readLine();
	            System.out.println("Enter course Name: ");
	            courseName=in.readLine();
	            System.out.println("Enter course Section: ");
	            courseSection=in.readLine();
	            if(s.equals("Set"))
	            return "Set_Course_Instructor\n"+requestID+"\n3\n"+instructorID+"\n"+courseName+"\n"+
	            courseSection+"\n";
	            else
	            return "Get_Course_Instructor\n"+requestID+"\n2\n"+courseName+"\n"+
	  	        courseSection+"\n";
	                        
		}
		catch(Exception ex)
		{}
		return "";
	}
	private String generateSetCourseNameRequest() {
		/*
		Set_Course_Name
		course_exists
		none
		Current Course Name
		Current Course Section
		New Course Name

		 */
		String courseName;
		String courseSection;
		String newCourseName;
		String requestID;
		try {
				System.out.println("Enter Request Id: ");
				requestID=in.readLine();
	            System.out.println("Enter course Name: ");
	            courseName=in.readLine();
	            System.out.println("Enter course Section: ");
	            courseSection=in.readLine();
	            System.out.println("Enter New Course Name: ");
	            newCourseName=in.readLine();
	            
	            return "Set_Course_Name\n"+requestID+"\n3\n"+courseName+"\n"+courseSection+"\n"+
	            newCourseName+"\n";
	                        
		}
		catch(Exception ex)
		{}
		return "";
	}
	private String generateCreateCourseRequest() {

		/*
		Create_Course
		malformed_location, malformed_hours, malformed_units, course_exists,
		 room_booked, schedule_conflict, create_exception
		none
		Course Name
		Course Section
		Instructor ID
		Location
		Units
		 */
		String courseName;
		String courseSection;
		String start;
		String end;
		String dow;
		int instructor_FK;
		String location;
		int units;
		String requestID;
		try {
				System.out.print("Enter Request Id: ");
				requestID=in.readLine();
	            System.out.print("Enter course Name: ");
	            courseName=in.readLine();
	            System.out.print("Enter course Section: ");
	            courseSection=in.readLine();
	            System.out.print("Enter Unit: ");
	            units=Integer.parseInt(in.readLine());
	            System.out.print("Enter start: ");
	            start=in.readLine();
	            System.out.print("Enter end: ");
	            end=in.readLine();
	            System.out.print("Enter days of week: ");
	            dow=in.readLine();
	            System.out.print("Enter Location: ");
	            location=in.readLine();
//	            System.out.print("Enter Instructor FK: ");
//	            instructor_FK=Integer.parseInt(in.readLine());
	            
	            return "Create_Course\n"+requestID+"\n7\n"+courseName+"\n"+courseSection+"\n"+
	            units+"\n"+start+"\n"+end+"\n"+dow+"\n"+location+"\n";
	                        
		}
		catch(Exception ex)
		{}
		return "";
	
	}
	private String generateGetCourseUnitsRequest() {
		/*
		Get_Course_Units
		none
		Number of Units
		Course Name
		Course Section

		 */
		String courseName;
		String courseSection;
		String requestID;
		try {
				System.out.println("Enter Request Id: ");
				requestID=in.readLine();
	            System.out.println("Enter course Name: ");
	            courseName=in.readLine();
	            System.out.println("Enter course Section: ");
	            courseSection=in.readLine();
	            	            
	            return "Get_Course_Units\n"+requestID+"\n2\n"+courseName+"\n"+courseSection+"\n";
	                        
		}
		catch(Exception ex)
		{}
		return "";
	}
}
