package agentmanager;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import agents.AID;
import agents.Agent;
import agents.AgentType;
import agents.CachedAgentsRemote;
import agents.UserAgent;
import util.JNDILookup;
import util.JNDITreeParser;

/**
 * Session Bean implementation class AgentManagerBean
 */
@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private JNDITreeParser jndiTreeParser;
	
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

	@Override
	public List<AgentType> getClasses() {
		try {
			return jndiTreeParser.parse();
		} catch (NamingException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	private String getAgentLookup(AgentType agentType) {
		return String.format("ejb:Chat-ear/Chat-jar//%s!%s?stateful",
				UserAgent.class.getSimpleName(), Agent.class.getName());
	
	}

}
