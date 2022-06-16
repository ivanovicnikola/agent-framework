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
@Path("/agents")
public class AgentsRestBean implements AgentsRest {

	@EJB
	private AgentManagerRemote agentManager;
	
	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private MessageManagerRemote messageManager;
	
	@Override
	public void getClasses(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getRunning(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}

}
