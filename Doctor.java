package infoMngmt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.Serializable;
import java.util.*;

@XmlRootElement(name = "doctor")
public class Doctor implements Comparable<Doctor>, Serializable {
    private String name;   // doctor's name

    private List<Patient> patients = new ArrayList<Patient>();  // patient list
    private int    id;    // identifier used as lookup-key

    public Doctor() { }
    public Doctor(String name){
		this.name = name;
		//Patient p = new Patient("test", "123");
		//addPatient(p);
		}


    @Override
    public String toString() {
	String s = "";
	for (Patient p : patients)
	s += "\t" + p.toString() + "\n";
	String result =  String.format("%2d: ", id) + name + ": " + "\n" + s;
	return result;
    }

    //** properties
    public void setName(String who) {
	this.name = who;
    }
    @XmlElement
    public String getName() {
	return this.name;
    }

 /*   public void addPatient(String name, String ins) {
	Patient p = new Patient(name, ins);
	this.patients.add(p);
    }*/

	public void addPatient(Patient p) {
	this.patients.add(p);
    }

    @XmlElement (name = "patient")
  	@XmlElementWrapper(name = "patients")
    public List<Patient> getPatients(){
		return this.patients;
	}

/*
    public String getAllPatients() {
		String s = "";
		for (Patient p : patients)
			s += p.toString();
		return s;
    }*/

    public void setId(int id) {
	this.id = id;
    }
    @XmlElement
    public int getId() {
	return this.id;
    }

    // implementation of Comparable interface
    public int compareTo(Doctor other) {
	return this.id - other.id;
    }



}