package agents;

import java.io.Serializable;

public class AgentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public AgentType() {
		super();
	}

	public AgentType(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		AgentType agentType = (AgentType) obj;
		return agentType.name.equals(name);
	}
	
	
}
