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
		AID collectorId = new AID(user.getUsername(), new AgentType("CollectorAgent", user.getHost()));
		agentManager.startAgent(collectorId);
		AID searchId = new AID(user.getUsername(), new AgentType("SearchAgent", user.getHost()));
		agentManager.startAgent(searchId);
		ACLMessage m = new ACLMessage();
		m.receivers.add(collectorId);
		m.replyTo = searchId;
		m.userArgs.put("location", location);
		messageManager.post(m);
	}

}
