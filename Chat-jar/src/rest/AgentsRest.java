package rest;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

@Remote
public interface AgentsRest {

	@GET
	@Path("/classes")
	public void getClasses(@HeaderParam("Authorization") String username);
	
	@GET
	@Path("/running")
	public void getRunning(@HeaderParam("Authorization") String username);
}
