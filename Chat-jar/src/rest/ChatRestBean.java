package rest;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.AgentType;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import util.AgentCenter;

@Stateless
@LocalBean
@Path("/users")
public class ChatRestBean implements ChatRest, ChatRestLocal {

	@EJB
	private MessageManagerRemote messageManager;
	
	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private AgentManagerRemote agentManager;
	
	@EJB
	private AgentsRest agentsRest;
	
	@Override
	public Response register(User user) {
		if(!chatManager.register(user)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), new AgentType("UserAgent", u.getHost())));
			}
		}
		message.userArgs.put("command", "GET_REGISTERED");
		messageManager.post(message);
		return Response.status(Response.Status.CREATED).entity(user).build();
	}

	@Override
	public Response login(User user) {
		if(!chatManager.login(user)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		agentsRest.runAgent("UserAgent", user.getUsername());
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), new AgentType("UserAgent", u.getHost())));
			}
		}
		message.userArgs.put("command", "GET_LOGGEDIN");
		messageManager.post(message);
		return Response.status(Response.Status.OK).entity(user).build();
	}

	@Override
	public void getloggedInUsers(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		message.userArgs.put("command", "GET_LOGGEDIN");
		messageManager.post(message);
	}

	@Override
	public Response logout(String username) {
		User user = chatManager.getByUsername(username);
		AID agentId = new AID(user.getUsername(), new AgentType("UserAgent", user.getHost()));
		if(!chatManager.logout(username)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		agentsRest.stopAgent(agentId);
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), new AgentType("UserAgent", u.getHost())));
			}
		}
		message.userArgs.put("command", "GET_LOGGEDIN");
		messageManager.post(message);
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public void getRegisteredUsers(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		message.userArgs.put("command", "GET_REGISTERED");
		messageManager.post(message);
	}

}
