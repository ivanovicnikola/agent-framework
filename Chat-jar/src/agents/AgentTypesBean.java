package agents;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.naming.NamingException;

import util.JNDITreeParser;

@Singleton
@LocalBean
@Remote(AgentTypesRemote.class)
public class AgentTypesBean implements AgentTypesRemote {

	private List<AgentType> agentTypes;
	@EJB
	private JNDITreeParser jndiTreeParser;
	
	@PostConstruct
	public void postConstruct() {
		agentTypes = getClasses();
	}
	
	private List<AgentType> getClasses() {
		try {
			return jndiTreeParser.parse();
		} catch (NamingException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void addAgentTypes(List<AgentType> types) {
		for(AgentType type : types) {
			if(!containsType(type)) {
				agentTypes.add(type);
			}
		}
	}
	
	private boolean containsType(AgentType type) {
		for(AgentType agentType: agentTypes) {
			if(agentType.getName().equals(type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<AgentType> getAgentTypes() {
		return agentTypes;
	}
}
