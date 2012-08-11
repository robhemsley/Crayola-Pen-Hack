package uk.co.robhemsley.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Time - Class
 * Provides some basic time functionality for working with
 * epoch time stamps etc.
 * 
 * @author roberthemsley
 * @version 0.1
 */

public class Time {
	
	public static long getTimeStamp(String format){
		long epoch = 0;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			Date date = new Date();
			
			epoch = new java.text.SimpleDateFormat(format).parse(dateFormat.format(date)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return epoch;
	}
	
	public static long getTimeStamp(){
		return getTimeStamp("dd/MM/yyyy HH:mm:ss:SSS");
	}
}
