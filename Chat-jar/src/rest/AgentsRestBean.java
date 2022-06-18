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
import util.AgentCenter;
import util.JNDILookup;

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
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_CLASSES");
		messageManager.post(message);
	}

	@Override
	public void getRunning(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}

	@Override
	public void runAgent(String type, String name) {
		AID agentId = new AID(name, AgentCenter.getHost(), new AgentType(type));
		agentManager.startAgent(JNDILookup.UserAgentLookup, agentId);
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), u.getHost(), new AgentType("UserAgent")));
			}	
		}
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}

	@Override
	public void stopAgent(AID agentId) {
		agentManager.stopAgent(agentId);
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), u.getHost(), new AgentType("UserAgent")));
			}	
		}
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}

	@Override
	public void getPerformatives(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_PERFORMATIVES");
		messageManager.post(message);
	}

}
