package agents;

import java.io.Serializable;

import models.Host;

public class AID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Host host;
	private AgentType type;
	
	public AID() {
		super();
	}

	public AID(String name, Host host, AgentType type) {
		super();
		this.name = name;
		this.host = host;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
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
		return agentId.getHost().equals(host) && agentId.getName().equals(name) && agentId.getType().equals(type);
	}

	@Override
	public String toString() {
		return name + "," + host.getAlias() + "," + host.getAddress() + "," + type.getName();
	}

}
