package agents;

import java.util.List;

public interface CachedAgentsRemote {

	public List<Agent> getRunningAgents();
	public void addRunningAgent(Agent agent);
	public Agent getByAID(AID agentId);
	public void deleteByAID(AID agentId);
}
