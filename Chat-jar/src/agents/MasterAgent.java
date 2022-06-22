package agents;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import messagemanager.ACLMessage;
import models.Apartment;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class MasterAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private WSChat ws;
	private AID agentId;
	
	@Override
	public AID init(AID agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(this);
		return agentId;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		String response = "APARTMENTS!";
		List<Apartment> apartments = (List<Apartment>) message.contentObj;
		for(Apartment apartment : apartments) {
			response += apartment.toString() + "|";
		}
		System.out.println(response);
		ws.onMessage(agentId.getName(), response);
	}

	@Override
	public AID getAgentId() {
		// TODO Auto-generated method stub
		return agentId;
	}

}
