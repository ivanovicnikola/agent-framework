package rest;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Remote
public interface AgentsRest {

	@GET
	@Path("/classes")
	public void getClasses(@HeaderParam("Authorization") String username);
	
	@GET
	@Path("/running")
	public void getRunning(@HeaderParam("Authorization") String username);
	
	@PUT
	@Path("/running/{type}/{name}")
	public void runAgent(@PathParam("type") String type, @PathParam("name") String name);
}
