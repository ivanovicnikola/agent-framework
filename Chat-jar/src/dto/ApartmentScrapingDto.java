package dto;

import java.io.Serializable;

import agents.AID;

public class ApartmentScrapingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AID agentId;
	public String source;
	public ApartmentSearch search;
	
	public ApartmentScrapingDto() {}

	public ApartmentScrapingDto(AID agentId, String source, ApartmentSearch search) {
		super();
		this.agentId = agentId;
		this.source = source;
		this.search = search;
	}
}
