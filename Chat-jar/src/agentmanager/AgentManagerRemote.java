package agentmanager;

import java.util.List;

import javax.ejb.Remote;

import agents.AID;
import agents.Agent;
import agents.AgentType;

@Remote
public interface AgentManagerRemote {
	public AID startAgent(AID agentId);
	public Agent getAgentById(AID agentId);
	public void stopAgent(AID agentId);
	public List<AgentType> getClasses();
}
