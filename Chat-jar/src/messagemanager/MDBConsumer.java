package messagemanager;

import java.util.ArrayList;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
import rest.AgentsRest;
import util.AgentCenter;

/**
 * Message-Driven Bean implementation class for: MDBConsumer
 */
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/topic/publicTopic") })
public class MDBConsumer implements MessageListener {


	@EJB
	private CachedAgentsRemote cachedAgents;
	/**
	 * Default constructor.
	 */
	public MDBConsumer() {

	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		try {
			ACLMessage aclMessage = (ACLMessage) ((ObjectMessage) message).getObject();
			for (AID receiver : aclMessage.receivers) {
				if(!receiver.getType().getHost().getAlias().equals(AgentCenter.getNodeAlias())) {
					ACLMessage acl = aclMessage;
					acl.receivers = new ArrayList<>();
					acl.receivers.add(receiver);
					forwardMessage(acl);
				}
				Agent agent = cachedAgents.getByAID(receiver);
				agent.handleMessage(aclMessage);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	private void forwardMessage(ACLMessage message) {
		ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = resteasyClient.target("http://" + message.receivers.get(0).getType().getHost().getAlias() + "/Chat-war/api/agents");
		AgentsRest rest = rtarget.proxy(AgentsRest.class);
		rest.sendMessage(message);
		resteasyClient.close();
	}

}
