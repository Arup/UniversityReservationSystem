package UniversityRecordsSystem;

import transferObject.Location;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BusinessDelegate.ParsingException;

public class UMSValidations {
	public final static String[] statesShort = { "AL", "AK", "AZ", "AR", "AS","CA", "CO","CT", "DE", "DC","FM","FL", "GA", "GU","HI", "ID", "IL", "IN", "IA", "KS", 
		"KY","LA", "ME", "MH","MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV","NH", "NJ", "NM", "NY", "NC","ND", "MP",
		"OH", "OK","OR","PW","PA","PR","RI","SC", "SD","TN", "TX", "UT", "VT", "VI","VA", "WA", "WV","WI", "WY" };
	public final static String[] statesLong={
		"ALABAMA", "ALASKA", "ARIZONA", "ARKANSAS", "AMERICAN SAMOA",
		"CALIFORNIA", "COLORADO", "CONNECTICUT", "DELAWARE",
		"DISTRICT OF COLUMBIA", "FEDERATED STATES OF MICRONESIA",
		"FLORIDA", "GEORGIA", "GUAM", "HAWAII", "IDAHO", "ILLINOIS",
		"INDIANA", "IOWA", "KANSAS", "KENTUCKY", "LOUISIANA", "MAINE",
		"MARSHALL ISLANDS", "MARYLAND", "MASSACHUSETTS", "MICHIGAN",
		"MINNESOTA", "MISSISSIPPI", "MISSOURI", "MONTANA", "NEBRASKA",
		"NEVADA", "NEW HAMPSHIRE", "NEW JERSEY", "NEW MEXICO", "NEW YORK",
		"NORTH CAROLINA", "NORTH DAKOTA", "NORTHERN MARIANA ISLANDS",
		"OHIO", "OKLAHOMA", "OREGON", "PALAU", "PENNSYLVANIA",
		"PUERTO RICO", "RHODE ISLAND", "SOUTH CAROLINA", "SOUTH DAKOTA",
		"TENNESSEE", "TEXAS", "UTAH", "VERMONT", "VIRGIN ISLANDS",
		"VIRGINIA", "WASHINGTON", "WEST VIRGINIA", "WISCONSIN", "WYOMING"};

	
	public static String getStateNormalized(String state) throws ParsingException
	{
		state=state.toUpperCase();
		for(int i=0;i<statesShort.length;i++)
		{
			if(state.equals(statesShort[i]))
				return statesShort[i];
			if(state.equals(statesLong[i]))
				return statesShort[i];
		}
		
		throw new BusinessDelegate.ParsingException(UMSException.malformed_state);
	}
	public static boolean isValidLocation(String lsLoc)
	{
		lsLoc=lsLoc.trim();
		String lsPattern1="TBA TBA TBA";
		String lsPattern2="TBA TBA [M]?[T]?[W]?[R]?[F]?[S]?[U]? ([0-1]?[0-9]|2[0-3])[0-5][0-9]-([0-1]?[0-9]|2[0-3])[0-5][0-9]";
		String lsPattern3="[a-zA-z0-9\\s]* TBA";
		String lsPattern4="[a-zA-z0-9\\s]* [M]?[T]?[W]?[R]?[F]?[S]?[U]? ([0-1]?[0-9]|2[0-3])[0-5][0-9]-([0-1]?[0-9]|2[0-3])[0-5][0-9]";
		
		if(lsLoc.matches(lsPattern1) || lsLoc.matches(lsPattern2) || lsLoc.matches(lsPattern3) || lsLoc.matches(lsPattern4))
			return true;
		
		return false;
	}

	public static boolean isValidZip(String sZip) {
		String zipCodePattern1 = "\\d{5}(-\\d{4})?";
		String zipCodePattern2 = "\\d{5}";
		if (sZip.matches(zipCodePattern1))
			return true;
		if (sZip.matches(zipCodePattern2))
			return true;

		return false;
	}

	public static boolean isNumeric(String sValue) {
		String sPattern = "\\d*";
		if (sValue.matches(sPattern))
			return true;
		return false;
	}

	public static boolean isValid24HoursFormat(String sValue) {
		String sPattern = "([0-1]?[0-9]|2[0-3])[0-5][0-9]";
		if (sValue.matches(sPattern))
			return true;
		return false;
	}

	public static boolean isValidTimeSpan(String start,String end) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
	    java.util.Date utilDate1 = null;
	    java.util.Date utilDate2 = null;
	    try 
	    {
			utilDate1 = formatter.parse(start);
			utilDate2 = formatter.parse(end);
		} 
	    catch (ParseException e) 
	    {
			return false;	    	
	    }
	    int comparator =utilDate1.compareTo(utilDate2);
	    if(comparator<0)
	    	 return true;
	    return false;// utilDate1.before(utilDate2);
	}
	public static boolean isOverlappingTimeSpan(String start1,String end1, String start2, String end2 ) throws ParseException
	{
			SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
			java.util.Date hours1_start = formatter.parse(start1);
			java.util.Date hours1_end = formatter.parse(end1);
			java.util.Date hours2_start = formatter.parse(start2);
			java.util.Date hours2_end = formatter.parse(end2);
	    
			if(! isValidTimeSpan(start1, end1))
				return false;
			if(! isValidTimeSpan(start2,end2))
				return false;
			if(hours1_start.equals(hours2_start) || hours1_end.equals(hours2_end))
				return true;
			
			if(hours1_start.before(hours2_start))
			{
				if(hours2_start.before(hours1_end))
					return true;
			}
			else if(hours2_start.before(hours1_start))
			{
				if(hours1_start.before(hours2_end))
					return true;
			}
			else
				return true;			
			
			return false;
	}	
	public static boolean isRoomConflict( transferObject.Location location1, transferObject.Location location2) 
	{
		if(location1.getBuilding_Room().toUpperCase().equals(location2.getBuilding_Room().toUpperCase()))
		{
			String dow1=location1.getDOW().toUpperCase();
			String dow2=location2.getDOW().toUpperCase();
			if((dow1.contains("M")&& dow2.contains("M"))|| (dow1.contains("T")&& dow2.contains("T"))||(dow1.contains("W")&& dow2.contains("W"))
					|| (dow1.contains("R")&& dow2.contains("R")) || (dow1.contains("F")&& dow2.contains("F"))||(dow1.contains("S")&& dow2.contains("S"))
					|| (dow1.contains("U")&& dow2.contains("U")))
			{
				try {
					return isOverlappingTimeSpan(location1.getStartTime(), location1.getEndTime(), location2.getStartTime(), location2.getEndTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}						
		}		
		return false;
	}
	

	public static boolean isScheduleConflict(transferObject.Location location1,
			transferObject.Location location2) 
	{
		String dow1 = location1.getDOW().toUpperCase();
		String dow2 = location2.getDOW().toUpperCase();
		if ((dow1.contains("M") && dow2.contains("M"))
				|| (dow1.contains("T") && dow2.contains("T"))
				|| (dow1.contains("W") && dow2.contains("W"))
				|| (dow1.contains("R") && dow2.contains("R"))
				|| (dow1.contains("F") && dow2.contains("F"))
				|| (dow1.contains("S") && dow2.contains("S"))
				|| (dow1.contains("U") && dow2.contains("U"))) {
			try {
				return isOverlappingTimeSpan(location1.getStartTime(),
						location1.getEndTime(), location2.getStartTime(),
						location2.getEndTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return false;
	}

	public static boolean isValidID(String sValue) {
		String sPattern = "[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
		return sValue.matches(sPattern);
	}

}