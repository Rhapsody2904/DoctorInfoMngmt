package infoMngmt;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.servlet.ServletContext;
import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.concurrent.atomic.AtomicInteger;

@Path("/")
public class DoctorsRS {

    @Context
    private ServletContext sctx;          // dependency injection
    private static DoctorsList docList; // set in populate()

    public DoctorsRS() { }

    @GET
    @Path("/xml")
    @Produces({MediaType.APPLICATION_XML})
    public Response getXml() {
	checkContext();
	return Response.ok(docList, "application/xml").build();
    }

    @GET
    @Path("/xml/{id: \\d+}")
    @Produces({MediaType.APPLICATION_XML}) // could use "application/xml" instead
    public Response getXml(@PathParam("id") int id) {
	checkContext();
	return toRequestedType(id, "application/xml");
    }

	@GET
	@Path("/plain/{id: \\d+}")
	@Produces({MediaType.TEXT_PLAIN})
	public String getPlain(@PathParam("id") int id) {
	checkContext();
	return docList.find(id).toString();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/json")
    public Response getJson() {
	checkContext();
	return Response.ok(toJson(docList), "application/json").build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/json/{id: \\d+}")
    public Response getJson(@PathParam("id") int id) {
	checkContext();
	return toRequestedType(id, "application/json");
    }

    @GET
    @Path("/plain")
    @Produces({MediaType.TEXT_PLAIN})
    public String getPlain() {
	checkContext();
	return docList.toString();
    }

	//Since the list of patients is of undefined length, a good approach to creating a new doctor is just passing the data in the form of
	//an XML document and then allow Jersey to deserialize it into an object that then gets passed to the create method
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    @Path("/create")
    public Response create(Doctor doctor) {
	checkContext();
	String msg = null;

	if (doctor == null) {
	    msg = "Property missing.\n";
	    return Response.status(Response.Status.BAD_REQUEST).
		                                   entity(msg).
		                                   type(MediaType.TEXT_PLAIN).
		                                   build();
	}
	// Otherwise, create the Doctor and add it to the collection.
	int id = addDoctor(doctor);
	msg = "Doctor " + id + " created. \n";
	return Response.ok(msg, "text/plain").build();
    }

    @PUT
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/update")
    public Response update(@FormParam("id") int id,
			   @FormParam("name") String name){
	checkContext();

	// Check that sufficient data are present to do an edit.
	String msg = null;
	if (name == null)
	    msg = "Nothing to edit.\n";

	Doctor p = docList.find(id);
	if (p == null)
	    msg = "There is no Doctor with ID " + id + "\n";

	if (msg != null)
	    return Response.status(Response.Status.BAD_REQUEST).
		                                   entity(msg).
		                                   type(MediaType.TEXT_PLAIN).
		                                   build();

	// Update.
	if (name != null) p.setName(name);
	msg = "Doctor " + id + " has been updated.\n";
	return Response.ok(msg, "text/plain").build();
    }

    @DELETE
    @Produces({MediaType.TEXT_PLAIN})
    @Path("/delete/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {
	checkContext();
	String msg = null;
	Doctor d = docList.find(id);
	if (d == null) {
	    msg = "There is no Doctor with ID " + id + ". Cannot delete.\n";
	    return Response.status(Response.Status.BAD_REQUEST).
		                                   entity(msg).
		                                   type(MediaType.TEXT_PLAIN).
		                                   build();
	}
	docList.getDoctors().remove(d);
	msg = "Doctor " + id + " deleted.\n";

	return Response.ok(msg, "text/plain").build();
    }

    //** utilities
    private void checkContext() {
	if (docList == null) populate();
    }

    private void populate() {
	docList = new DoctorsList();

	String filename = "/WEB-INF/data/drs.db";
	String filename2 = "/WEB-INF/data/patients.db";
	InputStream in = sctx.getResourceAsStream(filename);
	InputStream in2 = sctx.getResourceAsStream(filename2);

	// Read the data into the array of Predictions.
	String error = null;
	if (in != null) {
	    try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(in2));

			String record = null;

			int i = 0;
			int count = 0;
			while ((record = reader.readLine()) != null) {
				String[] parts = record.split("!");
				 count += Integer.parseInt(parts[1]);
				String doctor = parts[0];
				Doctor doc = new Doctor(doctor);

				while ((record = reader2.readLine()) != null) {
					String []parts2 = record.split("!");
					doc.addPatient(new Patient (parts2[0], parts2[1]));
					i++;
					if(i>= count)
					break;
				}
				addDoctor(doc);
			}
	    }
	    catch (Exception e) {
		throw new RuntimeException("I/O failed!");
	    }
	}
    }

    // Add a new Doctor to the list.
    private int addDoctor(Doctor doc) {
	int id = docList.addDoctor(doc);
	return id;
    }

    // Doctor --> JSON document
    private String toJson(Doctor p) {
	String json = "If you see this, there's a problem.";
	try {
	    json = new ObjectMapper().writeValueAsString(p);
	}
	catch(Exception e) { }
	return json;
    }

    // DoctorsList --> JSON document
    private String toJson(DoctorsList plist) {
	String json = "If you see this, there's a problem.";
	try {
	    json = new ObjectMapper().writeValueAsString(plist);
	}
	catch(Exception e) { }
	return json;
    }

	// Patient --> JSON document
	private String toJson(Patient p) {
	String json = "If you see this, there's a problem.";
	try {
		json = new ObjectMapper().writeValueAsString(p);
	}
	catch(Exception e) { }
	return json;
    }

    // Generate an HTTP error response or typed OK response.
    private Response toRequestedType(int id, String type) {
	Doctor pred = docList.find(id);
	if (pred == null) {
	    String msg = id + " is a bad ID.\n";
	    return Response.status(Response.Status.BAD_REQUEST).
		                                   entity(msg).
		                                   type(MediaType.TEXT_PLAIN).
		                                   build();
	}
	else if (type.contains("json"))
	    return Response.ok(toJson(pred), type).build();
	else
	    return Response.ok(pred, type).build(); // toXml is automatic
    }
}



