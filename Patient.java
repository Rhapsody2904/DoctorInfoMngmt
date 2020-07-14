package infoMngmt;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement(name = "patient")
public class Patient implements Comparable<Patient>, Serializable{
    private String name;   // patient's name
   	private String insId; //insurance card number
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;

    public Patient() { }
    public Patient(String name, String insid){this.name = name; this.insId = insid; id = count.incrementAndGet();}

    @Override
    public String toString() {
	return String.format("%2d: ", id) + name + " ==> " + insId;
    }

    //** properties
    public void setName(String who) {
	this.name = who;
    }
    @XmlElement
    public String getName() {
	return this.name;
    }

    public void setIns(String i) {
	this.insId = i;
    }
    @XmlElement
    public String getIns() {
	return this.insId;
    }

    public void setId(int id) {
	this.id = id;
    }
    @XmlElement
    public int getId() {
	return this.id;
    }

    // implementation of Comparable interface
    public int compareTo(Patient other) {
	return this.id - other.id;
    }
}