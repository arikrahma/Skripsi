
import icalendarconverter.ScheduleClass;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.GregorianCalendar;
import java.util.Iterator;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ariq
 */
public class ExampleCalendar {
    public ExampleCalendar()
    {
        
    }
    public static void main(String[] args) throws SocketException, FileNotFoundException, IOException, ValidationException, ParserException {
        LocalDate lc;
        LocalTime lt;
        lc = LocalDate.of(2016, Month.MARCH, 29);
        lt = LocalTime.of(14, 0);
        String subject = "Rekayasa Perangkat Lunak";
        String dosen ="Reinard";
        String location = "10323";
        System.out.println("Lc = "+lc+",lt = "+lt+",Subject = "+subject+", dosen = "+dosen+", location = "+location);
        
        ScheduleClass sch = new ScheduleClass(lc, lt, lt.plusHours(2), subject, dosen, location);
        String calFile = "mycalendar3.ics";
        
        //creating timezone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Asia/Jakarta");
        VTimeZone tz = timezone.getVTimeZone();
        
        //Start Date
        java.util.Calendar startDate = new GregorianCalendar();
        startDate.setTimeZone(timezone);
        startDate.set(java.util.Calendar.MONTH, sch.getDate().getMonthValue());
        startDate.set(java.util.Calendar.DAY_OF_MONTH, sch.getDate().getDayOfMonth());
        startDate.set(java.util.Calendar.YEAR, sch.getDate().getYear());
        startDate.set(java.util.Calendar.HOUR_OF_DAY, sch.getTimeAwal().getHour());
        startDate.set(java.util.Calendar.MINUTE, sch.getTimeAwal().getMinute());
        
        //EndDate
        java.util.Calendar endDate = new GregorianCalendar();
        endDate.setTimeZone(timezone);
        endDate.set(java.util.Calendar.MONTH, sch.getDate().getMonthValue());
        endDate.set(java.util.Calendar.DAY_OF_MONTH, sch.getDate().getDayOfMonth());
        endDate.set(java.util.Calendar.YEAR, sch.getDate().getYear());
        endDate.set(java.util.Calendar.HOUR_OF_DAY, sch.getTimeAkhir().getHour());
        endDate.set(java.util.Calendar.MINUTE, sch.getTimeAkhir().getMinute());
       
        
        //creating an event
        String eventName = sch.getSubject();
        String location2 = sch.getLocation();
        String desc = "Mengawas Ujian "+sch.getDosen();
        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent mengawas = new VEvent(start,end,eventName);
        mengawas.getProperties().add(new Location(location2));
        mengawas.getProperties().add(new Description());
        
        try {
            mengawas.getProperties().getProperty(Property.DESCRIPTION).setValue(desc);
        } catch (Exception e) {
        }
        
        //add timezone info
        mengawas.getProperties().add(tz.getTimeZoneId());
        
        //generate unique indentifier
        UidGenerator uidgenerator = new UidGenerator("uidGen");
        Uid uid = uidgenerator.generateUid();
        mengawas.getProperties().add(uid);
        
        
        //creating calendar
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
        calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        
        // Add the event and print
        calendar.getComponents().add(mengawas);
        System.out.println(calendar);
        
        //saving an iCalendar file
        FileOutputStream fout = new FileOutputStream(calFile);
        
        CalendarOutputter outputter = new CalendarOutputter();
        outputter.setValidating(false);
        outputter.output(calendar, fout);
        
        //Now Parsing an iCalendar file
        FileInputStream fin = new FileInputStream(calFile);
        
        CalendarBuilder builder = new CalendarBuilder();
        
        calendar = builder.build(fin);
        
        //iterating over a calendar
        for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
            Component component = (Component) i.next();
            System.out.println("Component ["+component.getName()+"]");
            
            for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
                Property property = (Property) j.next();
                System.out.println("Property ["+property.getName()+", "+property.getValue()+"]");
            }
        }
        
        
    }
    
}
