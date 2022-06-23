package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.User;

@Stateless
@Path("/apartments")
public class ApartmentsRestBean implements ApartmentsRest {

	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private AgentManagerRemote agentManager;
	
	@EJB
	private MessageManagerRemote messageManager;
	
	private String location = "C:\\Users\\nikol\\Desktop\\Nikola\\Data\\apartments.json";
	
	@Override
	public void getApartments(String username) {
		User user = chatManager.getByUsername(username);
		AID collectorId = new AID("collectorAgent", new AgentType("CollectorAgent", user.getHost()));
		agentManager.startAgent(collectorId);
		AID searchId = new AID("searchAgent", new AgentType("SearchAgent", user.getHost()));
		agentManager.startAgent(searchId);
		//AID masterId = new AID(user.getUsername(), new AgentType("MasterAgent", user.getHost()));
		//agentManager.startAgent(masterId);
		AID userId = new AID(user.getUsername(), new AgentType("UserAgent", user.getHost()));
		
		ACLMessage m = new ACLMessage();
		m.sender = userId;
		m.receivers.add(collectorId);
		m.replyTo = searchId;
		m.userArgs.put("location", location);
		m.userArgs.put("source", "4_ZIDA");
		messageManager.post(m);
		
		ACLMessage m2 = new ACLMessage();
		m2.sender = userId;
		m2.receivers.add(collectorId);
		m2.replyTo = searchId;
		m2.userArgs.put("location", location);
		m2.userArgs.put("source", "NEKRETNINE_RS");
		messageManager.post(m2);
	}

}
