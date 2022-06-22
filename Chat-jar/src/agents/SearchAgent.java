package agents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import messagemanager.ACLMessage;
import models.Apartment;

@Stateful
@Remote(Agent.class)
public class SearchAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CachedAgentsRemote cachedAgents;
	private AID agentId;

	@Override
	public AID init(AID agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(this);
		return agentId;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		ObjectMapper objectMapper = new ObjectMapper();
		String location = (String) message.userArgs.get("location");
		File file = new File(location);

		List<Apartment> apartments = new ArrayList<>();
		try {

			apartments = objectMapper.readValue(file, new TypeReference<List<Apartment>>() {
			});

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Apartment apartment : apartments) {
			System.out.println(apartment.toString());
		}
	}

	@Override
	public AID getAgentId() {
		return agentId;
	}

}
