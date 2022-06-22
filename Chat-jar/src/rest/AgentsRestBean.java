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
		message.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		message.userArgs.put("command", "GET_CLASSES");
		messageManager.post(message);
	}

	@Override
	public void getRunning(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}

	@Override
	public void runAgent(String type, String name) {
		AID agentId = new AID(name, new AgentType(type, AgentCenter.getHost()));
		agentManager.startAgent(agentId);
	}

	@Override
	public void stopAgent(AID agentId) {
		agentManager.stopAgent(agentId);
	}

	@Override
	public void getPerformatives(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		message.userArgs.put("command", "GET_PERFORMATIVES");
		messageManager.post(message);
	}

	@Override
	public void sendMessage(ACLMessage message) {
		for(AID receiver: message.receivers) {
			if(receiver.getType().getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				ACLMessage m = message;
				m.receivers.clear();
				m.receivers.add(receiver);
				messageManager.post(m);
			}
		}
	}

}
