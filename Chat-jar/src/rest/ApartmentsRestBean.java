package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import chatmanager.ChatManagerRemote;
import connectionmanager.ConnectionManager;
import dto.ApartmentScrapingDto;
import dto.ApartmentSearch;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import util.AgentCenter;

@Stateless
@Path("/apartments")
public class ApartmentsRestBean implements ApartmentsRest {

	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private AgentManagerRemote agentManager;
	
	@EJB
	private MessageManagerRemote messageManager;
	
	@EJB
	private ConnectionManager connectionManager;
	
	private String location = "C:\\Users\\nikol\\Desktop\\Nikola\\Data\\apartments.json";
	private String[] sources = {"NEKRETNINE_RS", "4_ZIDA"};
	
	@Override
	public void searchApartments(String username, ApartmentSearch search) {
		User user = chatManager.getByUsername(username);
		AID userId = new AID(user.getUsername(), new AgentType("UserAgent", user.getHost()));
		List<String> connections = connectionManager.getConnections();
		connections.add(0, AgentCenter.getNodeAlias());
		for(int i = 0; i < sources.length; i++) {
			String host = connections.get(i % connections.size());
			String source = sources[i];
			ApartmentScrapingDto dto = new ApartmentScrapingDto(userId, source, search);
			if(host.equals(AgentCenter.getNodeAlias())) {
				scrapeApartments(dto);
			} else {
				ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = resteasyClient.target("http://" + host + "/Chat-war/api/apartments");
				ApartmentsRest rest = rtarget.proxy(ApartmentsRest.class);
				rest.scrapeApartments(dto);
				resteasyClient.close();
			}
		}
	}
	
	@Override
	public void scrapeApartments(ApartmentScrapingDto dto) {
		AID userId = dto.agentId;
		String source = dto.source;
		ApartmentSearch search = dto.search;
		AID collectorId = new AID("collectorAgent", new AgentType("CollectorAgent", AgentCenter.getHost()));
		agentManager.startAgent(collectorId);
		AID searchId = new AID("searchAgent", new AgentType("SearchAgent", AgentCenter.getHost()));
		agentManager.startAgent(searchId);
		ACLMessage m = new ACLMessage();
		m.sender = userId;
		m.receivers.add(collectorId);
		m.replyTo = searchId;
		m.userArgs.put("location", location);
		m.userArgs.put("source", source);
		m.contentObj = search;
		messageManager.post(m);
	}

}
