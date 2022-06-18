package agentmanager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.AID;
import agents.Agent;
import agents.AgentType;
import agents.CachedAgentsRemote;
import agents.UserAgent;
import util.JNDILookup;

/**
 * Session Bean implementation class AgentManagerBean
 */
@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	
    public AgentManagerBean() {
        
    }

	@Override
	public AID startAgent(AID agentId) {
		Agent agent = (Agent) JNDILookup.lookUp(getAgentLookup(agentId.getType()), Agent.class);
		return agent.init(agentId);
	}

	@Override
	public Agent getAgentById(AID agentId) {
		return cachedAgents.getByAID(agentId);
	}

	@Override
	public void stopAgent(AID agentId) {
		cachedAgents.deleteByAID(agentId);
	}
	
	private String getAgentLookup(AgentType agentType) {
		return String.format("ejb:Chat-ear/Chat-jar//%s!%s?stateful",
				UserAgent.class.getSimpleName(), Agent.class.getName());
	
	}

}
