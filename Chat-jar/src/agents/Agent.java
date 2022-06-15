package agents;

import java.io.Serializable;

import messagemanager.ACLMessage;

public interface Agent extends Serializable {

	public AID init(AID agentId);
	public void handleMessage(ACLMessage message);
	public AID getAgentId();
}
