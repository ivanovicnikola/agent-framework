package agentmanager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
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
	public AID startAgent(String name, AID agentId) {
		Agent agent = (Agent) JNDILookup.lookUp(name, Agent.class);
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


}
