package com.project.elms;
  
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp; 

public class CommonFuns {
	
	 
	public static String Formatdt(Date s)
	{
		return   (new SimpleDateFormat("dd-MM-yyyy")).format(s);
	}
	
	
	public static String Formatdt(Timestamp s)
	{
		return   (new SimpleDateFormat("MMM dd,yyyy hh:mm a")).format(s);
	}
	
	public static int cint(String s)
	{
		return Integer.parseInt(s);
	} 
	
	public static float cfloat(String s)
	{
		return Float.parseFloat(s);
	} 
 
	public static Time toTime(String tm)
	{
       if(tm.length()==5)
    	   tm=tm+":00";
		return Time.valueOf(tm);
	}
	
	public static String fromTime(Time tm)
	{
      return tm.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
	}


	public static Double cdouble(String str) { 
		return Double.valueOf(str);
	}
	
	public static String formatfloat(float f)
	{
		return String.format("%.2f", f);
	}
}
