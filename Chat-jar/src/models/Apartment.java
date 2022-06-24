package models;

import java.io.Serializable;

public class Apartment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String metaInfo;
	private String location;
	private Double price;
	private Double surfaceArea;
	
	public Apartment() {}

	public Apartment(String title, String metaInfo, String location, Double price, Double surfaceArea) {
		super();
		this.title = title;
		this.metaInfo = metaInfo;
		this.location = location;
		this.price = price;
		this.surfaceArea = surfaceArea;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(Double surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	@Override
	public String toString() {
		return title + ";" + metaInfo + ";" + location + ";" + price + ";" + surfaceArea;
	}

}
