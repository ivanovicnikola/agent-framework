package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	List<Agent> runningAgents;

	/**
	 * Default constructor.
	 */
	public CachedAgents() {

		runningAgents = new ArrayList<>();
	}

	@Override
	public List<Agent> getRunningAgents() {
		return runningAgents;
	}

	@Override
	public void addRunningAgent(Agent agent) {
		runningAgents.add(agent);
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
				return;
			}
		}
	}

}
