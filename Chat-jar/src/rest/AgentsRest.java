package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import agents.AID;

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
	
	@PUT
	@Path("/stop")
	@Consumes(MediaType.APPLICATION_JSON)
	public void stopAgent(AID agentId);
	
	@GET
	@Path("/performatives")
	public void getPerformatives(@HeaderParam("Authorization") String username);
}
