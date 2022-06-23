package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

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
		ACLMessage aclMessage = new ACLMessage();
		aclMessage.userArgs.put("command", "MESSAGE");
		for(User user : chatManager.loggedInUsers()) {
			aclMessage.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		}
		aclMessage.contentObj = message;
		messageManager.post(aclMessage);
	}

	@Override
	public void messageUser(Message message) {
		User user = chatManager.getByUsername(message.getReceiver().getUsername());
		ACLMessage aclMessage = new ACLMessage();
		aclMessage.userArgs.put("command", "MESSAGE");
		aclMessage.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent", user.getHost())));
		aclMessage.contentObj = message;
		messageManager.post(aclMessage);
	}

	@Override
	public void getUserMessages(String username) {
		User user = chatManager.getByUsername(username);
		ACLMessage aclMessage = new ACLMessage();
		aclMessage.receivers.add(new AID(user.getUsername(), new AgentType("UserAgent",  user.getHost())));
		aclMessage.userArgs.put("command", "GET_MESSAGES");
		messageManager.post(aclMessage);
	}
}
