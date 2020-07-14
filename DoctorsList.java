package infoMngmt;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.databind.ObjectMapper;

@XmlRootElement(name = "DoctorsList")
public class DoctorsList {
    private List<Doctor> doctors;
    private AtomicInteger docId;
	private AtomicInteger patientId;

    public DoctorsList() {
	doctors = new CopyOnWriteArrayList<Doctor>();
	docId = new AtomicInteger();
    }

    @XmlElement (name = "doctor")
    @XmlElementWrapper(name = "doctors")
    public List<Doctor> getDoctors() {
	return this.doctors;
    }
    public void setDoctor(List<Doctor> doctors) {
	this.doctors = doctors;
    }

    @Override
    public String toString() {
	String s = "";
	for (Doctor p : doctors)
		s += p.toString() +"\n";
	return s;
    }

    public Doctor find(int id) {
	Doctor d = null;
	// Search the list -- for now, the list is short enough that
	// a linear search is ok but binary search would be better if the
	// list got to be an order-of-magnitude larger in size.
	for (Doctor p : doctors) {
	    if (p.getId() == id) {
		d = p;
		break;
	    }
	}
	return d;
    }

    public int addDoctor(Doctor d ) {
	int id = docId.incrementAndGet();
	d.setId(id);
	doctors.add(d);
	return id;
    }

}