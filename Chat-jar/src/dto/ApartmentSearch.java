package dto;

import java.io.Serializable;

public class ApartmentSearch implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String location;
	public Double minPrice;
	public Double maxPrice;
	public Double minArea;
	public Double maxArea;
	
	public ApartmentSearch() {}

	public ApartmentSearch(String location, Double minPrice, Double maxPrice, Double minArea, Double maxArea) {
		super();
		this.location = location;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.minArea = minArea;
		this.maxArea = maxArea;
	}
	
}
