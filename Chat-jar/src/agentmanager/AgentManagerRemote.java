package agentmanager;

import javax.ejb.Remote;

import agents.AID;
import agents.Agent;

@Remote
public interface AgentManagerRemote {
	public AID startAgent(AID agentId);
	public Agent getAgentById(AID agentId);
	public void stopAgent(AID agentId);
}
