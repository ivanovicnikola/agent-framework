package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

import agents.AID;
import dto.ApartmentScrapingDto;

@Remote
public interface ApartmentsRest {

	@GET
	public void getApartments(@HeaderParam("Authorization") String username);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void scrapeApartments(ApartmentScrapingDto dto);
}
