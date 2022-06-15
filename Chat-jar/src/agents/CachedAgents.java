package agents;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 * Session Bean implementation class CachedAgents
 */
@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	HashMap<AID, Agent> runningAgents;

	/**
	 * Default constructor.
	 */
	public CachedAgents() {

		runningAgents = new HashMap<>();
	}

	@Override
	public HashMap<AID, Agent> getRunningAgents() {
		return runningAgents;
	}

	@Override
	public void addRunningAgent(AID key, Agent agent) {
		runningAgents.put(key, agent);
	}

	@Override
	public Agent getByAID(AID agentId) {
		for(Agent agent : runningAgents.values()) {
			if(agent.getAgentId().equals(agentId)) {
				return agent;
			}
		}
		return null;
	}

	@Override
	public void deleteByAID(AID agentId) {
		for(Agent agent : runningAgents.values()) {
			if(agent.getAgentId().equals(agentId)) {
				runningAgents.remove(agent.getAgentId());
				return;
			}
		}
	}

}
