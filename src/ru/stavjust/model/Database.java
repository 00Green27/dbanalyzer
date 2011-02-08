package ru.stavjust.model;

/**
 * @author ehd
 */
public class Database {

	private String name;
	private String url;
	private String location;
	private boolean isSelected = true;

	public Database() {
	}

	public Database(String name, String url, String location) {
		this.name = name;
		this.url = url;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

}
