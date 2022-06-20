package agents;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import messagemanager.ACLMessage;

@Stateful
@Remote(Agent.class)
public class CollectorAgent implements Agent {

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
		Document doc;
		try {
			doc = Jsoup.connect("https://www.nekretnine.rs/stambeni-objekti/stanovi/lista/po-stranici/10/").timeout(6000).get();
			//System.out.println(doc.title());
			/*Elements newsHeadlines = doc.select("#mp-itn b a");
			for (Element headline : newsHeadlines) {
			  System.out.println(String.format("%s\n\t%s", 
			    headline.attr("title"), headline.absUrl("href")));
			}*/
			Elements body = doc.select("div.advert-list");
			System.out.println(body.select("div.row offer").size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return agentId;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		Document doc;
		try {
			doc = Jsoup.connect("https://www.nekretnine.rs/stambeni-objekti/stanovi/lista/po-stranici/10/").timeout(6000).get();
			//System.out.println(doc.title());
			/*Elements newsHeadlines = doc.select("#mp-itn b a");
			for (Element headline : newsHeadlines) {
			  System.out.println(String.format("%s\n\t%s", 
			    headline.attr("title"), headline.absUrl("href")));
			}*/
			Elements body = doc.select("div.advert-list");
			System.out.println(body.select("div.row offer").size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public AID getAgentId() {
		return agentId;
	}

}
