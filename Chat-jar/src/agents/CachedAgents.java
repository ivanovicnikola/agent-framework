package agents;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;

import connectionmanager.ConnectionManager;

/**
 * Session Bean implementation class CachedAgents
 */
@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	List<Agent> runningAgents;
	List<AID> allAgents;
	
	@EJB
	private ConnectionManager connectionManager;

	/**
	 * Default constructor.
	 */
	public CachedAgents() {

		runningAgents = new ArrayList<>();
		allAgents = new ArrayList<>();
	}

	@Override
	public List<Agent> getRunningAgents() {
		return runningAgents;
	}

	@Override
	public void addRunningAgent(Agent agent) {
		runningAgents.add(agent);
		allAgents.add(agent.getAgentId());
		connectionManager.notifyAllRunning();
	}

	@Override
	public Agent getByAID(AID agentId) {
		for(Agent agent : runningAgents) {
			if(agent.getAgentId().equals(agentId)) {
				return agent;
			}
		}
		return null;
	}

	@Override
	public void deleteByAID(AID agentId) {
		for(Agent agent : runningAgents) {
			if(agent.getAgentId().equals(agentId)) {
				runningAgents.remove(agent);
				break;
			}
		}
		for(AID aid : allAgents) {
			if(aid.equals(agentId)) {
				allAgents.remove(aid);
				connectionManager.notifyAllRunning();
				break;
			}
		}
	}

	@Override
	public List<AID> getAllAgents() {
		return allAgents;
	}

	@Override
	public void setAllAgents(List<AID> allAgents) {
		this.allAgents = allAgents;
	}

	@Override
	public void removeNode(String nodeAlias) {
		List<AID> temp = new ArrayList<>();
		for(AID agentId : allAgents) {
			if(!agentId.getHost().getAddress().equals(nodeAlias)) {
				temp.add(agentId);
			}
		}
		allAgents = temp;
	}

}
