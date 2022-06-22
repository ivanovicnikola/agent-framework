package agentmanager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.AID;
import agents.Agent;
import agents.AgentType;
import agents.CachedAgentsRemote;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.User;
import util.AgentCenter;
import util.JNDILookup;

/**
 * Session Bean implementation class AgentManagerBean
 */
@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private MessageManagerRemote messageManager;
	
    public AgentManagerBean() {
        
    }

	@Override
	public AID startAgent(AID agentId) {
		if(getAgentById(agentId) != null) {
			return null;
		}
		Agent agent = (Agent) JNDILookup.lookUp(getAgentLookup(agentId.getType()), Agent.class);
		agentId = agent.init(agentId);
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), new AgentType("UserAgent",  u.getHost())));
			}	
		}
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
		return agentId;
	}

	@Override
	public Agent getAgentById(AID agentId) {
		return cachedAgents.getByAID(agentId);
	}

	@Override
	public void stopAgent(AID agentId) {
		cachedAgents.deleteByAID(agentId);
		ACLMessage message = new ACLMessage();
		for(User u : chatManager.loggedInUsers()) {
			if(u.getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
				message.receivers.add(new AID(u.getUsername(), new AgentType("UserAgent", u.getHost())));
			}	
		}
		message.userArgs.put("command", "GET_RUNNING");
		messageManager.post(message);
	}
	
	private String getAgentLookup(AgentType agentType) {
		return String.format("ejb:Chat-ear/Chat-jar//%s!%s?stateful",
				agentType.getName(), Agent.class.getName());
	
	}

}
