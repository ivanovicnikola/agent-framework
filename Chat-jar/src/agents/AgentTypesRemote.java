package agents;

import java.util.List;

import javax.ejb.Remote;

@Remote
public interface AgentTypesRemote {
	public void addAgentTypes(List<AgentType> types);
	public List<AgentType> getAgentTypes();
	public void setAgentTypes(List<AgentType> types);
	public void removeNode(String nodeAlias);
}
