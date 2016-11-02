/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import icalendarconverter.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ariq
 */
public class ScheduleClass {
    private LocalDate date;
    private LocalTime timeAwal;
    private LocalTime timeAkhir;
    private StringProperty subject;
    private StringProperty dosen;
    private StringProperty location;
    
    public ScheduleClass (LocalDate date, LocalTime timeAwal
            ,LocalTime timeAkhir, String subject, String dosen
            , String location)
    {
        this.date = date;
        this.timeAwal = timeAwal;
        this.timeAkhir = timeAkhir;
        this.subject = new SimpleStringProperty(subject);
        this.dosen = new SimpleStringProperty(dosen);
        this.location = new SimpleStringProperty(location);
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public LocalTime getTimeAwal() {
        return timeAwal;
    }

    /**
     * @param time the time to set
     */
    public void setTimeAwal(LocalTime timeAwal) {
        this.timeAwal = timeAwal;
    }
    
     /**
     * @return the time
     */
    public LocalTime getTimeAkhir() {
        return timeAkhir;
    }

    /**
     * @param time the time to set
     */
    public void setTimeAkhir(LocalTime timeAkhir) {
        this.timeAkhir = timeAkhir;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject.get();
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    /**
     * @return the dosen
     */
    public String getDosen() {
        return dosen.get();
    }

    /**
     * @param dosen the dosen to set
     */
    public void setDosen(String dosen) {
        this.dosen.set(dosen);
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location.set(location);
    }
    
    public StringProperty subjectProperty()
    {
       return subject;
    }
    
    public StringProperty dosenProperty()
    {
        return dosen;
    }
    public StringProperty location()
    {
        return location;
    }
    
}
