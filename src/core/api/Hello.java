package core.api;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Application;

@Path("/hello")
public class Hello {

	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayHello() {
		String resource = "<?xml version='1.0' ?>" + "<hello>Hello from XML</hello>";

		return resource;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHelloJSON() {
		String resource = null;

		return resource;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHelloHTML() {
		String resource = "<h1>Hello from HTML</h1>";

		return resource;
	}

	// Example post request that produces a JSON response
	// The following annotations are not required. Everything is optional
	@POST
	@Path("/search/client")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String login(@FormParam("type") String type, @FormParam("user") String user,
			@FormParam("pass") String pass) {
		
				return null;
	}
}
