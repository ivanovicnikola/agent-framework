package agents;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import messagestorage.MessageStorageRemote;
import models.Message;
import models.User;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AID agentId;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private AgentTypesRemote agentTypes;
	@EJB
	private WSChat ws;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private MessageStorageRemote messageStorage;
	@EJB
	private MessageManagerRemote messageManager;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}
	
	@Override
	public void handleMessage(ACLMessage message) {
		String option = "";
		String response = "";
		option = (String) message.userArgs.get("command");
		switch (option) {
		case "GET_LOGGEDIN":
			response = "LOGGEDIN!";
			List<User> loggedUsers = chatManager.loggedInUsers();
			for (User u : loggedUsers) {
				response += u.toString() + "|";
			}

			break;
		case "GET_REGISTERED":
			response = "REGISTERED!";
			List<User> registeredUsers = chatManager.registeredUsers();
			for (User u : registeredUsers) {
				response += u.toString() + "|";
			}

			break;
		case "MESSAGE":
			response = "MESSAGE!";
			models.Message msg = (Message) message.contentObj;
			msg.setDateCreated(LocalDateTime.now());
			messageStorage.addMessage(msg);
			response += msg.toString();
			break;
		case "GET_MESSAGES":
			response = "MESSAGES!";
			for(models.Message m : messageStorage.getAll()) {
				response += m.toString() + "|";
			}
			break;
		case "GET_RUNNING":
			response = "RUNNING!";
			for(AID agentId : cachedAgents.getAllAgents()) {
				response += agentId.toString() + "|";
			}
			break;
		case "GET_CLASSES":
			response = "CLASSES!";
			for(AgentType agentType : agentTypes.getAgentTypes()) {
				response += agentType.toString() + "|";
			}
			break;
		case "GET_PERFORMATIVES":
			response = "PERFORMATIVES!";
			for(String performative : messageManager.getPerformatives()) {
				response += performative + "|";
			}
			break;
		default:
			response = "ERROR!Option: " + option + " does not exist.";
			break;
		}
		System.out.println(response);
		ws.onMessage(agentId.getName(), response);
	}

	@Override
	public AID init(AID agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(this);
		return agentId;
	}

	@Override
	public AID getAgentId() {
		return agentId;
	}
}
