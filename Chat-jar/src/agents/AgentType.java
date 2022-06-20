package agents;

import java.io.Serializable;

import models.Host;

public class AgentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Host host;

	public AgentType() {
		super();
	}

	public AgentType(String name, Host host) {
		super();
		this.name = name;
		this.host = host;
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

	@Override
	public boolean equals(Object obj) {
		AgentType agentType = (AgentType) obj;
		return agentType.name.equals(name) && agentType.host.equals(host);
	}

	@Override
	public String toString() {
		return name + "," + host.toString();
	}
	
	
}
