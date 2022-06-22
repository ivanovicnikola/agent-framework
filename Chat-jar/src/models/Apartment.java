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
	private String price;
	private String surfaceArea;
	
	public Apartment() {}

	public Apartment(String title, String metaInfo, String location, String price, String surfaceArea) {
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	@Override
	public String toString() {
		return title + ";" + metaInfo + ";" + location + ";" + price + ";" + surfaceArea;
	}

}
