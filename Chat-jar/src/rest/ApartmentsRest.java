package rest;

import javax.ejb.Remote;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;

@Remote
public interface ApartmentsRest {

	@GET
	public void getApartments(@HeaderParam("Authorization") String username);
}
