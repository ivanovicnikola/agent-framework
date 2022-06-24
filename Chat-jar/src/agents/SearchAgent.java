package agents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ApartmentSearch;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.Apartment;

@Stateful
@Remote(Agent.class)
public class SearchAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private MessageManagerRemote messageManager;
	private AID agentId;

	@Override
	public AID init(AID agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(this);
		return agentId;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		ObjectMapper objectMapper = new ObjectMapper();
		String location = (String) message.userArgs.get("location");
		File file = new File(location);

		List<Apartment> allApartments = new ArrayList<>();
		try {

			allApartments = objectMapper.readValue(file, new TypeReference<List<Apartment>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ApartmentSearch search = objectMapper.convertValue(message.contentObj, ApartmentSearch.class);
		List<Apartment> filteredApartments = new ArrayList<>();
		for(Apartment apartment : allApartments) {
			if(matchesSearch(apartment, search)) {
				filteredApartments.add(apartment);
			}
		}
		
		
		ACLMessage m = new ACLMessage();
		m.sender = agentId;
		m.receivers.add(message.replyTo);
		m.contentObj = filteredApartments;
		m.userArgs.put("command", "GET_APARTMENTS");
		messageManager.post(m);
	}

	private boolean matchesSearch(Apartment apartment, ApartmentSearch search) {
		return matchesMinPrice(apartment.getPrice(), search.minPrice) && matchesMaxPrice(apartment.getPrice(), search.maxPrice) && matchesMinArea(apartment.getSurfaceArea(), search.minArea) && matchesMaxArea(apartment.getSurfaceArea(), search.maxArea) && matchesLocation(apartment.getLocation(), search.location);
	}
	
	private boolean matchesMinPrice(Double price, Double minPrice) {
		return minPrice == null || price == null || price >= minPrice;
	}
	
	private boolean matchesMaxPrice(Double price, Double maxPrice) {
		return maxPrice == null || price == null || price <= maxPrice;
	}
	
	private boolean matchesMinArea(Double surfaceArea, Double minArea) {
		return minArea == null || surfaceArea == null || surfaceArea >= minArea;
	}
	
	private boolean matchesMaxArea(Double surfaceArea, Double maxArea) {
		return maxArea == null || surfaceArea == null || surfaceArea <= maxArea;
	}
	private boolean matchesLocation(String apartmentLocation, String searchLocation) {
		return searchLocation == null || apartmentLocation == null || apartmentLocation.toLowerCase().contains(searchLocation.toLowerCase());
	}
	
	@Override
	public AID getAgentId() {
		return agentId;
	}

}
