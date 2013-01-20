import javax.jms.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import javax.naming.*;

public class LoadTestClient 
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
		new LoadTestClient();
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
		
		consumer.close(); consumer=null;
		replyTopic=null;
		MP.close(); 
		MP=null;
		return Reply.getText();
	}
	private void CreateStudentTest()
	{
		String lsRequest="";
		long startTime = System.currentTimeMillis();
		for(int i=0; i<1000;i++)
		{
			int studentid=(100000000+i);
			lsRequest="Create_Student\n0000"+i+"\n7\nFirstname"+i+"\nLastname"+i+"\n"+studentid+"\naddress"+i+"\n"+
			"City"+i+"\nCA\n95110";
			try {
				System.out.println(sendRequest(lsRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request no."+i);
		}
		long stopTime = System.currentTimeMillis();
	      System.out.println("Avg Ping = "
	                       + ((stopTime - startTime)/1000f) + " msecs");
		
	}
	public void CreateCourses()
	{
		/*
		Create_Course
				none
		Course Name
		Course Section
		Instructor ID
		Location
		Units
		 */
		String lsRequest="";
		long startTime = System.currentTimeMillis();
		
		for(int i=0; i<100;i++)
		{
			lsRequest="Create_Course\n0000"+i+"\n5\nCMPE"+i+"\nSec1\n"+(200000000+i)+"\nBLDG1 Room"+i+" MWF 1000-1200\n3";
			try {
				System.out.println(sendRequest(lsRequest));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request no."+i);
		}
		long stopTime = System.currentTimeMillis();
	      System.out.println("Avg Ping = "
	                       + ((stopTime - startTime)/1000f) + " msecs");

	}
	public void CreateInstructors()
	{
		/*Create_Instructor
		malformed_state, malformed_zip, malformed_id, create_exception, person_exists
		none
		Instructor ID
		First Name
		Last Name
		Address
		City
		State
		Zip
		Department*/
		
		String lsRequest="";
		long startTime = System.currentTimeMillis();
		for(int i=0; i<100;i++)
		{
			int instructorID=(200000000+i);
			lsRequest="Create_Instructor\n0000"+i+"\n8\n"+instructorID+"\nInstructorFName"+i+"\nInstructorLName"+i+"\nInstructor address"+i+"\n"+
			"Instructor City"+i+"\nCA\n95110\nCMPE Department";
			try 
			{
				System.out.println(sendRequest(lsRequest));
			} catch (JMSException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request no."+i);
		}
		long stopTime = System.currentTimeMillis();
	    System.out.println("Avg Ping = "+ ((stopTime - startTime)/1000f) + " msecs");
		
	}
	public void EnrollStudent()
	{
		/*Enroll_Student
		schedule_conflict, student_already_enrolled, too_many_units, malformed_id, \
		 no_such_person
		none
		Student ID
		Course Name
		Course Section*/
		String lsRequest="";
		long startTime = System.currentTimeMillis();
		for(int i=0; i<1000;i++)
		{
			int studentID=(100000000+i);
			int courseId=i%100;
			lsRequest="Enroll_Student\n0000"+i+"\n3\n"+studentID+"\nCMPE"+courseId+"\nSec1";
			try 
			{
				System.out.println(sendRequest(lsRequest));
			} catch (JMSException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request no."+i);
		}
		long stopTime = System.currentTimeMillis();
	    System.out.println("Avg Ping = "+ ((stopTime - startTime)/1000f) + " msecs");
		
		
		
	}
	
	public void GenerateBills()
	{
		/*Calculate_Bill
		malformed_id, no_such_person
		Bill Amount
			Student ID*/
		String lsRequest="";
		long startTime = System.currentTimeMillis();
		for(int i=0; i<10;i++)
		{
			int studentID=(100000000+i);
			lsRequest="Calculate_Bill\n0000"+i+"\n1\n"+studentID;
			try 
			{
				System.out.println(sendRequest(lsRequest));
			} catch (JMSException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Request no."+i);
		}
		long stopTime = System.currentTimeMillis();
	    System.out.println("Avg Ping = "+ ((stopTime - startTime)/1000f) + " msecs");
	}
	public LoadTestClient()
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
			
			//System.out.println("Result : "+ sendRequest(lsRequest));
			Date d1= Calendar.getInstance().getTime();//.toString();
			
			System.out.println(Calendar.getInstance().getTime().toString());
			CreateStudentTest();
			CreateInstructors();
			CreateCourses();
			EnrollStudent();
			GenerateBills();
			System.out.println(d1.toString());
			System.out.println(Calendar.getInstance().getTime().toString());
			connection.close();
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
}

