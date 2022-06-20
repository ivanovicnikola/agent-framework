package agents;

import java.util.ArrayList;
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
			agentTypes.add(type);
		}
	}

	@Override
	public List<AgentType> getAgentTypes() {
		return agentTypes;
	}

	@Override
	public void setAgentTypes(List<AgentType> types) {
		agentTypes = types;
	}

	@Override
	public void removeNode(String nodeAlias) {
		List<AgentType> temp = new ArrayList<>();
		for(AgentType agentType : agentTypes) {
			if(!agentType.getHost().getAlias().equals(nodeAlias)) {
				temp.add(agentType);
			}
		}
		agentTypes = temp;
	}
}
