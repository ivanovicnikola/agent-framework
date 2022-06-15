package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AID;
import agents.AgentType;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.Message;
import models.User;

@Stateless
@Path("/messages")
public class MessagesRestBean implements MessagesRest {

	@EJB
	private MessageManagerRemote messageManager;
	@EJB
	private ChatManagerRemote chatManager;
	
	@Override
	public void messageAll(Message message) {
		for(User user : chatManager.loggedInUsers()) {
			message.setReceiver(user);
			messageUser(message);
		}
	}

	@Override
	public void messageUser(Message message) {
		User user = chatManager.getByUsername(message.getReceiver().getUsername());
		if(user.getHost().getAlias().equals(getNodeAlias() + ":8080")) {
			ACLMessage aclMessage = new ACLMessage();
			aclMessage.userArgs.put("command", "MESSAGE");
			aclMessage.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
			aclMessage.contentObj = message;
			messageManager.post(aclMessage);
		}
		else {
			System.out.println("Sending message to node: " + user.getHost().getAlias());
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + user.getHost().getAlias() + "/Chat-war/api/messages");
			MessagesRest rest = rtarget.proxy(MessagesRest.class);
			rest.messageUser(message);
			resteasyClient.close();
		}
	}

	@Override
	public void getUserMessages(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage aclMessage = new ACLMessage();
		aclMessage.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent")));
		aclMessage.userArgs.put("command", "GET_MESSAGES");
		messageManager.post(aclMessage);
	}

	private String getNodeAlias() {		
		return System.getProperty("jboss.node.name");
	}
}
