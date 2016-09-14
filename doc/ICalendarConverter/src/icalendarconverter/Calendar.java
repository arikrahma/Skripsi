/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icalendarconverter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;

/**
 *
 * @author Ariq
 */
public class Calendar {
    public static void main(String[] args) throws Exception {
		//Initialize values
		String calFile = "TestCalendar.ics";
		
		//start time
		java.util.Calendar startCal = new GregorianCalendar();
		
		
		//end time
		java.util.Calendar endCal = new GregorianCalendar();
		
		String subject = "Matematika Diskret";
		String location = "10316";
		String description = "Mariskha";
		
		//creating a new calendar
		net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		//Create a TimeZone
		startCal.setTimeZone(TimeZone.getDefault());
		endCal.setTimeZone(TimeZone.getDefault());
		// start date
		startCal.set(java.util.Calendar.MONTH, java.util.Calendar.OCTOBER);
		startCal.set(java.util.Calendar.DAY_OF_MONTH, 5);
		startCal.set(java.util.Calendar.YEAR, 2015);
		startCal.set(java.util.Calendar.HOUR_OF_DAY, 8);
		startCal.set(java.util.Calendar.MINUTE, 0);
		startCal.set(java.util.Calendar.SECOND, 0);
		
		//end date
		endCal.set(java.util.Calendar.MONTH, java.util.Calendar.OCTOBER);
		endCal.set(java.util.Calendar.DAY_OF_MONTH, 5);
		endCal.set(java.util.Calendar.YEAR, 2015);
		endCal.set(java.util.Calendar.HOUR_OF_DAY, 10);
		endCal.set(java.util.Calendar.MINUTE, 0);
		endCal.set(java.util.Calendar.SECOND, 0);
		
		//create the event
		DateTime start = new DateTime(startCal.getTime());
		DateTime end = new DateTime(endCal.getTime());
		VEvent meeting = new VEvent(start, end, subject);
		
		// add attendees..
		Attendee att1 = new Attendee();
		att1.getParameters().add(new Cn("Mariskha"));
		meeting.getProperties().add(att1);
		meeting.getProperties().add(new Location(location));
		
		calendar.getComponents().add(meeting);
		

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(calFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		 }
		
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.setValidating(false);

		 try {
		  outputter.output(calendar, fout);
		 } catch (IOException e) {
			e.printStackTrace();
		 } catch (ValidationException e) {
		    e.printStackTrace();
		 }
		 System.out.println(meeting);
	}
}
