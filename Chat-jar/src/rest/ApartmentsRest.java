package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import dto.ApartmentScrapingDto;
import dto.ApartmentSearch;

@Remote
public interface ApartmentsRest {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void searchApartments(@HeaderParam("Authorization") String username, ApartmentSearch search);
	
	@POST
	@Path("/scrape")
	@Consumes(MediaType.APPLICATION_JSON)
	public void scrapeApartments(ApartmentScrapingDto dto);
}
