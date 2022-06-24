package agents;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.Apartment;

@Stateful
@Remote(Agent.class)
public class CollectorAgent implements Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private MessageManagerRemote messageManager;
	private AID agentId;
	@Override
	public AID init(AID agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(this);
		return agentId;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		String option = (String) message.userArgs.get("source");
		List<Apartment> apartments = new ArrayList<>();
		switch (option) {
		case "NEKRETNINE_RS":
			try {
				Document doc = Jsoup.connect("https://www.nekretnine.rs/stambeni-objekti/stanovi/lista/po-stranici/10/").timeout(6000).get();
				Elements body = doc.select("div.advert-list");
				System.out.println(body.select("div.offer-body").size());
				for(Element e : body.select("div.offer-body")) {
					String title = getProcessed(e.select("a").text());
					System.out.println(title);
					String metaInfo = getProcessed(e.select("div.offer-meta-info").text().replace(" | ", ", "));
					System.out.println(metaInfo);
					String location = getProcessed(e.select("p.offer-location").text());
					System.out.println(location);
					String priceStr = getProcessed(e.select("p.offer-price").select("span").get(0).text());
					Double price = parseDouble(priceStr);
					System.out.println(price);
					String surfaceAreaStr = getProcessed(e.select("p.offer-price").select("span").get(1).text());
					Double surfaceArea = parseDouble(surfaceAreaStr);
					System.out.println(surfaceArea);
					Apartment apartment = new Apartment(title, metaInfo, location, price, surfaceArea);
					apartments.add(apartment);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "4_ZIDA":
			try {
				Document doc = Jsoup.connect("https://www.4zida.rs/prodaja-stanova").timeout(6000).get();
				Elements body = doc.select("div.preview-cards-container");
				System.out.println(body.select("div.meta-container").size());
				for(Element e : body.select("div.meta-container")) {
					String title = getProcessed(e.select("p.preview-desc").text());
					System.out.println(title);
					Elements metaLabels = e.select("div.meta-labels").select("span.ng-star-inserted");
					String metaInfo = getProcessed(metaLabels.get(1).text() + ", " + metaLabels.get(2).text());
					System.out.println(metaInfo);
					String location = getProcessed(e.select("div.place-names").select("span.ng-star-inserted").text());
					System.out.println(location);
					String priceStr = getProcessed(e.select("h3").text());
					Double price = parseDouble(priceStr);
					System.out.println(price);
					String surfaceAreaStr = getProcessed(metaLabels.get(0).text());
					Double surfaceArea = parseDouble(surfaceAreaStr);
					System.out.println(surfaceArea);
					Apartment apartment = new Apartment(title, metaInfo, location, price, surfaceArea);
					apartments.add(apartment);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
		String location = (String) message.userArgs.get("location");
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(location), apartments);

		} catch (IOException e) {
			e.printStackTrace();
		}
		ACLMessage m = new ACLMessage();
		m.sender = agentId;
		m.receivers.add(message.replyTo);
		m.replyTo = message.sender;
		m.userArgs.put("location", location);
		messageManager.post(m);
	}

	private Double parseDouble(String str) {
		Double d;
		try {
			d = Double.parseDouble(str.replaceAll("[^\\d]", ""));
		} catch (NumberFormatException e) {
			d = null;
		}
		return d;
	}
	
	private String getProcessed(String str) {
		return str.replace("!", "").replace("|", "").replace(";", "");
	}
	
	@Override
	public AID getAgentId() {
		return agentId;
	}
}
