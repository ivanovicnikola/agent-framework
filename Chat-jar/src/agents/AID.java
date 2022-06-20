package agents;

import java.io.Serializable;

public class AID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private AgentType type;
	
	public AID() {
		super();
	}

	public AID(String name, AgentType type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		AID agentId = (AID) obj;
		return agentId.getName().equals(name) && agentId.getType().equals(type);
	}

	@Override
	public String toString() {
		return name + "," + type.getHost().getAlias() + "," + type.getHost().getAddress() + "," + type.getName();
	}

}
