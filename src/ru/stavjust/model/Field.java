package ru.stavjust.model;

/**
 * @author ehd
 */
public class Field {
	private String name;
	private String title;
	private String type;
	private boolean isSelected;

	public Field(String name, String type, String title) {
		this.name = name;
		this.title = title;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
